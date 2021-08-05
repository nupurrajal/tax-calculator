package com.org.taxcalculator;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        while (true) {
            Double doubleval = new Scanner(System.in).nextDouble();
            doubleval = Math.round (doubleval * 100.0) / 100.0;
            System.out.println(doubleval);
        }
    }
}
