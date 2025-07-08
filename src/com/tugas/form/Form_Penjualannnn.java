package com.tugas.form;

import com.tugas.main.MenuUtama;
import java.sql.*;
import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;
import javax.swing.table.DefaultTableModel;
import com.tugas.koneksi.koneksi;
import java.text.SimpleDateFormat;

public class Form_Penjualannnn extends javax.swing.JPanel {
private Connection conn = new koneksi().connect();
private DefaultTableModel tabmode;
private String idRumah, idCustomer, idKaryawan;


private boolean isEditMode = false;
    public Form_Penjualannnn() {
    initComponents(); // Inisialisasi komponen dari GUI Builder

    // Inisialisasi data
    loadBooking();
    kosong();
    generateIdPenjualan();
    aktif();

    // Event saat memilih tenor → hanya hitung jika Kredit dipilih
    cmbTenor.addActionListener(evt -> {
        if (Kredit.isSelected()) {
            hitungSisaKredit(); // juga hitung cicilan di dalamnya
        }
    });

    // Event saat memilih metode pembayaran → update tampilan tenor
    Tunai.addActionListener(evt -> toggleTenor());
    Kredit.addActionListener(evt -> toggleTenor());

    // Event saat memilih booking → tampilkan data terkait
    cmbIdBooking.addActionListener(evt -> tampilDataBooking());

    // Event saat user mengetik harga rumah
    txtHargaJual.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            if (Kredit.isSelected()) {
                hitungSisaKredit();
            }
        }
    });

    // Event saat user mengetik uang muka
    txtUangMuka.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            if (Kredit.isSelected()) {
                hitungSisaKredit();
            }
        }
    });
}


    protected void aktif(){
    txtIdPenjualan.requestFocus();
    cmbIdBooking.setSelectedItem(null);
}

protected void kosong(){
    cmbIdBooking.setSelectedItem(null);
    txtRumah.setText("");
    txtHargaJual.setText("");
    txtCustomer.setText("");
    txtKaryawan.setText("");
    txtUangMuka.setText("");
    buttonGroup1.clearSelection();
    cmbTenor.setSelectedIndex(-1);
    cmbTenor.setEnabled(false);
    txtCicilan.setText("");
    txtSisa.setText("");
    txtIdBooking.setText("");
    jtgl.setDate(null);
    txtNmKaryawan.setText("");
    
}

     public void setEditMode(boolean isEdit) {
        this.isEditMode = isEdit;
        if (isEdit) {
            btnsimpan.setText("UPDATE");
            txtIdPenjualan.setEnabled(false); // ID tidak boleh diubah saat update
        } else {
            btnsimpan.setText("Simpan");
            txtIdPenjualan.setEnabled(true);
        }
    }
   private void toggleTenor() {
    boolean isKredit = Kredit.isSelected(); // gunakan radio button

    cmbTenor.setEnabled(isKredit); // aktifkan combo tenor jika kredit
    cmbTenor.setSelectedItem(isKredit ? cmbTenor.getSelectedItem() : null); // kosongkan jika bukan kredit

    if (isKredit) {
        hitungSisaKredit(); // hitung hanya jika kredit
    }
}


private void hitungSisaKredit() {
    try {
        double harga = Double.parseDouble(txtHargaJual.getText());
        double uangMuka = Double.parseDouble(txtUangMuka.getText());
        double sisa = harga - uangMuka;
        txtSisa.setText(String.format("%,.0f", sisa));

        // Hitung cicilan per bulan jika tenor valid
        if (cmbTenor.getSelectedItem() != null) {
            int tenor = Integer.parseInt(cmbTenor.getSelectedItem().toString());
            if (tenor > 0) {
                double cicilan = sisa / tenor;
                txtCicilan.setText( String.format("%,.0f", cicilan));
            } else {
                txtCicilan.setText("Rp 0");
            }
        } else {
            txtCicilan.setText("Rp 0");
        }

    } catch (NumberFormatException e) {
        txtSisa.setText("Rp 0");
        txtCicilan.setText("Rp 0");
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
            String sql = "SELECT b.id_booking, b.tglbooking, r.alamat, r.harga, c.namacust,k.id_karyawan, k.nama, b.dpayment, r.id_rumah, c.id_customer, k.id_karyawan " +
                         "FROM booking b " +
                         "JOIN rumah r ON b.id_rumah = r.id_rumah " +
                         "JOIN customer c ON b.id_customer = c.id_customer " +
                         "JOIN karyawan k ON b.id_karyawan = k.id_karyawan " +
                         "WHERE b.id_booking = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                jtgl.setDate(rs.getDate("tglbooking"));
                txtIdBooking.setText(rs.getString("id_booking"));
                txtRumah.setText(rs.getString("alamat"));
                txtHargaJual.setText(rs.getString("harga"));
                txtCustomer.setText(rs.getString("namacust"));
                txtKaryawan.setText(rs.getString("id_karyawan"));
                txtNmKaryawan.setText(rs.getString("nama"));
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
                         String tanggal, String harga, String metode, String uangMuka, String tenor) {
    
    txtIdPenjualan.setText(idPenjualan);
    cmbIdBooking.setSelectedItem(idBooking);
    txtRumah.setText(rumah);
    txtCustomer.setText(customer);
    txtKaryawan.setText(karyawan);

    // Convert tanggal dari String ke java.util.Date dan set ke JDateChooser
    try {
        java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
        jtgl.setDate(date);
    } catch (Exception e) {
        jtgl.setDate(new java.util.Date()); // fallback ke hari ini jika parsing gagal
    }

    txtHargaJual.setText(harga);
    txtUangMuka.setText(uangMuka);

    // Set radio button berdasarkan metode pembayaran
    if (metode.equalsIgnoreCase("Tunai")) {
        Tunai.setSelected(true);
        Kredit.setSelected(false);
        cmbTenor.setEnabled(false);
        cmbTenor.setSelectedItem(null);
    } else if (metode.equalsIgnoreCase("Kredit")) {
        Kredit.setSelected(true);
        Tunai.setSelected(false);
        cmbTenor.setEnabled(true);
        cmbTenor.setSelectedItem(tenor);
    }
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
            txtIdPenjualan.setText(newId);
        } else {
            txtIdPenjualan.setText("PNJ001"); // default jika belum ada data
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal generate ID Penjualan: " + e);
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
        txtIdPenjualan = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jtgl = new com.toedter.calendar.JDateChooser();
        jLabel21 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txtNmKaryawan = new javax.swing.JTextField();
        txtKaryawan = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        cmbIdBooking = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtRumah = new javax.swing.JTextField();
        txtIdBooking = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        txtHargaJual = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtUangMuka = new javax.swing.JTextField();
        txtCustomer = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        Tunai = new javax.swing.JRadioButton();
        Kredit = new javax.swing.JRadioButton();
        jLabel24 = new javax.swing.JLabel();
        cmbTenor = new javax.swing.JComboBox<>();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        txtSisa = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtCicilan = new javax.swing.JTextField();

        setOpaque(false);

        jPanel1.setBackground(new java.awt.Color(143, 148, 251));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Form Transaksi Penjualan");

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
                .addGap(256, 256, 256))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
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

        txtIdPenjualan.setEnabled(false);
        txtIdPenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdPenjualanActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel18.setText("Tanggal Penjualan :");

        jLabel21.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel21.setText("ID Karyawan       :");

        jLabel33.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel33.setText("Nama Karyawan :");

        txtNmKaryawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNmKaryawanActionPerformed(evt);
            }
        });

        txtKaryawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKaryawanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtgl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtIdPenjualan, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNmKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(120, 120, 120))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtIdPenjualan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel21)
                                .addComponent(txtKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel33)
                                    .addComponent(txtNmKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jtgl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jLabel15.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel15.setText(" Pilih Booking  : ");

        jLabel34.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel34.setText("ID Booking     : ");

        jLabel19.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel19.setText("Nama Rumah :");

        jLabel35.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel35.setText("Nama Customer  :");

        jLabel36.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel36.setText("Harga Rumah      :");

        jLabel23.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel23.setText("Uang muka          :");

        txtUangMuka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUangMukaActionPerformed(evt);
            }
        });

        txtCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustomerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtIdBooking, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtRumah, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cmbIdBooking, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36)
                    .addComponent(jLabel23)
                    .addComponent(jLabel35))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtHargaJual, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addComponent(txtUangMuka, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addComponent(txtCustomer))
                .addGap(121, 121, 121))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(txtHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(txtUangMuka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbIdBooking, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIdBooking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(txtRumah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel37.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel37.setText("Metode  :");

        buttonGroup1.add(Tunai);
        Tunai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Tunai.setText("Tunai");
        Tunai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TunaiActionPerformed(evt);
            }
        });

        buttonGroup1.add(Kredit);
        Kredit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Kredit.setText("Kredit");

        jLabel24.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel24.setText("Sisa Kredit      :");

        cmbTenor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "12", "24", "36" }));

        jLabel38.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel38.setText("Bulan");

        jLabel39.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel39.setText("Tenor    :");

        jLabel40.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel40.setText("Cicilan / Bulan :");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37)
                    .addComponent(jLabel39))
                .addGap(40, 40, 40)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(Tunai, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Kredit, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(cmbTenor, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel38)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40)
                    .addComponent(jLabel24))
                .addGap(37, 37, 37)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSisa, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCicilan, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(120, 120, 120))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37)
                            .addComponent(Tunai)
                            .addComponent(Kredit))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbTenor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38)
                            .addComponent(jLabel39)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(txtSisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel40)
                            .addComponent(txtCicilan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(126, 126, 126)
                                .addComponent(btnbatal, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(90, 90, 90))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(16, Short.MAX_VALUE)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnsimpan)
                    .addComponent(btnbatal)
                    .addComponent(btnKeluar))
                .addGap(57, 57, 57))
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
        MenuUtama.getMenuUtama().showForm(new Form_DataPenjualannn());
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void btnsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpanActionPerformed
        String metode = Tunai.isSelected() ? "Tunai" : "Kredit";

    if (isEditMode) {
        // ✅ MODE EDIT - UPDATE DATA
        String sql = "UPDATE penjualan_rumah SET id_booking=?, id_rumah=?, id_customer=?, id_karyawan=?, tanggal_penjualan=?, harga_jual=?, metode_pembayaran=?, uang_muka=?, tenor=? WHERE id_penjualan=?";
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, (String) cmbIdBooking.getSelectedItem());
            stat.setString(2, idRumah);
            stat.setString(3, idCustomer);
            stat.setString(4, idKaryawan);

            java.util.Date utilDate = (java.util.Date) jtgl.getDate();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            stat.setDate(5, sqlDate);

            stat.setString(6, txtHargaJual.getText());
            stat.setString(7, metode); // metode_pembayaran

            stat.setString(8, txtUangMuka.getText());

            if (Kredit.isSelected() && cmbTenor.getSelectedItem() != null) {
                stat.setInt(9, Integer.parseInt(cmbTenor.getSelectedItem().toString()));
            } else {
                stat.setNull(9, java.sql.Types.INTEGER);
            }

            stat.setString(10, txtIdPenjualan.getText()); // WHERE

            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data transaksi berhasil diperbarui.");
            kosong();
            setEditMode(false);
            generateIdPenjualan();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal update data transaksi: " + e);
        }
    } else {
        // ✅ MODE INSERT - TAMBAH DATA BARU
        String sql = "INSERT INTO penjualan_rumah (id_penjualan, id_booking, id_rumah, id_customer, id_karyawan, tanggal_penjualan, harga_jual, metode_pembayaran, uang_muka, tenor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
               stat.setString(1, txtIdPenjualan.getText());
            stat.setString(2, (String) cmbIdBooking.getSelectedItem());
            stat.setString(3, idRumah);
            stat.setString(4, idCustomer);
            stat.setString(5, idKaryawan);

            java.util.Date utilDate = (java.util.Date) jtgl.getDate();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            stat.setDate(6, sqlDate);

            stat.setString(7, txtHargaJual.getText());
            stat.setString(8, metode); // metode_pembayaran

            stat.setString(9, txtUangMuka.getText());

            if (Kredit.isSelected() && cmbTenor.getSelectedItem() != null) {
                stat.setInt(10, Integer.parseInt(cmbTenor.getSelectedItem().toString()));
            } else {
                stat.setNull(10, java.sql.Types.INTEGER);
            }

           stat.executeUpdate();

// Ubah status rumah jadi Terjual
String sqlUpdateStatus = "UPDATE rumah SET status='Terjual' WHERE id_rumah=?";
PreparedStatement statStatus = conn.prepareStatement(sqlUpdateStatus);
statStatus.setString(1, idRumah);
statStatus.executeUpdate();

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

    private void txtKaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKaryawanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKaryawanActionPerformed

    private void txtCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustomerActionPerformed

    private void txtNmKaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNmKaryawanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNmKaryawanActionPerformed

    private void txtUangMukaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUangMukaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUangMukaActionPerformed

    private void txtIdPenjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdPenjualanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdPenjualanActionPerformed

    private void TunaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TunaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TunaiActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton Kredit;
    private javax.swing.JRadioButton Tunai;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnbatal;
    private javax.swing.JButton btnsimpan;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbIdBooking;
    private javax.swing.JComboBox<String> cmbTenor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private com.toedter.calendar.JDateChooser jtgl;
    private javax.swing.JTextField txtCicilan;
    private javax.swing.JTextField txtCustomer;
    private javax.swing.JTextField txtHargaJual;
    private javax.swing.JTextField txtIdBooking;
    private javax.swing.JTextField txtIdPenjualan;
    private javax.swing.JTextField txtKaryawan;
    private javax.swing.JTextField txtNmKaryawan;
    private javax.swing.JTextField txtRumah;
    private javax.swing.JTextField txtSisa;
    private javax.swing.JTextField txtUangMuka;
    // End of variables declaration//GEN-END:variables

  
}
