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
    Name VARCHAR(255) NOT NULL,
    Description TEXT,
    ProfilId INT,
    FOREIGN KEY (ProfilId) REFERENCES Profil(ProfilId)
);

-- Création de la table Musique
CREATE TABLE IF NOT EXISTS Musique (
    MusicId SERIAL PRIMARY KEY,
    Title VARCHAR(255) NOT NULL,
    Artiste VARCHAR(255),
    Genre VARCHAR(100),
    PlaylistId INT,
    FOREIGN KEY (PlaylistId) REFERENCES Playlist(PlaylistId)
);
