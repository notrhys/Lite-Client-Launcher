package me.rhys.launcher.manager;

import jdk.nashorn.internal.scripts.JO;
import lombok.Getter;
import lombok.Setter;
import me.rhys.launcher.App;
import me.rhys.launcher.util.FileDownloader;
import me.rhys.launcher.util.HTTPUtil;
import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class UpdateManager {
    private float downloadPercentage;

    private final Yaml yaml = new Yaml();
    private final File cacheFile = new File(App.getInstance().getMinecraftLauncher()
            .getLiteCacheFolder() + "/cache.yml");
    private final String serverVersion = this.getServerVersion();

    public boolean download(File output) {
        this.cacheVersion();
        return new FileDownloader(
                "https://liteclient.club/download/Lite-Client.jar",
                new File(output.getAbsolutePath() + "/Lite-Client.jar")
        ).downloadFile();
    }

    private String getServerVersion() {
        return HTTPUtil.getResponse("https://liteclient.club/download/version.php");
    }

    public boolean checkUpdate() {
        if (this.serverVersion != null) {
            return this.getCachedVersion() != Double.parseDouble(this.serverVersion);
        }
        return false;
    }

    private double getCachedVersion() {
        try {
            Map<String, String> data = this.yaml.load(new FileInputStream(this.cacheFile.getAbsolutePath()));

            return Double.parseDouble(data.get("clientVersion"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private void cacheVersion() {
        Map<String, String> data = new HashMap<>();
        data.put("clientVersion", this.serverVersion);

        try {
            this.yaml.dump(data, new FileWriter(this.cacheFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
