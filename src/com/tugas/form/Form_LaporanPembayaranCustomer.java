package com.tugas.form;
import com.tugas.main.MenuUtama;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.tugas.koneksi.koneksi;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;


public class Form_LaporanPembayaranCustomer extends javax.swing.JPanel {
private Connection conn = new koneksi().connect();
private DefaultTableModel tabmode;
    
    public Form_LaporanPembayaranCustomer() {
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
public void cetakLaporan() {
        try {
            // Lokasi file .jasper (pastikan sudah dikompilasi dari .jrxml)
            String reportPath = "/com/tugas/report/Lap_Pembayaran.jasper";

            // Ambil file laporan
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream(reportPath));

            // Parameter (jika tidak ada, kosongkan)
            HashMap<String, Object> parameters = new HashMap<>();

            // Koneksi database (pastikan ini sesuai dengan class koneksi kamu)
            Connection conn = new koneksi().connect();

            // Isi laporan
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);

            // Tampilkan ke viewer
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setTitle("Laporan Pembayaran");
            viewer.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Gagal mencetak laporan: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btncetak = new javax.swing.JButton();
        btncari = new javax.swing.JButton();
        txtcari = new javax.swing.JTextField();
        roundPanel1 = new com.tugas.swing.RoundPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblpby = new com.tugas.swing.table.Table();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        btncetak.setBackground(new java.awt.Color(0, 204, 51));
        btncetak.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btncetak.setForeground(new java.awt.Color(255, 255, 255));
        btncetak.setText("Cetak Data");
        btncetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncetakActionPerformed(evt);
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
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI Historic", 1, 26)); // NOI18N
        jLabel1.setText("Laporan Pembayaran Customer");

        jSeparator1.setForeground(new java.awt.Color(143, 148, 251));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btncari, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btncetak, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jSeparator1))))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btncari, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btncetak, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roundPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btncetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncetakActionPerformed
// Membuat dan menampilkan form input rumah sebagai dialog
   cetakLaporan();
    }//GEN-LAST:event_btncetakActionPerformed

    private void txtcariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariKeyPressed
        if (evt.getKeyCode()==KeyEvent.VK_ENTER){
            datatable();
        }
    }//GEN-LAST:event_txtcariKeyPressed

    private void btncariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncariActionPerformed
        datatable();
    }//GEN-LAST:event_btncariActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btncari;
    private javax.swing.JButton btncetak;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private com.tugas.swing.RoundPanel roundPanel1;
    private com.tugas.swing.table.Table tblpby;
    private javax.swing.JTextField txtcari;
    // End of variables declaration//GEN-END:variables
}
