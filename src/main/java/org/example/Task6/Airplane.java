package org.example.Task6;
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

boolean assembled = false;
    public void assembleAirplane() {
        while (!assembled) {
            askUserAction("Собрать самолет? (Y/N)", () -> {
                System.out.println("Самолет собран.");
                assembled = true;
            }, () -> {
                throw new AirplaneNotAssembledException();
            });
        }
    }

    public void describeRoute(String route) {
        System.out.println("Маршрут полета: " + route);
    }

    public void startEngine() {
        askUserAction("Запустить двигатель? (Y/N)", () -> {
            if (engineRunning) {
                throw new EngineAlreadyRunningException();
            }
            engine.start();
            engineRunning = true;
            System.out.println("Двигатель запущен.");
        }, () -> {
        });
    }

    public void openChassis() {
        askUserAction("Открыть шасси? (Y/N)", () -> {
            chassisOpen = true;
            System.out.println("Шасси открыто.");
        }, () -> {
        });
    }

    public void refuel() {
        askUserAction("Заправить топливо? (Y/N)", () -> {
            if (isRefueled) {
                throw new IllegalStateException("Топливо уже заправлено.");
            }
            System.out.println("Топливо заправлено.");
            isRefueled = true;
        }, () -> {
        });
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
        boolean userInputValid = false;

        do {
            try {
                System.out.print(prompt + " ");
                String response = scanner.next().toLowerCase();

                if (response.equals("y")) {
                    positiveAction.run();
                    userInputValid = true;
                } else if (response.equals("n")) {
                    negativeAction.run();
                    userInputValid = true;
                } else {
                    System.out.println("Неверный ввод. Пожалуйста, введите 'Y' или 'N'.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка ввода: " + e.getMessage());
            }
        } while (!userInputValid);
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
        try {
            if (running) {
                throw new IllegalStateException("Двигатель уже запущен.");
            }
            running = true;
        } catch (Exception e) {
            System.out.println("Ошибка при запуске двигателя: " + e.getMessage());
        }
    }

    public void stop() {
        try {
            if (!running) {
                throw new IllegalStateException("Двигатель уже остановлен.");
            }
            running = false;
        } catch (Exception e) {
            System.out.println("Ошибка при остановке двигателя: " + e.getMessage());
        }
    }

    public void checkEngineState() {
    }
}

    class AirplaneException extends RuntimeException {
        public AirplaneException(String message) {
            super(message);
        }
    }

    class AirplaneNotAssembledException extends AirplaneException {
        public AirplaneNotAssembledException() {
            super("Самолет не собран.");
        }
    }

    class EngineAlreadyRunningException extends AirplaneException {
        public EngineAlreadyRunningException() {
            super("Двигатель уже запущен.");
        }
    }

    class EngineNotRunningException extends AirplaneException {
        public EngineNotRunningException() {
            super("Двигатель не запущен.");
        }
    }

    class ChassisNotOpenException extends AirplaneException {
        public ChassisNotOpenException() {
            super("Шасси не открыто.");
        }
    }

    class NotEnoughFuelException extends AirplaneException {
        public NotEnoughFuelException() {
            super("Топливо не заправлено.");
        }
    }