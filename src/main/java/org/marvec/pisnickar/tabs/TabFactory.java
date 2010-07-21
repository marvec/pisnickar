package org.marvec.pisnickar.tabs;

import java.util.List;
import javax.swing.JPanel;
import org.marvec.pisnickar.RegisterUtil;
import org.marvec.pisnickar.html.HtmlListener;
import org.marvec.pisnickar.panels.EditorPanel;
import org.marvec.pisnickar.panels.RegisterPanel;
import org.marvec.pisnickar.panels.SearchPanel;
import org.marvec.pisnickar.panels.SelectionPanel;
import org.marvec.pisnickar.panels.SongPanel;
import org.marvec.pisnickar.panels.SourcesPanel;
import org.marvec.pisnickar.panels.WelcomePanel;
import org.marvec.pisnickar.songs.SearchResult;
import org.marvec.pisnickar.songs.Song;
import org.marvec.pisnickar.songs.SongSource;
import org.marvec.pisnickar.songs.SourceManager;

/**
 *
 * @author mvecera
 */
public class TabFactory {

    HtmlListener listener;
    private JPanel welcomePanel = null;
    private JPanel sourcesPanel = null;

    public TabFactory(HtmlListener listener) {
        this.listener = listener;
    }

    public JPanel createWelcomeTab() {
        if (welcomePanel == null) {
            welcomePanel = new WelcomePanel(listener);
        }
        return welcomePanel;
    }

    public JPanel createSongTab(TabManipulator manipulator, SourceManager manager, SongSource source, String songId, Song song) {
        return new SongPanel(listener, manipulator, manager, source, songId, song);
    }

    public JPanel createEditorTab(TabManipulator manipulator, SongSource source, String songId, Song song) {
        return new EditorPanel(manipulator, source, songId, song);
    }

    public JPanel createSearchTab(TabManipulator manipulator, SourceManager manager, String query, List<SearchResult> results) {
        return new SearchPanel(manager, manipulator, listener, query, results);
    }

    public JPanel createSelectionTab(TabManipulator manipulator, SourceManager manager) {
        return new SelectionPanel(manipulator, manager, listener);
    }

    public JPanel createSourcesTab(TabManipulator manipulator, SourceManager manager) {
        return new SourcesPanel(manipulator, manager);
    }

    public JPanel createRegisterTab(TabManipulator manipulator, RegisterUtil register) {
        return new RegisterPanel(manipulator, register);
    }
}
