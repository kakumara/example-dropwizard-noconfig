package com.sonatype.dropwizard.example.dao;

import java.util.List;
import java.util.Optional;

import com.sonatype.dropwizard.example.model.Person;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class PersonDAO
    extends AbstractDAO<Person> {
    public PersonDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Person> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public Person create(Person person) {
        return persist(person);
    }

    public List<Person> findAll() {
        return list(namedTypedQuery("com.example.helloworld.core.Person.findAll"));
    }
}
