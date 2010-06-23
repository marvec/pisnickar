package org.marvec.pisnickar.songs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
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

        ObjectInputStream ois = new ObjectInputStream(new ZipInputStream(new FileInputStream(dataFile)));
        try {
            songs = (LinkedHashMap<String, Song>) ois.readObject();
        } catch (ClassNotFoundException cnfe) {
            throw new IOException("Nelze načíst databázi textů ze souboru " + dataFile, cnfe);
        }
        ois.close();

        lastId = determineLastId();
        dirty = false;
    }

    public void close() throws IOException {
        if (isDirty()) {
            flush();
        }
    }

    public void flush() throws IOException {
        File tmpDataFile = new File(dataFile + "2");
        ObjectOutputStream oos = new ObjectOutputStream(new ZipOutputStream(new FileOutputStream(tmpDataFile)));
        oos.writeObject(songs);
        oos.flush();
        oos.close();
        if (!dataFile.delete()) {
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
        return false;
    }

    public List<SearchResult> search(String query) {
        LinkedList<SearchResult> result = new LinkedList<SearchResult>();

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

}
