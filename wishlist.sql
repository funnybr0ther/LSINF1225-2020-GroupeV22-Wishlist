--
-- File generated with SQLiteStudio v3.2.1 on ven. mars 6 23:12:15 2020
--
-- Text encoding used: ISO-8859-4
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: amis
CREATE TABLE amis (num_suiveur REFERENCES utilisateur ("user id") NOT NULL, num_suivi REFERENCES utilisateur ("user id") NOT NULL, relation DEFAULT Amis);
INSERT INTO amis (num_suiveur, num_suivi, relation) VALUES (192764, 437625, 'proche');
INSERT INTO amis (num_suiveur, num_suivi, relation) VALUES (437625, 192764, 'proche');
INSERT INTO amis (num_suiveur, num_suivi, relation) VALUES (437625, 108494, 'ami ');
INSERT INTO amis (num_suiveur, num_suivi, relation) VALUES (437625, 837679, 'ami');
INSERT INTO amis (num_suiveur, num_suivi, relation) VALUES (108494, 437625, 'ami');
INSERT INTO amis (num_suiveur, num_suivi, relation) VALUES (837679, 437625, 'ami');
INSERT INTO amis (num_suiveur, num_suivi, relation) VALUES (287591, 289183, 'ami');
INSERT INTO amis (num_suiveur, num_suivi, relation) VALUES (289183, 287591, 'coll?gue');
INSERT INTO amis (num_suiveur, num_suivi, relation) VALUES (740618, 108494, 'proche');

-- Table: contenu wishlist
CREATE TABLE "contenu wishlist" ("num wishlist" INT REFERENCES wishlist ("num wishlist") NOT NULL, "num produit" INT REFERENCES produit ("num produit") NOT NULL, quantité INT DEFAULT (1));
INSERT INTO "contenu wishlist" ("num wishlist", "num produit", quantité) VALUES (4827093, 64291, 20);
INSERT INTO "contenu wishlist" ("num wishlist", "num produit", quantité) VALUES (2741785, 378294, 1);
INSERT INTO "contenu wishlist" ("num wishlist", "num produit", quantité) VALUES (1480197, 13783, 1);
INSERT INTO "contenu wishlist" ("num wishlist", "num produit", quantité) VALUES (3816821, 64091, 1);
INSERT INTO "contenu wishlist" ("num wishlist", "num produit", quantité) VALUES (9748472, 86482, 1);
INSERT INTO "contenu wishlist" ("num wishlist", "num produit", quantité) VALUES (9748472, 21890, 4);
INSERT INTO "contenu wishlist" ("num wishlist", "num produit", quantité) VALUES (3094918, 54061, 2);

-- Table: envie
CREATE TABLE envie ("num utilisateur" INT REFERENCES utilisateur ("user id") NOT NULL, "num produit" INT REFERENCES produit ("num produit") NOT NULL, évaluation INT NOT NULL);
INSERT INTO envie ("num utilisateur", "num produit", évaluation) VALUES (108494, 13783, 4);
INSERT INTO envie ("num utilisateur", "num produit", évaluation) VALUES (108494, 378294, 4);
INSERT INTO envie ("num utilisateur", "num produit", évaluation) VALUES (740618, 64091, 5);
INSERT INTO envie ("num utilisateur", "num produit", évaluation) VALUES (192764, 64291, 4);
INSERT INTO envie ("num utilisateur", "num produit", évaluation) VALUES (437625, 86482, 1);
INSERT INTO envie ("num utilisateur", "num produit", évaluation) VALUES (287591, 86482, 3);
INSERT INTO envie ("num utilisateur", "num produit", évaluation) VALUES (108494, 86482, 2);

-- Table: historique
CREATE TABLE historique ("num acheteur" INT NOT NULL REFERENCES utilisateur ("user id"), "num receveur" INT NOT NULL REFERENCES utilisateur ("user id"), "num produit" INT NOT NULL REFERENCES produit ("num produit"), date DATE NOT NULL, quantité INT NOT NULL, "num achat" INT NOT NULL);
INSERT INTO historique ("num acheteur", "num receveur", "num produit", date, quantité, "num achat") VALUES (437625, 108494, 13783, '16 janvier 2020', 1, 1);
INSERT INTO historique ("num acheteur", "num receveur", "num produit", date, quantité, "num achat") VALUES (437625, 192764, 64291, '5 février 2020', 2, 3);
INSERT INTO historique ("num acheteur", "num receveur", "num produit", date, quantité, "num achat") VALUES (108494, 289183, 45937, '1 juillet 2019', 1, 2);

-- Table: produit
CREATE TABLE produit ("num produit" INT PRIMARY KEY NOT NULL, nom NOT NULL, prix DOUBLE NOT NULL, catégorie, poids DOUBLE, image, dimension, description);
INSERT INTO produit ("num produit", nom, prix, catégorie, poids, image, dimension, description) VALUES (45937, 'Thinkpad T590', 1690.0, 'informatique', 1.75, 'image1.jpg', '32.9 x 22.7 x 1.91 [cm]', 'un des meilleurs ordinateurs portables sur le marché, il est rapide, puissant et a une excellente batterie');
INSERT INTO produit ("num produit", nom, prix, catégorie, poids, image, dimension, description) VALUES (64291, 'paquet carte pokemon', 5.99, 'jeux', 0.005, 'image3.jpg', '14.3 x 4.7 x 0.05 [cm]', 'Ces boosters Pokémon contiennent 10 cartes issues de l''extension Soleil & Lune 4');
INSERT INTO produit ("num produit", nom, prix, catégorie, poids, image, dimension, description) VALUES (13783, 'Xbox', 337.0, 'jeux', '', 'image7.jpg', NULL, NULL);
INSERT INTO produit ("num produit", nom, prix, catégorie, poids, image, dimension, description) VALUES (378294, 'Volant Mercedes w10', 2670.0, 'auto-moto', 0.254, 'image2.jpg', NULL, NULL);
INSERT INTO produit ("num produit", nom, prix, catégorie, poids, image, dimension, description) VALUES (21890, 'Câbles HDMI', 24.9, 'informatique', NULL, NULL, NULL, NULL);
INSERT INTO produit ("num produit", nom, prix, catégorie, poids, image, dimension, description) VALUES (86482, 'Playstation 4', 289.0, 'jeux', NULL, 'image4.jpg', NULL, NULL);
INSERT INTO produit ("num produit", nom, prix, catégorie, poids, image, dimension, description) VALUES (64091, 'Parasol', 13.95, 'nature', NULL, 'image6.jpg', NULL, NULL);
INSERT INTO produit ("num produit", nom, prix, catégorie, poids, image, dimension, description) VALUES (54061, 'guirlandes', 5.0, 'mobilier', NULL, 'image7.jpg', NULL, NULL);

-- Table: utilisateur
CREATE TABLE utilisateur ("adresse mail" NOT NULL UNIQUE, "mot de passe" VARCHAR (255) NOT NULL, "user id" INT NOT NULL UNIQUE PRIMARY KEY, prénom NOT NULL, nom NOT NULL, adresse NOT NULL, notification BOOLEAN DEFAULT (TRUE), "date de naissance" DATE NOT NULL, taille, photo, "couleur préférée", pointure DOUBLE);
INSERT INTO utilisateur ("adresse mail", "mot de passe", "user id", prénom, nom, adresse, notification, "date de naissance", taille, photo, "couleur préférée", pointure) VALUES ('francois.ferard@gmail.com', 'jl34527VD', 437625, 'Fran?ois', 'Ferard', 'Avenue Charles Bachman 42,
1410 Waterloo
Belgique', 1, '20 juillet 1969', 'L', 'photo2.jpg', 'orange', 42.0);
INSERT INTO utilisateur ("adresse mail", "mot de passe", "user id", prénom, nom, adresse, notification, "date de naissance", taille, photo, "couleur préférée", pointure) VALUES ('alain.dupont@gmail.com', 'algu729bdJ', 192764, 'Alain', 'Dupont', 'Chemin de Brangais 2, 1400 Nivelles Belgique', 1, '13 aout 1997', 'M', 'photo3.jpg', 'bleu', 41.0);
INSERT INTO utilisateur ("adresse mail", "mot de passe", "user id", prénom, nom, adresse, notification, "date de naissance", taille, photo, "couleur préférée", pointure) VALUES ('hamilton44@gmail.com', 'm6824HDZLG', 108494, 'Lewis', 'Hamilton', 'Hopewell Street 34, London UK', 'FALSE', '7 janvier 1985', NULL, 'photo6.jpg', NULL, NULL);
INSERT INTO utilisateur ("adresse mail", "mot de passe", "user id", prénom, nom, adresse, notification, "date de naissance", taille, photo, "couleur préférée", pointure) VALUES ('vettel.ferrari@gmail.com', 'alday232OHKEDV', 837679, 'Sebastian', 'Vettel', 'Plauener 
Straße 17, 44139 Dortmund Germany', 1, '3 juillet 1987', 'S', 'photo4.jpg', 'rouge', 44.0);
INSERT INTO utilisateur ("adresse mail", "mot de passe", "user id", prénom, nom, adresse, notification, "date de naissance", taille, photo, "couleur préférée", pointure) VALUES ('ricciardoRB@gmail.com', 'K2Jgzg6H29', 289183, 'Daniel', 'Ricciardo', 'Etwell street 41, 6101 Perth Australia', 'TRUE', '1 juillet 1989', 'L', 'photo5.jpg', 'bleu', 42.0);
INSERT INTO utilisateur ("adresse mail", "mot de passe", "user id", prénom, nom, adresse, notification, "date de naissance", taille, photo, "couleur préférée", pointure) VALUES ('maxvrstp@gmail.com', 'DjazgDJMBK279', 287591, 'Max', 'Verstappen', 'Kortendijksestraat 35, 4706 Rosendael Netherlands', 'FALSE', '30 septembre 1997', NULL, NULL, NULL, NULL);
INSERT INTO utilisateur ("adresse mail", "mot de passe", "user id", prénom, nom, adresse, notification, "date de naissance", taille, photo, "couleur préférée", pointure) VALUES ('antho.hamilton@gmail.com', 'KLGI219Hdbll7', 740618, 'Anthony', 'Hamilton', 'Hopewell Street 37, London UK', 'TRUE', '3 mars 1956', 'M', 'photo1.jpg', 'vert', 42.0);

-- Table: wishlist
CREATE TABLE wishlist (nom NOT NULL, "num utilisateur" INT NOT NULL REFERENCES utilisateur ("user id"), "num wishlist" INT PRIMARY KEY);
INSERT INTO wishlist (nom, "num utilisateur", "num wishlist") VALUES ('Formule 1', 108494, 2741785);
INSERT INTO wishlist (nom, "num utilisateur", "num wishlist") VALUES ('Cadeau enfant', 108494, 1480197);
INSERT INTO wishlist (nom, "num utilisateur", "num wishlist") VALUES ('Collection pokemon', 192764, 4827093);
INSERT INTO wishlist (nom, "num utilisateur", "num wishlist") VALUES ('Pendaison de crémaill?re', 192764, 3816821);
INSERT INTO wishlist (nom, "num utilisateur", "num wishlist") VALUES ('anniversaire', 287591, 9748472);
INSERT INTO wishlist (nom, "num utilisateur", "num wishlist") VALUES ('Noël', 289183, 3094918);
INSERT INTO wishlist (nom, "num utilisateur", "num wishlist") VALUES ('Judo', 192764, 389);

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
