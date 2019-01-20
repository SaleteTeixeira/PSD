/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psd.client;

/**
 *
 * @author diogo
 */
public class CompanyPage extends javax.swing.JFrame {

    /**
     * Creates new form InvestorPage
     * @param username
     */
    public CompanyPage(String username) {
        this.username = username;
        initComponents();
        refresh();
    }

    private void refresh() {
        Messages.CompanyInfoAuctionReply areply = ErlangBridge.getInstance().currentAuction(username);
        Messages.CompanyInfoFixedReply freply = ErlangBridge.getInstance().currentFixed(username);
        this.auctionsArea.setText("");
        this.fixedArea.setText("");
        if (areply.getEntry().getAmount() != 0) {
            Messages.AuctionEntry entry = areply.getEntry();
            this.auctionsArea.append(username 
                    + "\t" 
                    + entry.getAmount() 
                    + "\t" 
                    + entry.getInterest() 
                    + "\n");
        }
        if(freply.getEntry().getAmount() != 0) {
            Messages.FixedEntry entry = freply.getEntry();
            this.fixedArea.append(username 
                    + "\t" 
                    + entry.getAmount() 
                    + "\t" 
                    + entry.getInterest() 
                    + "\n");
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        auctionsNotiPane = new javax.swing.JScrollPane();
        auctionsNotiArea = new javax.swing.JTextArea();
        fixedNotiPane = new javax.swing.JScrollPane();
        fixedNotiArea = new javax.swing.JTextArea();
        amountAuctionField = new javax.swing.JTextField();
        maxInterestField = new javax.swing.JTextField();
        createAuctionButton = new javax.swing.JButton();
        amountFixedField = new javax.swing.JTextField();
        createFixedButton = new javax.swing.JButton();
        interestField = new javax.swing.JTextField();
        auctionsPane = new javax.swing.JScrollPane();
        auctionsArea = new javax.swing.JTextArea();
        fixedPane = new javax.swing.JScrollPane();
        fixedArea = new javax.swing.JTextArea();
        menu = new javax.swing.JMenuBar();
        file = new javax.swing.JMenu();
        refresh = new javax.swing.JMenuItem();
        logout = new javax.swing.JMenuItem();
        quit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel1.setText("Loan Auctions");

        jLabel2.setText("Fixed Rate Loans");

        auctionsNotiArea.setColumns(20);
        auctionsNotiArea.setRows(5);
        auctionsNotiPane.setViewportView(auctionsNotiArea);

        fixedNotiArea.setColumns(20);
        fixedNotiArea.setRows(5);
        fixedNotiPane.setViewportView(fixedNotiArea);

        amountAuctionField.setText("Amount");
        amountAuctionField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                amountAuctionFieldFocusGained(evt);
            }
        });
        amountAuctionField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                amountAuctionFieldActionPerformed(evt);
            }
        });

        maxInterestField.setText("Maximum Interest");
        maxInterestField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                maxInterestFieldFocusGained(evt);
            }
        });

        createAuctionButton.setText("Create");
        createAuctionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                createAuctionButtonMouseClicked(evt);
            }
        });

        amountFixedField.setText("Amount");
        amountFixedField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                amountFixedFieldFocusGained(evt);
            }
        });
        amountFixedField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                amountFixedFieldActionPerformed(evt);
            }
        });

        createFixedButton.setText("Create");
        createFixedButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                createFixedButtonMouseClicked(evt);
            }
        });
        createFixedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createFixedButtonActionPerformed(evt);
            }
        });

        interestField.setText("Interest");
        interestField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                interestFieldFocusGained(evt);
            }
        });

        auctionsArea.setColumns(20);
        auctionsArea.setRows(5);
        auctionsPane.setViewportView(auctionsArea);

        fixedArea.setColumns(20);
        fixedArea.setRows(5);
        fixedPane.setViewportView(fixedArea);

        file.setText("File");

        refresh.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, 0));
        refresh.setText("Refresh");
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshActionPerformed(evt);
            }
        });
        file.add(refresh);

        logout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        logout.setText("Logout");
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });
        file.add(logout);

        quit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        quit.setText("Quit");
        quit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitActionPerformed(evt);
            }
        });
        file.add(quit);

        menu.add(file);

        setJMenuBar(menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(auctionsNotiPane)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(amountAuctionField, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(maxInterestField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(createAuctionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(auctionsPane))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(amountFixedField, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(interestField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(createFixedButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(fixedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fixedNotiPane))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(auctionsPane, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(fixedPane))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(fixedNotiPane, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(createFixedButton)
                            .addComponent(interestField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(amountFixedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(auctionsNotiPane, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(createAuctionButton)
                            .addComponent(maxInterestField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(amountAuctionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        // TODO add your handling code here:
        ErlangBridge.getInstance().logout(this.username);
        ErlangBridge.clean();
        this.setVisible(false);
        new LoginPage().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_logoutActionPerformed

    private void quitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_quitActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        // TODO add your handling code here:
        refresh();
    }//GEN-LAST:event_refreshActionPerformed

    private void amountAuctionFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_amountAuctionFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_amountAuctionFieldActionPerformed

    private void amountFixedFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_amountFixedFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_amountFixedFieldActionPerformed

    private void createFixedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createFixedButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_createFixedButtonActionPerformed

    private void createAuctionButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_createAuctionButtonMouseClicked
        // TODO add your handling code here:
        int amount;
        try {
            amount = Integer.parseInt(this.amountAuctionField.getText());
            if (amount <= 0) {
                throw new NumberFormatException();
            }
        } catch(NumberFormatException e) {
            this.auctionsNotiArea.append("Error parsing amount\n");
            return;
        }
        double interest;
        try {
            interest = Double.parseDouble(this.maxInterestField.getText());
            if (interest <= 0.0f) {
                throw new NumberFormatException();
            }
        } catch(NumberFormatException e) {
            this.auctionsNotiArea.append("Error parsing interest\n");
            return;
        }
        final Messages.Reply reply = ErlangBridge.getInstance().createAuction(this.username, amount, interest);
        if (reply.getResult()) {
            this.auctionsNotiArea.append("Successfull creation of auction with amount "
                    + amount
                    + " and max interest "
                    + interest
                    + "\n"
            );
        } else {
            this.auctionsNotiArea.append(reply.getMessage() + "\n");
        }
    }//GEN-LAST:event_createAuctionButtonMouseClicked

    private void createFixedButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_createFixedButtonMouseClicked
        // TODO add your handling code here:
        int amount;
        try {
            amount = Integer.parseInt(this.amountFixedField.getText());
            if (amount <= 0) {
                throw new NumberFormatException();
            }
        } catch(NumberFormatException e) {
            this.fixedNotiArea.append("Error parsing amount\n");
            return;
        }
        double interest;
        try {
            interest = Double.parseDouble(this.interestField.getText());
            if (interest <= 0) {
                throw new NumberFormatException();
            }
        } catch(NumberFormatException e) {
            this.fixedNotiArea.append("Error parsing interest\n");
            return;
        }
        final Messages.Reply reply = ErlangBridge.getInstance().createLoan(this.username, amount, interest);
        if (reply.getResult()) {
            this.fixedNotiArea.append("Successfull creation of fixed loan with amount "
                    + amount
                    + "\n"
            );
        } else {
            this.fixedNotiArea.append(reply.getMessage() + "\n");
        }
    }//GEN-LAST:event_createFixedButtonMouseClicked

    private void amountAuctionFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_amountAuctionFieldFocusGained
        // TODO add your handling code here:
        if (this.amountAuctionField.getText().equals("Amount")) {
            this.amountAuctionField.setText("");
        }
    }//GEN-LAST:event_amountAuctionFieldFocusGained

    private void maxInterestFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_maxInterestFieldFocusGained
        // TODO add your handling code here:
        if (this.maxInterestField.getText().equals("Maximum Interest")) {
            this.maxInterestField.setText("");
        }
    }//GEN-LAST:event_maxInterestFieldFocusGained

    private void amountFixedFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_amountFixedFieldFocusGained
        // TODO add your handling code here:
        if (this.amountFixedField.getText().equals("Amount")) {
            this.amountFixedField.setText("");
        }
    }//GEN-LAST:event_amountFixedFieldFocusGained

    private void interestFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_interestFieldFocusGained
        // TODO add your handling code here:
        if (this.interestField.getText().equals("Interest")) {
            this.interestField.setText("");
        }
    }//GEN-LAST:event_interestFieldFocusGained

    private final String username;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField amountAuctionField;
    private javax.swing.JTextField amountFixedField;
    private javax.swing.JTextArea auctionsArea;
    private javax.swing.JTextArea auctionsNotiArea;
    private javax.swing.JScrollPane auctionsNotiPane;
    private javax.swing.JScrollPane auctionsPane;
    private javax.swing.JButton createAuctionButton;
    private javax.swing.JButton createFixedButton;
    private javax.swing.JMenu file;
    private javax.swing.JTextArea fixedArea;
    private javax.swing.JTextArea fixedNotiArea;
    private javax.swing.JScrollPane fixedNotiPane;
    private javax.swing.JScrollPane fixedPane;
    private javax.swing.JTextField interestField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JMenuItem logout;
    private javax.swing.JTextField maxInterestField;
    private javax.swing.JMenuBar menu;
    private javax.swing.JMenuItem quit;
    private javax.swing.JMenuItem refresh;
    // End of variables declaration//GEN-END:variables
}
