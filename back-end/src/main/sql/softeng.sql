-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 12, 2020 at 04:38 PM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `softeng`
--
CREATE DATABASE IF NOT EXISTS `softeng` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `softeng`;

-- --------------------------------------------------------

--
-- Table structure for table `incidents`
--

DROP TABLE IF EXISTS `incidents`;
CREATE TABLE IF NOT EXISTS `incidents` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(256) NOT NULL,
  `description` text DEFAULT NULL,
  `x` double NOT NULL,
  `y` double NOT NULL,
  `start_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `end_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `title` (`title`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Truncate table before insert `incidents`
--

TRUNCATE TABLE `incidents`;
--
-- Dumping data for table `incidents`
--

INSERT INTO `incidents` (`id`, `title`, `description`, `x`, `y`, `start_date`, `end_date`) VALUES
(1, 'Τροχαίο', 'Τροχαίο στη συμβολή Ακαδημίας και Πανεπιστημίου', 0, 0, '2020-03-25 13:01:00', NULL),
(2, 'Τροχαίο', 'Τροχαίο στη συμβολή Χαριλάου Τρικούπη και Ιπποκράτους, ΕΚΑΒ', 0, 0, '2020-03-25 13:02:00', NULL),
(3, 'Πυρκαγιά', 'Πυρκαγιά σε ταράτσα υπογείου', 0, 0, '2020-03-25 13:03:00', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `username` varchar(64) NOT NULL,
  `password` varchar(256) NOT NULL,
  `first_name` varchar(128) NOT NULL,
  `last_name` varchar(128) NOT NULL,
  `role` varchar(64) NOT NULL,
  `agency` varchar(64) NOT NULL,
  PRIMARY KEY (`username`),
  KEY `role` (`role`),
  KEY `agency` (`agency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Truncate table before insert `users`
--

TRUNCATE TABLE `users`;
--
-- Dumping data for table `users`
--

INSERT INTO `users` (`username`, `password`, `first_name`, `last_name`, `role`, `agency`) VALUES
('user1', 'elvis1935', 'elvis', 'presley', 'ROLE1', 'AGENCY1'),
('user2', 'do_not_store_clear_password_in_db', 'jimi', 'hendrix', 'ROLE2', 'AGENCY1');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
