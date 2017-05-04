#!/bin/bash
mkdir keys &> /dev/null
rm -r keys/* &> /dev/null

PORT=${PORT:=8080}
NUM_REPLICAS=${NUM_REPLICAS:=6}
NUM_FAULTS = ${NUM_FAULTS :=2}
DEBUG=true

PORT=$PORT NUM_REPLICAS=$NUM_REPLICAS NUM_FAULTS=$NUM_FAULTS DEBUG=$DEBUG mvn test
