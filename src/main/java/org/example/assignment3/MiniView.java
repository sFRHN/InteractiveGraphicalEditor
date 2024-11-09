package org.example.assignment3;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MiniView extends DetailView {

    private double size = 200;
    private final GraphicsContext gc;
    private final Canvas myCanvas;


    public MiniView() {
//        super();
        myCanvas = new Canvas(size, size);
        gc = myCanvas.getGraphicsContext2D();
        this.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        this.setPrefSize(size, size);
        this.getChildren().add(myCanvas);
        opacityProperty().set(0.5);
    }

    @Override
    public void draw() {
        double scale = size / iModel.getWorldSize();

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
                -iModel.getViewLeft(),
                -iModel.getViewTop(),
                ViewWidth(),
                ViewHeight()
        );
        gc.setFill(Color.YELLOW);
        gc.fillRect(
                -iModel.getViewLeft(),
                -iModel.getViewTop(),
                ViewWidth(),
                ViewHeight()
        );

        model.getBoxes().forEach(entity -> {
            if (iModel.getSelected() == entity) {
                gc.setFill(Color.ORANGE);
            } else {
                gc.setFill(Color.BLUE);
            }
            gc.fillRect(entity.getX(), entity.getY(),
                    entity.getWidth(), entity.getHeight());
            gc.strokeRect(entity.getX(), entity.getY(),
                    entity.getWidth(), entity.getHeight());
        });

        gc.restore();
    }

    public void modelChanged() {
        draw();
    }

}
