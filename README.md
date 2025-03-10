x# Spring Boot CRUD Application with Comprehensive Testing

This project demonstrates the implementation of a CRUD (Create, Read, Update, Delete) application using Spring Boot, MySQL, and Spring Data JPA with a `Person` entity. It emphasizes a comprehensive testing strategy, utilizing `@DataJpaTest` for repository testing and Mockito for service and controller testing.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Postman Collection](#postman-collection)
- [Testing Strategy](#testing-strategy)
  - [Repository Testing with @DataJpaTest](#repository-testing-with-datajpatest)
  - [Service Testing with Mockito](#service-testing-with-mockito)
  - [Controller Testing with MockMvc](#controller-testing-with-mockmvc)
- [Running Tests](#running-tests)
- [Technologies Used](#technologies-used)

## Prerequisites

Before you begin, ensure you have the following installed on your machine:

- Java 8 or higher
- Maven
- MySQL
- Postman (optional, for API testing)

## Setup

1. **Clone the repository:**

    ```bash
    git clone https://github.com/fahd-ops2/sample-unit-tests.git
    cd sample-unit-tests
    ```

2. **Create a MySQL database:**

    ```sql
    CREATE DATABASE crud_springboot;
    ```

3. **Configure the database connection:**

    Open `src/main/resources/application.properties` and configure the following properties with your MySQL credentials:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/crud_springboot
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update
    ```

## Running the Application

Use Maven to build and run the application:

```bash
mvn spring-boot:run
```

The application will start and be accessible at `http://localhost:8080`.

## API Endpoints

The application provides the following RESTful API endpoints for managing `Person` entities:

- **GET /api/persons**: Retrieve all persons.
- **GET /api/persons/{id}**: Retrieve a person by ID.
- **POST /api/persons**: Create a new person.
- **PUT /api/persons/{id}**: Update an existing person by ID.
- **DELETE /api/persons/{id}**: Delete a person by ID.

## Postman Collection

A Postman collection named `CRUD.postman_collection.json` is included in the repository. This collection contains pre-configured requests to test the API endpoints. To use it:

1. Open Postman.
2. Import the `CRUD.postman_collection.json` file.
3. Execute the requests to interact with the API.

## Testing Strategy

This project adopts a layered testing approach to ensure the reliability and correctness of each component.

### Repository Testing with @DataJpaTest

`@DataJpaTest` is used to test JPA repositories. It configures an in-memory database, scans for `@Entity` classes, and sets up Spring Data JPA repositories. Tests are transactional and roll back at the end of each execution, ensuring a clean state.

**Example:**

```java
package io.fahd.unittests.repository;

import io.fahd.unittests.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testSaveAndFindById() {
        Person person = new Person(null, "John Doe", "New York", "123456789");
        Person savedPerson = personRepository.save(person);
        Optional<Person> retrievedPerson = personRepository.findById(savedPerson.getId());
        assertThat(retrievedPerson).isPresent().contains(savedPerson);
    }
}
```

### Service Testing with Mockito

Mockito is employed to mock dependencies, allowing isolated testing of the service layer's business logic without involving actual database operations.

**Example:**

```java
package io.fahd.unittests.service;

import io.fahd.unittests.entity.Person;
import io.fahd.unittests.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    public PersonServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPersonById() {
        Person person = new Person(1L, "Jane Doe", "Los Angeles", "987654321");
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        Optional<Person> retrievedPerson = personService.getPersonById(1L);
        assertThat(retrievedPerson).isPresent().contains(person);
    }
}
```

### Controller Testing with MockMvc

`MockMvc` is utilized to test the web layer (controllers) without starting the full HTTP server. It allows sending mock HTTP requests and verifying responses.

**Example:**

```java
package io.fahd.unittests.controller;

import io.fahd.unittests.entity.Person;
import io.fahd.unittests.service.PersonService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    public void testGetAllPersons() throws Exception {
        Person person1 = new Person(1L, "John Doe", "New York", "123456789");
        Person person2 = new Person(2L, "Jane Doe", "Los Angeles", "987654321");
        when(personService.getAllPersons()).thenReturn(Arrays.asList(person1, person2));

        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }
}
```

## Running Tests

To execute the tests, run the following command:

```bash
mvn test
```

This command will compile and run all tests, providing a summary of the results.
