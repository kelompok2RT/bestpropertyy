package com.tugas.form;
import com.tugas.main.MenuUtama;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.tugas.koneksi.koneksi;
import java.awt.event.KeyEvent;


public class Form_DataBooking extends javax.swing.JPanel {
private Connection conn = new koneksi().connect();
private DefaultTableModel tabmode;
    
    public Form_DataBooking() {
        initComponents();
        datatable();
    }
    protected void datatable() {
    Object[] Baris = {"ID Booking", "Rumah", "Customer", "Karyawan", "Tanggal", "Uang Muka", "Status", "Keterangan"};
    tabmode = new DefaultTableModel(null, Baris);
    String cariitem = txtcari.getText();

    try {
        String sql = "SELECT b.id_booking, r.kode_rumah AS rumah, c.namacust AS customer, k.nama AS karyawan, " +
                     "b.tglbooking, b.dpayment, b.status, b.keterangan " +
                     "FROM booking b " +
                     "JOIN customer c ON b.id_customer = c.id_customer " +
                     "JOIN karyawan k ON b.id_karyawan = k.id_karyawan " +
                     "JOIN rumah r ON b.id_rumah = r.id_rumah " +
                     "WHERE b.id_booking LIKE '%" + cariitem + "%' " +
                     "   OR c.namacust LIKE '%" + cariitem + "%' " +
                     "   OR k.nama LIKE '%" + cariitem + "%' " +
                     "ORDER BY b.id_booking ASC";

        Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        while (hasil.next()) {
            tabmode.addRow(new Object[]{
                hasil.getString("id_booking"),
                hasil.getString("rumah"),
                hasil.getString("customer"),
                hasil.getString("karyawan"),
                hasil.getString("tglbooking"),
                hasil.getString("dpayment"),
                hasil.getString("status"),
                hasil.getString("keterangan")
            });
        }
        tblkry.setModel(tabmode);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Data gagal dipanggil: " + e.getMessage());
    }
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btntambah = new javax.swing.JButton();
        btnhapus = new javax.swing.JButton();
        btnedit = new javax.swing.JButton();
        btncari = new javax.swing.JButton();
        txtcari = new javax.swing.JTextField();
        roundPanel1 = new com.tugas.swing.RoundPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblkry = new com.tugas.swing.table.Table();
        jLabel1 = new javax.swing.JLabel();
        btnbatal = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setBackground(new java.awt.Color(255, 255, 255));

        btntambah.setBackground(new java.awt.Color(0, 204, 51));
        btntambah.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btntambah.setForeground(new java.awt.Color(255, 255, 255));
        btntambah.setText("Tambah Data");
        btntambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntambahActionPerformed(evt);
            }
        });

        btnhapus.setBackground(new java.awt.Color(255, 51, 0));
        btnhapus.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnhapus.setForeground(new java.awt.Color(255, 255, 255));
        btnhapus.setText("Hapus Data");
        btnhapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhapusActionPerformed(evt);
            }
        });

        btnedit.setBackground(new java.awt.Color(255, 204, 0));
        btnedit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnedit.setForeground(new java.awt.Color(255, 255, 255));
        btnedit.setText("Edit Data");
        btnedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditActionPerformed(evt);
            }
        });

        btncari.setBackground(new java.awt.Color(0, 204, 255));
        btncari.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btncari.setForeground(new java.awt.Color(255, 255, 255));
        btncari.setText("Cari");
        btncari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncariActionPerformed(evt);
            }
        });

        txtcari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtcariKeyPressed(evt);
            }
        });

        tblkry.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblkry);

        javax.swing.GroupLayout roundPanel1Layout = new javax.swing.GroupLayout(roundPanel1);
        roundPanel1.setLayout(roundPanel1Layout);
        roundPanel1Layout.setHorizontalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 857, Short.MAX_VALUE)
                .addContainerGap())
        );
        roundPanel1Layout.setVerticalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI Historic", 1, 26)); // NOI18N
        jLabel1.setText("Data Booking");

        btnbatal.setBackground(new java.awt.Color(153, 51, 255));
        btnbatal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnbatal.setForeground(new java.awt.Color(255, 255, 255));
        btnbatal.setText("Batal");
        btnbatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbatalActionPerformed(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(143, 148, 251));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btntambah, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnhapus, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnedit, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnbatal, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btncari, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtcari, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btntambah)
                        .addComponent(btnhapus)
                        .addComponent(btnedit)
                        .addComponent(btnbatal)
                        .addComponent(btncari)))
                .addGap(18, 18, 18)
                .addComponent(roundPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 547, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btntambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntambahActionPerformed
// Membuat dan menampilkan form input rumah sebagai dialog
    MenuUtama.getMenuUtama().showForm(new Form_Booking());
    }//GEN-LAST:event_btntambahActionPerformed

    private void btnhapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhapusActionPerformed
      int ok = JOptionPane.showConfirmDialog(null, "Hapus data booking ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
if (ok == JOptionPane.YES_OPTION) {
    int SelectedRow = tblkry.getSelectedRow();
    if (SelectedRow != -1) {
        String idBooking = tblkry.getValueAt(SelectedRow, 0).toString();
        String idRumah = null;

        try {
            // 1️⃣ Ambil id_rumah dari data booking
            String sqlGetRumah = "SELECT id_rumah FROM booking WHERE id_booking = ?";
            PreparedStatement pstGet = conn.prepareStatement(sqlGetRumah);
            pstGet.setString(1, idBooking);
            ResultSet rs = pstGet.executeQuery();

            if (rs.next()) {
                idRumah = rs.getString("id_rumah");
            }

            // 2️⃣ Hapus data booking
            String sqlDelete = "DELETE FROM booking WHERE id_booking = ?";
            PreparedStatement pstDel = conn.prepareStatement(sqlDelete);
            pstDel.setString(1, idBooking);
            pstDel.executeUpdate();

            // 3️⃣ Ubah status rumah menjadi "Tersedia" jika berhasil ambil id_rumah
            if (idRumah != null) {
                String sqlUpdateStatus = "UPDATE rumah SET status = 'Tersedia' WHERE id_rumah = ?";
                PreparedStatement pstStatus = conn.prepareStatement(sqlUpdateStatus);
                pstStatus.setString(1, idRumah);
                pstStatus.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data gagal dihapus: " + e.getMessage());
        }

        datatable(); // refresh tabel booking
    }
}

    }//GEN-LAST:event_btnhapusActionPerformed

    private void btneditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditActionPerformed
    int selectedRow = tblkry.getSelectedRow();
    if (selectedRow != -1) {
        // Ambil data dari tabel
        String idbooking = tblkry.getValueAt(selectedRow, 0).toString();
        String rumah = tblkry.getValueAt(selectedRow, 1).toString();
        String customer = tblkry.getValueAt(selectedRow, 2).toString();
        String karyawan = tblkry.getValueAt(selectedRow, 3).toString();
        String tgl = tblkry.getValueAt(selectedRow, 4).toString();
        String dp = tblkry.getValueAt(selectedRow, 5).toString();
        String status = tblkry.getValueAt(selectedRow, 6).toString();
        String keterangan = tblkry.getValueAt(selectedRow, 7).toString();



        // Buat form rumah baru dan isi datanya
        Form_Booking form = new Form_Booking();
        form.setData(idbooking, rumah, customer, karyawan, tgl, dp, status, keterangan);
        form.setEditMode(true);
        // Tampilkan form rumah di panel utama
        MenuUtama.getMenuUtama().showForm(form);
    } else {
        JOptionPane.showMessageDialog(this, "Silakan pilih data terlebih dahulu!");
    }
    }//GEN-LAST:event_btneditActionPerformed

    private void txtcariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariKeyPressed
        if (evt.getKeyCode()==KeyEvent.VK_ENTER){
            datatable();
        }
    }//GEN-LAST:event_txtcariKeyPressed

    private void btncariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncariActionPerformed
        datatable();
    }//GEN-LAST:event_btncariActionPerformed

    private void btnbatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbatalActionPerformed
        datatable();
    }//GEN-LAST:event_btnbatalActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnbatal;
    private javax.swing.JButton btncari;
    private javax.swing.JButton btnedit;
    private javax.swing.JButton btnhapus;
    private javax.swing.JButton btntambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private com.tugas.swing.RoundPanel roundPanel1;
    private com.tugas.swing.table.Table tblkry;
    private javax.swing.JTextField txtcari;
    // End of variables declaration//GEN-END:variables
}
