-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jan 31, 2020 at 07:59 PM
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
  `quantity` int(10) NOT NULL,
  `rate` int(10) NOT NULL,
  `description` varchar(300) NOT NULL,
  `pincode` int(6) NOT NULL,
  `clock` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ad_id`),
  KEY `mobile` (`mobile`),
  KEY `mobile_2` (`mobile`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `advertisements`
--

INSERT INTO `advertisements` (`ad_id`, `mobile`, `category`, `sub_category`, `name`, `quantity`, `rate`, `description`, `pincode`, `clock`) VALUES
(1, 7376088919, NULL, NULL, 'Weed', 10, 1000, 'High above the skies', 751024, '2020-01-30 21:07:12');

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE IF NOT EXISTS `categories` (
  `category_id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `sms_instance`
--

CREATE TABLE IF NOT EXISTS `sms_instance` (
  `sms_id` int(10) NOT NULL AUTO_INCREMENT,
  `mobile` bigint(10) NOT NULL,
  `status` int(1) NOT NULL,
  `name` varchar(200) NOT NULL,
  `description` varchar(300) NOT NULL,
  `pincode` int(6) NOT NULL,
  `quantity` int(10) NOT NULL,
  `rate` int(10) NOT NULL,
  `clock` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`sms_id`),
  KEY `mobile` (`mobile`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `sms_instance`
--

INSERT INTO `sms_instance` (`sms_id`, `mobile`, `status`, `name`, `description`, `pincode`, `quantity`, `rate`, `clock`) VALUES
(2, 7376088919, 7, '', '', 0, 0, 0, '2020-01-30 16:37:46'),
(3, 8825141548, 3, 'Tera gand', '', 0, 0, 0, '2020-01-30 16:41:37');

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE IF NOT EXISTS `transactions` (
  `transaction_id` int(10) NOT NULL,
  `ad_id` int(10) NOT NULL,
  `proposed_rate` int(10) DEFAULT NULL,
  `mobile` bigint(10) NOT NULL,
  `timestamp` date NOT NULL,
  `pincode` int(6) DEFAULT NULL,
  `status` int(2) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `ad_id` (`ad_id`,`mobile`),
  KEY `transaction_id` (`transaction_id`),
  KEY `mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `mobile` bigint(10) NOT NULL,
  `address` varchar(200) DEFAULT NULL,
  `pincode` int(6) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `role` int(2) DEFAULT NULL,
  PRIMARY KEY (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`mobile`, `address`, `pincode`, `name`, `role`) VALUES
(7376088919, NULL, NULL, NULL, NULL),
(8825141548, NULL, NULL, NULL, NULL);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `advertisements`
--
ALTER TABLE `advertisements`
  ADD CONSTRAINT `advertisements_ibfk_1` FOREIGN KEY (`mobile`) REFERENCES `users` (`mobile`);

--
-- Constraints for table `sms_instance`
--
ALTER TABLE `sms_instance`
  ADD CONSTRAINT `sms_instance_ibfk_1` FOREIGN KEY (`mobile`) REFERENCES `users` (`mobile`);

--
-- Constraints for table `sub_categories`
--
ALTER TABLE `sub_categories`
  ADD CONSTRAINT `sub_categories_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`ad_id`) REFERENCES `advertisements` (`ad_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `transactions_ibfk_3` FOREIGN KEY (`mobile`) REFERENCES `users` (`mobile`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
