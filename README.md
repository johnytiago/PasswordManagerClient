PasswordManagerClient
======

Password Manager Client
### Running
There is no interface, the Project only provides tests.
To run the tests use the `test.sh` script
You can pass the number of replicas that are running through the `NUM_REPLICAS`
variable and starting port `PORT`.

The default usage will start 2 replicas, on ports 8080 and 8081.

**Example**: To start 3 replicas beggining on port 8080:
```
$ NUM_REPLICAS=3 ./test.sh
```

### Debug

You can toggle debug prints through the `DEBUG` variable
