package org.marvec.pisnickar.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.marvec.pisnickar.songs.SearchResult;
import org.marvec.pisnickar.tabs.TabManipulator;

/**
 *
 * @author marvec
 */
public class ResourceProvider {

    private static final String CSS_MARKER = "%CSS%";

    public static InputStream loadResource(String res) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("org/marvec/pisnickar/resources/" + res);
    }

    public static String loadResourceAsString(String res) {
        InputStream is = loadResource(res);

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ResourceProvider.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(ResourceProvider.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(ResourceProvider.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return sb.toString();
    }

    public static String insertCss(String page) {
        String css = loadResourceAsString("default.css");
        return page.replaceFirst(CSS_MARKER, css);
    }

}
