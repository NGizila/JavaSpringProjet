package com.cfa.jobs.letterjob;



import com.cfa.objects.letter.Letter;
import lombok.NoArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
public class SimpleProcessor implements ItemProcessor<List<String>, Letter> {

    @Override
    public Letter process(final List<String> letterList) throws Exception {
        List<Letter> res = new ArrayList<>();

        for (String message: letterList) {
            Letter letter = new Letter();
            letter.setMessage(message);
            letter.setCreationDate(new Date().toString());
            letter.setTreatmentDate(new Date().toString());

            res.add(letter);
        }
        return res.get(0);
    }
}
