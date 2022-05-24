package com.cfa.objects.person;

import com.cfa.objects.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    List<Person> findByName(String name);

    @Transactional
    void deleteById(int id);
}