DROP TABLE Professor CASCADE;
DROP TABLE Dept CASCADE;
DROP TABLE Project CASCADE;
DROP TABLE Graduate CASCADE;
DROP TABLE Work_Proj;
DROP TABLE Work_In;
DROP TABLE Work_Dept;

CREATE TABLE Professor (
	p_ssn INTEGER NOT NULL,
	name CHAR(30),
	age INTEGER,
	rank INTEGER,
	specialty CHAR(30),
	PRIMARY KEY(p_ssn)
);

CREATE TABLE Dept (
	dno INTEGER NOT NULL,
	p_ssn INTEGER NOT NULL,
	dname CHAR(30),
	office CHAR(30),
	PRIMARY KEY(dno),
	FOREIGN KEY(p_ssn) REFERENCES Professor(p_ssn)
);

CREATE TABLE Project (
	pno INTEGER NOT NULL,
	p_ssn INTEGER NOT NULL,
	sponsor CHAR(30),
	start_date DATE,
	end_date DATE,
	budget INTEGER,
	PRIMARY KEY(pno),
	FOREIGN KEY(p_ssn) REFERENCES Professor(p_ssn)
);

CREATE TABLE Graduate (
	g_ssn INTEGER NOT NULL,
	dno INTEGER NOT NULL,
	adv_ssn INTEGER NOT NULL,
	name CHAR(30),
	age INTEGER,
	deg_pg CHAR(30),
	PRIMARY KEY(g_ssn),
	FOREIGN KEY(dno) REFERENCES Dept(dno),
	FOREIGN KEY(adv_ssn) REFERENCES Graduate(g_ssn)
);

CREATE TABLE Work_Proj (
	pno INTEGER NOT NULL,
	g_ssn INTEGER NOT NULL,
	p_ssn INTEGER NOT NULL,
	since DATE,
	PRIMARY KEY(pno, g_ssn),
	FOREIGN KEY(pno) REFERENCES Project(pno),
	FOREIGN KEY(g_ssn) REFERENCES Graduate(g_ssn),
	FOREIGN KEY(p_ssn) REFERENCES Professor(p_ssn)
);

CREATE TABLE Work_In (
	p_ssn INTEGER NOT NULL,
	pno INTEGER NOT NULL,
	PRIMARY KEY(p_ssn, pno),
	FOREIGN KEY(p_ssn) REFERENCES Professor(p_ssn),
	FOREIGN KEY(pno) REFERENCES Project(pno)
);

CREATE TABLE Work_Dept (
	p_ssn INTEGER NOT NULL,
	dno INTEGER NOT NULL,
	time_pc INTEGER,
	PRIMARY KEY(p_ssn, dno),
	FOREIGN KEY(p_ssn) REFERENCES Professor(p_ssn),
	FOREIGN KEY(dno) REFERENCES Dept(dno)
);


DROP TABLE Musicians CASCADE;
DROP TABLE Instrument CASCADE;
DROP TABLE Album CASCADE;
DROP TABLE Songs CASCADE;
DROP TABLE Plays;
DROP TABLE Perform;
DROP TABLE Place CASCADE;
DROP TABLE Telephone;
DROP TABLE Lives;

CREATE TABLE Musicians (
	ssn INTEGER NOT NULL,
	name CHAR(30),
	PRIMARY KEY(ssn)
);

CREATE TABLE Instrument (
	instrId INTEGER NOT NULL,
	dname CHAR(30),
	key CHAR(2),
	PRIMARY KEY(instrId)
);

CREATE TABLE Album (
	albumIdentifier INTEGER NOT NULL,
	copyrightDate DATE,
	speed INTEGER,
	title CHAR(30),
	produced_by INTEGER NOT NULL,
	PRIMARY KEY(albumIdentifier),
	FOREIGN KEY(produced_by) REFERENCES Musicians(ssn)
);

CREATE TABLE Songs (
	songId INTEGER NOT NULL,
	title CHAR(30),
	some_other_attr INTEGER,
	appears_on INTEGER NOT NULL,
	PRIMARY KEY(songId),
	FOREIGN KEY(appears_on) REFERENCES Album(albumIdentifier)
);

CREATE TABLE Plays (
	ssn INTEGER NOT NULL,
	instrId INTEGER NOT NULL,
	PRIMARY KEY(ssn, instrId),
	FOREIGN KEY(ssn) REFERENCES Musicians(ssn),
	FOREIGN KEY(instrId) REFERENCES Instrument(instrId)
);

CREATE TABLE Perform (
	ssn INTEGER NOT NULL,
	songId INTEGER NOT NULL,
	PRIMARY KEY(ssn, songId),
	FOREIGN KEY(ssn) REFERENCES Musicians(ssn),
	FOREIGN KEY(songId) REFERENCES Songs(songId)
);

CREATE TABLE Place (
	address CHAR(40) NOT NULL,
	PRIMARY KEY(address)
);

CREATE TABLE Telephone (
	phone_no INTEGER,
	address CHAR(40) NOT NULL,
	PRIMARY KEY(address),
	FOREIGN KEY(address) REFERENCES Place(address)
);

CREATE TABLE Lives (
	ssn INTEGER NOT NULL,
	address CHAR(40) NOT NULL,
	PRIMARY KEY(ssn, address),
	FOREIGN KEY(ssn) REFERENCES Musicians(ssn),
	FOREIGN KEY(address) REFERENCES Place(address)
);
