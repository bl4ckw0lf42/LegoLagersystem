-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 07. Dez 2017 um 11:49
-- Server-Version: 10.1.28-MariaDB
-- PHP-Version: 7.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `legolagersystem`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `artikel`
--

CREATE TABLE `artikel` (
  `artikelID` int(99) NOT NULL,
  `beschreibung` varchar(99) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `slot`
--

CREATE TABLE `slot` (
  `slotID` int(99) NOT NULL,
  `artikelID` int(99) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `artikel`
--
ALTER TABLE `artikel`
  ADD PRIMARY KEY (`artikelID`);

--
-- Indizes für die Tabelle `slot`
--
ALTER TABLE `slot`
  ADD PRIMARY KEY (`slotID`),
  ADD KEY `artikelID` (`artikelID`);

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `slot`
--
ALTER TABLE `slot`
  ADD CONSTRAINT `slot_ibfk_1` FOREIGN KEY (`artikelID`) REFERENCES `artikel` (`artikelID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
