package com.tugas.form;

import com.tugas.main.MenuUtama;
import java.sql.*;
import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;
import javax.swing.table.DefaultTableModel;
import com.tugas.koneksi.koneksi;
import java.text.SimpleDateFormat;

public class Form_Pembayaran extends javax.swing.JPanel {
private Connection conn = new koneksi().connect();
private DefaultTableModel tabmode;
private String idRumah, idCustomer, idKaryawan;


private boolean isEditMode = false;
    public Form_Pembayaran() {
    initComponents(); // Inisialisasi komponen dari GUI Builder

    // Panel informasi kredit disembunyikan di awal
    panelInfoKredit.setVisible(false);  

    // Muat data booking dan set default
    loadBooking();
    kosong();
    generateIdPenjualan();
    aktif();
    cmbTenor.addActionListener(evt -> {
    if ("Kredit".equals(cmbMetode.getSelectedItem())) {
        hitungSisaKredit(); // juga hitung cicilan di dalamnya
    }
});

    // Event saat memilih metode pembayaran
    cmbMetode.addActionListener(evt -> toggleTenor());
    cmbIdBooking.addActionListener(evt -> tampilDataBooking());

    // Event saat user mengetik harga rumah
    txtHargaJual.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            if ("Kredit".equals(cmbMetode.getSelectedItem())) {
                hitungSisaKredit();
            }
        }
    });

    // Event saat user mengetik uang muka
    txtUangMuka.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            if ("Kredit".equals(cmbMetode.getSelectedItem())) {
                hitungSisaKredit();
            }
        }
    });
}

    protected void aktif(){
    txtIdPembayaran.requestFocus();
    cmbIdBooking.setSelectedItem(null);
}

protected void kosong(){
    txtIdPembayaran.setText("");
    cmbIdBooking.setSelectedItem(null);
    txtRumah.setText("");
    txtHargaJual.setText("");
    txtCustomer.setText("");
    txtKaryawan.setText("");
    txtUangMuka.setText("");
    cmbMetode.setSelectedIndex(0);
    cmbTenor.setSelectedIndex(-1);
    cmbTenor.setEnabled(false);
    txtKeterangan.setText("");
}

     public void setEditMode(boolean isEdit) {
        this.isEditMode = isEdit;
        if (isEdit) {
            btnsimpan.setText("UPDATE");
            txtIdPembayaran.setEnabled(false); // ID tidak boleh diubah saat update
        } else {
            btnsimpan.setText("Simpan");
            txtIdPembayaran.setEnabled(true);
        }
    }
   private void toggleTenor() {
    String metode = (String) cmbMetode.getSelectedItem();
    boolean isKredit = "Kredit".equals(metode);
    
    cmbTenor.setEnabled(isKredit);
    panelInfoKredit.setVisible(isKredit);

    if (isKredit) {
        hitungSisaKredit(); // panggil perhitungan sisa kredit
    }
}

private void hitungSisaKredit() {
    try {
        double harga = Double.parseDouble(txtHargaJual.getText());
        double uangMuka = Double.parseDouble(txtUangMuka.getText());
        double sisa = harga - uangMuka;
        lblSisaKredit.setText("Sisa Kredit: Rp " + String.format("%,.0f", sisa));

        // Hitung cicilan per bulan jika tenor valid
        if (cmbTenor.getSelectedItem() != null) {
            int tenor = Integer.parseInt(cmbTenor.getSelectedItem().toString());
            if (tenor > 0) {
                double cicilan = sisa / tenor;
                lblCicilanPerBulan.setText("Cicilan per bulan: Rp " + String.format("%,.0f", cicilan));
            } else {
                lblCicilanPerBulan.setText("Cicilan per bulan: Rp 0");
            }
        } else {
            lblCicilanPerBulan.setText("Cicilan per bulan: Rp 0");
        }

    } catch (NumberFormatException e) {
        lblSisaKredit.setText("Sisa Kredit: Rp 0");
        lblCicilanPerBulan.setText("Cicilan per bulan: Rp 0");
    }
}



    private void loadBooking() {
        try {
            String sql = "SELECT id_booking FROM booking";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            cmbIdBooking.removeAllItems();
            while (rs.next()) {
                cmbIdBooking.addItem(rs.getString("id_booking"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load booking: " + e);
        }
    }

    private void tampilDataBooking() {
        
        String id = (String) cmbIdBooking.getSelectedItem();
        try {
            String sql = "SELECT b.tglbooking, r.alamat, r.harga, c.namacust, k.nama, b.dpayment, r.id_rumah, c.id_customer, k.id_karyawan " +
                         "FROM booking b " +
                         "JOIN rumah r ON b.id_rumah = r.id_rumah " +
                         "JOIN customer c ON b.id_customer = c.id_customer " +
                         "JOIN karyawan k ON b.id_karyawan = k.id_karyawan " +
                         "WHERE b.id_booking = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtTglPenjualan.setValue    (rs.getDate("tglbooking"));
                txtRumah.setText(rs.getString("alamat"));
                txtHargaJual.setText(rs.getString("harga"));
                txtCustomer.setText(rs.getString("namacust"));
                txtKaryawan.setText(rs.getString("nama"));
                txtUangMuka.setText(rs.getString("dpayment"));
                idRumah = rs.getString("id_rumah");
                idCustomer = rs.getString("id_customer");
                idKaryawan = rs.getString("id_karyawan");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal tampil data booking: " + e);
        }
    }
public void setFieldData(String idPenjualan, String idBooking, String rumah, String customer, String karyawan,
                         String tanggal, String harga, String metode, String uangMuka, String tenor, String keterangan) {

    txtIdPembayaran.setText(idPenjualan);
    cmbIdBooking.setSelectedItem(idBooking);
    txtRumah.setText(rumah);
    txtCustomer.setText(customer);
    txtKaryawan.setText(karyawan);

    // Convert tanggal dari String ke Date (untuk spinner)
    try {
        java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
        txtTglPenjualan.setValue(date);
    } catch (Exception e) {
        txtTglPenjualan.setValue(new java.util.Date());
    }

    txtHargaJual.setText(harga);
    cmbMetode.setSelectedItem(metode);
    txtUangMuka.setText(uangMuka);

    if (metode.equals("Kredit")) {
        cmbTenor.setEnabled(true);
        cmbTenor.setSelectedItem(tenor);
    } else {
        cmbTenor.setEnabled(false);
        cmbTenor.setSelectedItem(null);
    }

    txtKeterangan.setText(keterangan);
}
private void generateIdPenjualan() {
    try {
        String sql = "SELECT id_penjualan FROM penjualan_rumah ORDER BY id_penjualan DESC LIMIT 1";
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(sql);
        if (rs.next()) {
            String lastId = rs.getString("id_penjualan");
            int num = Integer.parseInt(lastId.substring(3)) + 1; // ambil angka dan tambah 1
            String newId = String.format("PNJ%03d", num); // format ke PNJ001, PNJ002, ...
            txtIdPembayaran.setText(newId);
        } else {
            txtIdPembayaran.setText("PNJ001"); // default jika belum ada data
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
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtIdPembayaran = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        cmbIdBooking = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtKeterangan = new javax.swing.JTextArea();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtRumah = new javax.swing.JTextField();
        txtHargaJual = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        btnKeluar = new javax.swing.JButton();
        btnbatal = new javax.swing.JButton();
        btnsimpan = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        cmbTenor = new javax.swing.JComboBox<>();
        txtUangMuka = new javax.swing.JTextField();
        cmbMetode = new javax.swing.JComboBox<>();
        txtKaryawan = new javax.swing.JTextField();
        txtCustomer = new javax.swing.JTextField();
        txtTglPenjualan = new javax.swing.JSpinner();
        panelInfoKredit = new javax.swing.JPanel();
        lblSisaKredit = new javax.swing.JLabel();
        lblCicilanPerBulan = new javax.swing.JLabel();

        setOpaque(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Dashboard > Form Rumah");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addContainerGap(633, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(17, 17, 17))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel13.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel13.setText("ID Pembayaran");

        jLabel14.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel14.setText("ID Booking");

        jLabel16.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel16.setText("Harga Rumah");

        cmbIdBooking.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tipe 36", "Tipe 45", "Tipe 60", "Tipe 90" }));

        txtKeterangan.setColumns(20);
        txtKeterangan.setRows(5);
        jScrollPane1.setViewportView(txtKeterangan);

        jLabel18.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel18.setText("Tanggal Penjualan");

        jLabel19.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel19.setText("Rumah");

        jLabel20.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel20.setText("Customer");

        jLabel21.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel21.setText("Karyawan");

        btnKeluar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnKeluar.setText("KELUAR");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        btnbatal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnbatal.setText("BATAL");
        btnbatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbatalActionPerformed(evt);
            }
        });

        btnsimpan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnsimpan.setText("SIMPAN");
        btnsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsimpanActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel22.setText("MEtode Pembayaran");

        jLabel23.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel23.setText("UAng muka");

        jLabel24.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel24.setText("Tenor");

        jLabel25.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel25.setText("KEterangan");

        cmbTenor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "12", "24", "36" }));

        txtUangMuka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUangMukaActionPerformed(evt);
            }
        });

        cmbMetode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tunai", "Kredit" }));

        txtKaryawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKaryawanActionPerformed(evt);
            }
        });

        txtCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustomerActionPerformed(evt);
            }
        });

        txtTglPenjualan.setModel(new javax.swing.SpinnerDateModel());

        panelInfoKredit.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Informasi Kredit", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N

        lblSisaKredit.setText("jLabel2");

        lblCicilanPerBulan.setText("jLabel2");

        javax.swing.GroupLayout panelInfoKreditLayout = new javax.swing.GroupLayout(panelInfoKredit);
        panelInfoKredit.setLayout(panelInfoKreditLayout);
        panelInfoKreditLayout.setHorizontalGroup(
            panelInfoKreditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInfoKreditLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblSisaKredit, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(lblCicilanPerBulan, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );
        panelInfoKreditLayout.setVerticalGroup(
            panelInfoKreditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInfoKreditLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(panelInfoKreditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSisaKredit)
                    .addComponent(lblCicilanPerBulan))
                .addContainerGap(110, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addComponent(btnsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnbatal, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(136, 136, 136))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(txtIdPembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20)
                            .addComponent(jLabel13)
                            .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbIdBooking, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(70, 70, 70)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel19)
                                .addComponent(jLabel18)
                                .addComponent(jLabel16)
                                .addComponent(txtRumah)
                                .addComponent(txtHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtTglPenjualan, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21)
                            .addComponent(txtKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(panelInfoKredit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(jLabel23)
                    .addComponent(jLabel22)
                    .addComponent(jLabel24)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTenor, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUangMuka, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMetode, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(txtTglPenjualan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel23))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtRumah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtUangMuka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbMetode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel16)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(25, 25, 25)
                                    .addComponent(txtHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel24)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(25, 25, 25)
                                    .addComponent(cmbTenor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(panelInfoKredit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdPembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbIdBooking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(56, 56, 56)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnsimpan)
                    .addComponent(btnbatal)
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
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        MenuUtama.getMenuUtama().showForm(new Form_DataPenjualan());
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void btnsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpanActionPerformed
        if (isEditMode) {
        // ✅ MODE EDIT - UPDATE DATA
        String sql = "UPDATE penjualan_rumah SET id_booking=?, id_rumah=?, id_customer=?, id_karyawan=?, tanggal_penjualan=?, harga_jual=?, metode_pembayaran=?, uang_muka=?, tenor=?, keterangan=? WHERE id_penjualan=?";
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, (String) cmbIdBooking.getSelectedItem());
            stat.setString(2, idRumah);
            stat.setString(3, idCustomer);
            stat.setString(4, idKaryawan);

            java.util.Date utilDate = (java.util.Date) txtTglPenjualan.getValue();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            stat.setDate(5, sqlDate);

            stat.setString(6, txtHargaJual.getText());
            stat.setString(7, (String) cmbMetode.getSelectedItem());
            stat.setString(8, txtUangMuka.getText());

            String metode = (String) cmbMetode.getSelectedItem();
            if (metode.equals("Kredit")) {
                stat.setInt(9, Integer.parseInt((String) cmbTenor.getSelectedItem()));
            } else {
                stat.setNull(9, java.sql.Types.INTEGER);
            }

            stat.setString(10, txtKeterangan.getText());
            stat.setString(11, txtIdPembayaran.getText()); // kondisi WHERE

            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data transaksi berhasil diperbarui.");
            kosong();
            setEditMode(false); // kembali ke mode tambah
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal update data transaksi: " + e);
        }
    } else {
        // ✅ MODE INSERT - TAMBAH DATA BARU
        String sql = "INSERT INTO penjualan_rumah (id_penjualan, id_booking, id_rumah, id_customer, id_karyawan, tanggal_penjualan, harga_jual, metode_pembayaran, uang_muka, tenor, keterangan) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, txtIdPembayaran.getText());
            stat.setString(2, (String) cmbIdBooking.getSelectedItem());
            stat.setString(3, idRumah);
            stat.setString(4, idCustomer);
            stat.setString(5, idKaryawan);

            java.util.Date utilDate = (java.util.Date) txtTglPenjualan.getValue();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            stat.setDate(6, sqlDate);

            stat.setString(7, txtHargaJual.getText());
            stat.setString(8, (String) cmbMetode.getSelectedItem());
            stat.setString(9, txtUangMuka.getText());

            String metode = (String) cmbMetode.getSelectedItem();
            if (metode.equals("Kredit")) {
                stat.setInt(10, Integer.parseInt((String) cmbTenor.getSelectedItem()));
            } else {
                stat.setNull(10, java.sql.Types.INTEGER);
            }

            stat.setString(11, txtKeterangan.getText());
            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data transaksi berhasil disimpan.");
            kosong();
            generateIdPenjualan();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal simpan data transaksi: " + e);
        }
    }
    }//GEN-LAST:event_btnsimpanActionPerformed

    private void btnbatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbatalActionPerformed
        kosong();
    }//GEN-LAST:event_btnbatalActionPerformed

    private void txtUangMukaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUangMukaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUangMukaActionPerformed

    private void txtKaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKaryawanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKaryawanActionPerformed

    private void txtCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustomerActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnbatal;
    private javax.swing.JButton btnsimpan;
    private javax.swing.JComboBox<String> cmbIdBooking;
    private javax.swing.JComboBox<String> cmbMetode;
    private javax.swing.JComboBox<String> cmbTenor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCicilanPerBulan;
    private javax.swing.JLabel lblSisaKredit;
    private javax.swing.JPanel panelInfoKredit;
    private javax.swing.JTextField txtCustomer;
    private javax.swing.JTextField txtHargaJual;
    private javax.swing.JTextField txtIdPembayaran;
    private javax.swing.JTextField txtKaryawan;
    private javax.swing.JTextArea txtKeterangan;
    private javax.swing.JTextField txtRumah;
    private javax.swing.JSpinner txtTglPenjualan;
    private javax.swing.JTextField txtUangMuka;
    // End of variables declaration//GEN-END:variables

  
}
