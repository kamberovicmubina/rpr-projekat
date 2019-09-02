BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "company" (
	"name"	TEXT,
	"id"	INTEGER,
	"address"	TEXT,
	"departments"	TEXT,
	"employees"	TEXT,
	"clients"	TEXT,
	"services"	TEXT,
	"owner"	INTEGER,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "person" (
	"id"	INTEGER,
	"name"	TEXT,
	"date_of_birth"	TEXT,
	"address"	TEXT,
	"phone_number"	TEXT,
	"e_mail"	TEXT,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "client" (
	"name"	TEXT,
	"date_of_birth"	TEXT,
	"address"	TEXT,
	"phone_number"	TEXT,
	"e_mail"	TEXT,
	"contracts"	TEXT,
	"profit"	REAL,
	"id"	INTEGER,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "contract" (
	"title"	TEXT,
	"id"	INTEGER,
	"person"	INTEGER,
	"sign_date"	TEXT,
	"end_date"	TEXT,
	PRIMARY KEY("id")
);
INSERT INTO "company" VALUES ('Building company',1,'Titova','Design, Build','','1 2','General Contracting, Pre-Construction, Design-Build Services, Construction Management',NULL);
INSERT INTO "client" VALUES ('Mubina Kamberović','1998 07 27','D ozme','018732873','mub@gmail.com','1',9382.5,1);
INSERT INTO "client" VALUES ('Novi klijent','1999 09 02','Vrazova','088923324','novimail@yahoo.com','2 3',2348.0,2);
INSERT INTO "contract" VALUES ('Employment contract',1,1,'2017 08 28','2020 09 13');
INSERT INTO "contract" VALUES ('Employment contract',2,2,'2019 03 04','2022 03 15');
INSERT INTO "contract" VALUES ('Parking contract',3,2,'2019 05 06','2019 12 04');
COMMIT;
