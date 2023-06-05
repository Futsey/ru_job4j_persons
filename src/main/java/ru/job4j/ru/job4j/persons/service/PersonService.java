package ru.job4j.ru.job4j.persons.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.ru.job4j.persons.model.Person;
import ru.job4j.ru.job4j.persons.repository.IPersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final IPersonRepository personRepository;

    private static final Logger LOG = LoggerFactory.getLogger(PersonService.class.getName());

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    public Optional<Person> save(Person person) {
        Optional<Person> rsl = Optional.empty();
        try {
            personRepository.save(person);
            rsl = Optional.of(person);
            LOG.info("Person was saved successfully");
        } catch (Exception e) {
            LOG.error("Person wasn`t saved. Exception: " + e, e);
        }
        return rsl;
    }

    public boolean update(Person person) {
        boolean rsl = false;
        Optional<Person> nonNullPerson = findById(person.getId());
        if (nonNullPerson.isPresent()) {
            personRepository.delete(nonNullPerson.get());
            personRepository.save(nonNullPerson.get());
            rsl = true;
        }
        return rsl;
    }

    public boolean delete(int id) {
        boolean rsl = false;
        Optional<Person> person = findById(id);
        if (person.isPresent()) {
            personRepository.delete(person.get());
            rsl = true;
        }
        return rsl;
    }
}
