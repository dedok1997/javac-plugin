package ru.jenya.javac.plugin;

public class Example {
    public static void main(String[] args) {
        for (int i = 0; i < 1000000; i++) {
            int x = 2;


            x++;x--;x++;

        }
        System.out.println("finished");
    }

}
