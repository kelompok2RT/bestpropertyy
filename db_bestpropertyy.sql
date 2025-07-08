-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 07, 2025 at 01:59 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_bestpropertyy`
--

-- --------------------------------------------------------

--
-- Table structure for table `booking`
--

CREATE TABLE `booking` (
  `id_booking` varchar(30) NOT NULL,
  `id_rumah` varchar(30) NOT NULL,
  `id_customer` varchar(30) NOT NULL,
  `id_karyawan` varchar(30) NOT NULL,
  `tglbooking` date NOT NULL,
  `dpayment` decimal(15,2) NOT NULL,
  `status` enum('booking','dibatalkan') NOT NULL,
  `keterangan` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `booking`
--

INSERT INTO `booking` (`id_booking`, `id_rumah`, `id_customer`, `id_karyawan`, `tglbooking`, `dpayment`, `status`, `keterangan`) VALUES
('BKG001', 'RMH001', 'CST001', 'KRY001', '2025-07-07', 10000000.00, 'booking', ''),
('BKG002', 'RMH002', 'CST002', 'KRY003', '2025-07-07', 10000000.00, 'booking', '');

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `id_customer` varchar(30) NOT NULL,
  `namacust` varchar(30) NOT NULL,
  `NIK` varchar(30) NOT NULL,
  `NoHP` varchar(30) NOT NULL,
  `Email` varchar(30) NOT NULL,
  `Alamat` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`id_customer`, `namacust`, `NIK`, `NoHP`, `Email`, `Alamat`) VALUES
('CST001', 'Alwan', '21752255', '021255', 'alawan@gmail.com', 'Jl. MANDOR'),
('CST002', 'cika', '3729489', '03642846', 'cikaciks@gmail.com', 'bogor'),
('CST004', 'wanda', '54645', '02479567', 'wanda@gmail.com', 'cibubur'),
('CST005', 'cici', '045845', '09384975', 'cici@gmail.com', 'jakarta');

-- --------------------------------------------------------

--
-- Table structure for table `detail_pembayaran_bulanan`
--

CREATE TABLE `detail_pembayaran_bulanan` (
  `id_detail` int(11) NOT NULL,
  `id_pembayaran` varchar(10) NOT NULL,
  `bulan_ke` int(11) NOT NULL,
  `jumlah_pembayaran` decimal(15,2) NOT NULL,
  `tanggal_bayar` date DEFAULT NULL,
  `status_bayar` enum('Belum','Lunas') DEFAULT 'Belum'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `detail_pembayaran_bulanan`
--

INSERT INTO `detail_pembayaran_bulanan` (`id_detail`, `id_pembayaran`, `bulan_ke`, `jumlah_pembayaran`, `tanggal_bayar`, `status_bayar`) VALUES
(29, 'BYR001', 1, 7916666.67, '2025-07-17', 'Lunas'),
(30, 'BYR002', 2, 7916666.67, '2025-07-31', 'Lunas'),
(31, 'BYR003', 1, 6666666.67, '2025-07-07', 'Lunas'),
(32, 'BYR004', 2, 6666666.67, '2025-07-09', 'Lunas');

-- --------------------------------------------------------

--
-- Table structure for table `karyawan`
--

CREATE TABLE `karyawan` (
  `id_karyawan` varchar(10) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `jabatan` varchar(50) NOT NULL,
  `no_hp` varchar(15) NOT NULL,
  `email` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `karyawan`
--

INSERT INTO `karyawan` (`id_karyawan`, `nama`, `jabatan`, `no_hp`, `email`) VALUES
('KRY001', 'Pikonn', 'Admin', '021255214', 'th89@BestProperty.com'),
('KRY002', 'Budi', 'Marketing', '0212544555', 'Budi@BestProperty.com'),
('KRY003', 'saddam', 'Marketing', '0858932222', 'dam@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `login`
--

CREATE TABLE `login` (
  `username` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `login`
--

INSERT INTO `login` (`username`, `password`) VALUES
('admin', 'admin123');

-- --------------------------------------------------------

--
-- Table structure for table `nota_pembayaran`
--

CREATE TABLE `nota_pembayaran` (
  `id_nota` varchar(10) NOT NULL,
  `id_pembayaran` varchar(10) DEFAULT NULL,
  `tanggal_cetak` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pembayaran`
--

CREATE TABLE `pembayaran` (
  `id_pembayaran` varchar(10) NOT NULL,
  `id_penjualan` varchar(10) DEFAULT NULL,
  `bulan_ke` int(11) DEFAULT NULL,
  `tanggal_pembayaran` date DEFAULT NULL,
  `jumlah_pembayaran` double DEFAULT NULL,
  `metode_pembayaran` varchar(20) DEFAULT NULL,
  `keterangan` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pembayaran_cicilan`
--

CREATE TABLE `pembayaran_cicilan` (
  `id_pembayaran` varchar(10) NOT NULL,
  `id_penjualan` varchar(20) NOT NULL,
  `tanggal_pembayaran` date NOT NULL,
  `metode_pembayaran` varchar(20) NOT NULL,
  `total_dibayar` decimal(15,2) DEFAULT 0.00,
  `sisa_cicilan` decimal(15,2) DEFAULT 0.00,
  `nama_karyawan` varchar(100) DEFAULT NULL,
  `nama_customer` varchar(100) DEFAULT NULL,
  `keterangan` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pembayaran_cicilan`
--

INSERT INTO `pembayaran_cicilan` (`id_pembayaran`, `id_penjualan`, `tanggal_pembayaran`, `metode_pembayaran`, `total_dibayar`, `sisa_cicilan`, `nama_karyawan`, `nama_customer`, `keterangan`) VALUES
('BYR001', 'PNJ001', '2025-07-17', 'Transfer', 7916666.67, 182083333.33, 'Pikonn', 'Alwan', ''),
('BYR002', 'PNJ001', '2025-07-31', 'Tunai', 15833333.34, 174166666.66, 'Pikonn', 'Alwan', ''),
('BYR003', 'PNJ002', '2025-07-07', 'Transfer', 6666666.67, 233333333.33, 'saddam', 'cika', ''),
('BYR004', 'PNJ002', '2025-07-09', 'Tunai', 13333333.34, 226666666.66, 'saddam', 'cika', '');

-- --------------------------------------------------------

--
-- Table structure for table `penjualan_rumah`
--

CREATE TABLE `penjualan_rumah` (
  `id_penjualan` varchar(10) NOT NULL,
  `id_booking` varchar(10) DEFAULT NULL,
  `id_rumah` varchar(10) DEFAULT NULL,
  `id_customer` varchar(10) DEFAULT NULL,
  `id_karyawan` varchar(10) DEFAULT NULL,
  `tanggal_penjualan` date DEFAULT NULL,
  `harga_jual` decimal(15,2) DEFAULT NULL,
  `metode_pembayaran` enum('Tunai','Kredit') DEFAULT NULL,
  `uang_muka` decimal(15,2) DEFAULT NULL,
  `tenor` int(11) DEFAULT NULL,
  `keterangan` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `penjualan_rumah`
--

INSERT INTO `penjualan_rumah` (`id_penjualan`, `id_booking`, `id_rumah`, `id_customer`, `id_karyawan`, `tanggal_penjualan`, `harga_jual`, `metode_pembayaran`, `uang_muka`, `tenor`, `keterangan`) VALUES
('PNJ001', 'BKG001', 'RMH001', 'CST001', 'KRY001', '2025-07-07', 200000000.00, 'Kredit', 10000000.00, 12, NULL),
('PNJ002', 'BKG002', 'RMH002', 'CST002', 'KRY003', '2025-07-07', 250000000.00, 'Kredit', 10000000.00, 36, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `rumah`
--

CREATE TABLE `rumah` (
  `id_rumah` varchar(10) NOT NULL,
  `kode_rumah` varchar(25) NOT NULL,
  `alamat` text NOT NULL,
  `tipe` varchar(20) NOT NULL,
  `luas_tanah` float NOT NULL,
  `luas_bangunan` float NOT NULL,
  `harga` decimal(15,2) NOT NULL,
  `status` enum('tersedia','terjual','dibooking','') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rumah`
--

INSERT INTO `rumah` (`id_rumah`, `kode_rumah`, `alamat`, `tipe`, `luas_tanah`, `luas_bangunan`, `harga`, `status`) VALUES
('RMH001', 'A01', 'JL. Mawar', 'Tipe 45', 90, 45, 200000000.00, 'dibooking'),
('RMH002', 'A02', 'JL. SALAH', 'Tipe 60', 12, 14, 250000000.00, 'terjual'),
('RMH003', 'A03', 'Cilungis', 'Tipe 36', 14, 15, 150000000.00, 'dibooking'),
('RMH004', '004', 'raden saleh', 'Tipe 45', 45, 35, 500000000.00, 'tersedia');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`id_booking`),
  ADD KEY `booking_ibfk_1` (`id_customer`),
  ADD KEY `booking_ibfk_2` (`id_karyawan`),
  ADD KEY `booking_ibfk_3` (`id_rumah`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`id_customer`);

--
-- Indexes for table `detail_pembayaran_bulanan`
--
ALTER TABLE `detail_pembayaran_bulanan`
  ADD PRIMARY KEY (`id_detail`),
  ADD UNIQUE KEY `id_pembayaran` (`id_pembayaran`,`bulan_ke`);

--
-- Indexes for table `karyawan`
--
ALTER TABLE `karyawan`
  ADD PRIMARY KEY (`id_karyawan`);

--
-- Indexes for table `login`
--
ALTER TABLE `login`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `nota_pembayaran`
--
ALTER TABLE `nota_pembayaran`
  ADD PRIMARY KEY (`id_nota`),
  ADD KEY `id_pembayaran` (`id_pembayaran`);

--
-- Indexes for table `pembayaran`
--
ALTER TABLE `pembayaran`
  ADD PRIMARY KEY (`id_pembayaran`),
  ADD KEY `id_penjualan` (`id_penjualan`);

--
-- Indexes for table `pembayaran_cicilan`
--
ALTER TABLE `pembayaran_cicilan`
  ADD PRIMARY KEY (`id_pembayaran`),
  ADD KEY `id_penjualan` (`id_penjualan`);

--
-- Indexes for table `penjualan_rumah`
--
ALTER TABLE `penjualan_rumah`
  ADD PRIMARY KEY (`id_penjualan`),
  ADD KEY `id_booking` (`id_booking`),
  ADD KEY `id_rumah` (`id_rumah`),
  ADD KEY `id_customer` (`id_customer`),
  ADD KEY `id_karyawan` (`id_karyawan`);

--
-- Indexes for table `rumah`
--
ALTER TABLE `rumah`
  ADD PRIMARY KEY (`id_rumah`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `detail_pembayaran_bulanan`
--
ALTER TABLE `detail_pembayaran_bulanan`
  MODIFY `id_detail` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `booking`
--
ALTER TABLE `booking`
  ADD CONSTRAINT `booking_ibfk_1` FOREIGN KEY (`id_customer`) REFERENCES `customer` (`id_customer`) ON UPDATE CASCADE,
  ADD CONSTRAINT `booking_ibfk_2` FOREIGN KEY (`id_karyawan`) REFERENCES `karyawan` (`id_karyawan`) ON UPDATE CASCADE,
  ADD CONSTRAINT `booking_ibfk_3` FOREIGN KEY (`id_rumah`) REFERENCES `rumah` (`id_rumah`) ON UPDATE CASCADE;

--
-- Constraints for table `detail_pembayaran_bulanan`
--
ALTER TABLE `detail_pembayaran_bulanan`
  ADD CONSTRAINT `detail_pembayaran_bulanan_ibfk_1` FOREIGN KEY (`id_pembayaran`) REFERENCES `pembayaran_cicilan` (`id_pembayaran`);

--
-- Constraints for table `nota_pembayaran`
--
ALTER TABLE `nota_pembayaran`
  ADD CONSTRAINT `nota_pembayaran_ibfk_1` FOREIGN KEY (`id_pembayaran`) REFERENCES `pembayaran` (`id_pembayaran`);

--
-- Constraints for table `pembayaran`
--
ALTER TABLE `pembayaran`
  ADD CONSTRAINT `pembayaran_ibfk_1` FOREIGN KEY (`id_penjualan`) REFERENCES `penjualan_rumah` (`id_penjualan`);

--
-- Constraints for table `pembayaran_cicilan`
--
ALTER TABLE `pembayaran_cicilan`
  ADD CONSTRAINT `pembayaran_cicilan_ibfk_1` FOREIGN KEY (`id_penjualan`) REFERENCES `penjualan_rumah` (`id_penjualan`);

--
-- Constraints for table `penjualan_rumah`
--
ALTER TABLE `penjualan_rumah`
  ADD CONSTRAINT `penjualan_rumah_ibfk_1` FOREIGN KEY (`id_booking`) REFERENCES `booking` (`id_booking`),
  ADD CONSTRAINT `penjualan_rumah_ibfk_2` FOREIGN KEY (`id_rumah`) REFERENCES `rumah` (`id_rumah`),
  ADD CONSTRAINT `penjualan_rumah_ibfk_3` FOREIGN KEY (`id_customer`) REFERENCES `customer` (`id_customer`),
  ADD CONSTRAINT `penjualan_rumah_ibfk_4` FOREIGN KEY (`id_karyawan`) REFERENCES `karyawan` (`id_karyawan`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
