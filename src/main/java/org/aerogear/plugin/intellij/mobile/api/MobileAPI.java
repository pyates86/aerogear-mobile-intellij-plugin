package org.aerogear.plugin.intellij.mobile.api;

import com.google.gson.Gson;
import org.aerogear.plugin.intellij.mobile.models.MobileServices;
import org.aerogear.plugin.intellij.mobile.models.ServiceClass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MobileAPI {
    private static final String AEROGEAR_NOTIFICATION_GROUP = "AerogearMobileNotifications";
    public MobileAPI() {}

    public MobileServices getServices() throws CLIException {
        ProcessBuilder pb = new ProcessBuilder("/home/jroche/go/src/github.com/aerogear/mobile-cli/mobile", "get", "services", "-o=json");
        StringBuilder sb = new StringBuilder();
        BufferedReader bf = null;

        try {
            Process p = pb.start();
            bf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while((line = bf.readLine()) != null) {
                sb.append(line);
            }
        } catch(Exception e) {
            throw new CLIException("Failed to retrieve mobile services from mobile cli ", e.getCause());
        } finally {
            try {
                if (bf != null) bf.close();
            } catch(Exception e) {
                System.out.println(e);
            }
        }

        Gson gson = new Gson();
        MobileServices services = gson.fromJson(sb.toString(), MobileServices.class);
        return services;
    }

    public void createService(ServiceClass sc, List<String> params) throws CLIException {
        List<String> command = new ArrayList<String>();
        command.add("/home/jroche/go/src/github.com/aerogear/mobile-cli/mobile");
        command.add("create");
        command.add("serviceinstance");
        command.add(sc.getServiceName());
        for (String param : params) {
            command.add(param);
        }

        new MobileWatch(command).start(new Watch() {
            @Override
            public void onError(Exception e) {
                //TODO: Handle success notification
            }

            @Override
            public void onSuccess(StringBuilder sbi) {
                //TODO: Handle error notification
            }
        });
    }
}