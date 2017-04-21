PasswordManagerClient
======

The manager stores the keys in the `keys` folder. Use `make` to make sure the 
folder is created, and empty everytime you run.

####Instalation

Start the PasswordManager then   
Generate Client Stubs
```
$ wsimport -s ./src/java http://localhost:8080/WS/HelloWorld?wsdl
```

### Running the tests
```
$ make test
```
