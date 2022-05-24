package com.cfa.objects.person;

import com.cfa.objects.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {

}