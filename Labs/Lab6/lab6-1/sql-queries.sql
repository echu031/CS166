/* Find the total # of parts supplied by each supplier */
SELECT S.sid, COUNT(C.pid) AS TotalParts
FROM Catalog C, Suppliers S
WHERE S.sid=C.sid
GROUP BY S.sid;

/* Find the total # of parts supplied by each supplier who supplies at least 3 parts */
SELECT S.sid, COUNT(C.pid) AS TotalParts
FROM Catalog C, Suppliers S
WHERE S.sid=C.sid
GROUP BY S.sid
HAVING COUNT(C.pid) > 2;

/* For every supplier that supplies only green parts, print the name of the supplier and the total # of parts that he supplies. */
SELECT S.sname, COUNT(C.pid) AS TotalParts
FROM Catalog C, Parts P, Suppliers S
WHERE S.sid=C.sid AND P.pid=C.pid
GROUP BY S.sid, S.sname
HAVING EVERY (P.color='Green');

/* For every supplier that supplies green part & red part, print the name of the supplier & the price of the most expensive part that he supplies. */
SELECT S.sname, MAX(C.cost) AS PartPrice
FROM Catalog C, Suppliers S
WHERE S.sid=C.sid AND C.sid IN (SELECT C2.sid
				FROM Catalog C2, Parts P2
				WHERE P2.pid=C2.pid AND P2.color='Red'
				INTERSECT
				SELECT C3.sid
				FROM Catalog C3, Parts P3
				WHERE P3.pid=C3.pid AND P3.color='Green')
GROUP BY S.sid, S.sname;
