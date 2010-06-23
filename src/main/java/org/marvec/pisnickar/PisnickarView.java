/*
 * PisnickarView.java
 */
package org.marvec.pisnickar;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.marvec.pisnickar.html.GlobalHtmlListener;
import org.marvec.pisnickar.html.HtmlListener;
import org.marvec.pisnickar.songs.DummySongSource;
import org.marvec.pisnickar.songs.SongSource;
import org.marvec.pisnickar.songs.SourceManager;
import org.marvec.pisnickar.tabs.TabManipulator;

/**
 * The application's main frame.
 */
public class PisnickarView extends FrameView {

    TabManipulator tabManipulator;
    HtmlListener htmlListener;
    SourceManager manager;

    public PisnickarView(SingleFrameApplication app) {
        super(app);

        Logger.getLogger("org.lobobrowser").setLevel(Level.WARNING);

        initComponents();

        try {
            manager = (SourceManager) getApplication().getContext().getLocalStorage().load(SourceManager.SOURCE_MANAGER_CONFIG);
        } catch (IOException e) {
            manager = null;
        }

        if (manager == null) {
            manager = new SourceManager();
            try {
                SongSource source = new DummySongSource();
                source.open("TMP", "Dočasná databáze písniček (v paměti)");
                manager.addSource(source);
            } catch (IOException ex) {
                Logger.getLogger(PisnickarView.class.getName()).log(Level.WARNING, "Cannot create dummy song source", ex);
            }
        }

        tabManipulator = new TabManipulator(jTabbedPane1, manager);
        htmlListener = new GlobalHtmlListener(tabManipulator);
        tabManipulator.openUrl(TabManipulator.WELCOME_URL);

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = PisnickarApp.getApplication().getMainFrame();
            aboutBox = new PisnickarAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        PisnickarApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        searchMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        sourcesMenuItem = new javax.swing.JMenuItem();
        transferMenuItem = new javax.swing.JMenuItem();
        setupMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        tabMenu = new javax.swing.JMenu();
        prevTabMenuItem = new javax.swing.JMenuItem();
        nextTabMenuItem = new javax.swing.JMenuItem();
        closeTabMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        welcomeMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N
        jTabbedPane1.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jTabbedPane1MouseWheelMoved(evt);
            }
        });
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTabbedPane1MousePressed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.marvec.pisnickar.PisnickarApp.class).getContext().getResourceMap(PisnickarView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem.setText(resourceMap.getString("newMenuItem.text")); // NOI18N
        newMenuItem.setName("newMenuItem"); // NOI18N
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newMenuItem);

        searchMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        searchMenuItem.setText(resourceMap.getString("searchMenuItem.text")); // NOI18N
        searchMenuItem.setName("searchMenuItem"); // NOI18N
        searchMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(searchMenuItem);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setText(resourceMap.getString("saveMenuItem.text")); // NOI18N
        saveMenuItem.setName("saveMenuItem"); // NOI18N
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        sourcesMenuItem.setText(resourceMap.getString("sourcesMenuItem.text")); // NOI18N
        sourcesMenuItem.setName("sourcesMenuItem"); // NOI18N
        sourcesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourcesMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(sourcesMenuItem);

        transferMenuItem.setText(resourceMap.getString("transferMenuItem.text")); // NOI18N
        transferMenuItem.setName("transferMenuItem"); // NOI18N
        fileMenu.add(transferMenuItem);

        setupMenuItem.setText(resourceMap.getString("setupMenuItem.text")); // NOI18N
        setupMenuItem.setName("setupMenuItem"); // NOI18N
        fileMenu.add(setupMenuItem);

        jSeparator2.setName("jSeparator2"); // NOI18N
        fileMenu.add(jSeparator2);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.marvec.pisnickar.PisnickarApp.class).getContext().getActionMap(PisnickarView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setToolTipText(resourceMap.getString("exitMenuItem.toolTipText")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        tabMenu.setText(resourceMap.getString("tabMenu.text")); // NOI18N
        tabMenu.setName("tabMenu"); // NOI18N

        prevTabMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        prevTabMenuItem.setText(resourceMap.getString("prevTabMenuItem.text")); // NOI18N
        prevTabMenuItem.setName("prevTabMenuItem"); // NOI18N
        prevTabMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevTabMenuItemActionPerformed(evt);
            }
        });
        tabMenu.add(prevTabMenuItem);

        nextTabMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        nextTabMenuItem.setText(resourceMap.getString("nextTabMenuItem.text")); // NOI18N
        nextTabMenuItem.setName("nextTabMenuItem"); // NOI18N
        nextTabMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextTabMenuItemActionPerformed(evt);
            }
        });
        tabMenu.add(nextTabMenuItem);

        closeTabMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.CTRL_MASK));
        closeTabMenuItem.setText(resourceMap.getString("closeTabMenuItem.text")); // NOI18N
        closeTabMenuItem.setName("closeTabMenuItem"); // NOI18N
        closeTabMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeTabMenuItemActionPerformed(evt);
            }
        });
        tabMenu.add(closeTabMenuItem);

        menuBar.add(tabMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        welcomeMenuItem.setText(resourceMap.getString("welcomeMenuItem.text")); // NOI18N
        welcomeMenuItem.setName("welcomeMenuItem"); // NOI18N
        welcomeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                welcomeMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(welcomeMenuItem);

        jSeparator3.setName("jSeparator3"); // NOI18N
        helpMenu.add(jSeparator3);

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 216, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        try {
            //getApplication().getContext().getTaskService().execute(task);
            manager.saveChanges();
        } catch (IOException ex) {
            Logger.getLogger(PisnickarView.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(getFrame(), "Nepodařilo se uložit změny.", "Chyba při ukládání", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        String id = (String) JOptionPane.showInputDialog(getFrame(), "Do kterého zdroje chcete přidat písničku?", "Výběr zdroje", JOptionPane.QUESTION_MESSAGE,
                null, manager.getWriteableSourceList(), null);
        tabManipulator.openUrl(TabManipulator.EDIT_URL + id);
    }//GEN-LAST:event_newMenuItemActionPerformed

    private void searchMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchMenuItemActionPerformed
        String query = (String) JOptionPane.showInputDialog(getFrame(), "Zadejte hledaný výraz:\n(používejte regulární výrazy, např. . pro libovolný znak, .* pro libovolný počet znaků)", "Hledání", JOptionPane.QUESTION_MESSAGE);
        if (query != null && !"".equals(query)) {
            try {
                tabManipulator.openUrl(TabManipulator.SEARCH_URL + query);
            } catch (PatternSyntaxException e) {
                JOptionPane.showMessageDialog(getFrame(), "Neplatný výraz pro vyhledávání:\n" + e.getMessage(), "Chyba při vyhledávání", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_searchMenuItemActionPerformed

    private void closeTabMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeTabMenuItemActionPerformed
        tabManipulator.closeCurrentTab();
    }//GEN-LAST:event_closeTabMenuItemActionPerformed

    private void prevTabMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevTabMenuItemActionPerformed
        tabManipulator.showPreviousTab();
    }//GEN-LAST:event_prevTabMenuItemActionPerformed

    private void nextTabMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextTabMenuItemActionPerformed
        tabManipulator.showNextTab();
    }//GEN-LAST:event_nextTabMenuItemActionPerformed

    private void jTabbedPane1MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseWheelMoved
        if (evt.getWheelRotation() < 0) {
            tabManipulator.showPreviousTab();
        } else {
            tabManipulator.showNextTab();
        }
    }//GEN-LAST:event_jTabbedPane1MouseWheelMoved

    private void jTabbedPane1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MousePressed
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON2) {
            tabManipulator.closeCurrentTab();
        }
    }//GEN-LAST:event_jTabbedPane1MousePressed

    private void welcomeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_welcomeMenuItemActionPerformed
        tabManipulator.openUrl(TabManipulator.WELCOME_URL);
    }//GEN-LAST:event_welcomeMenuItemActionPerformed

    private void sourcesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourcesMenuItemActionPerformed
        tabManipulator.openUrl(TabManipulator.SOURCES_URL);
    }//GEN-LAST:event_sourcesMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem closeTabMenuItem;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem nextTabMenuItem;
    private javax.swing.JMenuItem prevTabMenuItem;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem searchMenuItem;
    private javax.swing.JMenuItem setupMenuItem;
    private javax.swing.JMenuItem sourcesMenuItem;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JMenu tabMenu;
    private javax.swing.JMenuItem transferMenuItem;
    private javax.swing.JMenuItem welcomeMenuItem;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
}