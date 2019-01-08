/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psd.client;

/**
 * @author diogo
 */
public class InvestorPage extends javax.swing.JFrame {

    /**
     * Creates new form InvestorPage
     */
    public InvestorPage(String username) {
        this.erlang = ErlangBridge.getInstance();
        this.username = username;
        initComponents();
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
        auctionsPane = new javax.swing.JScrollPane();
        auctionsList = new javax.swing.JList<>();
        fixedPane = new javax.swing.JScrollPane();
        fixedList = new javax.swing.JList<>();
        auctionsNotiPane = new javax.swing.JScrollPane();
        auctionsNotiArea = new javax.swing.JTextArea();
        fixedNotiPane = new javax.swing.JScrollPane();
        fixedNotiArea = new javax.swing.JTextArea();
        companyFieldAuction = new javax.swing.JTextField();
        interestFieldAuction = new javax.swing.JTextField();
        bidButton = new javax.swing.JButton();
        companyFieldFixed = new javax.swing.JTextField();
        amountFieldFixed = new javax.swing.JTextField();
        subscribeButton = new javax.swing.JButton();
        amountFieldAuction = new javax.swing.JTextField();
        menu = new javax.swing.JMenuBar();
        file = new javax.swing.JMenu();
        refresh = new javax.swing.JMenuItem();
        logout = new javax.swing.JMenuItem();
        quit = new javax.swing.JMenuItem();
        notifications = new javax.swing.JMenu();
        subscribe = new javax.swing.JMenuItem();
        unsubscribe = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1000, 500));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel1.setText("Loan Auctions");

        jLabel2.setText("Fixed Rate Loans");

        auctionsPane.setViewportView(auctionsList);

        fixedPane.setViewportView(fixedList);

        auctionsNotiArea.setColumns(20);
        auctionsNotiArea.setRows(5);
        auctionsNotiPane.setViewportView(auctionsNotiArea);

        fixedNotiArea.setColumns(20);
        fixedNotiArea.setRows(5);
        fixedNotiPane.setViewportView(fixedNotiArea);

        companyFieldAuction.setText("Company");
        companyFieldAuction.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                companyFieldAuctionFocusGained(evt);
            }
        });
        companyFieldAuction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyFieldAuctionActionPerformed(evt);
            }
        });

        interestFieldAuction.setText("Interest");
        interestFieldAuction.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                interestFieldAuctionFocusGained(evt);
            }
        });

        bidButton.setText("Bid");
        bidButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bidButtonMouseClicked(evt);
            }
        });

        companyFieldFixed.setText("Company");
        companyFieldFixed.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                companyFieldFixedFocusGained(evt);
            }
        });

        amountFieldFixed.setText("Amount");
        amountFieldFixed.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                amountFieldFixedFocusGained(evt);
            }
        });
        amountFieldFixed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                amountFieldFixedActionPerformed(evt);
            }
        });

        subscribeButton.setText("Subscribe");
        subscribeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subscribeButtonMouseClicked(evt);
            }
        });

        amountFieldAuction.setText("Amount");
        amountFieldAuction.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                amountFieldAuctionFocusGained(evt);
            }
        });

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

        notifications.setText("Notifications");

        subscribe.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        subscribe.setText("Subscribe Selected");
        notifications.add(subscribe);

        unsubscribe.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        unsubscribe.setText("Unsubscribe Selected");
        notifications.add(unsubscribe);

        menu.add(notifications);

        setJMenuBar(menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(auctionsPane, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                    .addComponent(auctionsNotiPane)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(companyFieldAuction, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(interestFieldAuction, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(amountFieldAuction, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(bidButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(372, 372, 372))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fixedPane)
                                    .addComponent(fixedNotiPane))
                                .addContainerGap())))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(companyFieldFixed, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(amountFieldFixed, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(subscribeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(auctionsPane, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fixedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(auctionsNotiPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fixedNotiPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(85, 85, 85)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(companyFieldAuction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(interestFieldAuction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bidButton)
                            .addComponent(amountFieldAuction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(84, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(companyFieldFixed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(amountFieldFixed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(subscribeButton))
                        .addGap(81, 81, 81))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        // TODO add your handling code here:
        if (this.erlang.logout(this.username)) {
            this.setVisible(false);
            new LoginPage().setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_logoutActionPerformed

    private void quitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_quitActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_refreshActionPerformed

    private void companyFieldAuctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyFieldAuctionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_companyFieldAuctionActionPerformed

    private void amountFieldFixedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_amountFieldFixedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_amountFieldFixedActionPerformed

    private void bidButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bidButtonMouseClicked
        // TODO add your handling code here:
        final String company = this.companyFieldAuction.getText();
        final int amount = Integer.parseInt(this.amountFieldAuction.getText());
        final double interest = Double.parseDouble(this.interestFieldAuction.getText());
        final boolean success = this.erlang.bidAuction(this.username, company, amount, interest);
        if (success) {

        }
    }//GEN-LAST:event_bidButtonMouseClicked

    private void subscribeButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subscribeButtonMouseClicked
        // TODO add your handling code here:
        final String company = this.companyFieldFixed.getText();
        final int amount = Integer.parseInt(this.amountFieldFixed.getText());
        final boolean success = this.erlang.subscribeFixed(this.username, company, amount);
        if (success) {

        }
    }//GEN-LAST:event_subscribeButtonMouseClicked

    private void companyFieldAuctionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_companyFieldAuctionFocusGained
        // TODO add your handling code here:
        companyFieldAuction.setText("");
    }//GEN-LAST:event_companyFieldAuctionFocusGained

    private void interestFieldAuctionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_interestFieldAuctionFocusGained
        // TODO add your handling code here:
        interestFieldAuction.setText("");
    }//GEN-LAST:event_interestFieldAuctionFocusGained

    private void amountFieldAuctionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_amountFieldAuctionFocusGained
        // TODO add your handling code here:
        amountFieldAuction.setText("");
    }//GEN-LAST:event_amountFieldAuctionFocusGained

    private void companyFieldFixedFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_companyFieldFixedFocusGained
        // TODO add your handling code here:
        companyFieldFixed.setText("");
    }//GEN-LAST:event_companyFieldFixedFocusGained

    private void amountFieldFixedFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_amountFieldFixedFocusGained
        // TODO add your handling code here:
        amountFieldFixed.setText("");
    }//GEN-LAST:event_amountFieldFixedFocusGained

    private final String username;
    private final ErlangBridge erlang;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField amountFieldAuction;
    private javax.swing.JTextField amountFieldFixed;
    private javax.swing.JList<String> auctionsList;
    private javax.swing.JTextArea auctionsNotiArea;
    private javax.swing.JScrollPane auctionsNotiPane;
    private javax.swing.JScrollPane auctionsPane;
    private javax.swing.JButton bidButton;
    private javax.swing.JTextField companyFieldAuction;
    private javax.swing.JTextField companyFieldFixed;
    private javax.swing.JMenu file;
    private javax.swing.JList<String> fixedList;
    private javax.swing.JTextArea fixedNotiArea;
    private javax.swing.JScrollPane fixedNotiPane;
    private javax.swing.JScrollPane fixedPane;
    private javax.swing.JTextField interestFieldAuction;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JMenuItem logout;
    private javax.swing.JMenuBar menu;
    private javax.swing.JMenu notifications;
    private javax.swing.JMenuItem quit;
    private javax.swing.JMenuItem refresh;
    private javax.swing.JMenuItem subscribe;
    private javax.swing.JButton subscribeButton;
    private javax.swing.JMenuItem unsubscribe;
    // End of variables declaration//GEN-END:variables
}
