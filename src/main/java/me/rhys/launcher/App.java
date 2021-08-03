package me.rhys.launcher;

import lombok.Getter;
import me.rhys.launcher.runtime.MinecraftLauncher;

@Getter
public class App {
    @Getter private static App instance;

    private final MinecraftLauncher minecraftLauncher = new MinecraftLauncher();

    public App(String[] args) {
        instance = this;
        this.minecraftLauncher.start(args);
    }
}
