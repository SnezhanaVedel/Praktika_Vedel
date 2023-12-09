package org.example.Task8;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.*;

public class MenuExample extends Application {

    public static void launchMenuExample() {
        Platform.runLater(() -> {
            new MenuExample().start(new Stage());
        });
    }

    @Override
    public void start(Stage primaryStage) {
        TNeatMenu menu = new VerticalMenu(new String[]{"Option 1", "Option 2", "Option 3"});
        ObservableList<String> observableList = FXCollections.observableArrayList(menu.getItems());
        ListView<String> listView = new ListView<>(observableList);
        
        listView.setOnMouseClicked(event -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            if(selectedItem.equals("Option 1")){
                JOptionPane.showMessageDialog(null, "Привет!");
            } else if (selectedItem.equals("Option 2")) {
                JOptionPane.showMessageDialog(null, "Как дела?)");
            }else if (selectedItem.equals("Option 3")) {
                JOptionPane.showMessageDialog(null, "Пока!");
            }
            System.out.println("Выбрано: " + selectedItem);
        });

        VBox root = new VBox(listView);

        Scene scene = new Scene(root, 200, 150);

        primaryStage.setTitle("");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}