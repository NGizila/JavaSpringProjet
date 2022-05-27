package com.cfa.jobs.letterjob;

import com.cfa.objects.controller.ControllerLetter;
import com.cfa.objects.letter.Letter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.support.transaction.TransactionAwareProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class SimpleWriter implements ItemWriter<Letter> {
    @Autowired
    ControllerLetter controllerLetter;

    @Override
    public void write(List<? extends Letter> list) throws Exception {
        for (Letter letter: list) {
            controllerLetter.postLetter(letter);
            writeOnFile("Message treated: " + letter.getMessage() + "\n");
        }

    }

/*
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
            writeOnFile("Message treated: " + letter.getMessage() + "\n");
        }
    }
*/

    public static void writeOnFile(String msg) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("conversation.txt",true));
        writer.write(msg);
        writer.close();
    }

}
