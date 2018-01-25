package org.aerogear.plugin.intellij.mobile.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MobileWatch {
    private List<String> command;

    public MobileWatch(List<String> command) {
        this.command = command;
    }

    private StringBuilder getResult(InputStream s) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(s));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bf.readLine()) != null) {
              sb.append(line);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (bf != null) bf.close();
            } catch (Exception e) {
                throw e;
            }

        }

        return sb;
    }

    public void start(Watch w) {
        ProcessBuilder pb = new ProcessBuilder(command);
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Process p = pb.start();
                    StringBuilder input = getResult(p.getInputStream());
                    StringBuilder error = getResult(p.getErrorStream());
                    if (input.length() != 0) {
                        w.onSuccess(input);
                    } else if (error.length() != 0) {
                        w.onError(new Exception(error.toString()));
                    }
                } catch (IOException e) {
                    w.onError(e);
                }
            }
        });
    }
}
