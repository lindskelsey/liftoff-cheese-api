# Cheese Java REST API

This project will walk you through writing the code for a simple implementation of a REST API in Java. We won't worry about thing like error handling here- the idea is to familiarize yourself with these concepts so you can build on them in your capstone project.

When we are done you will be able to Add a Cheese to the database via a POST request:

![post](/assets/post_4.png)

And retrieve all Cheeses from the database via a GET request:

![get](/assets/get_2.png)

In LC101 we built a full-stack application (Cheese MVC) using Springboot.  The code that ran on the client side (the view) and the code that ran on the server side (model and controller) ran in the same project. This can be a great way to learn how to build an application, but many modern web applications don't follow this pattern!

Web applications follow a "separation of concerns" where the client-side code and server-side code are separate projects. For the server-side we will build a REST API.

### What is a REST API

A REST API will allow you to interact with data (CRUD operations) over HTTP, check out these links for more:

<https://www.youtube.com/watch?v=7YcW25PHnAA>

<https://www.codecademy.com/articles/what-is-rest>

<https://www.youtube.com/watch?v=s7wmiS2mSXY>

REST APIs can output data in JSON format or XML format. We'll be using JSON: <https://www.w3schools.com/whatis/whatis_json.asp>


### Generate a Spring Boot Project

Let's generate the a new Spring Boot project at <https://start.spring.io/>

You'll want these dependencies: Spring Data JPA, Spring Web, Spring Boot DevTools, Lombok, and MySQL Driver

If you haven't used Lombok before-- this is a library that will reduce the amount of Java boilerplate code you will have to write. It will provide you with annotations to do things like create Getters, Setters, and Constructors without writing any additional code. Check out this tutorial: <https://www.baeldung.com/intro-to-project-lombok>

Your `build.gradle` should look like this:

```
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	runtimeOnly 'mysql:mysql-connector-java'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

### Set up database connection

Creat a new database and user in PHPMyAdmin

For simplicity's sake, my user, db, and password are all `cheese`

Configuration should look like this:

![data](/assets/db_1.png)

Configure `application.properties`

```
# Database connection settings
spring.datasource.url=jdbc:mysql://localhost:8889/cheese?serverTimezone=UTC
spring.datasource.username=cheese
spring.datasource.password=cheese

# Specify the DBMS
spring.jpa.database = MYSQL

# Show or not log for each sql query
spring.jpa.show-sql = false

# Hibernate ddl auto (create, create-drop, update)
spring.jpa.hibernate.ddl-auto = update

# Use spring.jpa.properties.* for Hibernate native properties (the prefix is
# stripped before adding them to the entity manager)
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5Dialect
```

### Model

Now, let's make a model that represents the Cheese table in our database

Create a Package called `model` and a Java Class called `Cheese`

Notice here that `@Data` `@AllArgsConstructor` and `@NoArgsConstructor` are annotations from Lombok that reduce the code we need to write.

Let's create a table that has an ID that is auto-generated, and takes a Name and Description as user input:

```
package org.launchcode.liftoffapi.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cheese")
@Entity
public class Cheese
{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
}
```

### Repository

Now let's create a Repository interface so JPA can communicate with our database.

Create a Package called `repository` and a Java Class called `CheeseRepository`

The methods we'll be using are from `JpaRepository` so we don't need to write any additional code here.

```
package org.launchcode.liftoffapi.repository;
import org.launchcode.liftoffapi.model.Cheese;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheeseRepository extends JpaRepository<Cheese, Long>
{

}
```

### Service 

Next, we'll need to create a Service. 

Create a Package called `service` and a Java Class called `CheeseService`

When you built your assignments in LC101, you probably had a lot of logic in your controller.  However, our controllers should only help us organize our application and determine what actions to take. The controller will call another file called a service, which is the piece of code that does the work.

Check out this article on a Service vs Controller (it talks about Node, but the concept is the same in any language): <https://www.coreycleary.me/what-is-the-difference-between-controllers-and-services-in-node-rest-apis/>

```
package org.launchcode.liftoffapi.service;
import org.launchcode.liftoffapi.model.Cheese;
import org.launchcode.liftoffapi.repository.CheeseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheeseService {

    private CheeseRepository cheeseRepository;


    public CheeseService(CheeseRepository cheeseRepository)
    {
        this.cheeseRepository = cheeseRepository;
    }

    public List<Cheese> getAll()
    {
        return cheeseRepository.findAll();
    }

    public Cheese addCheese(Cheese cheese)
    {
        return cheeseRepository.save(cheese);
    }
}
```

### Controller

Last step-- we need to write a Controller so our Java knows what to do when someone interacts with our app.

Create a Package called `controller` and a Java Class called `CheeseController`

In this code you'll see `@RestController` instead of `@Controller`. Check out the difference here: <https://javarevisited.blogspot.com/2017/08/difference-between-restcontroller-and-controller-annotations-spring-mvc-rest.html>

Now let's create our `/cheese` endpoint

Inside we have two operations, GET and POST

With this code, when we hit `/cheese` we will execute a GET to return all cheeses from the table.

When we hit `cheese/new` with a POST and include a `body` (the data you want to put in the table) then we will add a cheese to the table.

```
package org.launchcode.liftoffapi.controller;
import org.launchcode.liftoffapi.model.Cheese;
import org.launchcode.liftoffapi.service.CheeseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cheese")
public class CheeseController
{
    private CheeseService cheeseService;

    public CheeseController(CheeseService cheeseService)
    {
        this.cheeseService = cheeseService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Cheese> getAll()
    {
        return cheeseService.getAll();
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public Cheese addNew(@RequestBody Cheese cheese)
    {
        return cheeseService.addCheese(cheese);
    }
}
```

### Testing with Postman

Download Postman: <https://www.getpostman.com/>

Select `POST` from the drop-down and enter the URL `http://localhost:8080/cheese/new`

![post](/assets/post_1.png)

Select the `Body` tab and click `raw`. Then select `JSON` from the drop-down on the right.

![post](/assets/post_2.png)

In the body below past this JSON, replacing the strings with your own data:

```
{
	"name": "Cheddar",
	"description": "The orange one"
}
```

![post](/assets/post_3.png)

You should get a response with the ID auto-incremented.

![post](/assets/post_4.png)

Go ahead and create a few more cheeses.

Now we need to get our cheese from the database.

Create a new tab in Postman and select `GET` from the drop-down and enter the URL `http://localhost:8080/cheese`

![post](/assets/get_1.png)

Press enter and you should see a JSON containing all of your cheeses.

![post](/assets/get_2.png)

Everything looks good?...

:star: Congrats! You just built a Java API :star:
