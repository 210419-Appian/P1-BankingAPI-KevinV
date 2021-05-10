DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS accStatus;
DROP TABLE IF EXISTS accType;
DROP TABLE IF EXISTS roles;

CREATE TABLE roles(
	roleID INTEGER PRIMARY KEY,
	roleName VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE accType(
	typeID INTEGER PRIMARY KEY,
	typeName VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE accStatus(
	statusID INTEGER PRIMARY KEY,
	statusName VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE users(
	userID SERIAL PRIMARY KEY,
	username VARCHAR(15) NOT NULL UNIQUE,
	password VARCHAR(15) NOT NULL,
	fname VARCHAR(15) NOT NULL,
	lname VARCHAR(15) NOT NULL,
	email VARCHAR(40) NOT NULL,
	roleID INTEGER REFERENCES roles(roleID)
);

--an account for a single user
CREATE TABLE accounts(
	accountID SERIAL PRIMARY KEY,
	balance NUMERIC NOT NULL,
	status INTEGER REFERENCES accStatus(statusID),
	TYPE INTEGER REFERENCES accType(typeID),
	OWNER INTEGER REFERENCES users(userID)
);

--INITIALIZATIONS-----------------------------

INSERT INTO roles
	VALUES (1, 'Admin'),
		(2, 'Employee'),
		(3, 'Standard'),
		(4, 'Premium');

INSERT INTO acctype 
	VALUES (1, 'Checking'),
		(2, 'Savings');

INSERT INTO accstatus 
	VALUES (1, 'Open'),
		(2, 'Pending'),
		(3, 'Denied'),
		(4, 'Closed');

--INITIAL USERS--------------------
TRUNCATE TABLE users CASCADE;
	
INSERT INTO users (
	username,
	password,
	fname,
	lname,
	email,
	roleID)
	VALUES ('ckramer','kramer', 'Cid','Kramer', 'ckramer@balamb.org',1);

INSERT INTO users (
	username,
	password,
	fname,
	lname,
	email,
	roleID)
	VALUES ('squall','leo', 'Squall','Leonhart', 'sleo@balamb.org',2);

INSERT INTO users (
	username,
	password,
	fname,
	lname,
	email,
	roleID)
	VALUES ('rinoa','angelo', 'Rinoa','Heartily', 'rh@petpals.org',3);

--INITIAL ACCOUNTS--------------------
--cid checking account
INSERT INTO accounts (
	balance,
	status,
	TYPE,
	OWNER)
	values(75000,1,1,1);

--cid savings account
INSERT INTO accounts (
	balance,
	status,
	TYPE,
	OWNER)
	values(15000,1,2,1);

--squall closed checking account, cannot be interacted with unless opened
INSERT INTO accounts(
	balance,
	status,
	TYPE,
	OWNER)
	values(800,4,1,2);

--rinoa pending checking account, to be opened by admin
INSERT INTO accounts (
	balance,
	status,
	TYPE,
	OWNER)
	values(2000,2,1,3);

--EXAMPLE RESTFUL DAO METHODS-------------------------------

SELECT * FROM users;
SELECT * FROM users WHERE userID=1;
SELECT * FROM users WHERE username='squall';
UPDATE users SET email='seedSquall@balamb.org' WHERE username='squall';

SELECT * FROM roles WHERE roleid=3;

SELECT * FROM acctype WHERE typeid=2;
SELECT * FROM accstatus WHERE statusid=2;

SELECT * FROM accounts;
SELECT * FROM accounts WHERE accountID=3;
SELECT * FROM accounts WHERE OWNER=1;
SELECT * FROM accounts WHERE status=2;
UPDATE accounts SET balance=2000 WHERE accountid =3;

--SEE USERS AND ACCOUNTS-------------------------------
SELECT * FROM accounts WHERE OWNER=1;
SELECT * FROM users u JOIN accounts a ON u.userID = a.owner;
