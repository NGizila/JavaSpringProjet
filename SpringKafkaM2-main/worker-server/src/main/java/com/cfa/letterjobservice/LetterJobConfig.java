package com.cfa.letterjobservice;

import com.cfa.objects.letter.Letter;
import com.cfa.remotepartition.PartitionConfig;
import org.springframework.batch.integration.chunk.RemoteChunkingWorkerBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

public class LetterJobConfig {

    @Autowired
    private RemoteChunkingWorkerBuilder<Letter, Letter> workerBuilder;

    public static String TOPIC = "step-execution-eventslol";
    public static String GROUP_ID = "stepresponse_partition";

    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private ConsumerFactory kafkaFactory;


    @Bean
    public IntegrationFlow workerFlow() {
        return this.workerBuilder
                .itemProcessor(itemProcessor())
                .itemWriter(itemWriter())
                .inputChannel(requests())
                .outputChannel(replies())
                .build();
    }

    @Bean
    public ItemProcessor<Letter, Letter> itemProcessor() {
        return item -> {
            System.out.println("processing letter " + item);
            return item;
        };
    }

    @Bean
    public ItemWriter<Letter> itemWriter() {
        return items -> {
            for (Letter item : items) {
                System.out.println("writing letter " + item);
            }
        };
    }

    @Bean
    public DirectChannel requests(){
        return new DirectChannel();
    }
    @Bean
    public QueueChannel replies() {
        return new QueueChannel();
    }

    @Bean
    public IntegrationFlow inboundFlow() {
        final ContainerProperties containerProps = new ContainerProperties(PartitionConfig.TOPIC);
        containerProps.setGroupId(PartitionConfig.GROUP_ID);

        final KafkaMessageListenerContainer container = new KafkaMessageListenerContainer(kafkaFactory, containerProps);
        final KafkaMessageDrivenChannelAdapter kafkaMessageChannel = new KafkaMessageDrivenChannelAdapter(container);

        return IntegrationFlows
                .from(kafkaMessageChannel)
                .channel(requests())
                .get();
    }

    @Bean
    public IntegrationFlow outboundFlow() {
        final KafkaProducerMessageHandler kafkaMessageHandler = new KafkaProducerMessageHandler(kafkaTemplate);
        kafkaMessageHandler.setTopicExpression(new LiteralExpression(PartitionConfig.TOPIC));
        return IntegrationFlows
                .from(replies())
                .handle(kafkaMessageHandler)
                .get();
    }

}
