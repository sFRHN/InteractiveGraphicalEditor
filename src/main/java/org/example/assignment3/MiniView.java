package org.example.assignment3;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class MiniView extends DetailView {

    private double size = 200;
    private final GraphicsContext gc;
    private final Canvas myCanvas;
    private double scale;

    public MiniView() {
        super();
        myCanvas = new Canvas(size, size);
        gc = myCanvas.getGraphicsContext2D();
        this.setMinSize(size, size);
        this.setMaxSize(size, size);
        StackPane.setAlignment(this, Pos.TOP_LEFT);
        this.getChildren().add(myCanvas);
        opacityProperty().set(0.5);
    }

    public void setupEvents(MiniController controller) {
        myCanvas.setOnMousePressed(e -> {
            controller.setScale(scale);
            controller.handlePressed(e);
        });
        myCanvas.setOnMouseDragged(e-> {
            controller.setScale(scale);
            controller.handleDragged(e);
        });
        myCanvas.setOnMouseReleased(e -> {
            controller.setScale(scale);
            controller.handleReleased(e);
        });
        setOnKeyPressed(controller::handleKeyPressed);
        setOnKeyReleased(controller::handleKeyReleased);
    }

    @Override
    public void draw() {
        scale = this.size / iModel.getWorldSize();

        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());

        gc.save();
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        gc.strokeRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());

        // Apply scaling transformation
        gc.scale(scale, scale);

        // Draw the visible area rectangle
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0 / scale);
        gc.strokeRect(
                iModel.getViewLeft(),
                iModel.getViewTop(),
                iModel.getViewWidth(),
                iModel.getViewHeight()
        );
        gc.setFill(Color.YELLOW);
        gc.fillRect(
                iModel.getViewLeft(),
                iModel.getViewTop(),
                iModel.getViewWidth(),
                iModel.getViewHeight()
        );

        model.getBoxes().forEach(entity -> {

            if (entity instanceof Portal portal) {
                drawPortal(portal);
            }
            else {
                drawBox(entity);
            }

            if (iModel.getSelected() == entity) {
                drawHandles(entity);
            }

        });

        gc.restore();
    }


    private void drawBox(Box entity) {

        if (iModel.getSelected() == entity) {
            gc.setFill(Color.ORANGE);
        } else {
            gc.setFill(Color.BLUE);
        }
        gc.fillRect(entity.getX(), entity.getY(),
                entity.getWidth(), entity.getHeight());
        gc.strokeRect(entity.getX(), entity.getY(),
                entity.getWidth(), entity.getHeight());

    }

    private void drawPortal(Portal portal) {

        gc.save();
        gc.beginPath();
        gc.strokeRect(portal.getX(), portal.getY(), portal.getWidth(), portal.getHeight());
        gc.rect(portal.getX(), portal.getY(), portal.getWidth(), portal.getHeight());
        gc.clip();
        gc.translate(portal.getPLeft(), portal.getPTop());
        gc.scale(portal.getScale(), portal.getScale());
        model.getBoxes().forEach(entity -> {
            if (!(entity instanceof Portal)) {
                drawBox(entity);
            }
        });
        gc.restore();

    }


    private void drawHandles(Box entity) {
        gc.setFill(Color.WHITE);
        double circleRadius = iModel.getHandleRadius();
        gc.strokeOval(entity.getX() - circleRadius, entity.getY() - circleRadius, 2 * circleRadius, 2 * circleRadius);
        gc.strokeOval(entity.getX() + entity.getWidth() - circleRadius, entity.getY() - circleRadius, 2 * circleRadius, 2 * circleRadius);
        gc.strokeOval(entity.getX() - circleRadius, entity.getY() + entity.getHeight() - circleRadius, 2 * circleRadius, 2 * circleRadius);
        gc.strokeOval(entity.getX() + entity.getWidth() - circleRadius, entity.getY() + entity.getHeight() - circleRadius, 2 * circleRadius, 2 * circleRadius);
        gc.fillOval(entity.getX() - circleRadius, entity.getY() - circleRadius, 2 * circleRadius, 2 * circleRadius);
        gc.fillOval(entity.getX() + entity.getWidth() - circleRadius, entity.getY() - circleRadius, 2 * circleRadius, 2 * circleRadius);
        gc.fillOval(entity.getX() - circleRadius, entity.getY() + entity.getHeight() - circleRadius, 2 * circleRadius, 2 * circleRadius);
        gc.fillOval(entity.getX() + entity.getWidth() - circleRadius, entity.getY() + entity.getHeight() - circleRadius, 2 * circleRadius, 2 * circleRadius);

    }



    public void modelChanged() {
        draw();
    }

}
