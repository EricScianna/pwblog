#!/bin/sh
mvn clean package && docker build -t it.tss/pwblog .
docker rm -f pwblog || true && docker run -d -p 8080:8080 -p 4848:4848 --name pwblog it.tss/pwblog 
