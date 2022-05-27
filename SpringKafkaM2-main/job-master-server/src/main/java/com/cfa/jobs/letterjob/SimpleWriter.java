package com.cfa.jobs.letterjob;

import com.cfa.objects.letter.Letter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.support.transaction.TransactionAwareProxyFactory;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class SimpleWriter implements ItemWriter<Letter> {

    List<Letter> output = TransactionAwareProxyFactory.createTransactionalList();

    @Override
    public void write(List<? extends Letter> letter) throws Exception {
        output.addAll(letter);
        postLetter(output);
    }

    public static void postLetter(List<Letter> letterList) throws IOException, InterruptedException
    {

        for (Letter letter: letterList) {

            String requestBody = "";

            ObjectMapper Obj = new ObjectMapper();
            try {
                requestBody = Obj.writeValueAsString(letter);
                System.out.println(requestBody);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:7777/v1/jobcontroller/letter/response"))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .setHeader("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());
        }
    }


}
