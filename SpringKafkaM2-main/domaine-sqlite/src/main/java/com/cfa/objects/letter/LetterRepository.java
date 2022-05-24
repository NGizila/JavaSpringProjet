package com.cfa.objects.letter;

import com.cfa.objects.letter.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, Integer> {
}