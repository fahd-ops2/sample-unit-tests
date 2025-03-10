package io.fahd.unittests.repository;

import io.fahd.unittests.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
