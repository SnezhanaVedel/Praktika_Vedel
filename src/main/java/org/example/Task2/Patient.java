package org.example.Task2;

public class Patient {
    int id;
    String surname;
    String name;
    String patronymic;
    String address;
    String phone_number;
    int medical_card_number;
    String diagnosis;

    Patient(int id, String surname, String name, String patronymic,
            String address, String phone_number, int medical_card_number, String diagnosis){
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.address = address;
        this.phone_number = phone_number;
        this.medical_card_number = medical_card_number;
        this.diagnosis = diagnosis;
    }

    public String getPatientInfo(){

        return "[id: " + id + "] [Фамилия: " + surname + "] [Имя: " + name + "] [Отчество: " + patronymic + "] [Адрес: " + address + "] [Номер телефона: " + phone_number
                + "] [Номер медицинской карты: " + medical_card_number + "] [Диагноз: " + diagnosis + "]";
    }
}