FROM mdsol/java17-jdk
MAINTAINER deelete
#VOLUME指定临时文件目录为/tmp，在主机/var/lib/docker目录下创建了一个临时文件并链接到容器的/tmp
VOLUME /tmp
ADD boot3.jar boot3.jar
RUN bash -c 'touch /boot3.jar'
ENTRYPOINT ["java","-jar","/boot3.jar"]
EXPOSE 8090