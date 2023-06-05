package ru.job4j.ru.job4j.persons.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.ru.job4j.persons.model.Person;
import ru.job4j.ru.job4j.persons.repository.IPersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final IPersonRepository personRepository;

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    public Optional<Person> save(Person person) {
        return Optional.ofNullable(personRepository.save(person));
    }

    public Optional<Person> delete(Integer id) {
        Optional<Person> deletedPerson = personRepository.delete(id);
        deletedPerson.ifPresent(person -> Person.builder()
                .id(person.getId())
                .login(person.getLogin())
                .password(person.getPassword())
                .build());
        return deletedPerson;
    }
}
