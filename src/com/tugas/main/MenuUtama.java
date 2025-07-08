package com.tugas.main;

import java.awt.Component;
import com.tugas.form.Form_Dashboard;
import com.tugas.form.Form_DataBooking;
import com.tugas.form.Form_DataCustomer;
import com.tugas.form.Form_DataKaryawann;
import com.tugas.form.Form_DataPembayaran;
import com.tugas.form.Form_DataPenjualannn;
import com.tugas.form.Form_Empty;
import com.tugas.form.Form_DataRumah;
import com.tugas.form.Form_Kebijakann;
import com.tugas.form.Form_LaporanDataKaryawan;
import com.tugas.form.Form_LaporanPembayaranCustomer;
import com.tugas.form.Form_LaporanPenjualanRumah;
import com.tugas.form.Form_LaporanRumahTersedia;
import com.tugas.form.Form_Logout;
import com.tugas.form.Form_VersiAPK;
import com.tugas.form.Form_creator;
import com.tugas.menu.EventMenuSelected;

public class MenuUtama extends javax.swing.JFrame {  // Ganti di sini
    
    private static MenuUtama menuUtama;  // Ganti variabel static juga
    
    public MenuUtama() {
        initComponents();
        init();
    }
    
    private void init() {
        menuUtama = this;
        titleBar1.initJFram(this);
        menu.addEvent(new EventMenuSelected() {
            @Override
            public void menuSelected(int index, int indexSubMenu) {
                if (index == 0 && indexSubMenu == 0) {
                    showForm(new Form_Dashboard());
                } else if (index == 1 && indexSubMenu == 0) {
                    showForm(new Form_DataRumah());
                } else if (index == 2 && indexSubMenu == 0) {
                    showForm(new Form_DataKaryawann());
                } else if (index == 3 && indexSubMenu == 0) {
                    showForm(new Form_DataCustomer());
                }else if (index == 4 && indexSubMenu == 0) {
                    showForm(new Form_DataBooking());
                }else if (index == 5 && indexSubMenu == 0) {
                    showForm(new Form_DataPenjualannn());
                }else if (index == 6 && indexSubMenu == 0) {
                    showForm(new Form_DataPembayaran());
                }else if (index == 7 && indexSubMenu == 0) {
                    showForm(new Form_LaporanRumahTersedia());
                }else if (index == 8 && indexSubMenu == 0) {
                    showForm(new Form_LaporanDataKaryawan());
                }else if (index == 9 && indexSubMenu == 0) {
                    showForm(new Form_LaporanPenjualanRumah());
                }else if (index == 10 && indexSubMenu == 0) {
                    showForm(new Form_LaporanPembayaranCustomer());
                }else if (index == 11 && indexSubMenu == 1) {
                    showForm(new Form_creator());
                }else if (index == 11 && indexSubMenu == 2) {
                    showForm(new Form_VersiAPK());
                }else if (index == 11 && indexSubMenu == 3) {
                    showForm(new Form_Kebijakann());
                }else if (index == 12 && indexSubMenu == 0) {
                    showForm(new Form_Logout());
                }else {
                    showForm(new Form_Empty(index + " " + indexSubMenu));
                }
            }
        });
        menu.setSelectedIndex(0, 0); // default tampilkan dashboard
    }
    
    public void showForm(Component com) {
        body.removeAll();
        body.add(com);
        body.repaint();
        body.revalidate();
    }
    
    public static MenuUtama getMenuUtama() {  // Sesuaikan getter juga
        return menuUtama;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        panelmenu = new javax.swing.JPanel();
        menu = new com.tugas.menu.Menu();
        titleBar1 = new com.tugas.swing.tittlebar.TitleBar();
        body = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        background.setBackground(new java.awt.Color(245, 245, 245));

        panelmenu.setBackground(new java.awt.Color(255, 255, 255));

        titleBar1.setBackground(new java.awt.Color(242, 242, 242));

        javax.swing.GroupLayout panelmenuLayout = new javax.swing.GroupLayout(panelmenu);
        panelmenu.setLayout(panelmenuLayout);
        panelmenuLayout.setHorizontalGroup(
            panelmenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelmenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
        );
        panelmenuLayout.setVerticalGroup(
            panelmenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelmenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE))
        );

        body.setBackground(new java.awt.Color(255, 255, 255));
        body.setOpaque(false);
        body.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout backgroundLayout = new javax.swing.GroupLayout(background);
        background.setLayout(backgroundLayout);
        backgroundLayout.setHorizontalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addComponent(panelmenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE))
        );
        backgroundLayout.setVerticalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelmenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(body, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuUtama().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel background;
    private javax.swing.JPanel body;
    private com.tugas.menu.Menu menu;
    private javax.swing.JPanel panelmenu;
    private com.tugas.swing.tittlebar.TitleBar titleBar1;
    // End of variables declaration//GEN-END:variables
}
