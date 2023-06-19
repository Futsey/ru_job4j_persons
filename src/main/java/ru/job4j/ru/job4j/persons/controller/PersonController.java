package ru.job4j.ru.job4j.persons.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.ru.job4j.persons.model.Person;
import ru.job4j.ru.job4j.persons.model.PersonLoginDTO;
import ru.job4j.ru.job4j.persons.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;
    private final BCryptPasswordEncoder encoder;
    private final ObjectMapper objectMapper;
    private static final Logger LOG = LoggerFactory.getLogger(PersonService.class.getName());

    @GetMapping("/list")
    public ResponseEntity<List<Person>> findAll() {
        List<Person> rsl = this.personService.findAll();
        if (rsl.size() == 0) {
            throw new IllegalArgumentException("No person saved in DB yet");
        }
        return new ResponseEntity<>(
                rsl,
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var isPersonFound = this.personService.findById(id);
        if (isPersonFound.isEmpty()) {
            throw new IllegalArgumentException("Person with id: ".concat(String.valueOf(id)).concat(" doesn`t exists"));
    }
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Job4jAUTH", "Person")
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(isPersonFound.get().toString().length())
                .body(isPersonFound.get());
    }

    @GetMapping("/login-search/{login}")
    public ResponseEntity<PersonLoginDTO> findByLogin(@PathVariable("login") String login) {
        Optional<PersonLoginDTO> isPersonFound = this.personService.findByLoginOnly(login);
        if (isPersonFound.isEmpty()) {
            throw new IllegalArgumentException("Person with login: ".concat(login).concat(" doesn`t exists"));
        }
        return new ResponseEntity<>(
                isPersonFound.get(),
                HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        boolean personUpdated =  this.personService.update(person);
        if (!personUpdated) {
            throw new IllegalArgumentException("Failed to update person with login: ".concat(person.getLogin()));
        }
        return personUpdated ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return this.personService.delete(id)
                ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Person> signUp(@Valid @RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        var isPersonSaved = this.personService.save(person);
        if (isPersonSaved.isEmpty()) {
            throw new IllegalArgumentException("Person with login: ".concat(person.getLogin()).concat(" already exists"));
        }
        return new ResponseEntity<Person>(
                isPersonSaved.orElse(new Person()),
                isPersonSaved.isPresent() ? HttpStatus.OK : HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public void exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
        LOG.error(e.getLocalizedMessage());
    }
}
