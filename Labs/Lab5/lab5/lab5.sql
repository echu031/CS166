/* Find pid of parts with cost less than 10 */
SELECT C.pid
FROM Catalog C
WHERE C.cost < 10;

/* Find name of parts with cost less than 10 */
SELECT P.pname
FROM Catalog C, Parts P
WHERE C.cost < 10 AND C.pid = P.pid;

/* Find address of suppliers who supply "Fire Hydrant Cap" */
SELECT S.address
FROM Catalog C, Parts P, Suppliers S
WHERE P.pname = 'Fire Hydrant Cap' AND C.pid = P.pid AND C.sid = S.sid;

/* Find the name of the suppliers who supply green parts */
SELECT S.sname
FROM Catalog C, Parts P, Suppliers S
WHERE P.color = 'Green' AND C.pid = P.pid AND C.sid = S.sid;

/* For each supplier, list the supplier's name along with all parts' name that it supplies */
SELECT S.sname, P.pname
FROM Catalog C, Parts P, Suppliers S
WHERE C.sid = S.sid AND C.pid = P.pid
