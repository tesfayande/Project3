# Project 3 

# Spring Boot And Jwt Backend Authentication For OpenClassrooms Angular & Java Path

This repository contains the backend implementation for the third project in the OpenClassrooms [Angular and Java development](https://openclassrooms.com/fr/paths/533-developpeur-full-stack-java-et-angular) development path.It serves as the server-side component, built with **Spring Boot** for robust and scalable application development.  

## Key Technologies Used:

### Spring Boot:

Simplifies backend development with its convention-over-configuration approach.  

### Spring Web:
 For building RESTful APIs to handle HTTP requests.  

### Spring Security & JWT: 
 
 To implement secure user authentication and authorization and ensures secure user access control.  

### JPA & MySQL: 

For efficient database management and object-relational mapping and provides a seamless way to interact with the database while maintaining data integrity.
### Swagger: 
 
  For API documentation and testing and Makes API testing and documentation effortless for developers.
### Maven:
For dependency management and project build automation.

## Installation & Usage

1. Open a terminal or command prompt and navigate to the directory where you want to clone the repository.

Run the following command to clone the repository:

    git clone  https://github.com/tesfayande/Project3.git

2.  Navigate to the cloned repository directory:
    `cd Project3`


## Config Databse

1 Open: src/main/resources/application.properties

 Configure the `application.properties` file with your MySQL database credentials.

## Compile, Build & Run

Before you compile and build, make sure you are at the project directory SpringBootProjectDir of this repo. Take note also, I'm using Maven build tool here.

1.To package your program as an executable jar file:

    mvn clean package

2.To simply clean and compile:
  
    mvn clean compile
3.To just clean your project:

    mvn clean
4.To run the program:

    mvn spring-boot:run
5.To build and run the program:

    mvn clean install

## Access the API documentation via Swagger UI at:

    http://localhost:8080/swagger-ui/index.html
  