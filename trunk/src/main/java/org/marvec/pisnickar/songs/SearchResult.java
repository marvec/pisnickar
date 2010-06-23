package org.marvec.pisnickar.songs;

import java.io.Serializable;

/**
 *
 * @author marvec
 */
public class SearchResult implements Serializable {
    private static final long serialVersionUID = 8980592893924664431L;

    private SongSource source;
    private String songId;

    public SearchResult(SongSource source, String songId) {
        this.source = source;
        this.songId = songId;
    }

    public String getSongId() {
        return songId;
    }

    public SongSource getSource() {
        return source;
    }
}
