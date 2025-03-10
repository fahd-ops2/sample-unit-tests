package io.fahd.unittests.repository;

import io.fahd.unittests.entity.Person;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void shouldFindAllPersons () {
        List<Person> persons = personRepository.findAll();
        Assertions.assertThat(persons).isNotEmpty();
    }

    @Test
    void shouldfindPersonById(){
        Person person = personRepository.findById(1L).get();

        Assertions.assertThat(person).isNotNull();
        Assertions.assertThat(person.getName()).isEqualTo("John Doe");
        Assertions.assertThat(person.getCity()).isEqualTo("Paris");
        Assertions.assertThat(person.getPhoneNumber()).isEqualTo("123-456-7890");
    }

    @Test
    void shouldSavePerson(){
        Person person = new Person();
        person.setName("fahd");
        person.setCity("casablanca");
        person.setPhoneNumber("212-234566789");

        Person savedPerson = personRepository.save(person);

        Assertions.assertThat(savedPerson).isNotNull();

        Assertions.assertThat(savedPerson.getName()).isEqualTo(person.getName());
        Assertions.assertThat(savedPerson.getCity()).isEqualTo(person.getCity());
        Assertions.assertThat(savedPerson.getPhoneNumber()).isEqualTo(person.getPhoneNumber());

    }

    @Test
    void shouldUpdatePerson(){

        Person person = personRepository.findById(1L).get();
        person.setCity("RABAT");

        Person updatedPerson = personRepository.save(person);

        Assertions.assertThat(updatedPerson.getCity()).isEqualTo("RABAT");

    }

    @Test
    void shouldDeletePerson() {

        personRepository.deleteById(3L);

        Optional<Person> person = personRepository.findById(3L);

        Assertions.assertThat(person).isEmpty();

    }

}