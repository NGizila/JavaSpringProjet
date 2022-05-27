package com.cfa.jobs.letterjob;

import com.cfa.objects.controller.ControllerLetter;
import com.cfa.objects.letter.Letter;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class LetterJobConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private Source sources;

    @Autowired
    private DataSource dataSource;

    @Autowired
    ControllerLetter controllerLetter;

    @Bean("letterJob")
    public Job letterJob() {
        return jobBuilderFactory
                .get("letterJob")
                .start(letterStep())
                .build();
    }

    @Bean
    public Step letterStep() {
        return this.stepBuilderFactory
                .get("letterStep").<Letter,Letter>chunk(10)
                .reader(readFromCsv())
                .writer(new SimpleWriter(controllerLetter))
                .processor(new SimpleProcessor())
                .build();
    }

    public FlatFileItemReader<Letter> readFromCsv() {
        FlatFileItemReader<Letter> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("input/inputData1.csv"));
        reader.setLineMapper(new DefaultLineMapper<Letter>() {
            {

                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "creationDate","message","treatmentDate" });
                    }
                });

                setFieldSetMapper(new BeanWrapperFieldSetMapper<Letter>() {
                    {
                        setTargetType(Letter.class);
                    }
                });
            }
        });

        return reader;
    }
}
