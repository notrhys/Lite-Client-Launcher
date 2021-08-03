package me.rhys.launcher.util;

import java.io.File;

public class FileUtil {
    public static File getMinecraftFolder() {
        String userHome = System.getProperty("user.home", ".");
        switch (OSUtil.getPlatform()) {

            case LINUX:
                return new File(userHome, ".minecraft/");

            case WINDOWS:
                String applicationData = System.getenv("APPDATA");
                return new File(applicationData != null ? applicationData : userHome, ".minecraft/");

            case MACOS:
                return new File(userHome, "Library/Application Support/minecraft");

            default:
                return new File(userHome, "minecraft/");
        }
    }
}
