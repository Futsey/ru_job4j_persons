package ru.job4j.ru.job4j.persons.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

import javax.validation.constraints.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "persons")
@EqualsAndHashCode(of = {"id"})
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Login must be not null")
    @NotBlank(message = "Login must be not empty")
    private String login;

    @NotNull(message = "Password must be not null")
    @Length(min = 3, max = 10, message = "Password must be more than 3 chars and less or equal than 10 chars")
    private String password;
}
