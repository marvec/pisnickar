package org.marvec.pisnickar.tabs;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.marvec.pisnickar.html.GlobalHtmlListener;
import org.marvec.pisnickar.html.HtmlListener;
import org.marvec.pisnickar.panels.SongPanel;
import org.marvec.pisnickar.songs.Song;
import org.marvec.pisnickar.songs.SongSource;
import org.marvec.pisnickar.songs.SourceManager;

/**
 *
 * @author mvecera
 */
public class TabManipulator {

    JTabbedPane tabbedPane;
    HtmlListener listener;
    TabFactory tabFactory;
    SourceManager manager;

    private int newDocumentCounter = 1;

    public static final String WELCOME_URL = GlobalHtmlListener.INTERNAL_URL_PREFIX + "welcome";
    public static final String SONG_URL = GlobalHtmlListener.INTERNAL_URL_PREFIX + "song/";
    public static final String EDIT_URL = GlobalHtmlListener.INTERNAL_URL_PREFIX + "edit/";
    public static final String CHORD_URL = GlobalHtmlListener.INTERNAL_URL_PREFIX + "chord/";
    public static final String SEARCH_URL = GlobalHtmlListener.INTERNAL_URL_PREFIX + "search/";
    public static final String SOURCES_URL = GlobalHtmlListener.INTERNAL_URL_PREFIX + "sources/";

    public TabManipulator(JTabbedPane tabbedPane, SourceManager manager) {
        this.tabbedPane = tabbedPane;
        this.manager = manager;
    }

    static public String formatSongUrl(String sourceId, String songId) {
        return SONG_URL + sourceId + "/" + songId;
    }

    static public String formatEditUrl(String sourceId, String songId) {
        return EDIT_URL + sourceId + "/" + songId;
    }

    public void addTab(String title, JPanel panel) {
        if (!bringToFront(title)) {
            tabbedPane.add(title, panel);
            bringToFront(title);
        }
    }

    private int getIndexByTitle(String title) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).equals(title)) {
                return i;
            }
        }
        return -1;
    }

    public boolean bringToFront(String title) {
        int index = getIndexByTitle(title);

        if (index >= 0) {
            tabbedPane.getModel().setSelectedIndex(index);
            return true;
        }

        return false;
    }

    public void openUrl(String url) {
        if (tabFactory == null) {
            throw new IllegalStateException("Cannot open an URL because TabFactory has not been initialized yet.");
        } else if (url == null) {
            throw new IllegalStateException("Illegal URL (null).");
        } else {
            if (WELCOME_URL.equals(url)) {
                addTab("Vítejte!", tabFactory.createWelcomeTab());
            } else if (url.startsWith(SONG_URL)) {
                openSongUrl(url);
            } else if (url.startsWith(EDIT_URL)) {
                openEditUrl(url);
            } else if (url.startsWith(SEARCH_URL)) {
                openSearchUrl(url);
            } else if (url.startsWith(SOURCES_URL)) {
                addTab("Zdroje", tabFactory.createSourcesTab(this, manager));
            } else {
                throw new IllegalStateException("Do not know how to handle URL: " + url);
            }
        }
    }

    private void openSongUrl(String url) {
        String[] params = url.substring(SONG_URL.length()).split("/", 2);
        try {
            SongSource source = manager.getSourceById(params[0]);
            Song song = source.getSong(params[1]);
            if (song == null) {
                throw new NullPointerException("Song id " + params[1] + " does not exists within source id " + params[0]);
            }
            if (bringToFront(song.getTitle())) {
                ((SongPanel) tabbedPane.getComponentAt(getIndexByTitle(song.getTitle()))).refresh(song);
            } else {
                addTab(song.getTitle(), tabFactory.createSongTab(source, params[1], song));
            }
        } catch (Exception ex) {
            Logger.getLogger(TabManipulator.class.getName()).log(Level.SEVERE, "Cannot open song " + (params.length > 1 ? params[1] : null) + " panel.", ex);
            JOptionPane.showMessageDialog(tabbedPane, "Písničku se nepodařilo načíst.", "Chyba při načítání", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openEditUrl(String url) {
        String[] params = url.substring(EDIT_URL.length()).split("/", 2);
        try {
            SongSource source = manager.getSourceById(params[0]);
            if (source != null) {
                if (params.length > 1) {
                    Song song = source.getSong(params[1]);
                    addTab("Upravit: " + song.getTitle(), tabFactory.createEditorTab(this, source, params[1], song));
                } else {
                    // must be unique name for manipulator not to highlight another empty editor
                    addTab("Upravit: Nová písnička " + newDocumentCounter++, tabFactory.createEditorTab(this, source, null, new Song()));
                }
            } else {
                JOptionPane.showMessageDialog(tabbedPane, "Zdroj s písničkou již není k dispozici.", "Chyba při načítání", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            Logger.getLogger(TabManipulator.class.getName()).log(Level.SEVERE, "Cannot open song " + (params.length > 1 ? params[1] : null) + " panel.", ex);
            JOptionPane.showMessageDialog(tabbedPane, "Písničku se nepodařilo načíst.", "Chyba při načítání", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openSearchUrl(String url) {
        String param = url.substring(SEARCH_URL.length());
        String title = "Hledat: " + param;
        if (bringToFront(title)) {
            closeCurrentTab();
        }

        addTab(title, tabFactory.createSearchTab(param, manager.search(param)));
    }

    public HtmlListener getListener() {
        return listener;
    }

    public void setListener(HtmlListener listener) {
        this.listener = listener;
        if (tabFactory == null) {
            tabFactory = new TabFactory(listener);
        } else {
            throw new IllegalStateException("Cannot use another HtmlListener when TabFactory has been already initialized.");
        }
    }

    public void closeCurrentTab() {
        if (tabbedPane.getSelectedIndex() >= 0) {
            tabbedPane.remove(tabbedPane.getSelectedIndex());
        }
    }

    public void showPreviousTab() {
        int index = tabbedPane.getSelectedIndex() - 1;
        if (index + 1 >= 0) {
            tabbedPane.setSelectedIndex(index < 0 ? tabbedPane.getTabCount() - 1 : index);
        }
    }

    public void showNextTab() {
        int index = tabbedPane.getSelectedIndex() + 1;
        if (index - 1 >= 0) {
            tabbedPane.setSelectedIndex(index >= tabbedPane.getTabCount() ? 0 : index);
        }
    }

    public void setCurrentTitle(String title) {
        int index = tabbedPane.getSelectedIndex();
        if (index >= 0) {
            tabbedPane.setTitleAt(index, title);
        }
    }

    public void updateEditTitle(String songTitle) {
        setCurrentTitle("Upravit: " + songTitle);
    }
}
