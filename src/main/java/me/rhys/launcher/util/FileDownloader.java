package me.rhys.launcher.util;

import lombok.Getter;
import me.rhys.launcher.App;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@Getter
public class FileDownloader {
    private final String link;
    private final File output;

    private File file;

    public FileDownloader(String link, File output) {
        this.link = link;
        this.output = output;
    }

    public boolean downloadFile() {
        App.getInstance().getMinecraftLauncher().getUpdateManager().setDownloadPercentage(0);

        URL url;
        URLConnection connection;

        try {
            url = new URL(this.link);

            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko)" +
                            " Chrome/23.0.1271.95 Safari/537.11");

            connection = urlConnection;
        } catch (IOException e) {
            return false;
        }

        try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
             FileOutputStream fileOutputStream = new FileOutputStream(this.file
                     = this.output.getAbsoluteFile())) {

            float percentage = 0.0f;
            int size = connection.getContentLength();

            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);

                percentage += bytesRead;
                if (size > 0) {
                    App.getInstance().getMinecraftLauncher().getUpdateManager()
                            .setDownloadPercentage((percentage / size * 100.0f));
                }
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}