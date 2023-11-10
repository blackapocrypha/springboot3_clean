#!/bin/sh
#取boot3 容器的id然后将其移除
docker rm -f `docker ps | grep boot3 | awk '{print $1}'`
#取boot3 镜像的id然后将其移除
docker rmi -f `docker images | grep boot3 | awk '{print $3}'`
docker build -t boot3.jar:1.1 .
docker run  -d -p 8090:8090   -e LANG=en_US.UTF-8  -e TZ="Asia/Shanghai"  -v /usr/local/nginx/html:/tmp boot3.jar:1.1
rm -rf boot3.jar