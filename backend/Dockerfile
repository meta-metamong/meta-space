FROM tomcat:10.1.34-jre17-temurin
EXPOSE 8080
COPY ./target/ROOT.war ./webapps
ENTRYPOINT ["sh", "./bin/catalina.sh", "run"]