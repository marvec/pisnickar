package org.marvec.pisnickar.songs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marvec
 */
public class SourceManager implements Serializable {
    private static final long serialVersionUID = -4908343081463897897L;
    public static final String SOURCE_MANAGER_CONFIG = "sourceManager";

    private List<SongSource> sources = new LinkedList<SongSource>();

    public List<SongSource> getSources() {
        return sources;
    }

    public void store(File directory) throws IOException {
       File f = new File(directory, SOURCE_MANAGER_CONFIG);
       ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
       try {
          oos.writeObject(this);
       } finally {
          oos.close();
       }
    }

    public static SourceManager load(File directory) throws IOException {
       File f = new File(directory, SOURCE_MANAGER_CONFIG);
       ObjectInputStream ois = null;
       try {
          ois = new ObjectInputStream(new FileInputStream(f));
          return (SourceManager) ois.readObject();
       } catch (Exception e) {
          return null;
       } finally {
          if (ois != null) {
              ois.close();
          }
       }
    }

    public void addSource(SongSource s) {
        if (sources.indexOf(s) == -1) {
            sources.add(s);
        }
    }

    public SongSource getSourceById(String id) {
        for (SongSource s: sources) {
            if (s.getId().equals(id)) {
                return s;
            }
        }

        return null;
    }

    public LinkedList<SearchResult> search(String queue) {
        LinkedList<SearchResult> result = new LinkedList<SearchResult>();
        
        for (SongSource s: sources) {
            try {
                List<SearchResult> localResult = null;
                localResult = s.search(queue);
                for (SearchResult res : localResult) {
                    result.add(res);
                }
            } catch (IOException ex) {
                Logger.getLogger(SourceManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return result;
    }

    public Song getSong(String sourceId, String songId) throws IOException {
        return getSourceById(sourceId).getSong(songId);
    }

    public String createSong(String sourceId, Song song) throws IOException {
        return getSourceById(sourceId).storeSong(null, song);
    }

    public String storeSong(String sourceId, String songId, Song song) throws IOException {
        return getSourceById(sourceId).storeSong(songId, song);
    }

    public void saveChanges() throws IOException {
        for (SongSource s: sources) {
            if (s.isDirty()) {
                s.flush();
            }
        }
    }

    public String[] getSourceList() {
        String[] list = new String[sources.size()];

        int i = 0;
        for (SongSource s: sources) {
            list[i++] = s.getId();
        }

        return list;
    }

    public String[] getWriteableSourceList() {
        String[] list = new String[sources.size()];

        int i = 0;
        for (SongSource s: sources) {
            if (s.isEnabled() && !s.isReadOnly()) {
                list[i++] = s.getId();
            }
        }

        return list;
    }
    // public ... saveConfiguration
    // public ... loadConfiguration
}
