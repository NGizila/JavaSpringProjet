package com.cfa.jobs.simplenewjob;

import lombok.NoArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class SimpleReader implements ItemReader<List<String>> {
    @Override
    public List<String> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        List<String> strings = new ArrayList<String>();
        strings.add("Matthias");
        strings.add("Natalia");

        return strings;

    }
}
