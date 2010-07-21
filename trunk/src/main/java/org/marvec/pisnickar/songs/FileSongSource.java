package org.marvec.pisnickar.songs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author marvec
 */
public class FileSongSource implements SongSource, Serializable {
    private static final long serialVersionUID = -6493549704906443751L;

    private File dataFile;
    private String id;
    private HashMap<String, Song> songs = new LinkedHashMap<String, Song>();
    private boolean dirty = false;
    private boolean enabled = true;
    private long lastId;

    //private

    private long determineLastId() {
        long tmpId = 0;
        long size = getCount();

        for (Entry<String, Song> e: songs.entrySet()) {
            String key = e.getKey();
            long val;
            try {
                val = Long.parseLong(key, 16);
            } catch (NumberFormatException nfe) {
                val = size;
            }
            if (val > tmpId) {
                tmpId = val;
            }
        }
        return tmpId;
    }

    private String getNextId() {
        return Long.toHexString(++lastId);
    }

    public void open(String id, String name) throws IOException {
        dataFile = new File(name);

        if (id == null) {
            id = getType() + ": " + dataFile.getName();
        }
        this.id = id;

        init();

        lastId = determineLastId();
        dirty = false;
    }

    protected void init() throws IOException {
        if (dataFile.exists()) {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(dataFile));
            zis.getNextEntry();
            ObjectInputStream ois = new ObjectInputStream(zis);
            try {
                songs = (LinkedHashMap<String, Song>) ois.readObject();
            } catch (ClassNotFoundException cnfe) {
                throw new IOException("Nelze načíst databázi textů ze souboru " + dataFile, cnfe);
            }
            zis.closeEntry();
            ois.close();
            zis.close();
        } else {
            flush();
        }
    }

    public void close() throws IOException {
        if (isDirty()) {
            flush();
        }
    }

    public void flush() throws IOException {
        File tmpDataFile = new File(dataFile + "2");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tmpDataFile));
        zos.putNextEntry(new ZipEntry("songs"));
        ObjectOutputStream oos = new ObjectOutputStream(zos);
        oos.writeObject(songs);
        zos.closeEntry();
        oos.flush();
        oos.close();
        zos.flush();
        zos.close();
        if (dataFile.exists() && !dataFile.delete()) {
            throw new IOException("Nelze smazat původní soubor s databází. Nová data byla uložena do souboru " + tmpDataFile);
        }
        if (!tmpDataFile.renameTo(dataFile)) {
            throw new IOException("Nelze uložit data do původního souboru s databází. Nová data byla uložena do souboru " + tmpDataFile);
        }
        dirty = false;
    }

    public int getCount() {
        return songs.size();
    }

    public String getId() {
        return id;
    }

    public Song getSong(String id) throws IOException {
        return songs.get(id);
    }

    public String getType() {
        return "ZipDb";
    }

    public boolean isDirty() {
        return dirty;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isReadOnly() {
        return dataFile == null ? false : !dataFile.canWrite();
    }

    public List<SearchResult> search(String query) {
        LinkedList<SearchResult> result = new LinkedList<SearchResult>();
        query = query.replaceAll("\\*", ".*");

        for (Entry<String, Song> e: songs.entrySet()) {
            Song s = e.getValue();

            if (s.getTitle().matches(query) || s.getTagsString().matches(query) ||
                    s.getTextWithoutChords().matches(query) || s.getAuthorMusic().matches(query) ||
                    s.getAuthorText().matches(query)) {
                result.add(new SearchResult(this, e.getKey()));
            }
        }

        return result;
    }

    public String storeSong(String id, Song song) throws IOException {
        dirty = true;
        if (id == null) {
            id = getNextId();
        }
        songs.put(id, song);

        return id;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(id);
        out.writeObject(dataFile);
        out.writeBoolean(enabled);
        out.writeLong(lastId);
        if (this instanceof DummySongSource) {
            out.writeObject(songs);
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        id = (String) in.readObject();
        dataFile = (File) in.readObject();
        enabled = in.readBoolean();
        lastId = in.readLong();
        if (this instanceof DummySongSource) {
            songs = (HashMap<String, Song>) in.readObject();
        }
    }

    public void clear() {
        songs = new LinkedHashMap<String, Song>();
        dirty = true;
        determineLastId();
    }
}
