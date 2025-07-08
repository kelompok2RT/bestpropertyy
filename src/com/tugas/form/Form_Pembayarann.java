package com.tugas.form;

import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.tugas.koneksi.koneksi;
import com.tugas.main.MenuUtama;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class Form_Pembayarann extends javax.swing.JPanel {

    private Connection conn = new koneksi().connect();
    private String idCustomer;
    private String idKaryawan;
    private boolean isEditMode = false;

    public Form_Pembayarann() {
        initComponents();
        loadIdPenjualanToComboBox();
        setupComboBoxListener();
        aktif();
        kosong();
        generateIdPembayaran();
    }

    protected void aktif() {
        txtIdPembayaran.requestFocus();
    }

    protected void kosong() {
        // txtIdPembayaran.setText(""); // jangan kosongkan ID agar tidak override
        cmbIdPenjualan.setSelectedItem(null);
        txtHargaJual.setText("");
        txtSisaCicilan.setText("");
        txtNamaKaryawan.setText("");
        txtNamaCustomer.setText("");
        txtUangMuka.setText("");
        txtTenor.setText("");
        txtBulanKe.setText("");
        txtJumlahBayar.setText("");
        txtCatatan.setText("");
        jtglbyr.setDate(null);
        cmbMetode.setSelectedItem("Tunai");
    }

    public void setEditMode(boolean editMode) {
        this.isEditMode = editMode;
    }

    public void setFieldDataPembayaran(String idPembayaran, String idPenjualan, String tanggal, String customer,
                                       String karyawan, String harga, String uangMuka, String tenor,
                                       String metode, String totalDibayar, String sisaCicilan) {
        txtIdPembayaran.setText(idPembayaran);
        cmbIdPenjualan.setSelectedItem(idPenjualan);
        jtglbyr.setDate(java.sql.Date.valueOf(tanggal));
        txtNamaCustomer.setText(customer);
        txtNamaKaryawan.setText(karyawan);
        txtHargaJual.setText(harga);
        txtUangMuka.setText(uangMuka);
        txtTenor.setText(tenor);
        txtMetode.setText(metode);
        txtTotalDibayar.setText(totalDibayar);
        txtSisaCicilan.setText(sisaCicilan);
    }

    private void generateIdPembayaran() {
        try {
            String sql = "SELECT id_pembayaran FROM pembayaran_cicilan ORDER BY id_pembayaran DESC LIMIT 1";
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            if (rs.next()) {
                String lastId = rs.getString("id_pembayaran");
                int num = Integer.parseInt(lastId.substring(3)) + 1;
                String newId = String.format("BYR%03d", num);
                txtIdPembayaran.setText(newId);
            } else {
                txtIdPembayaran.setText("BYR001");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal generate ID Pembayaran: " + e);
        }
    }

    private void loadIdPenjualanToComboBox() {
        try {
            String sql = "SELECT id_penjualan FROM penjualan_rumah";
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);

            cmbIdPenjualan.removeAllItems();
            cmbIdPenjualan.addItem("-- Pilih ID Penjualan --");

            while (rs.next()) {
                cmbIdPenjualan.addItem(rs.getString("id_penjualan"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load ID penjualan: " + e.getMessage());
        }
    }

    private void setupComboBoxListener() {
        cmbIdPenjualan.addActionListener(evt -> ambilDataPenjualan());
    }

    private void ambilDataPenjualan() {
    String idPenjualan = (String) cmbIdPenjualan.getSelectedItem();
    if (idPenjualan == null || idPenjualan.equals("-- Pilih ID Penjualan --")) return;

    double cicilan = 0;
    int tenor = 0;

    try {
        String sql = "SELECT p.*, r.harga, c.namacust, c.id_customer, k.nama AS nama_karyawan, k.id_karyawan " +
                     "FROM penjualan_rumah p " +
                     "JOIN rumah r ON p.id_rumah = r.id_rumah " +
                     "JOIN customer c ON p.id_customer = c.id_customer " +
                     "JOIN karyawan k ON p.id_karyawan = k.id_karyawan " +
                     "WHERE p.id_penjualan = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, idPenjualan);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            idCustomer = rs.getString("id_customer");
            idKaryawan = rs.getString("id_karyawan");

            DecimalFormat dfNoDecimal = new DecimalFormat("#");
            DecimalFormat dfTwoDecimal = new DecimalFormat("#.##");

            double hargaJual = rs.getDouble("harga");
            double uangMuka = rs.getDouble("uang_muka");
            tenor = rs.getInt("tenor");

            double sisa = hargaJual - uangMuka;
            cicilan = tenor > 0 ? sisa / tenor : 0;

            txtNamaCustomer.setText(rs.getString("namacust"));
            txtNamaKaryawan.setText(rs.getString("nama_karyawan"));
            txtHargaJual.setText(dfNoDecimal.format(hargaJual));
            txtUangMuka.setText(dfNoDecimal.format(uangMuka));
            txtTenor.setText(String.valueOf(tenor));
            txtMetode.setText(rs.getString("metode_pembayaran"));
            txtTotalDibayar.setText(dfNoDecimal.format(uangMuka));
            txtSisaCicilan.setText(dfNoDecimal.format(sisa));
            txtCicilanBulanan.setText(dfTwoDecimal.format(cicilan));
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal ambil data penjualan: " + e.getMessage());
        e.printStackTrace();
    }

    try {
        // Tampilkan informasi cicilan bulan ke-i dan jumlah bayar dari id_penjualan
        tampilkanPembayaranBulanKeDariPenjualan(idPenjualan);

        // Hitung ulang total bayar dan sisa cicilan
        double[] totalDanSisa = getTotalDanSisa(idPenjualan);
        double totalSudahBayar = totalDanSisa[0];
        double sisaCicilan = totalDanSisa[1];

        DecimalFormat dfNoDecimal = new DecimalFormat("#");
        txtTotalDibayar.setText(dfNoDecimal.format(totalSudahBayar));
        txtSisaCicilan.setText(dfNoDecimal.format(sisaCicilan));
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal proses cicilan bulanan: " + e.getMessage());
        e.printStackTrace();
    }
}

private double[] getTotalDanSisa(String idPenjualan) throws SQLException {
    // total bayar lama
    String sqlTot = "SELECT COALESCE(SUM(total_dibayar),0) AS tot FROM pembayaran_cicilan WHERE id_penjualan = ?";
    PreparedStatement pstTot = conn.prepareStatement(sqlTot);
    pstTot.setString(1, idPenjualan);
    ResultSet rsTot = pstTot.executeQuery();
    double totalLama = 0;
    if (rsTot.next()) totalLama = rsTot.getDouble("tot");

    // harga & DP
    String sqlHarga = "SELECT r.harga, p.uang_muka " +
                      "FROM penjualan_rumah p JOIN rumah r ON p.id_rumah=r.id_rumah " +
                      "WHERE p.id_penjualan = ?";
    PreparedStatement pstHarga = conn.prepareStatement(sqlHarga);
    pstHarga.setString(1, idPenjualan);
    ResultSet rsHarga = pstHarga.executeQuery();

    double harga = 0, dp = 0;
    if (rsHarga.next()) {
        harga = rsHarga.getDouble("harga");
        dp    = rsHarga.getDouble("uang_muka");
    }
    double totalBaru  = totalLama;              // caller akan + jumlahBaru
    double sisaAwal   = (harga - dp) - totalLama;

    return new double[]{totalBaru, sisaAwal};
}
private void tampilkanPembayaranBulanKeDariPenjualan(String idPenjualan) {
    try {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("###0.00", symbols);

        // Ambil data harga rumah, uang muka, tenor
        String sqlData = "SELECT r.harga, p.uang_muka, p.tenor " +
                         "FROM penjualan_rumah p " +
                         "JOIN rumah r ON p.id_rumah = r.id_rumah " +
                         "WHERE p.id_penjualan = ?";
        PreparedStatement pstData = conn.prepareStatement(sqlData);
        pstData.setString(1, idPenjualan);
        ResultSet rsData = pstData.executeQuery();

        double harga = 0, dp = 0, cicilanPerBulan = 0;
        int tenor = 0;

        if (rsData.next()) {
            harga = rsData.getDouble("harga");
            dp = rsData.getDouble("uang_muka");
            tenor = rsData.getInt("tenor");

            double sisa = harga - dp;
            cicilanPerBulan = tenor > 0 ? sisa / tenor : 0;
        }

        // Hitung jumlah cicilan yang sudah dibayar (bulan ke berapa sekarang)
        String sqlHitung = "SELECT COUNT(*) AS jumlah FROM detail_pembayaran_bulanan dp " +
                           "JOIN pembayaran_cicilan pc ON dp.id_pembayaran = pc.id_pembayaran " +
                           "WHERE pc.id_penjualan = ?";
        PreparedStatement pstHitung = conn.prepareStatement(sqlHitung);
        pstHitung.setString(1, idPenjualan);
        ResultSet rsHitung = pstHitung.executeQuery();

        int nextBulan = 1;
        if (rsHitung.next()) {
            nextBulan = rsHitung.getInt("jumlah") + 1;
        }

        txtBulanKe.setText(String.valueOf(nextBulan));
        txtJumlahBayar.setText(df.format(cicilanPerBulan));

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal tampilkan cicilan dari id_penjualan: " + e.getMessage());
        e.printStackTrace();
    }
}






    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btnKeluar = new javax.swing.JButton();
        btnbatal = new javax.swing.JButton();
        btnsimpan = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtIdPembayaran = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        txtHargaJual = new javax.swing.JTextField();
        txtCicilanBulanan = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtNamaCustomer = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        txtTotalDibayar = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        txtSisaCicilan = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtTenor = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtNamaKaryawan = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        cmbIdPenjualan = new javax.swing.JComboBox<>();
        txtUangMuka = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txtMetode = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        txtBulanKe = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtJumlahBayar = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jtglbyr = new com.toedter.calendar.JDateChooser();
        jLabel29 = new javax.swing.JLabel();
        cmbMetode = new javax.swing.JComboBox<>();
        txtCatatan = new javax.swing.JTextField();

        setOpaque(false);

        jPanel1.setBackground(new java.awt.Color(143, 148, 251));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Form Transaksi Pembayaran");

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1))
                .addGap(232, 232, 232))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btnKeluar.setBackground(new java.awt.Color(143, 148, 251));
        btnKeluar.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnKeluar.setForeground(new java.awt.Color(255, 255, 255));
        btnKeluar.setText("KELUAR");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        btnbatal.setBackground(new java.awt.Color(143, 148, 251));
        btnbatal.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnbatal.setForeground(new java.awt.Color(255, 255, 255));
        btnbatal.setText("BATAL");
        btnbatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbatalActionPerformed(evt);
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

        jLabel13.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel13.setText("ID Penjualan          :");

        txtIdPembayaran.setEnabled(false);
        txtIdPembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdPembayaranActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel14.setText("Harga Jual             :");

        jLabel41.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel41.setText("Cicilan / Bulan       :");

        jLabel35.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel35.setText("Nama Customer    :");

        txtNamaCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaCustomerActionPerformed(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel42.setText("Metode            :");

        jLabel38.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel38.setText("Bulan");

        jLabel39.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel39.setText("Tenor              :");

        jLabel25.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel25.setText("Sisa Cicilan      :");

        jLabel26.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel26.setText("Sudah Dibayar :");

        jLabel20.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel20.setText("Nama Karyawan    :");

        jLabel15.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel15.setText("ID Pembayaran      :");

        jLabel30.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel30.setText("Uang Muka      :");

        txtMetode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMetodeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41)
                    .addComponent(jLabel15)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(24, 24, 24)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtHargaJual)
                    .addComponent(cmbIdPenjualan, javax.swing.GroupLayout.Alignment.TRAILING, 0, 161, Short.MAX_VALUE)
                    .addComponent(txtCicilanBulanan)
                    .addComponent(txtNamaKaryawan, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNamaCustomer, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtIdPembayaran, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(81, 81, 81)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39)
                    .addComponent(jLabel30)
                    .addComponent(jLabel42)
                    .addComponent(jLabel26)
                    .addComponent(jLabel25))
                .addGap(24, 24, 24)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtUangMuka)
                    .addComponent(txtMetode)
                    .addComponent(txtTotalDibayar)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSisaCicilan, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(txtTenor, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel38)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(59, 59, 59))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap(33, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42)
                            .addComponent(txtMetode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(txtTotalDibayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(txtUangMuka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel39)
                            .addComponent(txtTenor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(txtSisaCicilan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(8, 8, 8))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIdPembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(cmbIdPenjualan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(txtHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41)
                            .addComponent(txtCicilanBulanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(txtNamaKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(txtNamaCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18))
        );

        jLabel16.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel16.setText("Pembayaran Bulan Ke - ");

        jLabel24.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel24.setText("Jumlah Pembayaran    :");

        jLabel27.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel27.setText("Catatan      :");

        jLabel28.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel28.setText("Tanggal Pembayaran      :");

        jLabel29.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel29.setText("Metode Pembayaran      :");

        cmbMetode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tunai", "Transfer", "Kartu Kredit" }));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtJumlahBayar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(txtBulanKe, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCatatan))
                .addGap(69, 69, 69)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel29)
                    .addComponent(jLabel28))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtglbyr, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMetode, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(txtBulanKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(txtJumlahBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(txtCatatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel28)
                            .addComponent(jtglbyr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(cmbMetode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(76, 76, 76))))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(21, 21, 21)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(92, 92, 92)
                            .addComponent(btnsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(91, 91, 91)
                            .addComponent(btnbatal, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(101, 101, 101)
                            .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(102, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnbatal)
                    .addComponent(btnKeluar)
                    .addComponent(btnsimpan))
                .addContainerGap(42, Short.MAX_VALUE))
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
                .addGap(14, 14, 14)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        MenuUtama.getMenuUtama().showForm(new Form_DataPembayaran());
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void btnsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpanActionPerformed
     try {
        /* --- ambil data form --- */
        String  idPembayaran = txtIdPembayaran.getText();      // sudah digenerate unik
        String  idPenjualan  = cmbIdPenjualan.getSelectedItem().toString();
        int     bulanKe      = Integer.parseInt(txtBulanKe.getText());
        double  jumlah       = Double.parseDouble(txtJumlahBayar.getText().replace(",", "."));
        String  metode       = cmbMetode.getSelectedItem().toString();
        String  catatan      = txtCatatan.getText();
        java.sql.Date sqlTgl = new java.sql.Date(jtglbyr.getDate().getTime());
        String  namaCust     = txtNamaCustomer.getText();
        String  namaKary     = txtNamaKaryawan.getText();

        /* --- kalkulasi total dan sisa sebelum ditambah pembayaran baru --- */
        double[] ts   = getTotalDanSisa(idPenjualan);   // [0] totalLama, [1] sisaAwal
        double   totalBaru = ts[0] + jumlah;
        double   sisaBaru  = ts[1] - jumlah;

        /* --- INSERT baris baru pembayaran_cicilan (selalu) --- */
        String insCicil = "INSERT INTO pembayaran_cicilan " +
                "(id_pembayaran,id_penjualan,tanggal_pembayaran,metode_pembayaran," +
                " total_dibayar,sisa_cicilan,nama_karyawan,nama_customer,keterangan) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst1 = conn.prepareStatement(insCicil);
        pst1.setString (1, idPembayaran);
        pst1.setString (2, idPenjualan);
        pst1.setDate   (3, sqlTgl);
        pst1.setString (4, metode);
        pst1.setDouble (5, totalBaru);   // kumulatif
        pst1.setDouble (6, sisaBaru);
        pst1.setString (7, namaKary);
        pst1.setString (8, namaCust);
        pst1.setString (9, catatan);
        pst1.executeUpdate();

        /* --- INSERT / cegah duplikat detail bulan_ke --- */
        String cekDet = "SELECT 1 FROM detail_pembayaran_bulanan WHERE id_pembayaran=? AND bulan_ke=?";
        PreparedStatement pstCek = conn.prepareStatement(cekDet);
        pstCek.setString(1, idPembayaran);
        pstCek.setInt   (2, bulanKe);
        ResultSet rsDet = pstCek.executeQuery();
        if (!rsDet.next()) {
            String insDet = "INSERT INTO detail_pembayaran_bulanan " +
                    "(id_pembayaran,bulan_ke,jumlah_pembayaran,tanggal_bayar,status_bayar) " +
                    "VALUES (?,?,?,?, 'Lunas')";
            PreparedStatement pst2 = conn.prepareStatement(insDet);
            pst2.setString (1, idPembayaran);
            pst2.setInt    (2, bulanKe);
            pst2.setDouble (3, jumlah);
            pst2.setDate   (4, sqlTgl);
            pst2.executeUpdate();
        } else {
            JOptionPane.showMessageDialog(this,
                "Bulan ke-" + bulanKe + " sudah tersimpan untuk ID pembayaran ini.");
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Pembayaran bulan ke-" + bulanKe + " berhasil disimpan.");

        /* --- reset form utk transaksi berikutnya --- */
        kosong();
        generateIdPembayaran();  // selalu buat ID baru utk bulan berikut

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Gagal simpan pembayaran: " + e.getMessage());
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnsimpanActionPerformed

    private void btnbatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbatalActionPerformed
        //kosong();
    }//GEN-LAST:event_btnbatalActionPerformed

    private void txtNamaCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaCustomerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaCustomerActionPerformed

    private void txtIdPembayaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdPembayaranActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdPembayaranActionPerformed

    private void txtMetodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMetodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMetodeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnbatal;
    private javax.swing.JButton btnsimpan;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbIdPenjualan;
    private javax.swing.JComboBox<String> cmbMetode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private com.toedter.calendar.JDateChooser jtglbyr;
    private javax.swing.JTextField txtBulanKe;
    private javax.swing.JTextField txtCatatan;
    private javax.swing.JTextField txtCicilanBulanan;
    private javax.swing.JTextField txtHargaJual;
    private javax.swing.JTextField txtIdPembayaran;
    private javax.swing.JTextField txtJumlahBayar;
    private javax.swing.JTextField txtMetode;
    private javax.swing.JTextField txtNamaCustomer;
    private javax.swing.JTextField txtNamaKaryawan;
    private javax.swing.JTextField txtSisaCicilan;
    private javax.swing.JTextField txtTenor;
    private javax.swing.JTextField txtTotalDibayar;
    private javax.swing.JTextField txtUangMuka;
    // End of variables declaration//GEN-END:variables

  
}
