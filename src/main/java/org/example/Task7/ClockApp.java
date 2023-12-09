package org.example.Task7;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ClockApp extends Application {

    public static void launchClockApp() {
        Platform.runLater(() -> {
            new ClockApp().start(new Stage());
        });
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("");

        Canvas canvas = new Canvas(300, 300);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.WHITE);
        gc.fillOval(50, 50, 200, 200);

        gc.setFill(Color.BLACK);
        for (int i = 1; i <= 12; i++) {
            double angle = Math.toRadians(90 - i * 30);
            double x = 150 + 80 * Math.cos(angle);
            double y = 150 - 80 * Math.sin(angle);
            gc.fillText(Integer.toString(i), x, y);
        }

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);

        drawHand(gc, 155, 145, 155, 90);
        drawArrowhead(gc, 155, 80, Math.toRadians(90));

        drawHand(gc, 155, 145, 210, 145);
        drawArrowhead(gc, 210, 140, Math.toRadians(120));

        Scene scene = new Scene(new javafx.scene.layout.StackPane(canvas), 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void drawHand(GraphicsContext gc, double startX, double startY, double endX, double endY) {
        gc.strokeLine(startX, startY, endX, endY);
    }

    private void drawArrowhead(GraphicsContext gc, double x, double y, double angle) {
        double arrowSize = 10;
        gc.setFill(Color.BLACK);
        gc.fillPolygon(new double[]{x, x - arrowSize * Math.cos(angle - Math.toRadians(30)), x - arrowSize * Math.cos(angle + Math.toRadians(30))},
                new double[]{y, y + arrowSize * Math.sin(angle - Math.toRadians(30)), y + arrowSize * Math.sin(angle + Math.toRadians(30))}, 3);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
