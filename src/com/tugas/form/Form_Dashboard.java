package com.tugas.form;

import com.tugas.card.ModelCard;
import com.tugas.koneksi.koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Form_Dashboard extends javax.swing.JPanel {

    public Form_Dashboard() {
        initComponents();
        init();
    }
    private void init() {
        table.fixTable(jScrollPane1);
        tampilkan10PenjualanTerakhir();
        loadCardDataFromDatabase();
    }
    private void loadCardDataFromDatabase() {
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    try {
        conn = new koneksi().connect();

        // --- Jumlah Rumah Tersedia ---
        String sqlRumah = "SELECT COUNT(*) AS total FROM rumah WHERE status = 'Tersedia'";
        pst = conn.prepareStatement(sqlRumah);
        rs = pst.executeQuery();
        if (rs.next()) {
            String totalRumah = rs.getString("total");
            card7.setData(new com.tugas.card.ModelCard(null, null, null, totalRumah + " Unit", "Rumah Tersedia"));
        }
        rs.close();
        pst.close();

        // --- Jumlah Karyawan ---
        String sqlKaryawan = "SELECT COUNT(*) AS total FROM karyawan";
        pst = conn.prepareStatement(sqlKaryawan);
        rs = pst.executeQuery();
        if (rs.next()) {
            String totalKaryawan = rs.getString("total");
            card8.setData(new com.tugas.card.ModelCard(null, null, null, totalKaryawan + " Orang", "Jumlah Karyawan"));
        }
        rs.close();
        pst.close();

        // --- Jumlah Rumah Terjual (opsional) ---
        String sqlTerjual = "SELECT COUNT(*) AS total FROM rumah WHERE status = 'Terjual'";
        pst = conn.prepareStatement(sqlTerjual);
        rs = pst.executeQuery();
        if (rs.next()) {
            String totalTerjual = rs.getString("total");
            card9.setData(new com.tugas.card.ModelCard(null, null, null, totalTerjual + " Unit", "Rumah Terjual"));
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal mengambil data dashboard: " + e.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

// === MENAMPILKAN 10 PENJUALAN TERAKHIR DI TABEL ===
private void tampilkan10PenjualanTerakhir() {
    
    String[] kolom = {"No", "Customer", "Email", "Karyawan", "Tanggal", "Harga"};
table.setModel(new DefaultTableModel(null, kolom));

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setRowCount(0);  // Kosongkan tabel sebelum isi ulang

    try {
        conn = new koneksi().connect();

        String sql = "SELECT p.tanggal_penjualan, c.namacust, c.email, k.nama AS nama_karyawan, p.harga_jual "+
            "FROM penjualan_rumah p "+
            "JOIN customer c ON p.id_customer = c.id_customer "+
            "JOIN karyawan k ON p.id_karyawan = k.id_karyawan "+
            "ORDER BY p.tanggal_penjualan DESC LIMIT 10";

        pst = conn.prepareStatement(sql);
        rs = pst.executeQuery();

        int no = 1;
        while (rs.next()) {
            String namaCust = rs.getString("namacust");
            String emailCust = rs.getString("email");
            String namaKaryawan = rs.getString("nama_karyawan");
            String tanggal = rs.getString("tanggal_penjualan");
            double harga = rs.getDouble("harga_jual");

            // Format harga
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            String hargaFormatted = formatRupiah.format(harga);

            model.addRow(new Object[]{
                String.valueOf(no++),
                namaCust,
                emailCust,
                namaKaryawan,
                tanggal,
                hargaFormatted
            });
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal memuat data penjualan: " + e.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        roundPanel1 = new com.tugas.swing.RoundPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new com.tugas.swing.table.Table();
        card7 = new com.tugas.card.Card();
        card8 = new com.tugas.card.Card();
        card9 = new com.tugas.card.Card();

        setOpaque(false);

        roundPanel1.setBackground(new java.awt.Color(255, 255, 255));
        roundPanel1.setRound(10);

        table.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Nama", "Email", "Position", "Date Join"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout roundPanel1Layout = new javax.swing.GroupLayout(roundPanel1);
        roundPanel1.setLayout(roundPanel1Layout);
        roundPanel1Layout.setHorizontalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addGap(14, 14, 14))
        );
        roundPanel1Layout.setVerticalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
                .addContainerGap())
        );

        card8.setColor1(new java.awt.Color(51, 255, 255));
        card8.setColor2(new java.awt.Color(0, 204, 204));
        card8.setIcon(javaswingdev.GoogleMaterialDesignIcon.PIE_CHART);

        card9.setColor1(new java.awt.Color(0, 204, 51));
        card9.setColor2(new java.awt.Color(0, 255, 102));
        card9.setIcon(javaswingdev.GoogleMaterialDesignIcon.INFO);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(card7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(card8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(30, 30, 30)
                        .addComponent(card9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(card7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.tugas.card.Card card7;
    private com.tugas.card.Card card8;
    private com.tugas.card.Card card9;
    private javax.swing.JScrollPane jScrollPane1;
    private com.tugas.swing.RoundPanel roundPanel1;
    private com.tugas.swing.table.Table table;
    // End of variables declaration//GEN-END:variables
}
