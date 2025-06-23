-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 22. Jun 2025 um 18:14
-- Server-Version: 10.4.32-MariaDB
-- PHP-Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `elitegloss_db`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `admin_user`
--

CREATE TABLE `admin_user` (
  `id` int(11) NOT NULL,
  `nutzername` varchar(100) NOT NULL,
  `passwort_hash` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `auftraege`
--

CREATE TABLE `auftraege` (
  `id` int(11) NOT NULL,
  `kunde_id` int(11) NOT NULL,
  `paket` varchar(100) NOT NULL,
  `zusatzleistungen` text DEFAULT NULL,
  `datum` date NOT NULL DEFAULT curdate(),
  `preis` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `auftraege`
--

INSERT INTO `auftraege` (`id`, `kunde_id`, `paket`, `zusatzleistungen`, `datum`, `preis`) VALUES
(1, 1, 'Advanced – 80 €', 'Motor', '2025-06-21', 100.00),
(2, 3, 'Ultra – 120 €', 'Motor, Innenreinigung', '2025-06-21', 155.00),
(3, 3, 'Ultra – 120 €', '', '2025-06-22', 120.00),
(4, 2, 'Basic – 50 €', 'Motor, Innenreinigung', '2025-06-22', 85.00),
(5, 1, 'Advanced – 80 €', 'Motor', '2025-06-22', 100.00),
(6, 1, 'Basic – 50 €', 'Innenreinigung', '2025-06-22', 65.00),
(7, 1, 'Basic – 50 €', 'Innenreinigung', '2025-06-22', 65.00),
(8, 4, 'Ultra – 120 €', 'Innenreinigung', '2025-06-22', 135.00),
(9, 5, 'Ultra – 120 €', '', '2025-06-22', 120.00);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `kunden`
--

CREATE TABLE `kunden` (
  `id` int(11) NOT NULL,
  `vorname` varchar(100) NOT NULL,
  `nachname` varchar(100) NOT NULL,
  `telefonnummer` varchar(50) NOT NULL,
  `email` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Daten für Tabelle `kunden`
--

INSERT INTO `kunden` (`id`, `vorname`, `nachname`, `telefonnummer`, `email`) VALUES
(1, 'Enzo', 'Gratz', '+43 213 2341', 'enzo@gmail.com'),
(2, 'Kristijan', 'Davidovic', '+4367663954403', 'kdavidovic@gmail.com'),
(3, 'Günter', 'Nussbaum', '+34 1231234512213', 'guenter@nussbaum.at'),
(4, 'Gerald', 'Delfser', '+43 25234112 213', 'gerald@delfser.at'),
(5, 'Peter', 'Lichtner', '+123 12353241', 'lichtner.peter@gmail.com');

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `admin_user`
--
ALTER TABLE `admin_user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nutzername` (`nutzername`);

--
-- Indizes für die Tabelle `auftraege`
--
ALTER TABLE `auftraege`
  ADD PRIMARY KEY (`id`),
  ADD KEY `kunde_id` (`kunde_id`);

--
-- Indizes für die Tabelle `kunden`
--
ALTER TABLE `kunden`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `admin_user`
--
ALTER TABLE `admin_user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `auftraege`
--
ALTER TABLE `auftraege`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT für Tabelle `kunden`
--
ALTER TABLE `kunden`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `auftraege`
--
ALTER TABLE `auftraege`
  ADD CONSTRAINT `auftraege_ibfk_1` FOREIGN KEY (`kunde_id`) REFERENCES `kunden` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
