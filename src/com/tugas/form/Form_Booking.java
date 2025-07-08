package com.tugas.form;

import com.tugas.koneksi.koneksi;
import com.tugas.main.MenuUtama;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.table.DefaultTableModel;


public class Form_Booking extends javax.swing.JPanel {
private Connection conn = new koneksi().connect();
private DefaultTableModel tabmode;

    private boolean isEditMode = false;
    public Form_Booking() {
        initComponents();
        kosong();
        generateIdBooking();
        aktif();
        loadComboBoxRumah();
        loadComboBoxCustomer();
        loadComboBoxKaryawan();
    }
    protected void aktif(){
        txtidbook.requestFocus();
        cbcust.setSelectedItem(null);
         tglbook.setEditor(new JSpinner.DateEditor(tglbook,"yyyy/MM/dd"));
        
    }
    
    protected void kosong(){
        txtidbook.setText("");
        cbrmh.setSelectedItem(null);
        cbcust.setSelectedItem(null);
        cbkry.setSelectedItem(null);
        txtdp.setText("");
        cbsts.setSelectedItem(null);
    }
    public void setData(String idbooking,String rumah,String customer,String karyawan ,String tgl, String dp, String status, String keterangan){
        txtidbook.setText(idbooking);
        cbrmh.setSelectedItem(rumah);
        cbcust.setSelectedItem(customer);
        cbkry.setSelectedItem(karyawan);
        try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = sdf.parse(tgl);
        tglbook.setValue(parsedDate);
    } catch (ParseException e) {
        JOptionPane.showMessageDialog(this, "Format tanggal tidak valid: " + e.getMessage());
    }
        txtdp.setText(dp);
        cbsts.setSelectedItem(status);
    }
     public void setEditMode(boolean isEdit) {
        this.isEditMode = isEdit;
        if (isEdit) {
            btnsimpan.setText("UPDATE");
            txtidbook.setEnabled(false); // ID tidak boleh diubah saat update
        } else {
            btnsimpan.setText("Simpan");
            txtidbook.setEnabled(true);
        }
    }
     private void loadComboBoxCustomer() {
    try {
        String sql = "SELECT id_customer, namacust FROM customer";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        cbcust.removeAllItems();
        while (rs.next()) {
            String id = rs.getString("id_customer");
            String nama = rs.getString("namacust");
            cbcust.addItem(id + " - " + nama);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal load data customer: " + e.getMessage());
    }
}

private void loadComboBoxKaryawan() {
    try {
        String sql = "SELECT id_karyawan, nama FROM karyawan";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        cbkry.removeAllItems();
        while (rs.next()) {
            String id = rs.getString("id_karyawan");
            String nama = rs.getString("nama");
            cbkry.addItem(id + " - " + nama);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal load data karyawan: " + e.getMessage());
    }
}
private void loadComboBoxRumah() {
    try {
        cbrmh.removeAllItems(); // Kosongkan dulu sebelum isi ulang

        String sql = "SELECT id_rumah, kode_rumah FROM rumah WHERE status = 'tersedia' OR status = 'dibooking'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            String id = rs.getString("id_rumah");
            String kode = rs.getString("kode_rumah"); // tampilkan kode biar lebih informatif (opsional)
            cbrmh.addItem(id + " - " + kode); // atau cukup id saja: cbrmh.addItem(id);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal Load Rumah: " + e.getMessage());
    }
}

private void generateIdBooking() {
    try {
        String sql = "SELECT id_booking FROM booking ORDER BY id_booking DESC LIMIT 1";
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(sql);
        if (rs.next()) {
            String lastId = rs.getString("id_booking");
            int num = Integer.parseInt(lastId.substring(3)) + 1; // ambil angka dan tambah 1
            String newId = String.format("BKG%03d", num); // format ke PNJ001, PNJ002, ...
            txtidbook.setText(newId);
        } else {
            txtidbook.setText("BKG001"); // default jika belum ada data
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal generate ID Penjualan: " + e);
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtidbook = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        cbcust = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        btnKeluar = new javax.swing.JButton();
        BtnBatal = new javax.swing.JButton();
        btnsimpan = new javax.swing.JButton();
        cbrmh = new javax.swing.JComboBox<>();
        cbkry = new javax.swing.JComboBox<>();
        tglbook = new javax.swing.JSpinner();
        txtdp = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        cbsts = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtket = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();

        setOpaque(false);

        jPanel1.setBackground(new java.awt.Color(143, 148, 251));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(" Form Booking");

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(373, 373, 373)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jSeparator1))
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel13.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel13.setText("ID Booking");

        jLabel14.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel14.setText("Pilih Rumah");

        txtidbook.setEnabled(false);

        jLabel16.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel16.setText("Pilih Customer");

        jLabel20.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel20.setText("Tanggal");

        jLabel21.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel21.setText("Pilih Karyawan");

        btnKeluar.setBackground(new java.awt.Color(143, 148, 251));
        btnKeluar.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnKeluar.setForeground(new java.awt.Color(255, 255, 255));
        btnKeluar.setText("KELUAR");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        BtnBatal.setBackground(new java.awt.Color(143, 148, 251));
        BtnBatal.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        BtnBatal.setForeground(new java.awt.Color(255, 255, 255));
        BtnBatal.setText("BATAL");
        BtnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBatalActionPerformed(evt);
            }
        });

        btnsimpan.setBackground(new java.awt.Color(143, 148, 251));
        btnsimpan.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnsimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnsimpan.setText("SIMPAN");
        btnsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsimpanActionPerformed(evt);
            }
        });

        tglbook.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tglbook.setModel(new javax.swing.SpinnerDateModel());

        jLabel15.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel15.setText("Uang Muka");

        cbsts.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbsts.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "booking", "dibatalkan" }));
        cbsts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbstsActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel22.setText("Status");

        jLabel23.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel23.setText("Keterangan");

        txtket.setColumns(20);
        txtket.setRows(5);
        jScrollPane1.setViewportView(txtket);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/tugas/icon/20250704_193236_0000.png"))); // NOI18N
        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80)
                        .addComponent(BtnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(108, 108, 108)
                        .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(114, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel21)
                            .addComponent(jLabel16)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13)
                            .addComponent(cbkry, 0, 241, Short.MAX_VALUE)
                            .addComponent(cbcust, 0, 241, Short.MAX_VALUE)
                            .addComponent(txtidbook, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                            .addComponent(cbrmh, 0, 241, Short.MAX_VALUE)
                            .addComponent(jLabel20)
                            .addComponent(tglbook))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel23)
                            .addComponent(jScrollPane1)
                            .addComponent(cbsts, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22)
                            .addComponent(txtdp)
                            .addComponent(jLabel15))
                        .addGap(17, 17, 17)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtdp)
                            .addComponent(txtidbook, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbrmh, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel16))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbsts, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cbcust, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbkry, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel20))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tglbook, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnsimpan)
                    .addComponent(BtnBatal)
                    .addComponent(btnKeluar))
                .addGap(64, 64, 64))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        MenuUtama.getMenuUtama().showForm(new Form_DataBooking());
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void btnsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpanActionPerformed
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String fd = sdf.format(tglbook.getValue());

    // Ambil ID saja dari ComboBox yang formatnya "id - nama"
String idRumah = cbrmh.getSelectedItem().toString().split(" - ")[0];
String idCustomer = cbcust.getSelectedItem().toString().split(" - ")[0];
String idKaryawan = cbkry.getSelectedItem().toString().split(" - ")[0];

    if (idRumah.isEmpty() || idCustomer.isEmpty() || idKaryawan.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Pastikan semua data telah dipilih.");
        return;
    }
        if (isEditMode) {
            
            String sql = "UPDATE booking SET id_booking=?, id_rumah=?, id_customer=?, id_karyawan=?, tglbooking=?, dpayment=?, status=?, keterangan=? where id_booking ='"+txtidbook.getText()+"'";
            try {
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.setString(1, txtidbook.getText());
                stat.setString(2, idRumah);
                stat.setString(3, idCustomer);
                stat.setString(4, idKaryawan);
                stat.setString(5, fd);
                stat.setString(6, txtdp.getText());
                stat.setString(7, cbsts.getSelectedItem().toString());
                stat.setString(8, txtket.getText());

                stat.executeUpdate();

// Update status rumah jadi "Dibooking"
String sqlUpdateStatus = "UPDATE rumah SET status='dibooking' WHERE id_rumah=?";
PreparedStatement statStatus = conn.prepareStatement(sqlUpdateStatus);
statStatus.setString(1, idRumah);
statStatus.executeUpdate();

JOptionPane.showMessageDialog(null, "Data berhasil diupdate");
kosong();
setEditMode(false);

                kosong();
                setEditMode(false); // reset ke mode tambah
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data gagal diupdate: " + e);
            }
        } else {
            String sql = "INSERT INTO booking VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.setString(1, txtidbook.getText());
                stat.setString(2, idRumah);
                stat.setString(3, idCustomer);
                stat.setString(4, idKaryawan);
                stat.setString(5, fd);
                stat.setString(6, txtdp.getText());
                stat.setString(7, cbsts.getSelectedItem().toString());
                stat.setString(8, txtket.getText());
                stat.executeUpdate();

// Update status rumah jadi "Dibooking"
String sqlUpdateStatus = "UPDATE rumah SET status='dibooking' WHERE id_rumah=?";
PreparedStatement statStatus = conn.prepareStatement(sqlUpdateStatus);
statStatus.setString(1, idRumah);
statStatus.executeUpdate();

JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
kosong();

                kosong();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data gagal disimpan: " + e);
            }
        }
    }//GEN-LAST:event_btnsimpanActionPerformed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        kosong();
    }//GEN-LAST:event_BtnBatalActionPerformed

    private void cbstsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbstsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbstsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnBatal;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnsimpan;
    private javax.swing.JComboBox<String> cbcust;
    private javax.swing.JComboBox<String> cbkry;
    private javax.swing.JComboBox<String> cbrmh;
    private javax.swing.JComboBox<String> cbsts;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner tglbook;
    private javax.swing.JTextField txtdp;
    private javax.swing.JTextField txtidbook;
    private javax.swing.JTextArea txtket;
    // End of variables declaration//GEN-END:variables
}
