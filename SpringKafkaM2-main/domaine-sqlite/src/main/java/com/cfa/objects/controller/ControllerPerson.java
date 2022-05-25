package com.cfa.objects.controller;

import com.cfa.objects.person.Person;
import com.cfa.objects.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@Slf4j
@RequestMapping(produces = "application/json; charset=UTF-8", value = "/v1/jobcontroller/person")
@RequiredArgsConstructor
public class ControllerPerson {

    @Autowired
    private final PersonRepository person;

    @GetMapping("/")
    public List<Person> getAllPeople(){
        return person.findAll();
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Person getPersonById(@PathVariable(value = "id") int id) {
        return person.findById(id).get();
    };

    @RequestMapping(value = "/name/{name}",method = RequestMethod.GET)
    public List<Person> getPersonByName(@PathVariable(value = "name") String name) {
        return person.findByName(name);
    };

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.GET)
    public ResponseEntity<String> deletePersonById(@PathVariable(value = "id") int id) {
        String personName = person.findById(id).get().getName();
        person.deleteById(id);
        return new ResponseEntity<>("User " + id + " : " + personName + " was deleted", HttpStatus.NOT_FOUND);
    };

}
