package org.example.assignment3;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Portal extends Box {

    private double scale;

    public Portal(double x, double y, double width, double height) {
        super(x, y, width, height);
        scale = 0.5;
    }



    public double getScale() { return scale; }


    public void setScale(double scale) { this.scale = scale; }

}
