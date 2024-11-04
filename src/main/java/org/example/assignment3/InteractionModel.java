package org.example.assignment3;

public class InteractionModel {

    Box selected;

    public InteractionModel() {
        selected = null;
    }

    public void setSelected(Box b) {
        this.selected = b;
    }

    public Box getSelected() {
        return this.selected;
    }

}
