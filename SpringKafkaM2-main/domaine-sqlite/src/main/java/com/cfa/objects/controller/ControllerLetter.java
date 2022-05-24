package com.cfa.objects.controller;


import com.cfa.objects.letter.Letter;
import com.cfa.objects.letter.LetterRepository;
import com.cfa.objects.person.Person;
import com.cfa.objects.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@Slf4j
@RequestMapping(produces = "application/json; charset=UTF-8", value = "/v1/jobcontroller/letter")
@RequiredArgsConstructor
public class ControllerLetter {

    @Autowired
    private final LetterRepository letter;

    @GetMapping("/")
    public List<Letter> getAllLetters(){
        return letter.findAll();
    }


}
