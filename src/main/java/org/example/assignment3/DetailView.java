package org.example.assignment3;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class DetailView extends StackPane implements Subscriber{

    double width, height;
    GraphicsContext gc;
    EntityModel model;
    InteractionModel iModel;
    Canvas myCanvas;

    public DetailView() {
        width = 700;
        height = 500;
        myCanvas = new Canvas(width, height);
        gc = myCanvas.getGraphicsContext2D();

        this.widthProperty().addListener((observable, oldValue, newValue) -> {
           myCanvas.setWidth(newValue.doubleValue());
           draw();
        });

        this.heightProperty().addListener((observable, oldValue, newValue) -> {
            myCanvas.setHeight(newValue.doubleValue());
            draw();
        });

        Platform.runLater(() -> {
            myCanvas.requestFocus();
        });

        this.getChildren().add(myCanvas);
    }

    public void setModel(EntityModel m) {
        this.model = m;
    }

    public void setiModel(InteractionModel im) {
        this.iModel = im;
    }

    public void setupEvents(AppController controller) {
        setOnMousePressed(controller::handlePressed);
        setOnMouseDragged(controller::handleDragged);
        setOnMouseReleased(controller::handleReleased);
        myCanvas.setOnKeyPressed(controller::handleKeyPressed);
    }

    public void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        model.getBoxes().forEach(entity -> {
            if (iModel.getSelected() == entity) {
                gc.setFill(Color.ORANGE);
            }
            else {
                gc.setFill(Color.BLUE);
            }
            gc.fillRect(entity.getX(), entity.getY(),
                        entity.getWidth(), entity.getHeight());
            gc.strokeRect(entity.getX(), entity.getY(),
                    entity.getWidth(), entity.getHeight());
        });
    }

    public void modelChanged() {
        draw();
    }

}
