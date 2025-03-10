package io.fahd.unittests.service;

import io.fahd.unittests.entity.Person;
import io.fahd.unittests.repository.PersonRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;


    @Test
    void shouldReturnAllPersons() {

        List<Person> people = List.of(
                new Person(1L, "David White", "Miami", "1122334455"),
                new Person(2L, "Emma Davis", "San Francisco", "5566778899"),
                new Person(3L, "Frank Miller", "Boston", "9988776655")
        );

        when(personRepository.findAll()).thenReturn(people);

        List<Person> persons = personService.findAll();

        Assertions.assertThat(persons).hasSize(3);

    }

    @Test
    void shouldPersonFindById() {

        Person preparedPerson = new Person(1L, "David White", "Miami", "1122334455");

        when(personRepository.findById(1L)).thenReturn(Optional.of(preparedPerson));

        Person person = personService.findById(1L);

        Assertions.assertThat(person).usingRecursiveAssertion().isEqualTo(preparedPerson);
    }

    @Test
    void shouldSaveOrUpdatePerson() {

        Person preparedPerson = new Person(4L, "fahd", "Miami", "1122334455");

        when(personRepository.save(preparedPerson)).thenReturn(preparedPerson);

        Person person = personService.saveOrUpdate(preparedPerson);

        Assertions.assertThat(person).usingRecursiveAssertion().isEqualTo(preparedPerson);
    }

    @Test
    void shouldDeletePersonById() {

        personService.deleteById(4L);

        verify(personRepository).deleteById(4L);

    }
}