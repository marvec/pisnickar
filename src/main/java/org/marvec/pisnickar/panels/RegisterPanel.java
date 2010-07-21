package org.marvec.pisnickar.panels;

import javax.swing.JOptionPane;
import org.marvec.pisnickar.RegisterUtil;
import org.marvec.pisnickar.tabs.TabManipulator;

/**
 *
 * @author marvec
 */
public class RegisterPanel extends javax.swing.JPanel {

    private RegisterUtil register;
    private TabManipulator tab;

    /** Creates new form RegisterPanel */
    public RegisterPanel(TabManipulator tab, RegisterUtil register) {
        initComponents();

        this.tab = tab;
        this.register = register;
        if (register.getUser() != null) {
            switchToRegistered();
        }

    }

    private void switchToRegistered() {
        jLabel1.setText("Tento Písničkář je registrován na uživatele:");
        registerField.setText(register.getUser());
        registerField.setEditable(false);
        registerButton.setEnabled(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        registerField = new javax.swing.JTextField();
        registerButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.marvec.pisnickar.PisnickarApp.class).getContext().getResourceMap(RegisterPanel.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        registerField.setText(resourceMap.getString("registerField.text")); // NOI18N
        registerField.setName("registerField"); // NOI18N

        registerButton.setText(resourceMap.getString("registerButton.text")); // NOI18N
        registerButton.setName("registerButton"); // NOI18N
        registerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(null);
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextPane1.setBorder(null);
        jTextPane1.setEditable(false);
        jTextPane1.setText(resourceMap.getString("jTextPane1.text")); // NOI18N
        jTextPane1.setName("jTextPane1"); // NOI18N
        jScrollPane1.setViewportView(jTextPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addComponent(registerField, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(registerButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(registerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(registerButton)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerButtonActionPerformed
        if (registerField.getText().length() < 5) {
            JOptionPane.showMessageDialog(this, "Uživatelské jméno musí mít alespoň 5 znaků.",
                    "Chyba při registraci", JOptionPane.ERROR_MESSAGE);
        } else if (!register.isRegistered()) {
            register.register(registerField.getText());
            switchToRegistered();
        }
    }//GEN-LAST:event_registerButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JButton registerButton;
    private javax.swing.JTextField registerField;
    // End of variables declaration//GEN-END:variables

}