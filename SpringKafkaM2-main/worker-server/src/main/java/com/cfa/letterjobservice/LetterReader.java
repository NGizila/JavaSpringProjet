package com.cfa.letterjobservice;
import com.cfa.objects.letter.Letter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;

public class LetterReader extends FlatFileItemReader {

    public FlatFileItemReader<Letter> reader() {
        FlatFileItemReader<Letter> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource("input/inputData1.csv"));
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<>() { {
            setLineTokenizer(new DelimitedLineTokenizer() { {
                setNames(new String[] {"creationDate","message","treatmentDate"});
            } });
            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() { {
                setTargetType(Letter.class);
            } });
        } });
        return reader;
    }
}