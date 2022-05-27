package com.cfa.jobs.simplenewjob;

import com.cfa.objects.letter.Letter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class SimpleWriter implements ItemWriter<List<String>> {

    @Override
    public void write(List<? extends List<String>> list) throws Exception {
        System.out.println(list);
    }
}
