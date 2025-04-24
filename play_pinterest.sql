-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Apr 23, 2025 at 03:49 AM
-- Server version: 8.0.30
-- PHP Version: 8.3.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `play_pinterest`
--

-- --------------------------------------------------------

--
-- Table structure for table `comments`
--

CREATE TABLE `comments` (
  `id` int NOT NULL,
  `user_id` int NOT NULL,
  `image_id` int NOT NULL,
  `content` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `comments`
--

INSERT INTO `comments` (`id`, `user_id`, `image_id`, `content`) VALUES
(1, 3, 5, 'Keren Abiezzz'),
(2, 3, 15, 'jomok'),
(3, 3, 7, 'Samurai cuy'),
(4, 3, 29, 'Bagus Banget Gambar nya '),
(5, 3, 5, 'test'),
(6, 3, 29, 'wow');

-- --------------------------------------------------------

--
-- Table structure for table `follows`
--

CREATE TABLE `follows` (
  `id` int NOT NULL,
  `follower_id` int NOT NULL,
  `following_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `images`
--

CREATE TABLE `images` (
  `id` int NOT NULL,
  `user_id` int NOT NULL,
  `image_path` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `images`
--

INSERT INTO `images` (`id`, `user_id`, `image_path`) VALUES
(5, 1, 'uploads/5260fb29-e7ce-4972-b428-5cb8bf0e3a33.jpg'),
(7, 2, 'uploads/3074e85e-ce3f-48b0-b541-cd43befe1f79.jpg'),
(8, 2, 'uploads/80768520-cda5-4a46-9505-768e0d8dd57f.jpg'),
(9, 2, 'uploads/3ac287c5-50e2-442e-83eb-58b7e869d5ea.jpg'),
(10, 2, 'uploads/eb0cb9e3-c311-4f01-99e7-4c77afb1b1d9.jpg'),
(11, 2, 'uploads/e1b10f3c-8ec0-437d-aa69-93b13afc7182.jpg'),
(12, 1, 'uploads/b196e3b8-60c8-40e5-b0f6-bd27bf86e100.jpg'),
(13, 1, 'uploads/cc49b627-7b4d-4fe5-ac60-6852a477aad6.jpg'),
(14, 1, 'uploads/4a0384f1-b411-4c66-ba7c-cafd9cf0f479.jpg'),
(15, 1, 'uploads/fd855574-9b63-413c-8724-4c1a940243b9.jpg'),
(16, 3, 'uploads/a9b39f6a-d074-4bc5-bb8d-80b7dddac456.jpg'),
(17, 3, 'uploads/96426fd4-97db-4448-9829-5dec5b33bde5.png'),
(27, 3, 'uploads/1a9cd35d-1214-402d-b726-5ead586e7ef8.jpeg'),
(28, 3, 'uploads/73f9468b-c932-416f-a4a9-5a36950d41fc.jpeg'),
(29, 3, 'uploads/3b7f9fdd-b634-4cf6-99ac-68e0250867d4.jpeg'),
(30, 3, 'uploads/f384e258-aa06-4987-b446-5e3537b0acb7.jpeg'),
(31, 3, 'uploads/b8e45968-e7bb-4c70-afba-eb99710613c0.jpeg'),
(32, 3, 'uploads/be0e55cd-5224-4eb3-b4ab-df24bc5a84a2.jpeg'),
(33, 3, 'uploads/71080300-4a19-4ef0-99db-52513a6e369c.jpeg');

-- --------------------------------------------------------

--
-- Table structure for table `likes`
--

CREATE TABLE `likes` (
  `id` int NOT NULL,
  `user_id` int NOT NULL,
  `image_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `likes`
--

INSERT INTO `likes` (`id`, `user_id`, `image_id`) VALUES
(7, 1, 4),
(8, 1, 5),
(9, 1, 6),
(10, 1, 8),
(11, 1, 10),
(12, 1, 13),
(13, 1, 12),
(14, 2, 5),
(15, 2, 9),
(16, 3, 5),
(17, 3, 10);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int NOT NULL,
  `username` varchar(50) NOT NULL,
  `password_user` varchar(255) NOT NULL,
  `pp_path` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password_user`, `pp_path`) VALUES
(1, 'zanuar', 'rikza', 'pp/43879802-7165-4038-b891-85a05f10c907.jpg'),
(2, 'Pahlawan', 'zanuar', NULL),
(3, 'Farhan Kebab 🥵🥵', 'dummy123', 'pp/b229ff61-905b-4224-9975-e8a1fcf98f87.jpg'),
(4, 'testakun', 'test', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `follows`
--
ALTER TABLE `follows`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `images`
--
ALTER TABLE `images`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `likes`
--
ALTER TABLE `likes`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `comments`
--
ALTER TABLE `comments`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `follows`
--
ALTER TABLE `follows`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `images`
--
ALTER TABLE `images`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `likes`
--
ALTER TABLE `likes`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
