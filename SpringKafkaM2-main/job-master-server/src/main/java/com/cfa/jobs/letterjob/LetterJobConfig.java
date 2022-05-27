package com.cfa.jobs.letterjob;

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
                .get("letterStep").<Letter,Letter>chunk(10) //.chunk(10)
                .reader(readFromCsv()) //reader(itemReader())
                /*.writer(batchItemWriter())*/
                .writer(new SimpleWriter())
                .build();
    }

    /*@Bean
    public Step letterStep() {
        return this.stepBuilderFactory
                .get("letterStep").<List<String>,Letter>chunk(10) //.chunk(10)
                .reader(new SimpleReader()) //reader(itemReader())
                .processor(new SimpleProcessor())
                .writer(new SimpleWriter())
                .build();
    }*/

    @Bean
    public JdbcBatchItemWriter<Letter> batchItemWriter() {
        JdbcBatchItemWriter<Letter> writer = new JdbcBatchItemWriter<Letter>();
                writer.setDataSource(dataSource);
                writer.setSql("INSERT INTO letter (creationDate,message,treatmentDate) VALUE ( :creationDate, :message, :treatmentDate)");
                writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Letter>());
                //.beanMapped()
                //.build();
        return writer;
    }

    public FlatFileItemReader<Letter> readFromCsv() {
        FlatFileItemReader<Letter> reader = new FlatFileItemReader<>();
        //reader.setResource(new ClassPathResource("input/inputData1.csv"));
        reader.setResource(new FileSystemResource("D://projects/cfa_2022/java/inputData1.csv"));
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

/*    @Bean
    public Step letterStep() {
        return this.stepBuilderFactory
                .get("letterStep").<List<String>,List<String>>chunk(2) //.chunk(10)
                .reader(new SimpleReader()) //reader(itemReader())
                .processor(new SimpleProcessor())
                //.writer(new SimpleWriter())
                .build();
    }*/
}
