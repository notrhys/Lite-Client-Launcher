package me.rhys.launcher.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HTTPUtil {
    public static String getResponse(String URL) {
        try {
            URLConnection urlConnection = new URL(URL).openConnection();
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11" +
                            " (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            urlConnection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),
                    StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            return stringBuilder.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
