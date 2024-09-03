FROM openjdk:17
EXPOSE 1990
ADD target/company-details.jar company-details.jar
ENTRYPOINT ["java","-jar","company-details.jar"]