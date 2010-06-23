package org.marvec.pisnickar.html;

import java.net.URL;
import org.w3c.dom.html2.HTMLElement;

/**
 *
 * @author mvecera
 */
public interface HtmlListener {
    public void linkClicked(HTMLElement linkNode, URL url, String target);
}
