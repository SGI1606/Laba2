package com.company;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        while(true) {
            String s = in.nextLine();
            if (Objects.equals(s,"e")) break;
            Calculator n = new Calculator();
            try {
                List<String> expression = Calculator.parse(s);
                for (String x : expression) System.out.print(x + " ");
                System.out.println();
                double res = Calculator.calc(expression);
                if (!Double.isInfinite(res))
                    System.out.println(res + "\n");
                else System.out.println("бесконечность\n");
            } catch (NumberFormatException e1) {
                System.out.println("Некорректный ввод\n");
            } catch (Exception e) {
                System.out.println(e.getMessage()+"\n");
            }
        }

    }
}
