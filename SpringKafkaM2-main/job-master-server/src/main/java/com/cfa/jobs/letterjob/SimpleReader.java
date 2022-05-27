package com.cfa.jobs.letterjob;

import lombok.NoArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



@NoArgsConstructor
public class SimpleReader implements ItemReader<List<String>> {
    @Override
    public List<String> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        String chemin = "D://projects/cfa_2022/java/samples.txt";
        Scanner scanner = new Scanner(new File(chemin));
        List<String> result = null;

        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            if (line != null)
            {
                result.add(line);
            }
        }
    return result;
    }
}
