DROP DATABASE IF EXISTS superheroSightingsTest;
CREATE DATABASE superheroSightingsTest;

USE superheroSightingsTest;

CREATE TABLE Organization (
    OrganizationId INT PRIMARY KEY AUTO_INCREMENT,
    IsHero BOOL NOT NULL,
    `Name` VARCHAR(100) NOT NULL,
    `Description` VARCHAR(255)  NULL,
    Address VARCHAR(255) NOT NULL,
    Contact VARCHAR(255) NOT NULL
);

CREATE TABLE Superpower (
    SuperpowerId INT PRIMARY KEY AUTO_INCREMENT,
    `Name` VARCHAR(50) NOT NULL,
    `Description` VARCHAR(255) NULL
);

CREATE TABLE Location (
    LocationId INT PRIMARY KEY AUTO_INCREMENT,
    `Name` VARCHAR(100) NOT NULL,
    `Description` VARCHAR(255) NULL,
    Address VARCHAR(255) NULL,
    Latitude DECIMAL(10,8) NOT NULL,
    Longitude DECIMAL(11,8) NOT NULL
);

CREATE TABLE Hero (
    HeroId INT PRIMARY KEY AUTO_INCREMENT,
    IsHero BOOL NOT NULL,
    `Name` VARCHAR(50) NOT NULL,
    `Description` VARCHAR(255) NULL,
    SuperpowerId INT NOT NULL,
    superImage blob,
     CONSTRAINT fk_Hero_Superpower FOREIGN KEY (SuperpowerId)
        REFERENCES Superpower (SuperpowerId)
);
 

  
CREATE TABLE HeroOrganization (
    HeroId INT NOT NULL,
    OrganizationId INT NOT NULL,
    PRIMARY KEY pk_HeroOrganization (HeroId , OrganizationId),
    CONSTRAINT fk__HeroOrganization_Hero FOREIGN KEY (HeroId)
        REFERENCES Hero (HeroId),
    CONSTRAINT fk_HeroOrganization_Organization FOREIGN KEY (OrganizationId)
        REFERENCES Organization (OrganizationId)
);




CREATE TABLE Sighting (
    SightingId INT PRIMARY KEY AUTO_INCREMENT,
    HeroId INT NOT NULL,
    LocationId INT NOT NULL,
    `Date` DATE NOT NULL,
    CONSTRAINT fk_Sighting_Hero FOREIGN KEY (HeroId)
        REFERENCES Hero (HeroId),
    CONSTRAINT fk_Sighting_Location FOREIGN KEY (LocationId)
        REFERENCES Location (LocationId)
);
  
 