package me.mat1337.easy.gui.util.logger;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@Getter
public class Logger {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");

    private String name;

    public void info(String info, Object... parameters) {
        String buffer = info;
        for (Object parameter : parameters) {
            buffer = buffer.replaceFirst("%s", parameter.toString());
        }
        info(buffer);
    }

    public void warn(String warn, Object... parameters) {
        String buffer = warn;
        for (Object parameter : parameters) {
            buffer = buffer.replaceFirst("%s", parameter.toString());
        }
        warn(buffer);
    }

    public void error(String error, Object... parameters) {
        String buffer = error;
        for (Object parameter : parameters) {
            error = error.replaceFirst("%s", parameter.toString());
        }
        error(error);
    }

    public void info(String info) {
        print(info, "INFO", ConsoleColor.RESET);
    }

    public void warn(String warn) {
        print(warn, "WARN", ConsoleColor.YELLOW);
    }

    public void error(String error) {
        print(error, "ERROR", ConsoleColor.RED);
    }

    private void print(String str, String type, ConsoleColor color) {
    }

    private static String formatLog(String thread, String type, String log) {
        return "[" + dateFormat.format(new Date()) + "] [" + thread + "/" + type + "] " + log + ConsoleColor.RESET;
    }
}