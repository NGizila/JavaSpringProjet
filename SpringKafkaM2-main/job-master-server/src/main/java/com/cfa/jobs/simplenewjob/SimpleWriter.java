package com.cfa.jobs.simplenewjob;

import org.springframework.batch.item.*;

import java.util.List;

public class SimpleWriter implements ItemWriter<List<String>> {

    @Override
    public void write(List<? extends List<String>> list) throws Exception {
        System.out.println(list);
    }
}
