package com.cfa.objects.letter;

import com.cfa.objects.letter.Letter;
import com.cfa.objects.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LetterRepository extends JpaRepository<Letter, Integer> {

    @Transactional
    void deleteById(int id);

}