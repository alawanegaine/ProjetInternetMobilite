DROP DATABASE IF EXISTS prj_internet_et_mobilite;
CREATE database prj_internet_et_mobilite;
USE prj_internet_et_mobilite;
CREATE TABLE pinpoint(
	id SMALLINT AUTO_INCREMENT PRIMARY KEY,
	latitude varchar(255) not null,
	longitude varchar(255) not null,
	lienImage varchar(255) not null,
	titre varchar(255) not null,
	commentaire varchar(2047),
	dateAjout date not null
);
INSERT INTO pinpoint(latitude, longitude, lienImage, titre, commentaire, dateAjout)
VALUES ('45.379575', '-71.926471', 'C:\\Users\\Damien\\Desktop\\ImageInternetEtMobilite\\voie7.bmp', 'Voie 7', 'premier item du jeu de test', '2015-12-08 16:20:54');
INSERT INTO pinpoint(latitude, longitude, lienImage, titre, commentaire, dateAjout)
VALUES ('45.378934', '-71.929432', 'C:\\Users\\Damien\\Desktop\\ImageInternetEtMobilite\\coop.bmp', 'Coop', 'second item du jeu de test', '2015-10-08 11:17:25');
INSERT INTO pinpoint(latitude, longitude, lienImage, titre, commentaire, dateAjout)
VALUES ('45.380494', '-71.926868', 'C:\\Users\\Damien\\Desktop\\ImageInternetEtMobilite\\Fac de science.bmp', 'Fac de science', 'troisième item du jeu de test', '2015-08-08 07:27:15');
INSERT INTO pinpoint(latitude, longitude, lienImage, titre, commentaire, dateAjout)
VALUES ('45.377430', '-71.928220', 'C:\\Users\\Damien\\Desktop\\ImageInternetEtMobilite\\Centre sportif.bmp', 'Voie 1 - Gymnase', 'quatrième item du jeu de test', '2015-06-08 19:45:19');
INSERT INTO pinpoint(latitude, longitude, lienImage, titre, commentaire, dateAjout)
VALUES ('45.380413', '-71.912317', 'C:\\Users\\Damien\\Desktop\\ImageInternetEtMobilite\\Mont bellevue.bmp', 'Mont Bellevue', 'cinquième item du jeu de test', '2015-04-08 12:00:00');
INSERT INTO pinpoint(latitude, longitude, lienImage, titre, commentaire, dateAjout)
VALUES ('45.378411', '-71.926477', 'C:\\Users\\Damien\\Desktop\\ImageInternetEtMobilite\\Fac de génie.bmp', 'Fac de génie', 'sixième et dernier item du jeu de test', '2015-02-08 13:59:42');
