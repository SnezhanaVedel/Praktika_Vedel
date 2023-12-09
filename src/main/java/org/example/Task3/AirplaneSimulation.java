package org.example.Task3;
import org.example.Task;
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
        double fuelAmount = scanner.nextDouble();
        airplane.refuel();

        System.out.println("Введите маршрут полета: ");
        String route = scanner.next();

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
}