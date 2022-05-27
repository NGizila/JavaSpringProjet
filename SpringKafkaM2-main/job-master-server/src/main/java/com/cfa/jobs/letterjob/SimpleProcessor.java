package com.cfa.jobs.letterjob;



import com.cfa.objects.letter.Letter;
import lombok.NoArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
public class SimpleProcessor implements ItemProcessor<Letter,Letter> {

    @Override
    public Letter process(Letter letter) throws Exception {

        letter.setCreationDate(new Date().toString());
        letter.setTreatmentDate(new Date().toString());
        return letter;
    }
}
