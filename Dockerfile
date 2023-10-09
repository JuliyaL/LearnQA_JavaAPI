FROM maven:3.9.4-openjdk-14
WORKDIR /tests
COPY . .
CMD mvn clean test