DROP TABLE Model CASCADE;
DROP TABLE Plane CASCADE;
DROP TABLE Employee CASCADE;
DROP TABLE Technician CASCADE;
DROP TABLE Traffic_Controller CASCADE;
DROP TABLE Test CASCADE;
DROP TABLE Exam CASCADE;
DROP TABLE Fixes;
DROP TABLE Takes;
DROP TABLE Evaluates;

CREATE TABLE Model (
	model_num CHAR(30) NOT NULL,	
	capacity INTEGER,
	weight INTEGER,
	fuel_type CHAR(30),
	PRIMARY KEY(model_num)	
);

CREATE TABLE Plane (
	registration_num INTEGER NOT NULL,
	model_num CHAR(30) NOT NULL,
	company CHAR(30),
	PRIMARY KEY(registration_num),
	FOREIGN KEY(model_num) REFERENCES Model(model_num)
);

CREATE TABLE Employee (
	ssn INTEGER NOT NULL,
	union_num INTEGER,
	PRIMARY KEY(ssn)
);

CREATE TABLE Technician (
	t_ssn INTEGER NOT NULL REFERENCES Employee(ssn)
	ON DELETE CASCADE,
	last_name CHAR(20),
	address CHAR(50),
	phone CHAR(10),
	salary INTEGER,
	PRIMARY KEY(t_ssn)
);

CREATE TABLE Traffic_Controller (
	tc_ssn INTEGER NOT NULL REFERENCES Employee(ssn)
	ON DELETE CASCADE,
	yrs_experience INTEGER,
	age INTEGER,
	PRIMARY KEY(tc_ssn)
);

CREATE TABLE Test (
	name CHAR(30),
	max_score INTEGER,
	test_num INTEGER NOT NULL,
	PRIMARY KEY(test_num)
);

CREATE TABLE Exam (
	exam_level INTEGER NOT NULL,
	PRIMARY KEY(exam_level)
);

CREATE TABLE Fixes (
	model_num CHAR(30) NOT NULL,
	t_ssn INTEGER NOT NULL,
	PRIMARY KEY(model_num, t_ssn),
	FOREIGN KEY(model_num) REFERENCES Model(model_num),
	FOREIGN KEY(t_ssn) REFERENCES Technician(t_ssn)
);

CREATE TABLE Takes (
	tc_ssn INTEGER NOT NULL,
	exam_level INTEGER NOT NULL,
	test_date DATE NOT NULL,
	hours INTEGER NOT NULL,
	PRIMARY KEY(exam_level, tc_ssn),
	FOREIGN KEY(tc_ssn) REFERENCES Traffic_Controller(tc_ssn),
	FOREIGN KEY(exam_level) REFERENCES Exam(exam_level)
);

CREATE TABLE Evaluates (
	t_ssn INTEGER NOT NULL,
	test_num INTEGER NOT NULL,
	registration_num INTEGER NOT NULL,
	eval_date DATE,
	hours INTEGER,
	score INTEGER,
	PRIMARY KEY(t_ssn, test_num, registration_num),
	FOREIGN KEY(t_ssn) REFERENCES Technician(t_ssn),
	FOREIGN KEY(test_num) REFERENCES Test(test_num),
	FOREIGN KEY(registration_num) REFERENCES Plane(registration_num)
);
