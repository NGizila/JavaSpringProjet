
package com.cfa.jobs.temp;

import com.cfa.objects.letter.Letter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchConfig {
    private static final int chunkSize = 10;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    @Autowired
    private final DataSource dataSource;

    @Bean
    public Job jdbcBatchItemWriterJob() {
        return jobBuilderFactory.get("jdbcBatchItemWriterJob")
                .start(jdbcBatchItemWriterStep())
                .build();
    }

    @Bean
    public Step jdbcBatchItemWriterStep() {
        return stepBuilderFactory.get("jdbcBatchItemWriterStep")
                .<Letter, Letter>chunk(chunkSize)
                .reader(jdbcBatchItemReader())
                .writer(jdbcBatchItemWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Letter> jdbcBatchItemReader() {
        return new JdbcCursorItemReaderBuilder<Letter>()
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Letter.class))
                .sql("SELECT id, creation_date,message,treatment_date FROM letter")
                .name("jdbcCursorItemReader")
                .build();
    }
    @Bean
    public FlatFileItemReader<Letter> jdbcBatchItemReader2() {
        FlatFileItemReader<Letter> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("input/inputData1.csv"));
        reader.setLineMapper(new DefaultLineMapper<Letter>() {
            {

                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "creation_date","message","treatment_date" });
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



    @Bean
    public JdbcBatchItemWriter<Letter> jdbcBatchItemWriter() {
        return new JdbcBatchItemWriterBuilder<Letter>()
                .dataSource(dataSource)
                .sql("INSERT INTO letter (creation_date,message,treatment_date) VALUE (:creation_date, :message, :treatment_date)")
                .beanMapped()
                .build();
    }
}