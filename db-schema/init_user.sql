ALTER DATABASE OPEN;

DROP USER hr CASCADE;

CREATE USER hr IDENTIFIED BY hr;

GRANT CREATE SESSION TO hr;
GRANT CREATE TABLE TO hr;
GRANT CREATE VIEW TO hr;
GRANT CREATE SEQUENCE TO hr;
GRANT CREATE PROCEDURE TO hr;