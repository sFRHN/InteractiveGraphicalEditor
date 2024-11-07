package org.example.assignment3;

import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class InteractionModel {

    private Box selected;
    private ArrayList<Subscriber> subs;
    private final int worldSize = 2000;
    private double viewLeft = 0;
    private double viewTop = 0;

    public InteractionModel() {
        selected = null;
        subs = new ArrayList<>();
    }

    public Box getSelected() { return this.selected; }
    public int getWorldSize() { return this.worldSize; }
    public double getViewLeft() { return this.viewLeft; }
    public double getViewTop() { return this.viewTop; }

    public void setSelected(Box b) {
        this.selected = b;
    }

    public void addSubscriber(Subscriber sub) {
        subs.add(sub);
    }

    private void notifySubscribers() {
        subs.forEach(Subscriber::modelChanged);
    }

    public void moveViewport(double dX, double dY) {
        viewLeft += dX;
        viewTop += dY;
        notifySubscribers();
    }

}
