# Accounts and Transfers
Demo object oriented web application: create accounts and transfer money between them via a RESTful API.

# API definition
1. Open https://editor.swagger.io/
2. Click File -> Import URL
3. Paste https://raw.githubusercontent.com/vzurauskas/accounts-transfers/master/swagger.yaml

# Build and run
mvn clean install  
mvn exec:java  
  
This will start the server on port 8080 and the root URI will be http://localhost:8080
