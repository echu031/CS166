-- Michael Wessels (862296838)

DROP SEQUENCE IF EXISTS part_number_seq;
CREATE SEQUENCE part_number_seq START WITH 50000;

CREATE OR REPLACE LANGUAGE plpgsql;
CREATE OR REPLACE FUNCTION insert_part_no() RETURNS trigger AS $BODY$
	BEGIN
		NEW.part_number := nextval('part_number_seq');
		RETURN NEW;
	END;
$BODY$ LANGUAGE plpgsql VOLATILE;

DROP TRIGGER IF EXISTS update_part_no ON part_nyc;
CREATE TRIGGER update_part_no BEFORE INSERT ON part_nyc
	FOR EACH ROW EXECUTE PROCEDURE insert_part_no();
