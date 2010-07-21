package org.marvec.pisnickar.html;

import com.lowagie.text.html.HtmlEncoder;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.marvec.pisnickar.songs.SearchResult;
import org.marvec.pisnickar.songs.Song;
import org.marvec.pisnickar.tabs.TabManipulator;

/**
 *
 * @author marvec
 */
public class SearchPageFactory {
    private static final String SEARCH_QUERY = "%QUERY%";
    private static final String SEARCH_RESULTS = "%RESULTS%";
    private static final String SEARCH_RESULTS_PAGE = "search.html";

    public static String getSerachResultsAsHtml(String query, List<SearchResult> results) {
        String page = ResourceProvider.insertCss(ResourceProvider.loadResourceAsString(SEARCH_RESULTS_PAGE)).replaceFirst(SEARCH_QUERY, query);
        StringBuilder sb = new StringBuilder();
        String sid = "";
        boolean openedList = false;

        for (SearchResult r: results) {
            if (!r.getSource().getId().equals(sid)) {
                if (openedList) {
                    sb.append("</ul>");
                }
                sb.append("<h2>" + r.getSource().getId() + "</h2><ul>");
                openedList = true;
                sid = r.getSource().getId();
            }
            try {
                Song s = r.getSource().getSong(r.getSongId());
                String title = s.getTitle();
                sb.append("<li>" + HtmlEncoder.encode(s.getAuthorText()) +
                        ": <a href=\"" + TabManipulator.formatSongUrl(sid, r.getSongId()) + "\">");
                sb.append(HtmlEncoder.encode(title) + "</a> (" +
                        HtmlEncoder.encode(s.getTagsString()) + ")</li>");
            } catch (IOException ex) {
                Logger.getLogger(ResourceProvider.class.getName()).log(Level.SEVERE, "Cannot read song title, song id=" +
                        r.getSongId() + ", source id=" + r.getSource().getId(), ex);
            }
        }

        if (openedList) {
            sb.append("</ul>");
        }

        return page.replaceFirst(SEARCH_RESULTS, sb.toString());
    }
}
