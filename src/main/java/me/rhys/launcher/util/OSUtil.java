package me.rhys.launcher.util;

public class OSUtil {

    public static OS getPlatform() {
        String s = System.getProperty("os.name").toLowerCase();

        return s.contains("win")
                ? OS.WINDOWS
                : (s.contains("mac")
                ? OS.MACOS
                : (s.contains("solaris") ? OS.SOLARIS
                : (s.contains("sunos") ? OS.SOLARIS
                : (s.contains("linux") ? OS.LINUX
                : (s.contains("unix") ? OS.LINUX
                : OS.UNKNOWN)))));
    }

    public enum OS {
        LINUX,
        SOLARIS,
        WINDOWS,
        MACOS,
        UNKNOWN;
    }
}
