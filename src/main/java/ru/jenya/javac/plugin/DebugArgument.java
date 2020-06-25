package ru.jenya.javac.plugin;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DebugArgument {

    public final String className;
    public final int line;
    public final String condition;

    public DebugArgument(String className, int line, String condition) {
        this.className = className;
        this.line = line;
        this.condition = condition;
    }

    public static DebugArgument parse(String line) {
        String[] split = line.split(":");
        if (split.length < 3) {
            throw new IllegalArgumentException("<class-name>:<line>");
        }
        int numb = Integer.parseInt(split[1]);
        String condition = Arrays.stream(split).skip(2).collect(Collectors.joining());
        return new DebugArgument(split[0], numb, condition);
    }
}
