package ru.job4j.ru.job4j.persons.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.ru.job4j.persons.model.Person;
import ru.job4j.ru.job4j.persons.service.PersonService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    @GetMapping("/")
    public List<Person> findAll() {
        return this.personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var isPersonFound = this.personService.findById(id);
        return new ResponseEntity<Person>(
                isPersonFound.orElse(new Person()),
                isPersonFound.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        var isPersonSaved = this.personService.save(person);
        ResponseEntity<Person> rsl = new ResponseEntity<>(HttpStatus.CONFLICT);
        if (isPersonSaved.isPresent()) {
            rsl = new ResponseEntity<Person>(
                    isPersonSaved.get(),
                    HttpStatus.CREATED
                    );
        }
        return rsl;
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        var isPersonUpdated = this.personService.save(person);
        return isPersonUpdated.isPresent()
                ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        var isPersonDeleted = this.personService.delete(id);
        return isPersonDeleted.isPresent()
                ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
