package org.marvec.pisnickar.songs;

import java.io.IOException;

/**
 *
 * @author marvec
 */
public class DummySongSource extends FileSongSource {
    private static final long serialVersionUID = 1L;
    private String name;
    private String id;
    private long lastId = 0;

    @Override
    public void open(String id, String name) throws IOException {
        this.id = id;
        this.name = name;
    }

    @Override
    public void close() throws IOException {
  
    }

    @Override
    public void flush() throws IOException {

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return "Dummy";
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public String storeSong(String id, Song song) throws IOException {
        if (id == null) {
            id = String.valueOf(lastId++);
        }
        super.storeSong(id, song);

        return id;
    }
}
