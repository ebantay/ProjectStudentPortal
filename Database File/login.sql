-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Oct 21, 2014 at 09:54 AM
-- Server version: 5.6.20
-- PHP Version: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `login`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `showlogin`()
BEGIN
SELECT * FROM tablelogin;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `tableadmin`
--

CREATE TABLE IF NOT EXISTS `tableadmin` (
`ID` int(2) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `status` varchar(10) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `tableadmin`
--

INSERT INTO `tableadmin` (`ID`, `username`, `password`, `status`) VALUES
(1, 'admin', 'admin', 'ADMIN');

-- --------------------------------------------------------

--
-- Table structure for table `tablelogin`
--

CREATE TABLE IF NOT EXISTS `tablelogin` (
`ID` int(50) NOT NULL,
  `username` bigint(15) NOT NULL,
  `password` varchar(25) NOT NULL,
  `fname` varchar(20) NOT NULL,
  `mname` varchar(20) NOT NULL,
  `lname` varchar(20) NOT NULL,
  `course` varchar(25) NOT NULL,
  `address` varchar(25) NOT NULL,
  `contact` bigint(25) NOT NULL,
  `balance` bigint(20) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=21 ;

--
-- Dumping data for table `tablelogin`
--

INSERT INTO `tablelogin` (`ID`, `username`, `password`, `fname`, `mname`, `lname`, `course`, `address`, `contact`, `balance`) VALUES
(1, 201312516, '071288', 'LAMBERTO JR', 'AZUR', 'BRUNO', 'BSCS', 'LAVANYA', 9161051015, 6000),
(10, 201412021, 'EARL', 'EARL', 'MONSALUD', 'BANTAY', 'BSCS', 'TEJERO', 9273822075, 0),
(11, 11001101, 'QWERTY', 'JOMS', 'LAKAS', 'GAGO', 'BSCS', '166 JULUGAN', 9999999, 0),
(12, 55555555, 'FUMIKO', 'SHOARAN', 'LANSANGAN', 'AUSTRIA', 'BSCS', 'BATANGAS CITY', 9999999999, 0),
(13, 12102036, 'DAVEARIAS', 'CHRISTIAN', 'QUIZA', 'ARIAS', 'BSCS', 'CAVITE CITY', 9267769001, 0),
(14, 123456, '123456', 'JES', 'P', 'BANIAGA', 'BSCS', 'TANZA ', 888888, 0),
(15, 201312058, 'KATHERINE', 'KATHERINE', '', 'BALLESTEROS', 'BSCS', 'LANDMASS PARK', 9057025721, 0),
(16, 201312059, 'CHECHE', 'CHERRIE', 'CAUTIVAR', 'BATOON', 'BSINFOTECH', 'TANZA', 9785745654, 6000),
(19, 12345, 'ADMIN', 'JUAN', 'PONSE', 'POSAS', 'BSCS', 'ALFONSO.CAVITE', 9123456789, 10000),
(20, 54321, 'POSAS', 'ALAISSA', 'HANDCUFFS', 'PRISINTO', 'DMT', 'ZAMBOANGA CITY', 9968752214, 6000);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tableadmin`
--
ALTER TABLE `tableadmin`
 ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `tablelogin`
--
ALTER TABLE `tablelogin`
 ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tableadmin`
--
ALTER TABLE `tableadmin`
MODIFY `ID` int(2) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `tablelogin`
--
ALTER TABLE `tablelogin`
MODIFY `ID` int(50) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=21;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
