#!/bin/bash
mkdir keys &> /dev/null
rm -r keys/* &> /dev/null

PORT=${PORT:=8080}
NUM_FAULTS = ${NUM_FAULTS :=1}
NUM_REPLICAS=${NUM_REPLICAS:=3}
DEBUG=true

PORT=$PORT NUM_FAULTS=$NUM_FAULTS NUM_REPLICAS=$NUM_REPLICAS DEBUG=$DEBUG mvn test
