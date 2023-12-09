package lpz.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import lpz.bdclient.ClientPostgreSQL;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lpz.util.MyAlerts;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DialogAddRecordsController implements Initializable {
    public VBox inputVBox;
    public ChoiceBox<String> booksChoiceBox;
    public RadioButton anyGenderRB;
    public Button idBottomAdd;
    private ClientPostgreSQL clientPostgreSQL;
    private List<String> columnNames;
    private String selectedTable;
    public DialogAddRecordsController(String selectedTable) {
//        this.columnNames = columnNames;
        this.selectedTable = selectedTable;
        clientPostgreSQL = ClientPostgreSQL.getInstance();

    }


    public void onActionBottomAdd(ActionEvent actionEvent) {

        ObservableList<Node> list = inputVBox.getChildren();

        String sqlAdd = "INSERT INTO учет (название_книги, адрес_магазина, количество_книг) VALUES (";

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i) instanceof TextField) {

                if (i + 1 == list.size()) {

                    sqlAdd += "'" + ((TextField) list.get(i)).getText() + "');";
                    continue;
                }
                sqlAdd += "'" + ((TextField) list.get(i)).getText() + "',";
            }
        }

//        System.out.println(sqlAdd);
        ClientPostgreSQL.getInstance().simpleQuery(selectedTable, sqlAdd);
        MyAlerts.showInfoAlert("Запись добавлена успешно!");
        showTable();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ArrayList<String> resultList = clientPostgreSQL.stringListQuery("SELECT название FROM книги ORDER BY id", "название");
//        String[] resultSet = new String[resultList.size()];
//        resultSet = resultList.toArray(resultSet);
        booksChoiceBox.getItems().addAll(resultList);

        // Обработчик выбора препарата в ChoiceBox
//        booksChoiceBox.setOnAction(event -> calculateTotal());
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