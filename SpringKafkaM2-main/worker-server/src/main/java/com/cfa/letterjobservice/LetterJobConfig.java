package com.cfa.letterjob;

import com.cfa.objects.letter.Letter;
import org.springframework.batch.integration.chunk.RemoteChunkingWorkerBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;

public class LetterJobConfig {

    @Autowired
    private RemoteChunkingWorkerBuilder<Letter, Letter> workerBuilder;


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

}
