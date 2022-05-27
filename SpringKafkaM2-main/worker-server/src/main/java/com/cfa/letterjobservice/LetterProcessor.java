package com.cfa.letterjobservice;

import com.cfa.objects.letter.Letter;
import org.springframework.batch.item.ItemProcessor;
import java.sql.Date;

public class LetterProcessor implements ItemProcessor<Letter, Letter> {

    public Letter process(Letter letter){
        Date date = new Date(System.currentTimeMillis());
        letter.setTreatmentDate(String.valueOf(date));
        letter.setCreationDate(String.valueOf(date));
        return letter;
    }
}