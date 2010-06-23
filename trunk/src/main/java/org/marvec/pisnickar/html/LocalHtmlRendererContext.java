package org.marvec.pisnickar.html;

import java.net.URL;
import java.util.ArrayList;
import org.lobobrowser.html.HtmlRendererContext;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.test.SimpleHtmlRendererContext;
import org.w3c.dom.html2.HTMLElement;

/**
 *
 * @author mvecera
 */
public class LocalHtmlRendererContext extends SimpleHtmlRendererContext {
    private ArrayList<HtmlListener> listeners = new ArrayList<HtmlListener>();
    private boolean supressOriginalHanlder = false;

    public LocalHtmlRendererContext(HtmlPanel contextComponent, UserAgentContext ucontext) {
        super(contextComponent, ucontext);
    }

    @Override
    public void linkClicked(HTMLElement linkNode, URL url, String target) {
        if (!supressOriginalHanlder) {
            super.linkClicked(linkNode, url, target);
        }

        for (HtmlListener l : listeners) {
            l.linkClicked(linkNode, url, target);
        }
    }

    public boolean isSupressOriginalHanlder() {
        return supressOriginalHanlder;
    }

    public void setSupressOriginalHanlder(boolean supressOriginalHanlder) {
        this.supressOriginalHanlder = supressOriginalHanlder;
    }

    public void addHtmlListener(HtmlListener listener) {
        listeners.add(listener);
    }

    public void removeHtmlListener(HtmlListener listener) {
        listeners.remove(listener);
    }
}
