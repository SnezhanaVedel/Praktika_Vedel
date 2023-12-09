package org.example.Task3;
import java.util.Scanner;
public class Airplane {
    private Wing wing;
    private Chassis chassis;
    private Engine engine;
    private boolean engineRunning;
    private boolean chassisOpen;
    private boolean isRefueled;
    private Scanner scanner;

    public Airplane(Wing wing, Chassis chassis, Engine engine, Scanner scanner) {
        this.wing = wing;
        this.chassis = chassis;
        this.engine = engine;
        this.engineRunning = false;
        this.chassisOpen = false;
        this.isRefueled = false;
        this.scanner = scanner;
    }

    public void assembleAirplane() {
        askUserAction("Собрать самолет? (Y/N)", () -> System.out.println("Самолет собран."), () -> {System.out.println("Самолет не собран."); assembleAirplane();});
    }

    public void describeRoute(String route) {
        System.out.println("Маршрут полета: " + route);
    }

    public void startEngine() {
        askUserAction("Запустить двигатель? (Y/N)", () -> {
            engine.start();
            engineRunning = true;
            System.out.println("Двигатель запущен.");
        }, () -> System.out.println("Двигатель не запущен."));
    }

    public void openChassis() {
        askUserAction("Открыть шасси? (Y/N)", () -> {
            chassisOpen = true;
            System.out.println("Шасси открыто.");
        }, () -> System.out.println("Шасси не открыто."));
    }

    public void refuel() {
        askUserAction("Заправить топливо? (Y/N)", () -> {System.out.println("Топливо заправлено.");isRefueled = true;}, () -> System.out.println("Топливо не заправлено."));
    }

    public void takeOff() {
        if (engineRunning && chassisOpen) {
            System.out.println("Самолет взлетает.");
        } else {
            System.out.println("Невозможно взлететь. Проверьте двигатель и шасси.");

            if (!engineRunning) {
                startEngine();
            }
            if (!chassisOpen) {
                openChassis();
            }
            if (!isRefueled){
                refuel();
            }
            takeOff();
        }
    }

    public void land() {
        askUserAction("Приземлиться? (Y/N)", () -> System.out.println("Самолет приземляется."), () -> System.out.println("Самолет не приземлился."));
    }

    private void askUserAction(String prompt, Runnable positiveAction, Runnable negativeAction) {
        System.out.print(prompt + " ");
        String response = scanner.next().toLowerCase();
        if (response.equals("y")) {
            positiveAction.run();
        } else {
            negativeAction.run();
        }
    }
}

class Wing {
    private double length;
    private double width;

    public Wing(double length, double width) {
        this.length = length;
        this.width = width;
    }

    public void checkWingState() {
    }
}

class Chassis {
    private int numberOfWheels;
    private double height;

    public Chassis(int numberOfWheels, double height) {
        this.numberOfWheels = numberOfWheels;
        this.height = height;
    }

    public void checkChassisState() {
    }
}

class Engine {
    private double power;
    private double fuelConsumption;
    private boolean running;
    public Engine(double power, double fuelConsumption) {
        this.power = power;
        this.fuelConsumption = fuelConsumption;
        this.running = false;
    }
    public void start() {
        running = true;
    }
    public void stop() {
        running = false;
    }

    public void checkEngineState() {
    }
}