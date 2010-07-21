package org.marvec.pisnickar.tabs;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.marvec.pisnickar.RegisterUtil;
import org.marvec.pisnickar.html.GlobalHtmlListener;
import org.marvec.pisnickar.html.HtmlListener;
import org.marvec.pisnickar.panels.RegisterPanel;
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
    JFrame frame;
    RegisterUtil register;

    private int newDocumentCounter = 1;

    public static final String WELCOME_URL = GlobalHtmlListener.INTERNAL_URL_PREFIX + "welcome";
    public static final String REGISTER_URL = GlobalHtmlListener.INTERNAL_URL_PREFIX + "register";
    public static final String SELECTION_URL = GlobalHtmlListener.INTERNAL_URL_PREFIX + "selection";
    public static final String SONG_URL = GlobalHtmlListener.INTERNAL_URL_PREFIX + "song/";
    public static final String EDIT_URL = GlobalHtmlListener.INTERNAL_URL_PREFIX + "edit/";
    public static final String CHORD_URL = GlobalHtmlListener.INTERNAL_URL_PREFIX + "chord/";
    public static final String SEARCH_URL = GlobalHtmlListener.INTERNAL_URL_PREFIX + "search/";
    public static final String SOURCES_URL = GlobalHtmlListener.INTERNAL_URL_PREFIX + "sources/";

    private static final String SELECTION_TITLE = "Obsah výběru";

    public TabManipulator(JFrame frame, JTabbedPane tabbedPane, SourceManager manager, RegisterUtil register) {
        this.frame = frame;
        this.tabbedPane = tabbedPane;
        this.manager = manager;
        this.register = register;
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

    public void bringToFront(int index) {
        if (index >= 0 && index < tabbedPane.getTabCount()) {
            tabbedPane.getModel().setSelectedIndex(index);
        }
    }

    public void openUrl(String url) {
        if (tabFactory == null) {
            throw new IllegalStateException("Cannot open an URL because TabFactory has not been initialized yet.");
        } else if (url == null) {
            throw new IllegalStateException("Illegal URL (null).");
        } else {
            if (WELCOME_URL.equals(url)) {
                addTab("Vítejte!", tabFactory.createWelcomeTab());
            } else if (REGISTER_URL.equals(url)) {
                addTab("Registrace", tabFactory.createRegisterTab(this, register));
            } else if (url.startsWith(SONG_URL)) {
                openSongUrl(url);
            } else if (url.startsWith(EDIT_URL)) {
                openEditUrl(url);
            } else if (url.startsWith(SEARCH_URL)) {
                openSearchUrl(url);
            } else if (SELECTION_URL.equals(url)) {
                openSelectionUrl();
            } else if (url.startsWith(SOURCES_URL)) {
                addTab("Zdroje", tabFactory.createSourcesTab(this, manager));
            } else {
                throw new IllegalStateException("Do not know how to handle URL: " + url);
            }
        }
    }

    public JFrame getParentFrame() {
        return frame;
    }

    private void openSelectionUrl() {
        addTab(SELECTION_TITLE, tabFactory.createSelectionTab(this, manager));
    }

    private String getSongTabTitle(SongSource source, Song song) {
        return song.getTitle() + " (" + source.getId() + ")";
    }

    private void openSongUrl(String url) {
        String[] params = url.substring(SONG_URL.length()).split("/", 2);
        try {
            SongSource source = manager.getSourceById(params[0]);
            Song song = source.getSong(params[1]);
            if (song == null) {
                throw new NullPointerException("Song id " + params[1] + " does not exists within source id " + params[0]);
            }
            if (bringToFront(getSongTabTitle(source, song))) {
                ((SongPanel) tabbedPane.getComponentAt(getIndexByTitle(getSongTabTitle(source, song)))).refresh(song);
            } else {
                addTab(getSongTabTitle(source, song), tabFactory.createSongTab(this, manager, source, params[1], song));
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
                    addTab("Upravit: " + getSongTabTitle(source, song), tabFactory.createEditorTab(this, source, params[1], song));
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

        addTab(title, tabFactory.createSearchTab(this, manager, param, manager.search(param)));
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
        if (tabbedPane.getSelectedComponent() instanceof RegisterPanel && !register.isRegistered()) {
            return; // block closing the register form until registered
        }
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

    public void refreshSelection() {
        String originalSelection = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
        
        int index = getIndexByTitle(SELECTION_TITLE);
        if (index != -1) {
            bringToFront(index);
            closeCurrentTab();
            openSelectionUrl();
        }

        bringToFront(originalSelection);
    }
}
