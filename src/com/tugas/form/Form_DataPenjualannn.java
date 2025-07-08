package com.tugas.form;
import com.tugas.main.MenuUtama;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.tugas.koneksi.koneksi;
import java.awt.event.KeyEvent;


public class Form_DataPenjualannn extends javax.swing.JPanel {
private Connection conn = new koneksi().connect();
private DefaultTableModel tabmode;
    
    public Form_DataPenjualannn() {
        initComponents();
        datatable();
    }
        protected void datatable() {
        Object[] Baris = {
            "ID Penjualan", "ID Booking", "Rumah", "Customer", "Karyawan",
            "Tanggal", "Harga Jual", "Metode", "Uang Muka", "Tenor"
        };
        tabmode = new DefaultTableModel(null, Baris);
        String cariitem = txtcari.getText();

        try {
           String sql = "SELECT p.id_penjualan, p.id_booking, r.alamat, c.namacust, k.nama, " +
             "p.tanggal_penjualan, p.harga_jual, p.metode_pembayaran, p.uang_muka, p.tenor " + 
             "FROM penjualan_rumah p " +
             "JOIN booking b ON p.id_booking = b.id_booking " +
             "JOIN rumah r ON p.id_rumah = r.id_rumah " +
             "JOIN customer c ON p.id_customer = c.id_customer " +
             "JOIN karyawan k ON p.id_karyawan = k.id_karyawan " +
             "WHERE p.id_penjualan LIKE '%" + cariitem + "%' " +
             "OR c.namacust LIKE '%" + cariitem + "%' " +
             "ORDER BY p.id_penjualan ASC";


            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()) {
                tabmode.addRow(new Object[] {
                    hasil.getString("id_penjualan"),
                    hasil.getString("id_booking"),
                    hasil.getString("alamat"),
                    hasil.getString("namacust"),
                    hasil.getString("nama"),
                    hasil.getString("tanggal_penjualan"),
                    hasil.getString("harga_jual"),
                    hasil.getString("metode_pembayaran"),
                    hasil.getString("uang_muka"),
                    hasil.getString("tenor")
                });
            }
            tblrmh.setModel(tabmode);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data gagal dipanggil: " + e);
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
        tblrmh = new com.tugas.swing.table.Table();
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
        btnedit.setText("Edit");
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

        tblrmh.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tblrmh);

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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI Historic", 1, 26)); // NOI18N
        jLabel1.setText("Data Penjualan");

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
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 668, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btntambah, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnhapus, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnedit, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnbatal, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btncari, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator1))))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGap(5, 5, 5)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btntambah)
                        .addComponent(btnhapus)
                        .addComponent(btnedit)
                        .addComponent(btnbatal))
                    .addComponent(btncari, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtcari, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(18, 18, 18)
                .addComponent(roundPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btntambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntambahActionPerformed
// Membuat dan menampilkan form input rumah sebagai dialog
    MenuUtama.getMenuUtama().showForm(new Form_Penjualannnn());
    }//GEN-LAST:event_btntambahActionPerformed

    private void btnhapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhapusActionPerformed
       int ok = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data penjualan?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
    
    if (ok == JOptionPane.YES_OPTION) {
        int SelectedRow = tblrmh.getSelectedRow();
        
        if (SelectedRow != -1) {
            String idPenjualan = tblrmh.getValueAt(SelectedRow, 0).toString(); // kolom 0 = id_penjualan

            try {
                // ✅ Ambil id_rumah dari data penjualan
                String sqlGetRumah = "SELECT id_rumah FROM penjualan_rumah WHERE id_penjualan = ?";
                PreparedStatement statGet = conn.prepareStatement(sqlGetRumah);
                statGet.setString(1, idPenjualan);
                ResultSet rs = statGet.executeQuery();

                String idRumah = null;
                if (rs.next()) {
                    idRumah = rs.getString("id_rumah");
                }

                // ✅ Hapus data penjualan
                String sqlDelete = "DELETE FROM penjualan_rumah WHERE id_penjualan = ?";
                PreparedStatement statDel = conn.prepareStatement(sqlDelete);
                statDel.setString(1, idPenjualan);
                statDel.executeUpdate();

                // ✅ Ubah status rumah menjadi "Tersedia" jika idRumah ditemukan
                if (idRumah != null) {
                    String sqlUpdateStatus = "UPDATE rumah SET status = 'dibooking' WHERE id_rumah = ?";
                    PreparedStatement statUpdate = conn.prepareStatement(sqlUpdateStatus);
                    statUpdate.setString(1, idRumah);
                    statUpdate.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
                datatable(); // Refresh tabel
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data gagal dihapus: " + e.getMessage());
            }
        }
    }
    }//GEN-LAST:event_btnhapusActionPerformed

    private void btneditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditActionPerformed
    int selectedRow = tblrmh.getSelectedRow();
    if (selectedRow != -1) {
        // Ambil data dari tabel
        String idPenjualan = tblrmh.getValueAt(selectedRow, 0).toString();
        String idBooking = tblrmh.getValueAt(selectedRow, 1).toString();
        String rumah = tblrmh.getValueAt(selectedRow, 2).toString();
        String customer = tblrmh.getValueAt(selectedRow, 3).toString();
        String karyawan = tblrmh.getValueAt(selectedRow, 4).toString();
        String tanggal = tblrmh.getValueAt(selectedRow, 5).toString();
        String harga = tblrmh.getValueAt(selectedRow, 6).toString();
        String metode = tblrmh.getValueAt(selectedRow, 7).toString();
        String uangMuka = tblrmh.getValueAt(selectedRow, 8).toString();
        String tenor = tblrmh.getValueAt(selectedRow, 9) != null ? tblrmh.getValueAt(selectedRow, 9).toString() : "";

        // Buat Form_Penjualan dan set datanya
        Form_Penjualannnn form = new Form_Penjualannnn();
        form.setEditMode(true); // Aktifkan mode edit
        form.setFieldData(idPenjualan, idBooking, rumah, customer, karyawan, tanggal, harga, metode, uangMuka, tenor);

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
    private com.tugas.swing.table.Table tblrmh;
    private javax.swing.JTextField txtcari;
    // End of variables declaration//GEN-END:variables
}
