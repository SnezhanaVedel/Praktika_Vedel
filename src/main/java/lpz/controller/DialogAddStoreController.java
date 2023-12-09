package lpz.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lpz.bdclient.ClientPostgreSQL;
import lpz.util.MyAlerts;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DialogAddStoreController implements Initializable {
    public VBox inputVBox;
    public RadioButton anyGenderRB;
    public Button idBottomAdd;
    private List<String> columnNames;
    private String selectedTable;
    public DialogAddStoreController(List<String> columnNames, String selectedTable) {
        this.columnNames = columnNames;
        this.selectedTable = selectedTable;
    }


    // метод, вызываемый при нажатии на кнопку добавления
    public void onActionBottomAdd(ActionEvent actionEvent) {

        // Создаём список и помещаем в него все компоненты,
        // находящиеся в inputVBox (TextField, HBox с радиокнопками)
        ObservableList<Node> list = inputVBox.getChildren();


        // Создаём строку для формирования запроса; указываем, что хотим добавить
        // строку в таблицу 'обувь', указываем набор и порядок полей таблицы,
        // которые для этого будут задействованы
        String sqlAdd = "INSERT INTO магазины (адрес, телефон, электронная_почта, рабочие_часы) VALUES (";


        // ОБЩИЙ ФОРМАТ запроса выглядит следующим образом:
        // INSERT INTO название_таблицы (поле1, поле2) VALUES ('значения_для_поля1', 'значение_для_поля2');


        // Здесь начинаем задавать конкретные значения.
        // Данный цикл for начинает перебирать все элементы,
        // содержащиеся в списке элементов из VBox.
        for (int i = 0; i < list.size(); i++) {

            // если элемент является текстовым полем (TextField)
            if (list.get(i) instanceof TextField) {

                // проверяем, является ли элемент последним из списка,
                // и в зависимости от результата дописываем после значения
                // либо ');
                // либо ',
                // чтобы в результате запрос соответствовал ОБЩЕМУ ФОРМАТУ (см. выше)
                if (i + 1 == list.size()) {

                    sqlAdd += "'" + ((TextField) list.get(i)).getText() + "');";
                    continue; // переход на следующую итерацию цикла (строки, описанные после continue, игнорируются)

                    // т.к. continue выполняется только при условии, что мы рассматриваем последний
                    // элемент из списка, и, следовательно, находимся на последней итерации цикла,
                    // то, по сути, в данном случае, посредством continue мы просто выходим из цикла
                }
                sqlAdd += "'" + ((TextField) list.get(i)).getText() + "',";


            }
        }

        // отладочная строка; не несёт практического смысла и пользы, существует
        // просто для того, чтобы увидеть в консоли, как в конечном итоге выглядит запрос
//        System.out.println(sqlAdd);

        // выполняем SQL запрос посредством соответствующего метода из класса,
        // отвечающего за соединение с БД
        ClientPostgreSQL.getInstance().simpleQuery(selectedTable, sqlAdd);

        // после выполнения запроса, выводим окно с таблицей (BD.fxml)
        MyAlerts.showInfoAlert("Запись добавлена успешно!");
        showTable();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        // при инициализации окна, делаем одну из радиокнопок выбранной по умолчанию,
        // чтобы нельзя было оставить пол без выбора
//        anyGenderRB.setSelected(true);
    }


    private void showTable() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BD.fxml"));
            BDController dialogAddController = new BDController(selectedTable);
            loader.setController(dialogAddController);
            Stage stage = new Stage();
            stage.setTitle("Таблица");
            stage.setScene(new Scene(loader.load()));
            stage.show();
            ((Stage) idBottomAdd.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onCancelBtn(ActionEvent actionEvent) {
        showTable();
    }

}