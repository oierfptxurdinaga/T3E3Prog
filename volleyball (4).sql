-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 16-03-2026 a las 11:45:05
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `volleyball`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `denboraldia`
--

CREATE TABLE `denboraldia` (
  `denboraldia_kod` int(11) NOT NULL,
  `izena` varchar(50) NOT NULL,
  `hasiera_data` date DEFAULT NULL,
  `amaiera_data` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `denboraldia`
--

INSERT INTO `denboraldia` (`denboraldia_kod`, `izena`, `hasiera_data`, `amaiera_data`) VALUES
(1, '2025/2026', '2026-03-12', '2026-03-12');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `denboraldia_jaurdunaldia`
--

CREATE TABLE `denboraldia_jaurdunaldia` (
  `denboraldia_kod` int(11) NOT NULL,
  `jaurdunaldi_kod` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `denboraldia_jaurdunaldia`
--

INSERT INTO `denboraldia_jaurdunaldia` (`denboraldia_kod`, `jaurdunaldi_kod`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 9),
(1, 10);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `entrenatzailea`
--

CREATE TABLE `entrenatzailea` (
  `entrenatzaile_kod` int(11) NOT NULL,
  `izena` varchar(100) DEFAULT NULL,
  `abizena` varchar(100) DEFAULT NULL,
  `NAN` varchar(9) DEFAULT NULL,
  `telefonoa` varchar(15) DEFAULT NULL,
  `herritartasuna` varchar(50) DEFAULT NULL,
  `taldea_kod` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `entrenatzailea`
--

INSERT INTO `entrenatzailea` (`entrenatzaile_kod`, `izena`, `abizena`, `NAN`, `telefonoa`, `herritartasuna`, `taldea_kod`) VALUES
(1, 'Jon', 'García', '12345678A', '600111222', 'ESP', 1),
(2, 'Marta', 'López', '87654321B', '600222333', 'ESP', 2),
(3, 'John', 'Brown', '11223344C', '600333444', 'ENG', 3),
(4, 'Ane', 'Fernández', '44332211D', '600444555', 'ESP', 4),
(5, 'Iker', 'Pérez', '55667788E', '600555666', 'ESP', 5),
(6, 'Leire', 'González', '99887766F', '600666777', 'ESP', 6);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `epailea`
--

CREATE TABLE `epailea` (
  `epailea_kod` int(11) NOT NULL,
  `izena` varchar(100) DEFAULT NULL,
  `abizena` varchar(100) DEFAULT NULL,
  `NAN` varchar(9) DEFAULT NULL,
  `herritartasuna` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `epailea`
--

INSERT INTO `epailea` (`epailea_kod`, `izena`, `abizena`, `NAN`, `herritartasuna`) VALUES
(1, 'David', 'Ruiz', '11111111A', 'ESP'),
(2, 'Sara', 'Sánchez', '22222222B', 'ESP'),
(3, 'Joseba', 'Alonso', '33333333C', 'ESP'),
(4, 'Nekane', 'Jiménez', '44444444D', 'ESP'),
(5, 'Xabier', 'Ortega', '55555555E', 'ESP'),
(6, 'Michael', 'Jordan', '66666666F', 'ENG'),
(7, 'Mikel', 'Romero', '77777777G', 'ESP'),
(8, 'Olatz', 'Serrano', '88888888H', 'ESP');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jaurdunaldia`
--

CREATE TABLE `jaurdunaldia` (
  `jaurdunaldi_kod` int(11) NOT NULL,
  `hasiera_data` date DEFAULT NULL,
  `amaiera_data` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `jaurdunaldia`
--

INSERT INTO `jaurdunaldia` (`jaurdunaldi_kod`, `hasiera_data`, `amaiera_data`) VALUES
(1, '2026-03-12', '2026-03-12'),
(2, '2026-03-19', '2026-03-19'),
(3, '2026-03-26', '2026-03-26'),
(4, '2026-04-02', '2026-04-02'),
(5, '2026-04-09', '2026-04-09'),
(6, '2026-04-16', '2026-04-16'),
(7, '2026-04-23', '2026-04-23'),
(8, '2026-04-30', '2026-04-30'),
(9, '2026-05-07', '2026-05-07'),
(10, '2026-05-14', '2026-05-14');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jokalariak`
--

CREATE TABLE `jokalariak` (
  `jokalariak_kod` int(11) NOT NULL,
  `izena` varchar(100) DEFAULT NULL,
  `abizena` varchar(100) DEFAULT NULL,
  `NAN` varchar(9) DEFAULT NULL,
  `posizioa` varchar(50) DEFAULT NULL,
  `pisua` decimal(5,2) DEFAULT NULL,
  `altuera` decimal(4,2) DEFAULT NULL,
  `herritartasuna` varchar(50) DEFAULT NULL,
  `taldea_kod` int(11) DEFAULT NULL
) ;

--
-- Volcado de datos para la tabla `jokalariak`
--

INSERT INTO `jokalariak` (`jokalariak_kod`, `izena`, `abizena`, `NAN`, `posizioa`, `pisua`, `altuera`, `herritartasuna`, `taldea_kod`) VALUES
(1, 'Carlos', 'Etxebarria', NULL, 'Armador', 78.00, 1.82, 'ESP', 1),
(2, 'Laura', 'Ruiz', '00000002A', 'Opuesta', 70.00, 1.75, 'ESP', 1),
(3, 'David', 'Goikoetxea', '00000003B', 'Central', 85.00, 1.90, 'ESP', 1),
(4, 'Sara', 'Arrieta', '00000004C', 'Receptora/Atacante', 68.00, 1.73, 'ESP', 1),
(5, 'Javier', 'Aguirre', NULL, 'Central', 88.00, 1.92, 'ESP', 1),
(6, 'Marta', 'Larrinaga', '00000006D', 'Líbero', 65.00, 1.68, 'ESP', 1),
(7, 'Daniel', 'Zubizarreta', '00000007E', 'Armador', 76.00, 1.80, 'ESP', 1),
(8, 'Elena', 'Etxeberria', '00000008F', 'Opuesta', 69.00, 1.74, 'ESP', 1),
(9, 'John', 'Johnson', '00000009G', 'Central', 87.00, 1.91, 'ENG', 1),
(10, 'Ana', 'Mendizabal', NULL, 'Receptora/Atacante', 67.00, 1.72, 'ESP', 1),
(11, 'Alejandro', 'Laka', '00000011H', 'Central', 89.00, 1.93, 'ESP', 1),
(12, 'Cristina', 'Zabaleta', '00000012I', 'Líbero', 64.00, 1.67, 'ESP', 1),
(13, 'Miguel', 'Salazar', '00000013J', 'Armador', 77.00, 1.81, 'ESP', 2),
(14, 'Paula', 'Iglesias', '00000014K', 'Opuesta', 71.00, 1.76, 'ESP', 2),
(15, 'Raúl', 'Bilbao', NULL, 'Central', 86.00, 1.89, 'ESP', 2),
(16, 'Isabel', 'Goitia', '00000016L', 'Receptora/Atacante', 66.00, 1.71, 'ESP', 2),
(17, 'Sergio', 'Etxaniz', '00000017M', 'Central', 90.00, 1.94, 'ESP', 2),
(18, 'Lucía', 'Urkijo', '00000018N', 'Líbero', 63.00, 1.66, 'ESP', 2),
(19, 'Fernando', 'Mendizabal', '00000019O', 'Armador', 75.00, 1.79, 'ESP', 2),
(20, 'Carmen', 'Aranburu', '00000020P', 'Opuesta', 70.00, 1.75, 'ESP', 2),
(21, 'Adrián', 'Zuloaga', '00000021Q', 'Central', 88.00, 1.92, 'ESP', 2),
(22, 'Claudia', 'Altuna', '00000022R', 'Receptora/Atacante', 68.00, 1.73, 'ESP', 2),
(23, 'Rubén', 'Aristi', '00000023S', 'Central', 91.00, 1.95, 'ESP', 2),
(24, 'Sandra', 'Zubieta', '00000024T', 'Líbero', 65.00, 1.68, 'ESP', 2),
(25, 'Marcos', 'Aretxaga', '00000025U', 'Armador', 76.00, 1.80, 'ESP', 3),
(26, 'Patricia', 'Mendieta', '00000026V', 'Opuesta', 72.00, 1.77, 'ESP', 3),
(27, 'Morlesin', 'Popov', '00000027W', 'Central', 87.00, 1.91, 'ESP', 3),
(28, 'Andrea', 'Etxeondo', '00000028X', 'Receptora/Atacante', 69.00, 1.74, 'ESP', 3),
(29, 'Álvaro', 'Altuna', '00000029Y', 'Central', 90.00, 1.94, 'ESP', 3),
(30, 'Eva', 'Urresti', '00000030Z', 'Líbero', 64.00, 1.67, 'ESP', 3),
(31, 'Roberto', 'Bilbao', '00000031A', 'Armador', 78.00, 1.83, 'ESP', 3),
(32, 'María', 'Zuloaga', '00000032B', 'Opuesta', 71.00, 1.76, 'ESP', 3),
(33, 'Luis', 'Lertxundi', '00000033C', 'Central', 88.00, 1.92, 'ESP', 3),
(34, 'Natalia', 'Goiko', '00000034D', 'Receptora/Atacante', 67.00, 1.72, 'ESP', 3),
(35, 'Diego', 'Aramendi', '00000035E', 'Central', 89.00, 1.93, 'ESP', 3),
(36, 'Beatriz', 'Zubia', '00000036F', 'Líbero', 66.00, 1.69, 'ESP', 3),
(37, 'Jonathan', 'Puccini', '00000037G', 'Armador', 79.00, 1.84, 'ITA', 4),
(38, 'Rosa', 'Santisteban', '00000038H', 'Opuesta', 72.00, 1.77, 'ESP', 4),
(39, 'Francisco', 'Zabala', '00000039I', 'Central', 87.00, 1.91, 'ESP', 4),
(40, 'Silvia', 'Kortazar', '00000040J', 'Receptora/Atacante', 68.00, 1.73, 'ESP', 4),
(41, 'José', 'Uranga', '00000041K', 'Central', 90.00, 1.94, 'ESP', 4),
(42, 'Teresa', 'Eguren', '00000042L', 'Líbero', 65.00, 1.68, 'ESP', 4),
(43, 'Juan', 'Goiko', '00000043M', 'Armador', 77.00, 1.81, 'ESP', 4),
(44, 'Raquel', 'Zarate', '00000044N', 'Opuesta', 70.00, 1.75, 'ESP', 4),
(45, 'Pedro', 'Lopez', '00000045O', 'Central', 89.00, 1.93, 'ESP', 4),
(46, 'Nuria', 'Elosegi', '00000046P', 'Receptora/Atacante', 67.00, 1.72, 'ESP', 4),
(47, 'Antonio', 'Berasategi', '00000047Q', 'Central', 91.00, 1.95, 'ESP', 4),
(48, 'Inés', 'Zubiri', '00000048R', 'Líbero', 64.00, 1.67, 'ESP', 4),
(49, 'Víctor', 'Bilbao', '00000049S', 'Armador', 78.00, 1.82, 'ESP', 5),
(50, 'Concha', 'Aranburu', '00000050T', 'Opuesta', 71.00, 1.76, 'ESP', 5),
(51, 'Ramón', 'Etxeberria', '00000051U', 'Central', 90.00, 1.94, 'ESP', 5),
(52, 'Lourdes', 'Zubizarreta', '00000052V', 'Receptora/Atacante', 68.00, 1.73, 'ESP', 5),
(53, 'Alberto', 'Goitia', '00000053W', 'Central', 88.00, 1.92, 'ESP', 5),
(54, 'Angela', 'Zabaleta', '00000054X', 'Líbero', 65.00, 1.68, 'ESP', 5),
(55, 'Enrique', 'Zubia', '00000055Y', 'Armador', 76.00, 1.80, 'ESP', 5),
(56, 'Mónica', 'Odriozola', '00000056Z', 'Opuesta', 72.00, 1.77, 'ESP', 5),
(57, 'Joaquín', 'Zuloaga', '00000057A', 'Central', 89.00, 1.93, 'ESP', 5),
(58, 'Lorena', 'Arregi', '00000058B', 'Receptora/Atacante', 67.00, 1.72, 'ESP', 5),
(59, 'Ricardo', 'Lertxundi', '00000059C', 'Central', 91.00, 1.95, 'ESP', 5),
(60, 'Esther', 'Goiko', '00000060D', 'Líbero', 64.00, 1.67, 'ESP', 5),
(61, 'Aitor', 'Arrieta', '00000061E', 'Armador', 77.00, 1.81, 'ESP', 6),
(62, 'Nerea', 'Bilbao', '00000062F', 'Opuesta', 71.00, 1.76, 'ESP', 6),
(63, 'Iker', 'Mendieta', '00000063G', 'Central', 88.00, 1.92, 'ESP', 6),
(64, 'Maialen', 'Lazkano', '00000064H', 'Receptora/Atacante', 68.00, 1.73, 'ESP', 6),
(65, 'Unai', 'Etxaniz', '00000065I', 'Central', 90.00, 1.94, 'ESP', 6),
(66, 'Ane', 'Iparragirre', '00000066J', 'Líbero', 65.00, 1.68, 'ESP', 6),
(67, 'Jon', 'Aramendi', '00000067K', 'Armador', 79.00, 1.84, 'ESP', 6),
(68, 'Leire', 'Ezkurdia', '00000068L', 'Opuesta', 70.00, 1.75, 'ESP', 6),
(69, 'Ander', 'Santamaria', '00000069M', 'Central', 87.00, 1.91, 'ESP', 6),
(70, 'Irati', 'Goienola', '00000070N', 'Receptora/Atacante', 67.00, 1.72, 'ESP', 6),
(71, 'Mikel', 'Zabala', '00000071O', 'Central', 91.00, 1.95, 'ESP', 6),
(72, 'Oihana', 'Altube', '00000072P', 'Líbero', 64.00, 1.67, 'ESP', 6),
(73, 'Urtzi', 'Estevez', NULL, 'Libero', 80.00, 1.78, NULL, 4),
(74, 'Ekaitz', 'Miguel', '79113972K', 'Libero', 78.00, 1.75, 'Bilbao', 2);

--
-- Disparadores `jokalariak`
--
DELIMITER $$
CREATE TRIGGER `trg_check_nan_format` BEFORE INSERT ON `jokalariak` FOR EACH ROW BEGIN
    IF NEW.NAN IS NOT NULL AND NOT (NEW.NAN REGEXP '^[0-9]{8}[A-Z]$') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Formato NAN inválido. Debe ser 8 dígitos seguidos de 1 letra mayúscula.';
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jokalari_taldea`
--

CREATE TABLE `jokalari_taldea` (
  `jokalariak_kod` int(11) NOT NULL,
  `taldea_kod` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `jokalari_taldea`
--

INSERT INTO `jokalari_taldea` (`jokalariak_kod`, `taldea_kod`) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(5, 1),
(6, 1),
(7, 1),
(8, 1),
(9, 1),
(10, 1),
(11, 1),
(12, 1),
(13, 2),
(14, 2),
(15, 2),
(16, 2),
(17, 2),
(18, 2),
(19, 2),
(20, 2),
(21, 2),
(22, 2),
(23, 2),
(24, 2),
(25, 3),
(26, 3),
(27, 3),
(28, 3),
(29, 3),
(30, 3),
(31, 3),
(32, 3),
(33, 3),
(34, 3),
(35, 3),
(36, 3),
(37, 4),
(38, 4),
(39, 4),
(40, 4),
(41, 4),
(42, 4),
(43, 4),
(44, 4),
(45, 4),
(46, 4),
(47, 4),
(48, 4),
(49, 5),
(50, 5),
(51, 5),
(52, 5),
(53, 5),
(54, 5),
(55, 5),
(56, 5),
(57, 5),
(58, 5),
(59, 5),
(60, 5),
(61, 6),
(62, 6),
(63, 6),
(64, 6),
(65, 6),
(66, 6),
(67, 6),
(68, 6),
(69, 6),
(70, 6),
(71, 6),
(72, 6);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `partida`
--

CREATE TABLE `partida` (
  `partida_kod` int(11) NOT NULL,
  `data` date DEFAULT NULL,
  `ordua` time DEFAULT NULL,
  `emaitza` varchar(20) DEFAULT NULL,
  `zigorrak` int(11) DEFAULT NULL,
  `txartelak` int(11) DEFAULT NULL,
  `epailea_kod` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `partida_jaurdunaldia`
--

CREATE TABLE `partida_jaurdunaldia` (
  `partida_kod` int(11) NOT NULL,
  `jaurdunaldi_kod` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `taldea`
--

CREATE TABLE `taldea` (
  `taldea_kod` int(11) NOT NULL,
  `izena` varchar(100) NOT NULL,
  `sortze_data` date DEFAULT NULL,
  `zelaia` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `taldea`
--

INSERT INTO `taldea` (`taldea_kod`, `izena`, `sortze_data`, `zelaia`) VALUES
(1, 'Matiko Txirrindulariak', '1998-01-01', 1),
(2, 'Miribilla Uhinen Jokoak', '2002-01-01', 2),
(3, 'Txurdinaga Harriak', '2000-01-01', 3),
(4, 'Usansolo Ortzadak', '2005-01-01', 4),
(5, 'Santutxu Haizeak', '1997-01-01', 5),
(6, 'Otxarkoaga Distira', '2010-01-01', 6);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `taldea_denboraldia`
--

CREATE TABLE `taldea_denboraldia` (
  `taldea_kod` int(11) NOT NULL,
  `denboraldia_kod` int(11) NOT NULL,
  `irabaziak` int(11) DEFAULT NULL,
  `galduak` int(11) DEFAULT NULL,
  `puntuak` int(11) DEFAULT NULL,
  `posizioa` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `taldea_partida`
--

CREATE TABLE `taldea_partida` (
  `taldea_kod` int(11) NOT NULL,
  `partida_kod` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `zelaia`
--

CREATE TABLE `zelaia` (
  `zelaia_kod` int(11) NOT NULL,
  `izena` varchar(100) NOT NULL,
  `kokapena` varchar(150) DEFAULT NULL,
  `kapazitatea` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `zelaia`
--

INSERT INTO `zelaia` (`zelaia_kod`, `izena`, `kokapena`, `kapazitatea`) VALUES
(1, 'Frontón Matiko', 'Matiko', 1200),
(2, 'Polideportivo Miribilla', 'Miribilla', 3000),
(3, 'Frontón Txurdinaga', 'Txurdinaga', 1500),
(4, 'Polideportivo Usansolo', 'Usansolo', 2500),
(5, 'Frontón Santutxu', 'Santutxu', 1400),
(6, 'Polideportivo Otxarkoaga', 'Otxarkoaga', 2800);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `denboraldia`
--
ALTER TABLE `denboraldia`
  ADD PRIMARY KEY (`denboraldia_kod`);

--
-- Indices de la tabla `denboraldia_jaurdunaldia`
--
ALTER TABLE `denboraldia_jaurdunaldia`
  ADD PRIMARY KEY (`denboraldia_kod`,`jaurdunaldi_kod`),
  ADD KEY `jaurdunaldi_kod` (`jaurdunaldi_kod`);

--
-- Indices de la tabla `entrenatzailea`
--
ALTER TABLE `entrenatzailea`
  ADD PRIMARY KEY (`entrenatzaile_kod`),
  ADD UNIQUE KEY `taldea_kod` (`taldea_kod`),
  ADD UNIQUE KEY `NAN` (`NAN`);

--
-- Indices de la tabla `epailea`
--
ALTER TABLE `epailea`
  ADD PRIMARY KEY (`epailea_kod`),
  ADD UNIQUE KEY `NAN` (`NAN`);

--
-- Indices de la tabla `jaurdunaldia`
--
ALTER TABLE `jaurdunaldia`
  ADD PRIMARY KEY (`jaurdunaldi_kod`);

--
-- Indices de la tabla `jokalariak`
--
ALTER TABLE `jokalariak`
  ADD PRIMARY KEY (`jokalariak_kod`),
  ADD UNIQUE KEY `NAN` (`NAN`),
  ADD KEY `idx_posizioa` (`posizioa`),
  ADD KEY `idx_altuera` (`altuera`),
  ADD KEY `idx_izena_abizena` (`izena`,`abizena`),
  ADD KEY `fk_taldea` (`taldea_kod`);

--
-- Indices de la tabla `jokalari_taldea`
--
ALTER TABLE `jokalari_taldea`
  ADD PRIMARY KEY (`jokalariak_kod`,`taldea_kod`),
  ADD KEY `taldea_kod` (`taldea_kod`);

--
-- Indices de la tabla `partida`
--
ALTER TABLE `partida`
  ADD PRIMARY KEY (`partida_kod`),
  ADD KEY `epailea_kod` (`epailea_kod`);

--
-- Indices de la tabla `partida_jaurdunaldia`
--
ALTER TABLE `partida_jaurdunaldia`
  ADD PRIMARY KEY (`partida_kod`,`jaurdunaldi_kod`),
  ADD KEY `jaurdunaldi_kod` (`jaurdunaldi_kod`);

--
-- Indices de la tabla `taldea`
--
ALTER TABLE `taldea`
  ADD PRIMARY KEY (`taldea_kod`),
  ADD KEY `zelaia` (`zelaia`);

--
-- Indices de la tabla `taldea_denboraldia`
--
ALTER TABLE `taldea_denboraldia`
  ADD PRIMARY KEY (`taldea_kod`,`denboraldia_kod`),
  ADD KEY `denboraldia_kod` (`denboraldia_kod`);

--
-- Indices de la tabla `taldea_partida`
--
ALTER TABLE `taldea_partida`
  ADD PRIMARY KEY (`taldea_kod`,`partida_kod`),
  ADD KEY `partida_kod` (`partida_kod`);

--
-- Indices de la tabla `zelaia`
--
ALTER TABLE `zelaia`
  ADD PRIMARY KEY (`zelaia_kod`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `denboraldia`
--
ALTER TABLE `denboraldia`
  MODIFY `denboraldia_kod` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `entrenatzailea`
--
ALTER TABLE `entrenatzailea`
  MODIFY `entrenatzaile_kod` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `epailea`
--
ALTER TABLE `epailea`
  MODIFY `epailea_kod` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `jaurdunaldia`
--
ALTER TABLE `jaurdunaldia`
  MODIFY `jaurdunaldi_kod` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `jokalariak`
--
ALTER TABLE `jokalariak`
  MODIFY `jokalariak_kod` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `partida`
--
ALTER TABLE `partida`
  MODIFY `partida_kod` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `taldea`
--
ALTER TABLE `taldea`
  MODIFY `taldea_kod` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `zelaia`
--
ALTER TABLE `zelaia`
  MODIFY `zelaia_kod` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `denboraldia_jaurdunaldia`
--
ALTER TABLE `denboraldia_jaurdunaldia`
  ADD CONSTRAINT `denboraldia_jaurdunaldia_ibfk_1` FOREIGN KEY (`denboraldia_kod`) REFERENCES `denboraldia` (`denboraldia_kod`),
  ADD CONSTRAINT `denboraldia_jaurdunaldia_ibfk_2` FOREIGN KEY (`jaurdunaldi_kod`) REFERENCES `jaurdunaldia` (`jaurdunaldi_kod`);

--
-- Filtros para la tabla `entrenatzailea`
--
ALTER TABLE `entrenatzailea`
  ADD CONSTRAINT `entrenatzailea_ibfk_1` FOREIGN KEY (`taldea_kod`) REFERENCES `taldea` (`taldea_kod`);

--
-- Filtros para la tabla `jokalariak`
--
ALTER TABLE `jokalariak`
  ADD CONSTRAINT `fk_taldea` FOREIGN KEY (`taldea_kod`) REFERENCES `taldea` (`taldea_kod`);

--
-- Filtros para la tabla `jokalari_taldea`
--
ALTER TABLE `jokalari_taldea`
  ADD CONSTRAINT `jokalari_taldea_ibfk_1` FOREIGN KEY (`jokalariak_kod`) REFERENCES `jokalariak` (`jokalariak_kod`),
  ADD CONSTRAINT `jokalari_taldea_ibfk_2` FOREIGN KEY (`taldea_kod`) REFERENCES `taldea` (`taldea_kod`);

--
-- Filtros para la tabla `partida`
--
ALTER TABLE `partida`
  ADD CONSTRAINT `partida_ibfk_1` FOREIGN KEY (`epailea_kod`) REFERENCES `epailea` (`epailea_kod`);

--
-- Filtros para la tabla `partida_jaurdunaldia`
--
ALTER TABLE `partida_jaurdunaldia`
  ADD CONSTRAINT `partida_jaurdunaldia_ibfk_1` FOREIGN KEY (`partida_kod`) REFERENCES `partida` (`partida_kod`),
  ADD CONSTRAINT `partida_jaurdunaldia_ibfk_2` FOREIGN KEY (`jaurdunaldi_kod`) REFERENCES `jaurdunaldia` (`jaurdunaldi_kod`);

--
-- Filtros para la tabla `taldea`
--
ALTER TABLE `taldea`
  ADD CONSTRAINT `taldea_ibfk_1` FOREIGN KEY (`zelaia`) REFERENCES `zelaia` (`zelaia_kod`);

--
-- Filtros para la tabla `taldea_denboraldia`
--
ALTER TABLE `taldea_denboraldia`
  ADD CONSTRAINT `taldea_denboraldia_ibfk_1` FOREIGN KEY (`taldea_kod`) REFERENCES `taldea` (`taldea_kod`),
  ADD CONSTRAINT `taldea_denboraldia_ibfk_2` FOREIGN KEY (`denboraldia_kod`) REFERENCES `denboraldia` (`denboraldia_kod`);

--
-- Filtros para la tabla `taldea_partida`
--
ALTER TABLE `taldea_partida`
  ADD CONSTRAINT `taldea_partida_ibfk_1` FOREIGN KEY (`taldea_kod`) REFERENCES `taldea` (`taldea_kod`),
  ADD CONSTRAINT `taldea_partida_ibfk_2` FOREIGN KEY (`partida_kod`) REFERENCES `partida` (`partida_kod`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
