## Before run:

### DB create
For db create use createDB.sql at the root of project.

## Config file (.properties)

This file include params needed for application work.

### config params.

 * `JDBC_URL` - jdbcUrl has format (jdbc:postgresql://localhost:5432/mydb)
 * `DB_LOGIN`- login for db
 * `DB_PASSWORD` - password
 * `OBSERVERS` - this param include observer names, thats observe your about new advert, if you have
 few observers write with '/'.
 * `STORAGE` - class name for storage implementation.

## Run
After create DB and .poperties file you can ran app:

* go in the root of project.
* mvn package
* java -cp target/advert-parser-1.jar clientcli.Launcher -h


 
