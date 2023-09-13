/* Count how many parts in NYC have move than 70 parts on hand. */
SELECT COUNT(*)
FROM part_nyc
WHERE on_hand > 70;

/* Count how many total parts on hand, in both NYC & SFO, are Red. */
SELECT
	(SELECT SUM(N.on_hand)
	 FROM color C, part_nyc N
	 WHERE (N.color=C.color_id AND C.color_name='Red'))
	+
	(SELECT SUM(S.on_hand)
	 FROM color C, part_sfo S
	 WHERE (S.color=C.color_id AND C.color_name='Red')) AS "Total";

/* List all the suppliers that have more total on hand parts in NYC than they do in SFO. */
SELECT S.supplier_id, S.supplier_name
FROM supplier S
WHERE 	( 	SELECT SUM(N.on_hand)
		FROM part_nyc N
        	WHERE S.supplier_id=N.supplier)
	>
	(	SELECT SUM(SF.on_hand)
	 	FROM part_sfo SF
	 	WHERE S.supplier_id=SF.supplier);

/* List all suppliers that supply parts in NYC that aren't supplied by anyone in SFO. */
SELECT DISTINCT S.supplier_name
FROM part_nyc N, supplier S
WHERE S.supplier_id=N.supplier AND N.part_number IN (	SELECT N2.part_number
							FROM part_nyc N2
							EXCEPT
							SELECT SF.part_number
							FROM part_sfo SF);

/* Update all of the NYC on_hand values to on_hand - 10. */
UPDATE part_nyc
SET on_hand = on_hand - 10;

/* Delete all parts from NYC which have less than 30 parts on hand. */
DELETE
FROM part_nyc
WHERE on_hand < 30;
