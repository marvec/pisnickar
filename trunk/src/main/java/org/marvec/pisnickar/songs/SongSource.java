package org.marvec.pisnickar.songs;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author marvec
 */
public interface SongSource {

    public void open(String id, String name) throws IOException;
    public String getId();
    public String getType();
    public Song getSong(String id) throws IOException;
    public String storeSong(String id, Song song) throws IOException;
    public List<SearchResult> search(String query) throws IOException;
    public int getCount();
    public void flush() throws IOException;
    public void close() throws IOException;
    public boolean isEnabled();
    public void setEnabled(boolean enabled);
    public boolean isReadOnly();
    public boolean isDirty();

}
