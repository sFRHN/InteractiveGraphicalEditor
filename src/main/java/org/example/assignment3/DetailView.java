package org.example.assignment3;

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
    private int MAX_DEPTH = 3;


    /**
     * DetailView Constructor
     */
    public DetailView() {
        width = 800;
        height = 800;
        myCanvas = new Canvas(width, height);
        gc = myCanvas.getGraphicsContext2D();

        // Add listeners to the width and height properties of the DetailView
        this.widthProperty().addListener((observable, oldValue, newValue) -> {
            this.width = newValue.doubleValue();
            myCanvas.setWidth(this.width);
            iModel.setViewWidth(this.width);
            draw();
        });

        this.heightProperty().addListener((observable, oldValue, newValue) -> {
            this.height = newValue.doubleValue();
            myCanvas.setHeight(this.height);
            iModel.setViewHeight(this.height);
            draw();
        });

        this.setMinSize(0,0);
        this.getChildren().add(myCanvas);
    }


    /**
     * Setup the events for the DetailView
     * @param controller the AppController
     */
    public void setupEvents(AppController controller) {
        myCanvas.setOnMousePressed(controller::handlePressed);
        myCanvas.setOnMouseDragged(controller::handleDragged);
        myCanvas.setOnMouseReleased(controller::handleReleased);
        setOnKeyPressed(controller::handleKeyPressed);
        setOnKeyReleased(controller::handleKeyReleased);
    }


    /**
     * Draw the boxes and portals on the canvas of the DetailView
     */
    public void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        gc.save();
        gc.translate(-iModel.getViewLeft(), -iModel.getViewTop());
        model.getBoxes().forEach(entity -> {

            if (entity instanceof Portal portal) {
                drawPortal(portal, 0);
            }
            else {
                drawBox(entity);
            }

        });
        gc.restore();
    }


    /**
     * Draw the box on the canvas
     * @param entity the box to draw
     */
    private void drawBox(Box entity) {

        if (iModel.getSelected() == entity) {
            gc.setFill(Color.ORANGE);
        }
        else {
            gc.setFill(Color.BLUE);
        }
        gc.fillRect(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight());
        gc.strokeRect(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight());

        if (iModel.getSelected() == entity) {
            drawHandles(entity);
        }


    }


    /**
     * Draw the portal on the canvas
     * @param portal the portal to draw
     * @param depth the depth of the portal
     */
    private void drawPortal(Portal portal, double depth) {

        if (depth > MAX_DEPTH) return;

        gc.save();
        gc.beginPath();
        gc.strokeRect(portal.getX(), portal.getY(), portal.getWidth(), portal.getHeight());
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(portal.getX(), portal.getY(), portal.getWidth(), portal.getHeight());

        if (iModel.getSelected() == portal) {
            drawHandles(portal);
        }

        gc.rect(portal.getX(), portal.getY(), portal.getWidth(), portal.getHeight());
        gc.clip();
        gc.translate(portal.getX() + portal.getPLeft(), portal.getY() + portal.getPTop());
        gc.scale(portal.getScale(), portal.getScale());
        model.getBoxes().forEach(entity -> {
            if (entity instanceof Portal) {
                drawPortal((Portal)entity, depth + 1);
            }
            else if (depth < MAX_DEPTH) {
                drawBox(entity);
            }
        });
        gc.restore();

    }


    /**
     * Draw the handles on the selected box
     * @param entity the entity to draw the handles for
     */
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

    public Canvas getCanvas() {return myCanvas; }
    public double ViewWidth() { return width; }
    public double ViewHeight() { return height; }
    public void setViewWidth(double w) { width = w; }
    public void setViewHeight(double h) { height = h; }
    public void setModel(EntityModel m) { this.model = m; }
    public void setiModel(InteractionModel im) { this.iModel = im; }

    public void modelChanged() {
        draw();
    }

}
