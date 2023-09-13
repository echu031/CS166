DROP INDEX nyc_on_hand_idx IF EXISTS;
DROP INDEX color_idx IF EXISTS;
DROP INDEX nyc_color_idx IF EXISTS;
DROP INDEX sfo_color_idx IF EXISTS;
DROP INDEX nyc_supplier_idx IF EXISTS;
DROP INDEX sfo_supplier_idx IF EXISTS;
DROP INDEX nyc_part_num_idx IF EXISTS;
DROP INDEX sfo_part_num_idx IF EXISTS;

/* Index for Queries 1, 5, and 6 */
CREATE INDEX nyc_on_hand_idx
ON part_nyc
USING BTREE(on_hand);

/* Indexes for Query 2 */
CREATE INDEX color_idx
ON color
USING BTREE(color_id);

CREATE INDEX nyc_color_idx
ON part_nyc
USING BTREE(color);

CREATE INDEX sfo_color_idx
ON part_sfo
USING BTREE(color);

/* Indexes for Query 3 */
CREATE INDEX nyc_supplier_idx
ON part_nyc
USING BTREE(supplier);

CREATE INDEX sfo_supplier_idx
ON part_sfo
USING BTREE(supplier);

/* Indexes for Query 4 */
CREATE INDEX nyc_part_num_idx
ON part_nyc
USING BTREE(part_number);

CREATE INDEX sfo_part_num_idx
ON part_sfo
USING BTREE(part_number);
