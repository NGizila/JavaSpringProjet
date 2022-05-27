package com.cfa.jobs.letterjob;

import com.cfa.objects.controller.ControllerLetter;
import com.cfa.objects.letter.Letter;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

    }

    public static void writeOnFile(String msg) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("conversation.txt",true));
        writer.write(msg);
        writer.close();
    }

}
