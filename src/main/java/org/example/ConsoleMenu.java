package org.example;

//import javafx.concurrent.Task;


import org.example.Task2.Main;
import org.example.Task3.AirplaneSimulation;
import org.example.Task4.Account;
import org.example.Task5.ReplaceLetters;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleMenu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

//        Task[] tasks = {
//                new org.example.Task1.Main(),
//                new Main(),
//                new AirplaneSimulation(),
//                new Account(),
//                new ReplaceLetters(),
//                new AirplaneSimulation(),
//                // Добавьте остальные задачи
//        };


        while (!exit) {
            System.out.println("\nВыберите задание:");

//            for (int i = 0; i < tasks.length; i++) {
            for (int i = 0; i < 6; i++) {
                System.out.println((i + 1) + ". Задание " + (i + 1));
            }

            System.out.println("0. Выход");

            int choice = 0;

            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Некорректный ввод.");
            }

            switch (choice) {
                case 1: new org.example.Task1.Main(); break;
                case 2: new Main(); break;
                case 3: new AirplaneSimulation(); break;
                case 4: new Account(true); break;
                case 5: new ReplaceLetters(); break;
                case 6: new org.example.Task6.AirplaneSimulation(); break;
                case 0: System.exit(0);
                default:
                    System.out.println("Выбранного пункта не существует.");
            }

//            if (choice >= 0 && choice <= tasks.length) {
//                if (choice == 0) {
//                    exit = true;
//                } else {
//                    tasks[choice - 1].runTask();
//                }
//            } else {
//                System.out.println("Некорректный выбор. Попробуйте снова.");
//            }
        }
    }
}
