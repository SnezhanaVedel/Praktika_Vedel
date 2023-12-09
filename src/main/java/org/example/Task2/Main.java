package org.example.Task2;
import org.example.Task;
import java.util.ArrayList;
import java.util.Scanner;
public class Main implements Task {
    public Main(){
        patientList = new ArrayList<>();
        addPatients();

        Scanner scanner = new Scanner(System.in);

        for (Patient patient : patientList) {
            System.out.println(patient.getPatientInfo());
        }

        System.out.print("\na) список пациентов, имеющих данный диагноз: ");

        String diagnosis = scanner.nextLine();

        for (Patient patient : patientList) {
            if(patient.diagnosis.equals(diagnosis)){
                System.out.println(patient.getPatientInfo());
            }
        }

        System.out.print("\nb) список пациентов, номер медицинской карты которых находится в заданном интервале: ");

        int intBegin = scanner.nextInt();
        int intEnd = scanner.nextInt();

        for (Patient patient : patientList) {
            if(patient.medical_card_number <= intEnd && patient.medical_card_number >= intBegin){
                System.out.println(patient.getPatientInfo());
            }
        }
    }
    private static ArrayList<Patient> patientList;
    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void runTask() {
        new Main();
    }

    static void addPatients(){
        patientList.add(new Patient(1, "Селезнев", "Никита", "Германович", "Западная ул., д. 20 кв.116", "88002874068", 865, "Онкология"));
        patientList.add(new Patient(2, "Иванов", "Иван", "Иванович", "Центральная ул., д. 15 кв.205", "88005553535", 876, "Грипп"));
        patientList.add(new Patient(3, "Петрова", "Мария", "Александровна", "Северная ул., д. 10 кв.301", "88001234567", 987, "Ангина"));
        patientList.add(new Patient(4, "Смирнов", "Алексей", "Петрович", "Восточная ул., д. 5 кв.102", "88009998877", 754, "Гастрит"));
        patientList.add(new Patient(5, "Козлов", "Егор", "Сергеевич", "Южная ул., д. 25 кв.401", "88006667788", 632, "Онкология"));
        patientList.add(new Patient(6, "Михайлов", "Артем", "Анатольевич", "Западная ул., д. 30 кв.210", "88001112233", 521, "Диабет"));
        patientList.add(new Patient(7, "Васнецова", "Ольга", "Владимировна", "Центральная ул., д. 12 кв.115", "88004449999", 489, "Грипп"));
        patientList.add(new Patient(8, "Лебедев", "Денис", "Дмитриевич", "Северная ул., д. 8 кв.303", "88007776666", 753, "Пневмония"));
        patientList.add(new Patient(9, "Зайцев", "Кирилл", "Игоревич", "Восточная ул., д. 3 кв.205", "88005557777", 862, "Эпилепсия"));
        patientList.add(new Patient(10, "Кузнецова", "Анна", "Алексеевна", "Южная ул., д. 22 кв.401", "88006665544", 924, "Онкология"));
        patientList.add(new Patient(11, "Соловьев", "Максим", "Артемович", "Западная ул., д. 25 кв.116", "88002871111", 633, "Артрит"));
        patientList.add(new Patient(12, "Трофимов", "Владислав", "Егорович", "Центральная ул., д. 17 кв.202", "88002223344", 789, "Мигрень"));
        patientList.add(new Patient(13, "Федоров", "Дмитрий", "Сергеевич", "Северная ул., д. 14 кв.301", "88001112233", 654, "Грипп"));
        patientList.add(new Patient(14, "Калинина", "Елена", "Игоревна", "Восточная ул., д. 6 кв.105", "88007776666", 977, "Эпилепсия"));
        patientList.add(new Patient(15, "Григорьев", "Илья", "Владимирович", "Южная ул., д. 18 кв.410", "88004448888", 741, "Гипертония"));

    }
}