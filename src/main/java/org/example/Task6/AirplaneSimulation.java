package org.example.Task6;
import org.example.Task;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AirplaneSimulation implements Task {
    public AirplaneSimulation(){
        Scanner scanner = new Scanner(System.in);

        Wing wing = new Wing(30.5, 5.0);
        Chassis chassis = new Chassis(3, 2.0);
        Engine engine = new Engine(5000, 20);

        Airplane airplane = new Airplane(wing, chassis, engine, scanner);

        airplane.assembleAirplane();
        airplane.startEngine();
        airplane.openChassis();

        System.out.print("Введите количество топлива для заправки: ");
        boolean inputSuccessful = false;

        do {
            try {
                double fuelAmount = scanner.nextDouble();
                if (fuelAmount < 0) {
                    throw new IllegalArgumentException("Количество топлива не может быть отрицательным.");
                }
                airplane.refuel();
                inputSuccessful = true;
            } catch (InputMismatchException ex) {
                System.out.println("Ошибка ввода топлива. Попробуйте снова.");
                scanner.next();
                System.out.print("Введите количество топлива для заправки: ");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Попробуйте снова.");
            } catch (Exception e) {
                System.out.println("Неожиданная ошибка. Попробуйте снова.");
            }
        } while (!inputSuccessful);

        System.out.println("Введите маршрут полета (В формате Город1-Город2): ");
        String route;

        do {
            try {
                route = scanner.next();
                System.out.println(route);
                if (isValidRouteFormat1(route)) {
                    airplane.describeRoute(route);
                    inputSuccessful = true;
                } else {
                    System.out.println("Неверный формат маршрута. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!inputSuccessful);

        airplane.takeOff();
        airplane.land();
        engine.stop();

    }
    public static void main(String[] args) {
        new AirplaneSimulation();
    }

    @Override
    public void runTask() {
        new AirplaneSimulation();
    }

    private static boolean isValidRouteFormat(String route) {
        String regex = "^[А-Яа-я\\s-]+$";
        return route.matches(regex);
    }
    private static boolean isValidRouteFormat1(String route) {
        return route.trim().matches(".*-.*");
    }
}