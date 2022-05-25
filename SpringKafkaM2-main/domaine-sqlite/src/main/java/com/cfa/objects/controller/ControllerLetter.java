package com.cfa.objects.controller;


import com.cfa.objects.letter.Letter;
import com.cfa.objects.letter.LetterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Letter getLetterById(@PathVariable(value = "id") int id) {
        return letter.findById(id).get();
    };
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.GET)
    public ResponseEntity<String> deleteLetterById(@PathVariable(value = "id") int id) {
        letter.deleteById(id);
        return new ResponseEntity<>("User " + id + " was deleted", HttpStatus.NOT_FOUND);
    };


}
