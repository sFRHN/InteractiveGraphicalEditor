package org.example.assignment3;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class DetailView extends StackPane implements Subscriber{

    private double width, height;
    private final GraphicsContext gc;
    protected EntityModel model;
    protected InteractionModel iModel;
    private final Canvas myCanvas;

    public DetailView() {
        width = 800;
        height = 800;
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
        setOnKeyPressed(controller::handleKeyPressed);
        setOnKeyReleased(controller::handleKeyReleased);
    }

    public void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        gc.save();
        gc.translate(iModel.getViewLeft(), iModel.getViewTop());
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
        gc.restore();
    }

    public double ViewWidth() {
        return width;
    }

    public double ViewHeight() {
        return height;
    }

    public void modelChanged() {
        draw();
    }

}
