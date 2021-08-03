package me.rhys.launcher.runtime;

import lombok.Getter;
import me.rhys.launcher.manager.UpdateManager;
import me.rhys.launcher.ui.UpdateUI;
import me.rhys.launcher.util.FileUtil;
import me.rhys.launcher.util.Logger;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

@Getter
public class MinecraftLauncher {

    private final File minecraftFolder = FileUtil.getMinecraftFolder();
    private final File liteCacheFolder = new File(this.minecraftFolder.getAbsolutePath() + "/LiteCache");

    private final UpdateUI updateUI = new UpdateUI();
    private UpdateManager updateManager;

    private String[] launchArgs;

    public void start(String[] args) {
        this.launchArgs = args;
        updateManager = new UpdateManager();

        if (this.minecraftFolder.exists()) {

            if (!this.liteCacheFolder.exists() || this.updateManager.checkUpdate()) {
                Logger.log("Update found, downloading...");
                this.liteCacheFolder.mkdir();
                this.updateUI.showUI();
            } else {
                Logger.log("No update found! launching game...");
                this.launchGame();
            }

        } else {
            Logger.warn("Unable to find .minecraft folder, check your Minecraft installation!");
        }
    }

    public void launchGame() {
        File clientJar = new File(this.liteCacheFolder.getAbsolutePath() + "/Lite-Client.jar");

        if (clientJar.exists()) {
            Logger.log("Classloading client jar, this may take some time depending on your systems speed.");

            try {
                URLClassLoader urlClassLoader = new URLClassLoader(
                        new URL[]{clientJar.toURI().toURL()},
                        this.getClass().getClassLoader()
                );

                Class<?> startClass = Class.forName(
                        "net.minecraft.client.main.Main",
                        true,
                        urlClassLoader
                );

                Method startMethod = startClass.getMethod("main", String[].class);

                Logger.log("Hooked " + startClass.getName() + " - " + startClass.getName());
                startMethod.invoke(null, (Object) this.launchArgs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Logger.warn("Unable to find client jar...?");
        }
    }

    boolean checkUpdate() {
        return false;
    }
}
