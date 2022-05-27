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
        /*output.addAll(items);*/
        /*Letter letter = new Letter();
        letter.setMessage("lol");
        letter.setCreationDate("mmmm");
        letter.setTreatmentDate("llll");*/

        //getResult("http://localhost:9623/v1/jobcontroller/letter/response",letter);



        /*for (int i = 0; i< list.size(); i++ )
        {
            Letter letter = (Letter) list.get(i);
*/
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
                    .uri(URI.create("http://localhost:9623/v1/jobcontroller/letter/response"))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .setHeader("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());
        //}>

    }

    private Letter getResult(@NotNull final String parUrl, Letter request) {
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(parUrl, request, Letter.class);
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
