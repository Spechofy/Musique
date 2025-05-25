-- Création de la table Profil
CREATE TABLE IF NOT EXISTS Profil (
    ProfilId SERIAL PRIMARY KEY,
    Description TEXT,
    Photo TEXT,
    Biography TEXT,
    UserId INT,
    Information TEXT
);

-- Création de la table Playlist
CREATE TABLE IF NOT EXISTS Playlist (
    PlaylistId SERIAL PRIMARY KEY,
    Name VARCHAR(550) NOT NULL UNIQUE,
    Description VARCHAR(550),
    ProfilId INT,
    FOREIGN KEY (ProfilId) REFERENCES Profil(ProfilId)
);

-- Création de la table Musique
CREATE TABLE IF NOT EXISTS Musique (
    MusicId SERIAL PRIMARY KEY,
    Title VARCHAR(550) NOT NULL,
    Artiste VARCHAR(550),
    Genre VARCHAR(550),
    PlaylistId INT,
    FOREIGN KEY (PlaylistId) REFERENCES Playlist(PlaylistId)
);

--Création de la table Artistes, contenant les artistes préférés des utilisateurs.
CREATE TABLE IF NOT EXISTS Artistes (
    ArtisteId SERIAL PRIMARY KEY,
    Name VARCHAR(550) NOT NULL,
    Genres TEXT[], 
    Rang INTEGER NOT NULL CHECK (Rang >= 1 AND Rang <= 5),
    ProfilId INT,
    FOREIGN KEY (ProfilId) REFERENCES Profil(ProfilId)
);

--Création de la table MusiqueFav, contenant les musiques les plus écoutés des utilisateurs
CREATE TABLE IF NOT EXISTS MusiqueFav (
    MusicId SERIAL PRIMARY KEY,
    Title VARCHAR(550) NOT NULL,
    Artiste VARCHAR(550),
    Genre VARCHAR(550),
    Rang INTEGER NOT NULL CHECK (Rang >= 1 AND Rang <= 5),
    ProfilId INT,
    FOREIGN KEY (ProfilId) REFERENCES Profil(ProfilId),
);

