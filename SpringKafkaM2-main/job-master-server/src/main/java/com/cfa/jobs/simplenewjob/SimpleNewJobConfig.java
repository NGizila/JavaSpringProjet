package com.cfa.jobs.simplenewjob;

import com.cfa.jobs.jobexample.SimpleTaskletSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SimpleNewJobConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    private Source sources;


    @Bean
    public Job simpleNewJob() throws Exception {
        return jobBuilderFactory
                .get("simpleNewJob")
                .start(simpleStep2())
                .build();
    }

    @Bean
    public Step simpleNewStep() {
        return this.stepBuilderFactory
                .get("simpleNewStep")
                .tasklet(new SimpleTaskletSource(sources))
                .build();
    }

    @Bean
    public Step simpleStep2() throws Exception {
        return this.stepBuilderFactory
                .get("simpleStep2").<List<String>, List<String>>chunk(2)
                .reader(new SimpleReader())
                .processor(new SimpleProcessor())
                .writer(new SimpleWriter())
                .build();
    }

}
