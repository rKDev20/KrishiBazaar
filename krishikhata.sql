-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Feb 16, 2020 at 05:46 PM
-- Server version: 5.5.27
-- PHP Version: 5.4.7

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `krishikhata`
--

-- --------------------------------------------------------

--
-- Table structure for table `advertisements`
--

CREATE TABLE IF NOT EXISTS `advertisements` (
  `ad_id` int(10) NOT NULL AUTO_INCREMENT,
  `mobile` bigint(10) NOT NULL,
  `category` int(10) DEFAULT NULL,
  `sub_category` int(10) DEFAULT NULL,
  `name` varchar(200) NOT NULL,
  `quantity` float NOT NULL,
  `rate` float NOT NULL,
  `description` varchar(300) NOT NULL,
  `pincode` int(6) NOT NULL,
  `clock` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ad_id`),
  KEY `mobile` (`mobile`),
  KEY `mobile_2` (`mobile`),
  KEY `category` (`category`),
  KEY `sub_category` (`sub_category`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=23 ;

--
-- Dumping data for table `advertisements`
--

INSERT INTO `advertisements` (`ad_id`, `mobile`, `category`, `sub_category`, `name`, `quantity`, `rate`, `description`, `pincode`, `clock`, `status`) VALUES
(20, 8981874182, NULL, NULL, 'rice', 34, 21, 'my rice', 700102, '2020-02-08 20:15:34', 1),
(22, 7652089925, 6, 7, 'rice', 34, 21, 'tasty tasty rice', 751024, '2020-02-08 20:17:48', 1);

-- --------------------------------------------------------

--
-- Table structure for table `authorisation`
--

CREATE TABLE IF NOT EXISTS `authorisation` (
  `token` varchar(32) NOT NULL,
  `mobile` bigint(10) NOT NULL,
  `fcm` varchar(255) NOT NULL,
  PRIMARY KEY (`token`),
  KEY `mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `authorisation`
--

INSERT INTO `authorisation` (`token`, `mobile`, `fcm`) VALUES
('28e06f15b902e31c3268ee5c9627b015', 9559277825, 'edRtLdFTpgs:APA91bHpGCvq0djKdedq552NGe4tpTLPy-6sbBomAE8NdbcNPGAGmQtf6PDQgqaeAotDnkTyBsqbnkO7O-eLTkS6bwYNB3DVaMdvyADI8cLLpQGRTIhMunWXY7LTXIgiWenDLb60gsSD'),
('6f7b2f9e255c6cfc865d974d447b9bec', 8981874182, '2'),
('9db8c926f7fd8bf6e1cb278f7ac9732e', 8981874182, 'eAg61-tM_lA:APA91bGcji8F4WYqaaXZQqi0hruJa3IjCiGhDDJ4YGLhYW514adzsbjk9Z9BDeZ6kv3enaYBeLylzX-4loazzDXhu2Og5YS1MuwrId6u_IgdFjDhexw-xk7MRi63xocS5YvjK-vTBS3h'),
('dde72148969b02e238174678e7cc3062', 8981874182, 'eX742imfeeg:APA91bHFyxPvl6ytTuaYkgDMZsI12f366U52r1wKEQ4JSaLKORyDUJtjWyd-srPDX6NBm9d89HBbg7tqoywQJ89KCsUkZBALSfWmwSjHQZR1aMkZcJgpLHS2258wXASfE4mIKEucbg8l'),
('eba1d20d809756f6425c91fe6a941ca9', 8981874182, 'eX742imfeeg:APA91bHFyxPvl6ytTuaYkgDMZsI12f366U52r1wKEQ4JSaLKORyDUJtjWyd-srPDX6NBm9d89HBbg7tqoywQJ89KCsUkZBALSfWmwSjHQZR1aMkZcJgpLHS2258wXASfE4mIKEucbg8l');

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE IF NOT EXISTS `categories` (
  `category_id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `variety_id` int(11) NOT NULL,
  PRIMARY KEY (`category_id`),
  KEY `variety_id` (`variety_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=106 ;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`category_id`, `name`, `variety_id`) VALUES
(2, 'Baajra', 1),
(3, 'Jowar', 1),
(4, 'Jau', 1),
(5, 'Jaee', 1),
(6, 'Rice', 1),
(7, 'Rye', 1),
(8, 'Wheat', 1),
(9, 'Besan', 2),
(10, 'Chana', 2),
(12, 'Urad', 2),
(13, 'Moong', 2),
(14, 'Masoor', 2),
(15, 'Matar', 2),
(16, 'Arhar/Toor', 2),
(17, 'Rajma', 2),
(18, 'Sarson', 3),
(19, 'Kusum', 3),
(20, 'Alsi', 3),
(21, 'Bhaang', 3),
(22, 'Sunflower', 3),
(23, 'Khasakhas', 3),
(24, 'Mango', 4),
(25, 'Banana', 4),
(26, 'Amla', 4),
(27, 'Kharbuja', 4),
(28, 'Papaya', 4),
(29, 'Grapes', 4),
(30, 'Pears', 4),
(31, 'Pomogranate', 4),
(32, 'Shahtooth', 4),
(33, 'Orange', 4),
(34, 'Watermelon', 4),
(35, 'Mosambi', 4),
(36, 'Pineapple', 4),
(37, 'Jamun', 4),
(38, 'Ber', 4),
(39, 'Aadoo', 4),
(40, 'Amarood', 4),
(41, 'Khubaanee', 4),
(42, 'Litchi', 4),
(43, 'Cherry', 4),
(44, 'Sitaphal', 4),
(45, 'Khajoor', 4),
(46, 'Strawberry', 4),
(47, 'Chiku', 4),
(48, 'Ber', 4),
(49, 'Badam', 5),
(50, 'Kaaju', 5),
(51, 'Anzeer', 5),
(52, 'Kishamish', 5),
(53, 'Akhrot', 5),
(54, 'Khubaanee', 5),
(55, 'Pista', 5),
(56, 'Kesar', 5),
(57, 'Moongaphalee', 5),
(58, '', 5),
(59, 'Saunf', 6),
(60, 'Hing', 6),
(61, 'Haldi', 6),
(62, 'Laal mirch', 6),
(63, 'Tez patta', 6),
(64, 'Elaichi', 6),
(65, 'Jeera', 6),
(66, 'Rai', 6),
(67, 'Kali mirch', 6),
(68, 'Shahjeera', 6),
(69, 'Ajwain', 6),
(70, 'Dalchini', 6),
(71, 'Laung', 6),
(72, 'Nariyal', 4),
(73, 'Dhaniya powder', 6),
(74, 'Kari patta', 6),
(75, 'Kasoori', 6),
(76, 'Karela', 7),
(77, 'Lauki', 7),
(78, 'Phool Gobhi', 7),
(79, 'Arbi', 7),
(80, 'Kheera', 7),
(81, 'Patta Gobhi', 7),
(82, 'Gajar', 7),
(83, 'Dhaniya', 7),
(84, 'Baingan', 7),
(85, 'Methi patta', 7),
(86, '', 7),
(87, 'Hari mirch', 7),
(88, 'Lahasun', 7),
(89, 'Adrakh', 7),
(90, 'Shimla mirch', 7),
(91, 'Hari pyaaz', 7),
(92, 'Matar ', 7),
(93, 'Tinde', 7),
(94, 'Bhindi ', 7),
(95, 'Nimbu', 7),
(96, 'Phudina', 7),
(97, 'Pyaaz', 7),
(98, 'Kaddu', 7),
(99, 'Aloo', 7),
(100, 'Mooli', 7),
(101, 'Turai', 7),
(102, 'Sakarkand ', 7),
(103, 'Parval', 7),
(104, 'Palak', 7),
(105, 'Tamatar', 7);

-- --------------------------------------------------------

--
-- Table structure for table `otpverification`
--

CREATE TABLE IF NOT EXISTS `otpverification` (
  `clock` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `otp` int(6) NOT NULL,
  `mobile` bigint(10) NOT NULL,
  PRIMARY KEY (`mobile`),
  UNIQUE KEY `mobile` (`mobile`),
  KEY `mobile_2` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `pincode`
--

CREATE TABLE IF NOT EXISTS `pincode` (
  `pincode` int(11) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  PRIMARY KEY (`pincode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `sms_instance`
--

CREATE TABLE IF NOT EXISTS `sms_instance` (
  `sms_id` int(10) NOT NULL AUTO_INCREMENT,
  `mobile` bigint(10) NOT NULL,
  `status` int(2) NOT NULL,
  `ad_id` int(11) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `description` varchar(300) DEFAULT NULL,
  `pincode` int(6) DEFAULT NULL,
  `quantity` float DEFAULT NULL,
  `rate` float DEFAULT NULL,
  `clock` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`sms_id`),
  KEY `mobile` (`mobile`),
  KEY `product_id` (`ad_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `sms_instance`
--

INSERT INTO `sms_instance` (`sms_id`, `mobile`, `status`, `ad_id`, `name`, `description`, `pincode`, `quantity`, `rate`, `clock`) VALUES
(10, 7652089925, 8, 22, NULL, NULL, NULL, NULL, NULL, '2020-02-12 18:23:12');

-- --------------------------------------------------------

--
-- Table structure for table `sub_categories`
--

CREATE TABLE IF NOT EXISTS `sub_categories` (
  `sub_id` int(10) NOT NULL AUTO_INCREMENT,
  `category_id` int(10) NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`sub_id`),
  KEY `category_id` (`category_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=39 ;

--
-- Dumping data for table `sub_categories`
--

INSERT INTO `sub_categories` (`sub_id`, `category_id`, `name`) VALUES
(2, 6, 'Basmati'),
(3, 6, 'Brown'),
(4, 6, 'Arwa'),
(5, 6, 'Sugandha'),
(6, 6, 'Sharbati'),
(7, 6, 'Govindbhog'),
(8, 6, 'Hansraj'),
(9, 6, 'Laxmibhog'),
(10, 6, 'Minicate'),
(11, 6, 'Annapoorna'),
(12, 10, 'Kala'),
(13, 10, 'Safed/Kabuli'),
(14, 15, 'Hara'),
(15, 15, 'Safed'),
(16, 24, 'Alphonsos'),
(17, 24, 'Badami'),
(18, 24, 'Chaunsa '),
(19, 24, 'Dasheri'),
(20, 24, 'Kesar'),
(21, 24, 'Langra'),
(22, 24, 'Mulgoba'),
(23, 24, 'Neelam'),
(24, 24, 'Raspuri'),
(25, 24, 'Himsagar'),
(26, 24, 'Totapuri'),
(27, 24, 'Benishan'),
(28, 24, 'Malda'),
(29, 25, 'Robusta'),
(30, 25, 'Rasthali'),
(31, 25, 'Chakrakeli'),
(32, 25, 'Matti'),
(33, 25, 'Singapuri'),
(34, 25, 'Chinia'),
(35, 25, 'Harichal (Lokhandi)'),
(36, 25, 'Shreemanti'),
(37, 25, 'Poovan'),
(38, 25, 'Monthan');

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE IF NOT EXISTS `transactions` (
  `transaction_id` int(10) NOT NULL AUTO_INCREMENT,
  `ad_id` int(10) NOT NULL,
  `proposed_rate` float DEFAULT NULL,
  `mobile` bigint(10) NOT NULL,
  `clock` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `pincode` int(6) NOT NULL,
  `status` int(2) NOT NULL,
  PRIMARY KEY (`ad_id`,`mobile`),
  UNIQUE KEY `transaction_id_2` (`transaction_id`),
  KEY `ad_id` (`ad_id`,`mobile`),
  KEY `transaction_id` (`transaction_id`),
  KEY `mobile` (`mobile`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`transaction_id`, `ad_id`, `proposed_rate`, `mobile`, `clock`, `pincode`, `status`) VALUES
(7, 20, NULL, 9559277825, '2020-02-15 22:03:24', 751024, 2),
(2, 22, NULL, 8981874182, '2020-02-09 12:04:40', 333333, 2);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `mobile` bigint(10) NOT NULL,
  `address` varchar(200) DEFAULT NULL,
  `pincode` int(6) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`mobile`, `address`, `pincode`, `name`) VALUES
(7375088919, 'scasca', 232323, 'ayush'),
(7376088919, NULL, NULL, NULL),
(7652089925, NULL, NULL, NULL),
(8383893403, 'KP 10 B', 751024, 'Vipul Gehun'),
(8825141548, NULL, NULL, NULL),
(8981874182, 'kiti uncd', 333333, 'anon'),
(9559277825, '8b152', 751024, 'Ayush ');

-- --------------------------------------------------------

--
-- Table structure for table `variety`
--

CREATE TABLE IF NOT EXISTS `variety` (
  `variety_id` int(11) NOT NULL AUTO_INCREMENT,
  `variety` varchar(50) NOT NULL,
  PRIMARY KEY (`variety_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `variety`
--

INSERT INTO `variety` (`variety_id`, `variety`) VALUES
(1, 'Grains'),
(2, 'Dal'),
(3, 'Oil seeds'),
(4, 'Fruits'),
(5, 'Dry fruits'),
(6, 'Spices'),
(7, 'Vegetables');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `advertisements`
--
ALTER TABLE `advertisements`
  ADD CONSTRAINT `advertisements_ibfk_2` FOREIGN KEY (`mobile`) REFERENCES `users` (`mobile`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `advertisements_ibfk_3` FOREIGN KEY (`category`) REFERENCES `categories` (`category_id`) ON DELETE SET NULL ON UPDATE SET NULL,
  ADD CONSTRAINT `advertisements_ibfk_4` FOREIGN KEY (`sub_category`) REFERENCES `sub_categories` (`sub_id`) ON DELETE SET NULL ON UPDATE SET NULL;

--
-- Constraints for table `authorisation`
--
ALTER TABLE `authorisation`
  ADD CONSTRAINT `authorisation_ibfk_2` FOREIGN KEY (`mobile`) REFERENCES `users` (`mobile`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `categories`
--
ALTER TABLE `categories`
  ADD CONSTRAINT `categories_ibfk_1` FOREIGN KEY (`variety_id`) REFERENCES `variety` (`variety_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `sms_instance`
--
ALTER TABLE `sms_instance`
  ADD CONSTRAINT `sms_instance_ibfk_2` FOREIGN KEY (`mobile`) REFERENCES `users` (`mobile`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `sub_categories`
--
ALTER TABLE `sub_categories`
  ADD CONSTRAINT `sub_categories_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_4` FOREIGN KEY (`ad_id`) REFERENCES `advertisements` (`ad_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `transactions_ibfk_5` FOREIGN KEY (`mobile`) REFERENCES `users` (`mobile`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
