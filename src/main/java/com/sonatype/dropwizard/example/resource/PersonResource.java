package com.sonatype.dropwizard.example.resource;

import java.util.OptionalLong;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sonatype.dropwizard.example.dao.PersonDAO;
import com.sonatype.dropwizard.example.model.Person;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/people/{personId}")
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource
{

    private final PersonDAO peopleDAO;

    public PersonResource(PersonDAO peopleDAO) {
        this.peopleDAO = peopleDAO;
    }

    @GET
    @UnitOfWork
    public Person getPerson(@PathParam("personId") OptionalLong personId) {
        return findSafely(personId.orElseThrow(() -> new BadRequestException("person ID is required")));
    }


    private Person findSafely(long personId) {
        return peopleDAO.findById(personId).orElseThrow(() -> new NotFoundException("No such user."));
    }
}
