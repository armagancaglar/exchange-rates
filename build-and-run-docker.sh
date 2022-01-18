#!/bin/zsh
mvn package
docker build -t asetad/exchange-rates:1.0 .
docker run --name=exchange-rates -d -p 8080:8080 --restart=unless-stopped asetad/exchange-rates:1.0