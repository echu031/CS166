COPY Users
<<<<<<< HEAD
FROM '/extra/echu031/finalproj166/cs166/Final Project/project/data/users.csv'
=======
FROM '/extra/mwess005/cs166/cs166/Final Project/project/data/users.csv'
>>>>>>> eebd94943c1379b8f475c0ca4c1c6ee26c779a8c
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE users_userID_seq RESTART 101;

COPY Store
<<<<<<< HEAD
FROM '/extra/echu031/finalproj166/cs166/Final Project/project/data/stores.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Product
FROM '/extra/echu031/finalproj166/cs166/Final Project/project/data/products.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Warehouse
FROM '/extra/echu031/finalproj166/cs166/Final Project/project/data/warehouse.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Orders
FROM '/extra/echu031/finalproj166/cs166/Final Project/project/data/orders.csv'
=======
FROM '/extra/mwess005/cs166/cs166/Final Project/project/data/stores.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Product
FROM '/extra/mwess005/cs166/cs166/Final Project/project/data/products.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Warehouse
FROM '/extra/mwess005/cs166/cs166/Final Project/project/data/warehouse.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Orders
FROM '/extra/mwess005/cs166/cs166/Final Project/project/data/orders.csv'
>>>>>>> eebd94943c1379b8f475c0ca4c1c6ee26c779a8c
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE orders_orderNumber_seq RESTART 501;


COPY ProductSupplyRequests
<<<<<<< HEAD
FROM '/extra/echu031/finalproj166/cs166/Final Project/project/data/productSupplyRequests.csv'
=======
FROM '/extra/mwess005/cs166/cs166/Final Project/project/data/productSupplyRequests.csv'
>>>>>>> eebd94943c1379b8f475c0ca4c1c6ee26c779a8c
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE productsupplyrequests_requestNumber_seq RESTART 11;

COPY ProductUpdates
<<<<<<< HEAD
FROM '/extra/echu031/finalproj166/cs166/Final Project/project/data/productUpdates.csv'
=======
FROM '/extra/mwess005/cs166/cs166/Final Project/project/data/productUpdates.csv'
>>>>>>> eebd94943c1379b8f475c0ca4c1c6ee26c779a8c
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE productupdates_updateNumber_seq RESTART 51;
