package me.rhys.launcher.ui;

import lombok.Getter;
import me.mat1337.easy.gui.EasyGui;
import me.mat1337.easy.gui.graphics.element.label.Label;
import me.mat1337.easy.gui.graphics.element.progressbar.ProgressBar;
import me.mat1337.easy.gui.graphics.theme.impl.DarkTheme;
import me.mat1337.easy.gui.util.Constraint;
import me.mat1337.easy.gui.util.action.ActionType;
import me.rhys.launcher.App;
import me.rhys.launcher.util.Logger;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
public class UpdateUI {
    private EasyGui easyGui;
    private Label label;
    private ProgressBar progressBar;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public void showUI() {
        Logger.log("Starting update...");

        this.easyGui = new EasyGui("Lite Update Installing...", 330, 170, new DarkTheme());
        this.easyGui.setResizable(false);

        this.label = this.easyGui.label("Download Lite Client Update, please wait...",
                new Constraint(Constraint.Type.FULL_CENTER).offset(0, 60));
        this.label.setTextColor(Color.YELLOW);

        // for progress bar
        this.executorService.scheduleAtFixedRate(this::updateBar, 10L, 10L, TimeUnit.MILLISECONDS);

        // fixes some bugs with EasyGUI not rendering some elements when added
        this.easyGui.getDisplay().repaint();

        if (App.getInstance().getMinecraftLauncher().getUpdateManager().download(App.getInstance()
                .getMinecraftLauncher().getLiteCacheFolder())) {
            Logger.log("Hiding UI...");
            this.easyGui.setVisible(false);
            Logger.log("Downloaded, starting client");
            App.getInstance().getMinecraftLauncher().launchGame();
        }
    }

    private void updateBar() {
        if (this.progressBar == null) {
            this.progressBar = this.easyGui.progressBar(170, 50, 0.0f, 100.0f,
                    0, new Constraint(Constraint.Type.FULL_CENTER));
        } else {
            this.progressBar.setCurrent(App.getInstance().getMinecraftLauncher().getUpdateManager()
                    .getDownloadPercentage());
        }
    }
}
