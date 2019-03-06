[![EO principles respected here](http://www.elegantobjects.org/badge.svg)](http://www.elegantobjects.org)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/vzurauskas/accounts-transfers/blob/master/LICENSE)

# Accounts and Transfers
Demo object oriented web application: create accounts and transfer money between them via a RESTful API.

## API definition
1. Open https://editor.swagger.io/
2. Click File -> Import URL
3. Paste https://raw.githubusercontent.com/vzurauskas/accounts-transfers/master/swagger.yaml

### POST /transfers idempotency
N.B. POST /transfers operation is idempotent. An idempotency key (header 'x-idempotency-key') is used to guard against the execution of duplicate transfers. The request is treated as idempotent if a request from the same client (header x-client-id) with the same idempotency key has already been executed.

## Build and run
mvn clean install  
mvn exec:java  
  
This will start the server on port 8080 and the root URI will be http://localhost:8080

## Technology stack
Takes framework - https://github.com/yegor256/takes  
JOOQ - https://www.jooq.org/  
H2 database (in memory) - http://www.h2database.com/html/main.html  
