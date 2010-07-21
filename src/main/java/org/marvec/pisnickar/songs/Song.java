package org.marvec.pisnickar.songs;

import com.lowagie.text.html.HtmlEncoder;
import java.io.Serializable;
import java.util.Arrays;
import org.marvec.pisnickar.chords.Transposer;
import org.marvec.pisnickar.html.ResourceProvider;
import org.marvec.pisnickar.tabs.TabManipulator;

/**
 *
 * @author marvec
 */
public class Song implements Serializable {
    private static final long serialVersionUID = -1498699939047849700L;
    private static final String HTML_CHORD_OPEN = "<span class='chord'>";
    private static final String HTML_CHORD_CLOSE = "</span>";
    private static final String HTML_SONG_TEMPLATE = "song.html";
    private static final String HTML_TITLE_MARKER = "%TITLE%";
    private static final String HTML_TAGS_MARKER = "%TAGS%";
    private static final String HTML_SONG_MARKER = "%SONG%";
    private static final String HTML_EDIT_MARKER = "%EDIT%";
    private static final String HTML_MUSIC_MARKER = "%MUSIC%";
    private static final String HTML_TEXT_MARKER = "%TEXT%";

    private String title = "";
    private String authorText = "";
    private String authorMusic = "";
    private String[] tags = null;
    private String text = "";
    private int transpose = 0;

    public Song() {
        
    }

    public Song(String title, String tags, String text) {
        this.title = title;
        this.tags = tags.split(" ");
        this.text = text;
    }

    public boolean isValid() {
        return title != null && text != null && !"".equals(title) && !"".equals(text);
    }

    public String getAuthorMusic() {
        return authorMusic;
    }

    public void setAuthorMusic(String authorMusic) {
        this.authorMusic = authorMusic;
    }

    public String getAuthorText() {
        return authorText;
    }

    public void setAuthorText(String authorText) {
        this.authorText = authorText;
    }

    public String[] getTags() {
        return tags;
    }

    public String getTagsString() {
        if (tags != null) {
            String tagString = Arrays.toString(tags);
            return tagString.substring(1, tagString.length() - 1);
        } else {
            return "";
        }
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTranspose() {
        return transpose;
    }

    public void setTranspose(int transpose) {
        this.transpose = transpose;
    }

    public String getTextWithoutChords() {
        return text.replaceAll("\\{[^ ]\\}", "");
    }

    @Override
    public Object clone() {
        Song s = new Song();
        s.authorMusic = this.authorMusic;
        s.authorText = this.authorText;
        s.tags = this.tags.clone();
        s.title = this.title;
        s.text = this.text;

        return s;
    }

    public String toHtml(String sourceId, String songId) {
        return toHtml(sourceId, songId, false);
    }

    private String encode(String s) {
        return HtmlEncoder.encode(s);
    }

    public String toHtml(String sourceId, String songId, boolean readOnly) {
        String template = ResourceProvider.insertCss(ResourceProvider.loadResourceAsString(HTML_SONG_TEMPLATE));
        template = template.replaceFirst(HTML_TITLE_MARKER, encode(title));
        template = template.replaceFirst(HTML_MUSIC_MARKER, encode(getAuthorMusic()));
        template = template.replaceFirst(HTML_TEXT_MARKER, encode(getAuthorText()));
        template = template.replaceFirst(HTML_TAGS_MARKER, encode(getTagsString()));
        if (readOnly) {
            template = template.replaceFirst(HTML_EDIT_MARKER, "");
        } else {
            template = template.replaceFirst(HTML_EDIT_MARKER, TabManipulator.formatEditUrl(sourceId, songId));
        }
        String transposedText = Transposer.transpose(encode(text), transpose);
        transposedText = transposedText.replaceAll("\\n", "<br />");
        String chordMarksReplaced = transposedText.replaceAll("\\{", HTML_CHORD_OPEN).replaceAll("\\}", HTML_CHORD_CLOSE);
        return template.replaceFirst(HTML_SONG_MARKER, chordMarksReplaced);
    }
}
