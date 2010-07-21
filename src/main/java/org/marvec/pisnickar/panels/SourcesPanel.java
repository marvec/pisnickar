/*
 * SourcesPanel.java
 *
 * Created on 24.5.2009, 15:16:27
 */

package org.marvec.pisnickar.panels;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.application.Application;
import org.marvec.pisnickar.dialogs.NewSourceDialog;
import org.marvec.pisnickar.songs.FileSongSource;
import org.marvec.pisnickar.songs.SongSource;
import org.marvec.pisnickar.songs.SourceManager;
import org.marvec.pisnickar.tabs.TabManipulator;

/**
 *
 * @author marvec
 */
public class SourcesPanel extends javax.swing.JPanel {
    private static final long serialVersionUID = 4042374650166053772L;

    SourceManager source;
    TabManipulator manipulator;

    private void save() throws IOException {
        source.store(Application.getInstance().getContext().getLocalStorage().getDirectory());
    }

    /** Creates new form SourcesPanel */
    public SourcesPanel(TabManipulator manipulator, SourceManager source) {
        this.source = source;
        this.manipulator = manipulator;

        initComponents();
        fillInTable();
    }

    private void fillInTable() {
        DefaultTableModel m = (DefaultTableModel) sourcesTable.getModel();
        String[] sources = source.getSourceList();
        m.setRowCount(sources.length);

        int row = 0;
        for (String id: sources) {
           SongSource s = source.getSourceById(id);
           m.setValueAt(s.getId(), row, 0);
           m.setValueAt(s.getType(), row, 1);
           m.setValueAt(s.getCount(), row, 2);
           m.setValueAt(s.isReadOnly() ? "Ano" : "Ne", row, 3);
           m.setValueAt(s.isEnabled() ? "Ano" : "Ne", row, 4);
           row++;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        saveCloseButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        sourcesTable = new javax.swing.JTable();
        removejButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();

        setName("Form"); // NOI18N

        saveCloseButton.setMnemonic('u');
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.marvec.pisnickar.PisnickarApp.class).getContext().getResourceMap(SourcesPanel.class);
        saveCloseButton.setText(resourceMap.getString("saveCloseButton.text")); // NOI18N
        saveCloseButton.setName("saveCloseButton"); // NOI18N
        saveCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveCloseButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        sourcesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Typ", "Velikost", "Pouze pro čtení", "Povolený"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        sourcesTable.setName("sourcesTable"); // NOI18N
        jScrollPane1.setViewportView(sourcesTable);

        removejButton.setMnemonic('o');
        removejButton.setText(resourceMap.getString("removejButton.text")); // NOI18N
        removejButton.setName("removejButton"); // NOI18N
        removejButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removejButtonActionPerformed(evt);
            }
        });

        editButton.setMnemonic('r');
        editButton.setText(resourceMap.getString("editButton.text")); // NOI18N
        editButton.setName("editButton"); // NOI18N
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        addButton.setMnemonic('p');
        addButton.setText(resourceMap.getString("addButton.text")); // NOI18N
        addButton.setName("addButton"); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removejButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveCloseButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(removejButton)
                    .addComponent(editButton)
                    .addComponent(saveCloseButton)
                    .addComponent(addButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveCloseButtonActionPerformed
        try {
            save();
            manipulator.closeCurrentTab();
        } catch (IOException ex) {
            Logger.getLogger(SourcesPanel.class.getName()).log(Level.SEVERE, "Cannot save source manager.", ex);
            JOptionPane.showMessageDialog(this, "Nastavení zdrojů se nepodařilo uložit.", "Chyba při ukládání", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveCloseButtonActionPerformed

    private void removejButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removejButtonActionPerformed
        String[] sources = source.getSourceList();

        int sel = sourcesTable.getSelectedRow();
        int result = JOptionPane.showConfirmDialog(this, "Skutečně chcete odebrat zdroj " + sources[sel] + "?",
                "Odebrání zdroje", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                source.removeSource(source.getSourceById(sources[sel]));
            } catch (IOException ex) {
                Logger.getLogger(SourcesPanel.class.getName()).log(Level.SEVERE, "Cannot remove song source.", ex);
                JOptionPane.showMessageDialog(this, "Odebíraný zdroj se nepodařilo uzavřít. Odebírání bylo zrušeno.",
                        "Chyba při odebírání", JOptionPane.ERROR_MESSAGE);
            }
        }

        fillInTable();
    }//GEN-LAST:event_removejButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        NewSourceDialog dlg = new NewSourceDialog(manipulator.getParentFrame(), true);
        dlg.setLocationRelativeTo(manipulator.getParentFrame());
        dlg.setVisible(true);

        if (dlg.getResult() == NewSourceDialog.APPROVED_FILE) {
            try {
                SongSource s = new FileSongSource();
                s.open(null, dlg.getSelectedFile());
                source.addSource(s);
            } catch (IOException ex) {
                Logger.getLogger(SourcesPanel.class.getName()).log(Level.SEVERE, "File source " +
                        dlg.getSelectedFile() + " cannot be used.", ex);
                JOptionPane.showMessageDialog(this, "Nepodařilo se otevřít ani vytvořit souborový zdroj.",
                        "Chybný zdroj", JOptionPane.ERROR_MESSAGE);
            }
        } else if (dlg.getResult() != NewSourceDialog.CANCELED) {
            Logger.getLogger(SourcesPanel.class.getName()).log(Level.SEVERE, "Invalid source has been requested for creation.");
            JOptionPane.showMessageDialog(this, "Byl vybrán neznámý typ zdroje.",
                    "Neznámý zdroj", JOptionPane.ERROR_MESSAGE);
        }
        
        fillInTable();
    }//GEN-LAST:event_addButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        String[] sources = source.getSourceList();
        int sel = sourcesTable.getSelectedRow();
        SongSource s = source.getSourceById(sources[sel]);
        s.setEnabled(!s.isEnabled());
        fillInTable();
    }//GEN-LAST:event_editButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton editButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton removejButton;
    private javax.swing.JButton saveCloseButton;
    private javax.swing.JTable sourcesTable;
    // End of variables declaration//GEN-END:variables

}
