package com.cfa.jobs.letterjob;

import com.cfa.objects.letter.Letter;
import lombok.NoArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



@NoArgsConstructor
public class SimpleReader extends  FlatFileItemReader {

    public FlatFileItemReader<Letter> readFromCsv() {
        FlatFileItemReader<Letter> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("input/inputData1.csv"));
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<>() {
            {

                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "creationDate","message","treatmentDate" });
                    }
                });

                setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {
                    {
                        setTargetType(Letter.class);
                    }
                });
            }
        });

        return reader;
    }
}
