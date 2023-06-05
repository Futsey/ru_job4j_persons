package ru.job4j.ru.job4j.persons.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.ru.job4j.persons.model.Person;

import java.util.List;
import java.util.Optional;

public interface IPersonRepository extends CrudRepository<Person, Integer> {

    List<Person> findAll();

    Optional<Person> findById(int id);
}
