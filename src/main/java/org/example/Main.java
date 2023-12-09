package org.example;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.Task7.ClockApp;
import org.example.Task8.MenuExample;
//import org.example.Task8.NeatMenu;
//import org.example.Task8.TNeatMenu;
//import org.example.Task8.VerticalMenu;
import org.example.Task9.MatematikoGame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {
    private final Map<String, String> classPaths = new HashMap<>();
    private ArrayList<Button> btnList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Задания: 7-9");
        VBox root = new VBox(10);

        btnList = new ArrayList<>();

        btnList.add(new Button("Задание 7"));
        btnList.get(0).setOnAction(event -> ClockApp.launchClockApp());


        btnList.add(new Button("Задание 8"));
        btnList.get(1).setOnAction(event -> MenuExample.launchMenuExample());

        btnList.add(new Button("Задание 9"));
        btnList.get(2).setOnAction(event -> MatematikoGame.launchMatematikoGame());

        for (Button button : btnList) {
            root.getChildren().add(button);
            root.setAlignment(Pos.CENTER );
            root.setStyle("-fx-font-size: 20;");
            root.setMinSize(100, 60);
        }

//        TNeatMenu menu = new VerticalMenu(new String[]{"Option 1", "Option 2", "Option 3"});
//
//        ObservableList<String> observableList = FXCollections.observableArrayList(menu.getItems());
//        ListView<String> listView = new ListView<>(observableList);
//
//        listView.setOnMouseClicked(event -> {
//            String selectedItem = listView.getSelectionModel().getSelectedItem();
//            System.out.println("Выбрано: " + selectedItem);
//        });
//
//        VBox root = new VBox(listView);
        Scene scene = new Scene(root, 300, 300);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

}
