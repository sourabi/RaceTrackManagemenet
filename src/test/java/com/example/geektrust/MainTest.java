package com.example.geektrust;

import org.junit.jupiter.api.Test;

public class MainTest {
    private final String[] args = new String[5];
    @Test
    void mainMethodTest(){
        args[0] = "sample_input/input6.txt";
        Main.main(args);
    }
}