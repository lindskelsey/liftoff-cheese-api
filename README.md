# Cheese Java REST API

### What is a REST API

### Generate a Spring Boot Project

`build.gradle`

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

`application.properties`

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

### Contoller

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
