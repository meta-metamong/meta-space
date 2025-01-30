#!/bin/sh

echo -e "CREATE USER scott IDENTIFIED BY tiger;" | sqlplus -s / as sysdba
echo -e "GRANT CREATE SESSION TO scott;" | sqlplus -s / as sysdba
echo -e "GRANT CREATE TABLE TO scott;" | sqlplus -s / as sysdba
echo -e "GRANT CREATE SEQUENCE TO scott;" | sqlplus -s / as sysdba
echo -e "GRANT CREATE VIEW TO scott;" | sqlplus -s / as sysdba
echo -e "GRANT CREATE PROCEDURE TO scott;" | sqlplus -s / as sysdba
sqlplus -s scott/tiger @schema.sql