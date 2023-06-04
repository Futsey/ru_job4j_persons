package ru.job4j.ru.job4j.persons.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.ru.job4j.persons.model.Person;

import java.util.List;

public interface IPersonRepository extends CrudRepository<Person, Integer> {

    List<Person> findAll();
}
