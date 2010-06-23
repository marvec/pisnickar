package org.marvec.pisnickar.html;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.marvec.pisnickar.tabs.TabManipulator;
import org.w3c.dom.html2.HTMLElement;

/**
 *
 * @author marvec
 */
public class GlobalHtmlListener implements HtmlListener {

    public static final String INTERNAL_URL_PREFIX = "http://pisnickar/";
    TabManipulator manipulator;

    public GlobalHtmlListener(TabManipulator manipulator) {
        this.manipulator = manipulator;
        manipulator.setListener(this);
    }

    public void linkClicked(HTMLElement linkNode, URL url, String target) {
        if (url.toString().startsWith(INTERNAL_URL_PREFIX)) {
            manipulator.openUrl(url.toString());
        } else {
            try {
                java.awt.Desktop.getDesktop().browse(url.toURI());
            } catch (IOException ex) {
                Logger.getLogger(GlobalHtmlListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(GlobalHtmlListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
