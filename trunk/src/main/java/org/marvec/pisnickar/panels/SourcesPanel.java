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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

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

        jButton1.setMnemonic('o');
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jButton2.setMnemonic('r');
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        jButton3.setMnemonic('p');
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
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
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(saveCloseButton)
                    .addComponent(jButton3))
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton saveCloseButton;
    private javax.swing.JTable sourcesTable;
    // End of variables declaration//GEN-END:variables

}
