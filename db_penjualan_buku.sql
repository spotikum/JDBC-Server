/*
SQLyog Ultimate v12.5.1 (64 bit)
MySQL - 10.4.21-MariaDB : Database - db_penjualan
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`db_penjualan` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `db_penjualan`;

/*Table structure for table `tb_buku` */

DROP TABLE IF EXISTS `tb_buku`;

CREATE TABLE `tb_buku` (
  `id_buku` int(11) NOT NULL AUTO_INCREMENT,
  `judul_buku` varchar(255) DEFAULT NULL,
  `deskripsi` varchar(255) DEFAULT NULL,
  `penulis` varchar(255) DEFAULT NULL,
  `penerbit` varchar(255) DEFAULT NULL,
  `tanggal_terbit` date DEFAULT NULL,
  PRIMARY KEY (`id_buku`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

/*Data for the table `tb_buku` */

insert  into `tb_buku`(`id_buku`,`judul_buku`,`deskripsi`,`penulis`,`penerbit`,`tanggal_terbit`) values 
(1,'Malam Jumat Kliwon','Pada malam jumat kliwon','H. Ma\'Alam','CV. Serem','2021-01-05'),
(2,'Muay Thai Master Ching Chong','Perjalanan sang petarunx, Ching Chong','Khong Guan','PT. Terbit Jaya','2021-01-05'),
(3,'Awas ada Sule','Keseharian Sule','Getivi','PT. Terbit Jaya','2021-01-05'),
(4,'Spring Without You','Musim Semi tanpamu, akan tiba sebentar lagi','Illana Tan','PT. Terbit Jaya','2021-01-05'),
(5,'Istriku adalah Ayahnya?','Selama ini, kok bisa?','Kang Lawak','PT. Terbit Jaya','2021-01-05'),
(6,'Detektif Too-Cool','Legenda detektif Too-Cool memecahkan masalah - masalah','Kinan Doyle','PT. Terbit Jaya','2021-01-05'),
(7,'Ulat dan Ular','Cerita fabel anak - anak','Kak Setow','CV. Serem','2021-01-05'),
(8,'Cerita di balik masa lampau','Cerita fakta dan konspirasi tahun 576','Soeganteng','CV. Serem','2021-01-05'),
(9,'1001 Cara Pintar','Buku ini akan membuatmu pintar dengan cepat','Rahmawati','CV. Serem','2021-01-05'),
(10,'Laba - laba mutant','Tergigit laba - laba membuat pria ini berubah!','Stanley','CV. Media Printdo','2021-01-05'),
(11,'Ilmu hitam Nusantara','Mengenal tradisi dan budaya ilmu hitam Nusantara','Dukun Lokalan','CV. Media Printdo','2021-01-05'),
(12,'Maling Kondang','Cerita legenda Si Maling Kondang','Legend Man','CV. Media Printdo','2021-01-05');

/*Table structure for table `tb_kategori` */

DROP TABLE IF EXISTS `tb_kategori`;

CREATE TABLE `tb_kategori` (
  `id_kategori` int(11) NOT NULL AUTO_INCREMENT,
  `id_buku` int(11) DEFAULT NULL,
  `kategori` varchar(255) DEFAULT NULL,
  `harga` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_kategori`),
  KEY `id_barang` (`id_buku`),
  CONSTRAINT `tb_kategori_ibfk_1` FOREIGN KEY (`id_buku`) REFERENCES `tb_buku` (`id_buku`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

/*Data for the table `tb_kategori` */

insert  into `tb_kategori`(`id_kategori`,`id_buku`,`kategori`,`harga`) values 
(1,1,'Horror',5000000),
(2,2,'Aksi',600000),
(3,4,'Drama',5000000),
(5,5,'Komedi',5500000),
(6,6,'Misteri',6000000),
(7,7,'Anak-anak',700000),
(8,8,'Sejarah',300000),
(9,9,'Edukasi',500000),
(10,10,'Sci-fi',8000000),
(11,11,'Mistis',900000),
(12,12,'Dongeng',800000);

/*Table structure for table `tb_pembelian` */

DROP TABLE IF EXISTS `tb_pembelian`;

CREATE TABLE `tb_pembelian` (
  `id_pembelian` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) DEFAULT NULL,
  `id_kategori` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_pembelian`),
  KEY `id_user` (`id_user`),
  KEY `tb_pembelian_ibfk_2` (`id_kategori`),
  CONSTRAINT `tb_pembelian_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `tb_user` (`id_user`),
  CONSTRAINT `tb_pembelian_ibfk_2` FOREIGN KEY (`id_kategori`) REFERENCES `tb_kategori` (`id_kategori`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

/*Data for the table `tb_pembelian` */

insert  into `tb_pembelian`(`id_pembelian`,`id_user`,`id_kategori`) values 
(11,20,11),
(12,22,1),
(13,22,10);

/*Table structure for table `tb_user` */

DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
  `id_user` int(11) NOT NULL AUTO_INCREMENT,
  `nama` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `alamat` varchar(255) DEFAULT NULL,
  `no_hp` bigint(14) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4;

/*Data for the table `tb_user` */

insert  into `tb_user`(`id_user`,`nama`,`username`,`alamat`,`no_hp`,`password`) values 
(19,'Test','test123','jalan kenangan',819191919191,'ZaKnzlF/XtIo0yHxyaKdoA=='),
(20,'Chingchong','cingcong','Jalan China',81269696969,'fdYHlKmFDHnouCNYmJm1ww=='),
(21,'spot','bukanspot','Jln. Kenangan Indah',8123456789,'Ik6fApU1SiFJ/ad4Qq0Qvw=='),
(22,'Alan','alan123','Jalan Gatsu',813728192,'Z0EhDwGSdj/6BtgJ92ihvQ==');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
