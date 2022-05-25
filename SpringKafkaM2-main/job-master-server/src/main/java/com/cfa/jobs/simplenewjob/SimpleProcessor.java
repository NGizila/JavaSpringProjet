package com.cfa.jobs.simplenewjob;



import lombok.NoArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

@NoArgsConstructor
public class SimpleProcessor implements ItemProcessor<List<String>, List<String>> {

    @Override
    public List<String> process(List<String> strings) throws Exception {

        strings.add("inconnue");
        return strings;
    }
}
