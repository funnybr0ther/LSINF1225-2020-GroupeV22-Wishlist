--
-- File generated with SQLiteStudio v3.2.1 on dim. mars 1 18:59:16 2020
--
-- Text encoding used: ISO-8859-4
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: amis
CREATE TABLE amis (num_suiveur REFERENCES utilisateur ("user id") NOT NULL, num_suivi REFERENCES utilisateur ("user id") NOT NULL, relation DEFAULT Amis);
INSERT INTO amis (num_suiveur, num_suivi, relation) VALUES (192764, 437625, 'famille');
INSERT INTO amis (num_suiveur, num_suivi, relation) VALUES (437625, 192764, 'famille');
INSERT INTO amis (num_suiveur, num_suivi, relation) VALUES (437625, 108494, 'ami proche');
INSERT INTO amis (num_suiveur, num_suivi, relation) VALUES (437625, 837679, 'proche');
INSERT INTO amis (num_suiveur, num_suivi, relation) VALUES (108494, 437625, NULL);
INSERT INTO amis (num_suiveur, num_suivi, relation) VALUES (837679, 437625, 'proche');

-- Table: contenu wishlist
CREATE TABLE "contenu wishlist" ("num wishlist" INT REFERENCES wishlist ("num wishlist") NOT NULL, "num produit" INT REFERENCES produit ("num produit") NOT NULL, quantité INT DEFAULT (1));
INSERT INTO "contenu wishlist" ("num wishlist", "num produit", quantité) VALUES (4827093, 64291, 20);
INSERT INTO "contenu wishlist" ("num wishlist", "num produit", quantité) VALUES (2741785, 378294, 1);
INSERT INTO "contenu wishlist" ("num wishlist", "num produit", quantité) VALUES (1480197, 13783, 1);

-- Table: envie
CREATE TABLE envie ("num utilisateur" INT REFERENCES utilisateur ("user id") NOT NULL, "num produit" INT REFERENCES produit ("num produit") NOT NULL, évaluation INT NOT NULL);
INSERT INTO envie ("num utilisateur", "num produit", évaluation) VALUES (108494, 13783, 4);

-- Table: historique
CREATE TABLE historique ("num acheteur" INT NOT NULL REFERENCES utilisateur ("user id"), "num receveur" INT NOT NULL REFERENCES utilisateur ("user id"), "num produit" INT NOT NULL REFERENCES produit ("num produit"), date DATE NOT NULL, quantité INT NOT NULL, "num achat" INT NOT NULL);
INSERT INTO historique ("num acheteur", "num receveur", "num produit", date, quantité, "num achat") VALUES (437625, 108494, 13783, '16 janvier', 1, 1);
INSERT INTO historique ("num acheteur", "num receveur", "num produit", date, quantité, "num achat") VALUES (437625, 192764, 64291, '5 février', 2, 3);

-- Table: produit
CREATE TABLE produit ("num produit" INT PRIMARY KEY NOT NULL, nom NOT NULL, prix DOUBLE NOT NULL, catégorie NOT NULL DEFAULT autre, poids DOUBLE, image, dimension, description);
INSERT INTO produit ("num produit", nom, prix, catégorie, poids, image, dimension, description) VALUES (45937, 'Thinkpad T590', 1690.0, 'informatique', 1.75, NULL, '32.9 x 22.7 x 1.91 [cm]', 'un des meilleurs ordinateurs portables sur le marché, il est rapide, puissant et a une excellente batterie');
INSERT INTO produit ("num produit", nom, prix, catégorie, poids, image, dimension, description) VALUES (64291, 'paquet carte pokemon', 5.99, 'jeux et jouet', 0.005, NULL, '14.3 x 4.7 x 0.05 [cm]', 'Ces boosters Pokémon contiennent 10 cartes issues de l''extension Soleil & Lune 4');
INSERT INTO produit ("num produit", nom, prix, catégorie, poids, image, dimension, description) VALUES (13783, 'Xbox', 337.0, 'jeux vidéo', '', NULL, NULL, NULL);
INSERT INTO produit ("num produit", nom, prix, catégorie, poids, image, dimension, description) VALUES (378294, 'Volant Mercedes w10', 2670.0, 'sport automobile', 0.254, NULL, NULL, NULL);

-- Table: profil
CREATE TABLE profil (prénom NOT NULL, nom NOT NULL, adresse NOT NULL, "adresse mail" NOT NULL PRIMARY KEY UNIQUE, notification BOOLEAN NOT NULL DEFAULT (TRUE), taille NOT NULL, photo, pointure DOUBLE, "date de naissance" DATE, "couleur préférée");
INSERT INTO profil (prénom, nom, adresse, "adresse mail", notification, taille, photo, pointure, "date de naissance", "couleur préférée") VALUES ('Fran?ois', 'Ferard', 'Avenue Charles Bachman 42,
1410 Waterloo
Belgique', 'francois.ferard@gmail.com', 'TRUE', 'L', NULL, 42.0, '20 juillet 1969', 'orange');
INSERT INTO profil (prénom, nom, adresse, "adresse mail", notification, taille, photo, pointure, "date de naissance", "couleur préférée") VALUES ('Alain', 'Dupont', 'Chemin de Brangais 2, 1400 Nivelles Belgique', 'alain.dupont@gmail.com', 'TRUE', 'M', NULL, 41.0, '13 aout 1997', 'bleu');
INSERT INTO profil (prénom, nom, adresse, "adresse mail", notification, taille, photo, pointure, "date de naissance", "couleur préférée") VALUES ('Lewis', 'Hamilton', 'Hopewell Street 34, London UK', 'hamilton44@gmail.com', 'FALSE', 'NULL', NULL, 'NULL', '7 janvier 1985', NULL);
INSERT INTO profil (prénom, nom, adresse, "adresse mail", notification, taille, photo, pointure, "date de naissance", "couleur préférée") VALUES ('Sebastian ', 'Vettel', 'Plauener 
Straße 17, 44139 Dortmund Germany', 'vettel.ferrari@gmail.com', 'TRUE', 'M', NULL, 44.0, '3 juillet 1987', 'rouge');

-- Table: utilisateur
CREATE TABLE utilisateur ("adresse mail" NOT NULL UNIQUE REFERENCES profil ("adresse mail"), "mot de passe" VARCHAR (255) NOT NULL, "user id" INT NOT NULL UNIQUE PRIMARY KEY);
INSERT INTO utilisateur ("adresse mail", "mot de passe", "user id") VALUES ('francois.ferard@gmail.com', 'jl34527VD', 437625);
INSERT INTO utilisateur ("adresse mail", "mot de passe", "user id") VALUES ('alain.dupont@gmail.com', 'algu729bdJ', 192764);
INSERT INTO utilisateur ("adresse mail", "mot de passe", "user id") VALUES ('hamilton44@gmail.com', 'm6824HDZLG', 108494);
INSERT INTO utilisateur ("adresse mail", "mot de passe", "user id") VALUES ('vettel.ferrari@gmail.com', 'alday232OHKEDV', 837679);

-- Table: wishlist
CREATE TABLE wishlist (nom NOT NULL, "num utilisateur" INT NOT NULL REFERENCES utilisateur ("user id"), "num wishlist" INT PRIMARY KEY);
INSERT INTO wishlist (nom, "num utilisateur", "num wishlist") VALUES ('Formule 1', 108494, 2741785);
INSERT INTO wishlist (nom, "num utilisateur", "num wishlist") VALUES ('Cadeau enfant', 108494, 1480197);
INSERT INTO wishlist (nom, "num utilisateur", "num wishlist") VALUES ('Collection pokemon', 192764, 4827093);

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
