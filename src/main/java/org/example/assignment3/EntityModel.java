/*
 * NAME: Sayed Farhaan Rafi Bhat
 * NSID: bcl568
 * Student Number: 11354916
 */

package org.example.assignment3;

import java.util.ArrayList;

public class EntityModel {

    private final ArrayList<Box> boxes;
    private final ArrayList<Subscriber> subs;

    /**
     * EntityModel Constructor
     */
    public EntityModel() {
        boxes = new ArrayList<>();
        subs = new ArrayList<>();
    }

    /**
     * Get the boxes
     * @return the boxes
     */
    public ArrayList<Box> getBoxes() {
        return this.boxes;
    }


    /**
     * Add a box to the model
     * @param x x-coordinate
     * @param y y-coordinate
     * @param width width
     * @param height height
     */
    public void addBox(double x, double y, double width, double height) {
        boxes.add(new Box(x, y, width, height));
        notifySubscribers();
    }


    /**
     * Add a portal to the model
     * @param x x-coordinate
     * @param y y-coordinate
     * @param width width
     * @param height height
     */
    public void addPortal(double x, double y, double width, double height) {
        boxes.add(new Portal(x, y, width, height));
        notifySubscribers();
    }


    /**
     * Delete a portal from the model
     * @param p the portal to delete
     */
    public void deletePortal(Portal p) {
        boxes.remove(p);
        notifySubscribers();
    }


    /**
     * Delete a box from the model
     * @param b the box to delete
     */
    public void deleteBox(Box b) {
        boxes.remove(b);
        notifySubscribers();
    }


    /**
     * Move a box by dX and dY
     * @param b the box to move
     * @param dX distance to move in x-direction
     * @param dY distance to move in y-direction
     */
    public void moveBox(Box b, double dX, double dY) {
        b.move(dX, dY);
        notifySubscribers();
    }


    /**
     * Check if any box contains the point (x, y)
     * @param x x-coordinate
     * @param y y-coordinate
     * @return true if any box contains the point, false otherwise
     */
    public boolean contains(double x, double y) {
        return boxes.stream().anyMatch(e -> e.contains(x,y));
    }


    /**
     * Get the box that contains the point (x, y)
     * @param x x-coordinate
     * @param y y-coordinate
     * @return the box that contains the point
     */
    public Box whichBox(double x, double y) {
        return boxes.stream()
                .filter(e -> e.contains(x, y))
                .findFirst()
                .orElse(null);
    }


    /**
     * Add a subscriber to the model
     * @return the subscriber
     */
    public void addSubscriber(Subscriber sub) {
        subs.add(sub);
    }


    /**
     * Notify all subscribers that the model has changed
     */
    public void notifySubscribers() {
        subs.forEach(Subscriber::modelChanged);
    }


}
