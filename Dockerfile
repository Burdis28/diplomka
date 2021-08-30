# we are extending everything from tomcat:8.0 image ...
FROM tomcat:latest

MAINTAINER Ondrej_Burda

# COPY path-to-your-application-war path-to-webapps-in-docker-tomcat
COPY diplomovaPrace/target/diplomka-1.0.-SNAPSHOT.war /usr/local/tomcat/webapps/

