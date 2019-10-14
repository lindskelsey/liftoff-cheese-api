# Cheese Java REST API

This project will walk you through writing the code for a REST API in Java that will Create a Cheese and Get all Cheeses from the database.

In LC101 we built a full-stack application (Cheese MVC) using Springboot.  The code that ran on the client side (the view) and the code that ran on the server side (model and controller) ran in the same project. This can be a great way to learn how to build an application, but many modern web applications don't follow this pattern!

Web applications follow a "separation of concerns" where the client-side code and server-side code are separate projects. For the server-side we wil build a REST API.

### What is a REST API

A REST API will allow you to interact with data (CRUD operations) over HTTP, check out these links for more:

https://www.youtube.com/watch?v=7YcW25PHnAA

https://www.codecademy.com/articles/what-is-rest

https://www.youtube.com/watch?v=s7wmiS2mSXY


### Generate a Spring Boot Project

Let's generate the a new Spring Boot project at https://start.spring.io/

You'll want these dependencies: Spring Data JPA, Spring Web, Spring Boot DevTools, Lombok, and MySQL Driver

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

Creat a new database and user in PHPMyAdmin and configure `application.properties`

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

Create a Package called `model` and a Java Class called `Cheese`

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

Create a Package called `repository` and a Java Class called `CheeseRepository`

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

Create a Package called `service` and a Java Class called `CheeseService`

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

Create a Package called `controller` and a Java Class called `CheeseController`

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

Download Postman: https://www.getpostman.com/

Select `POST` from the drop-down and enter the URL `http://localhost:8080/cheese/new`

Select the `Body` tab and click `raw`. Then select `JSON` from the drop-down on the right.

In the body below past this JSON, replacing the strings with your own data:

```
{
	"name": "NameOfCheeseHere",
	"description": "DescriptionHere"
}
```

You should get a response with the ID auto-incremented.

Go ahead and create a few more cheeses.

Now we need to get our cheese from the database.

Create a new tab in Postman and select `GET` from the drop-down and enter the URL `http://localhost:8080/cheese`

Press enter and you should see a JSON containing all of your cheeses.
