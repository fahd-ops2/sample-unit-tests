package io.fahd.unittests.controller;

import io.fahd.unittests.entity.Person;
import io.fahd.unittests.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    void shouldReturnAllPersons() throws Exception {

        List<Person> people = List.of(
                new Person(1L, "David White", "Miami", "1122334455"),
                new Person(2L, "Emma Davis", "San Francisco", "5566778899"),
                new Person(3L, "Frank Miller", "Boston", "9988776655")
        );

        when(personService.findAll()).thenReturn(people);

        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("David White"));
    }

    @Test
    void shouldFindPersonById() throws Exception {

        Person person = new Person(1L, "John Doe", "Paris", "123-456-7890");

        when(personService.findById(1L)).thenReturn(person);

        mockMvc.perform(get("/api/persons/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void shouldCreatePerson() throws Exception {

        String json = """
                {
                "id": 4,
                "name": "John Doe",
                "city": "New York",
                "phoneNumber": "123-456-7890"
                }
                """;
        Person person = new Person(4L, "John Doe", "New York", "123-456-7890");

        when(personService.saveOrUpdate(person)).thenReturn(person);

        mockMvc.perform(post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4L));

    }

    @Test
    void shouldUpdatePerson() throws Exception {

        String json = """
                {
                "id": 1,
                "name": "John Doe",
                "city": "Los Angeles",
                "phoneNumber": "103-486-7890"
                }
                """;
        Person existingPerson = new Person(1L, "John Doe", "New York", "103-486-7890");
        Person updatedPerson = new Person(1L, "John Doe", "Los Angeles", "103-486-7890");
        when(personService.findById(1L)).thenReturn(existingPerson);
        when(personService.saveOrUpdate(updatedPerson)).thenReturn(updatedPerson);

        mockMvc.perform(put("/api/persons/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Los Angeles"));
    }

    @Test
    void shouldDeletePerson() throws Exception {

        Person p = new Person(1L, "John Doe", "New York", "103-486-7890");
        when(personService.findById(1L)).thenReturn(p);

        mockMvc.perform(delete("/api/persons/1"))
                .andExpect(status().isOk());
    }
}