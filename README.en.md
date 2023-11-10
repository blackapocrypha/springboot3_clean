1. # springboot3_clean

   #### Introduce

   A personal template architecture for small and micro projects based on Vue3/Type Script/Element Plus and Spring Boot3.

   <br/><br/>

   #### SystemModel

   ```cmd
   springboot3_clean
       ├─boot3-common 			// model common
       ├─boot3-create-code		// code
       ├─boot3-excel-boot		// excel
       ├─boot3-security		// model security	
       ├─boot3-test			// test and startApplication
       ├─boot3-utils			// model utils
       ├─docker				// docker
       └─vue3-front			// front vue code
   ```

   <br/><br/>

   #### Software architecture

   | environmen | version |
   | ---------- | ------- |
   | jdk        | >=17    |
   | mysql      | >=5.7   |
   | redis      | >=6     |

   <br/><br/>

   

   #### Instructions for use

   ##### 1 myConfig in yml

   ```yaml
   myConfig:
     useMinio: N          #use minio upload or not  N is not Y is yes
     token: 			   #In the development environment, the specified token can be configured here to never expire, but the prod environment needs to delete the configuration
     file:
       frontUploadUrl: D:\Environment\Nginx\nginx-1.15.2\html\    #uploadFile url
   ```

   <br>

   ##### 2 logback-spring配置

   ```xml
       <!--  <property name="LOG_HOME" value="./boot3_logs/logs" />-->
         <property name="LOG_HOME" value="E:/GitWorkSpace/log/logs" />
   ```

   <br>

   ##### 3 Docker相关

   ```yml
   # 1 in Dockerfile，make the volume is /tmp
   VOLUME /tmp
   
   # 2 in application-prod.yaml, file upload url is /tmp
   myConfig:
     useMinio: N         
     file:
       frontUploadUrl: /tmp/    #file url
       
   # 3 finally, the script that builds the image and publishes maps/tmp to the nginx path, making it easy to access images through the address when not using Minio
   docker run  -d -p 8090:8090   -e LANG=en_US.UTF-8  -e TZ="Asia/Shanghai"  -v /usr/local/nginx/html:/tmp boot3.jar:1.1
   ```

   <br/><br/>

   #### Function preview

   | ![](D:/md文档/image/index1.png)                  | ![./imgs/index.png](D:/md文档/image/index2.png) |
   | ------------------------------------------------ | ----------------------------------------------- |
   | ![](D:/md文档/image/index3-1699588567747-10.png) | ![](D:/md文档/image/index4.png)                 |
   | ![](D:/md文档/image/index5-1699588567747-11.png) | ![](D:/md文档/image/index6.png)                 |
   | ![](D:/md文档/image/index7-1699588567747-12.png) | ![](D:/md文档/image/index8.png)                 |

   
