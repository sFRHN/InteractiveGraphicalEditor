package org.example.assignment3;

import java.util.ArrayList;

public class EntityModel {

    private final ArrayList<Box> boxes;
    private final ArrayList<Subscriber> subs;

    public EntityModel() {
        boxes = new ArrayList<>();
        subs = new ArrayList<>();
    }

    public ArrayList<Box> getBoxes() {
        return this.boxes;
    }

    public void addBox(double x, double y, double width, double height) {
        boxes.add(new Box(x, y, width, height));
        notifySubscribers();
    }

    public void deleteBox(Box b) {
        boxes.remove(b);
        notifySubscribers();
    }

    public void moveBox(Box b, double dX, double dY) {
        b.move(dX, dY);
        notifySubscribers();
    }

    public boolean contains(double x, double y) {
        return boxes.stream().anyMatch(e -> e.contains(x,y));
    }

    public Box whichBox(double x, double y) {
        return boxes.stream()
                .filter(e -> e.contains(x, y))
                .findFirst()
                .orElse(null);
    }

    public void addSubscriber(Subscriber sub) {
        subs.add(sub);
    }

    public void notifySubscribers() {
        subs.forEach(Subscriber::modelChanged);
    }


}
