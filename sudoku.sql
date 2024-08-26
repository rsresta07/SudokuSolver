-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 26, 2024 at 08:14 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sudoku`
--

-- --------------------------------------------------------

--
-- Table structure for table `scores`
--

CREATE DATABASE IF NOT EXISTS `sudoku`;
USE `sudoku`;

CREATE TABLE `scores` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `score` INT(11) NOT NULL,
  `time_taken` INT(11) NOT NULL,
  `difficulty` VARCHAR(10) NOT NULL,
  `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `scores`
--

INSERT INTO `scores` (`id`, `username`, `score`, `time_taken`, `difficulty`, `timestamp`) VALUES
(5, 'Salin', 10, 72, 'Easy', '2024-03-14 10:24:11'),
(6, 'Salin', 10, 66, 'Easy', '2024-04-05 14:46:33'),
(7, 'Pranaya', 20, 68, 'Medium', '2024-03-22 16:08:12'),
(8, 'Pranaya', 20, 97, 'Medium', '2024-05-02 09:37:45'),
(9, 'Player', 10, 281, 'Easy', '2024-05-15 17:53:19'),
(10, 'Player', 30, 72, 'Hard', '2024-04-23 13:28:54'),
(11, 'Swariya', 10, 154, 'Easy', '2024-03-28 11:12:08'),
(12, 'Player', 10, 132, 'Easy', '2024-05-18 06:37:55');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

`scores``users``scores`

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
