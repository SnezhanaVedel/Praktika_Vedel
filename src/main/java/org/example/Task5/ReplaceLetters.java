package org.example.Task5;
import org.example.Task;
import java.util.Scanner;

/*
Работ выполнял задачи на фабрике.Кораль сидел на троне.Раждество — праздник.Раза расцвела в саду.Радник в горах.Грам прогремел.Красс был испытанием для участников.

Работ Кораль Раждество Раза Радник Грам Красс
 */

public class ReplaceLetters implements Task {
    public ReplaceLetters(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите текст с ошибкой:");
        String originalText = scanner.nextLine();

        String correctedText = replaceIncorrectLetter(originalText);
        System.out.println("Исправленный текст:");
        System.out.println(correctedText);
    }
    public static void main(String[] args) {
        new ReplaceLetters();
    }

    @Override
    public void runTask() {
        new ReplaceLetters();
    }

    public static String replaceIncorrectLetter(String text) {
        char[] charArray = text.toCharArray();

        for (int i = 0; i < charArray.length - 1; i++) {
            if (charArray[i] == 'Р' || charArray[i] == 'р') {
                if (charArray[i + 1] == 'А' || charArray[i + 1] == 'а') {
                    charArray[i + 1] = 'О';
                }
            }
        }
        return new String(charArray);
    }
}