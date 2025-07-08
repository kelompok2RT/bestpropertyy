package com.tugas.form;
import com.tugas.main.MenuUtama;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.tugas.koneksi.koneksi;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;


public class Form_DataPembayaran extends javax.swing.JPanel {
private Connection conn = new koneksi().connect();
private DefaultTableModel tabmode;
    
    public Form_DataPembayaran() {
        initComponents();
        datatable();
    }
protected void datatable() {
    Object[] Kolom = {
        "ID Pembayaran", "ID Penjualan", "Tanggal Pembayaran", "Customer", "Karyawan",
        "Harga Jual", "Uang Muka", "Tenor", "Metode", "Total Dibayar", "Sisa Cicilan"
    };
    tabmode = new DefaultTableModel(null, Kolom);
    String cariitem = txtcari.getText();

    try {
        String sql = "SELECT pc.id_pembayaran, pc.id_penjualan, pc.tanggal_pembayaran, " +
                     "c.namacust AS nama_customer, k.nama AS nama_karyawan, " +
                     "p.harga_jual, p.uang_muka, p.tenor, " +
                     "pc.metode_pembayaran, pc.total_dibayar, pc.sisa_cicilan " +
                     "FROM pembayaran_cicilan pc " +
                     "JOIN penjualan_rumah p ON pc.id_penjualan = p.id_penjualan " +
                     "JOIN customer c ON p.id_customer = c.id_customer " +
                     "JOIN karyawan k ON p.id_karyawan = k.id_karyawan " +
                     "WHERE pc.id_pembayaran LIKE '%" + cariitem + "%' " +
                     "OR pc.id_penjualan LIKE '%" + cariitem + "%' " +
                     "OR c.namacust LIKE '%" + cariitem + "%' " +
                     "ORDER BY pc.id_pembayaran ASC";

        Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);

        // Format angka agar tidak pakai notasi E
        DecimalFormat df = new DecimalFormat("#,##0.00");

        while (hasil.next()) {
            tabmode.addRow(new Object[]{
                hasil.getString("id_pembayaran"),
                hasil.getString("id_penjualan"),
                hasil.getDate("tanggal_pembayaran"),
                hasil.getString("nama_customer"),
                hasil.getString("nama_karyawan"),
                df.format(hasil.getDouble("harga_jual")),
                df.format(hasil.getDouble("uang_muka")),
                hasil.getInt("tenor"),
                hasil.getString("metode_pembayaran"),
                df.format(hasil.getDouble("total_dibayar")),
                df.format(hasil.getDouble("sisa_cicilan"))
            });
        }

        tblpby.setModel(tabmode);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Data gagal dipanggil: " + e.getMessage());
    }
}
private void cetakNotaPembayaran(String idPembayaran) {
    try {
        // Ambil file .jasper dari resources (pastikan sudah dikompilasi dan ditempatkan di folder yang sesuai)
        InputStream jasperStream = getClass().getResourceAsStream("/com/tugas/report/nota_pembayaran_faktur_style.jasper");

        if (jasperStream == null) {
            JOptionPane.showMessageDialog(this, "File report tidak ditemukan.");
            return;
        }

        // Parameter untuk report
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id_pembayaran", idPembayaran);

        // Koneksi database
        Connection conn = new koneksi().connect();

        // Mengisi report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, parameters, conn);

        // Tampilkan preview
        JasperViewer viewer = new JasperViewer(jasperPrint, false);
        viewer.setTitle("Nota Pembayaran");
        viewer.setVisible(true);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal mencetak nota: " + e.getMessage());
        e.printStackTrace();
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btntambah = new javax.swing.JButton();
        btncari = new javax.swing.JButton();
        txtcari = new javax.swing.JTextField();
        roundPanel1 = new com.tugas.swing.RoundPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblpby = new com.tugas.swing.table.Table();
        jLabel1 = new javax.swing.JLabel();
        btncetak = new javax.swing.JButton();
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

        tblpby.setModel(new javax.swing.table.DefaultTableModel(
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
        tblpby.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblpbyMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblpby);

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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 535, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI Historic", 1, 26)); // NOI18N
        jLabel1.setText("Data Pembayaran");

        btncetak.setBackground(new java.awt.Color(153, 51, 255));
        btncetak.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btncetak.setForeground(new java.awt.Color(255, 255, 255));
        btncetak.setText("Cetak Data");
        btncetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncetakActionPerformed(evt);
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
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btntambah, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btncetak, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btncari, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btncari)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btncetak)
                                .addComponent(btntambah))
                            .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(roundPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btntambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntambahActionPerformed
// Membuat dan menampilkan form input rumah sebagai dialog
    MenuUtama.getMenuUtama().showForm(new Form_Pembayarann());
    }//GEN-LAST:event_btntambahActionPerformed

    private void txtcariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariKeyPressed
        if (evt.getKeyCode()==KeyEvent.VK_ENTER){
            datatable();
        }
    }//GEN-LAST:event_txtcariKeyPressed

    private void btncariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncariActionPerformed
        datatable();
    }//GEN-LAST:event_btncariActionPerformed

    private void btncetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncetakActionPerformed
       int selectedRow = tblpby.getSelectedRow();
    if (selectedRow != -1) {
        // Ambil ID Pembayaran dari kolom pertama
        String idPembayaran = tblpby.getValueAt(selectedRow, 0).toString();
        
        // Cetak berdasarkan ID Pembayaran
        cetakNotaPembayaran(idPembayaran);
    } else {
        JOptionPane.showMessageDialog(this, "Silakan pilih data pembayaran dari tabel terlebih dahulu!");
    }
    }//GEN-LAST:event_btncetakActionPerformed

    private void tblpbyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblpbyMouseClicked
        int baris = tblpby.getSelectedRow();
    if (baris != -1) {
        String idPembayaran = tblpby.getValueAt(baris, 0).toString(); // kolom pertama = ID Pembayaran
    }
    }//GEN-LAST:event_tblpbyMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btncari;
    private javax.swing.JButton btncetak;
    private javax.swing.JButton btntambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private com.tugas.swing.RoundPanel roundPanel1;
    private com.tugas.swing.table.Table tblpby;
    private javax.swing.JTextField txtcari;
    // End of variables declaration//GEN-END:variables
}
