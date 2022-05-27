package com.cfa.jobs.letterjob;

import com.cfa.objects.letter.Letter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.integration.chunk.RemoteChunkingManagerStepBuilderFactory;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

//@Configuration
@EnableBatchIntegration
@EnableBatchProcessing
public class LetterJobKafkaConfig {

    public static String TOPIC = "step-execution-events-lol";
    public static String GROUP_ID = "step-response_partition";

    @Autowired
    private RemoteChunkingManagerStepBuilderFactory managerStepBuilderFactory;
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private ConsumerFactory kafkaFactory;

    @Bean("letterKafkaJob")
    public Job letterKafkaJob() {
        return jobBuilderFactory
                .get("letterKafkaJob")
                .start(letterStep())
                .build();
    }

    @Bean
    public Step letterStep() {
        return this.managerStepBuilderFactory.get("letterStep")
                .<Letter,Letter>chunk(10)
                .reader(readFromCsv())
                .outputChannel(requests())
                .inputChannel(replies())
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

    @Bean
    public DirectChannel requests() {
        return new DirectChannel();
    }

    @Bean
    public QueueChannel replies() { return new QueueChannel(); }

    @Bean
    public IntegrationFlow outboundFlow() {
        final KafkaProducerMessageHandler kafkaMessageHandler = new KafkaProducerMessageHandler(kafkaTemplate);
        kafkaMessageHandler.setTopicExpression(new LiteralExpression(TOPIC));
        return IntegrationFlows
                .from(requests())
                .handle(kafkaMessageHandler)
                .get();
    }

    @Bean
    public IntegrationFlow inboundFlow() {
        final ContainerProperties containerProps = new ContainerProperties(TOPIC);
        containerProps.setGroupId(GROUP_ID);

        final KafkaMessageListenerContainer container = new KafkaMessageListenerContainer(kafkaFactory, containerProps);
        final KafkaMessageDrivenChannelAdapter kafkaMessageChannel = new KafkaMessageDrivenChannelAdapter(container);

        return IntegrationFlows
                .from(kafkaMessageChannel)
                .channel(replies())
                .get();
    }


}
