package org.example.assignment3;

import javafx.scene.input.MouseEvent;

public class AppController {

    private EntityModel model;
    private InteractionModel iModel;
    private ControllerState currentState;
    private double prevX, prevY, dX, dY;

    public abstract static class ControllerState {
        void handlePressed(MouseEvent event) {}
        void handleDragged(MouseEvent event) {}
        void handleReleased(MouseEvent event) {}
    }

    public AppController() {
        currentState = ready;
    }

    public void setModel(EntityModel model) {
        this.model = model;
    }

    public void setiModel(InteractionModel iModel) {
        this.iModel = iModel;
    }

    public void handlePressed(MouseEvent event) {
        currentState.handlePressed(event);
    }

    public void handleDragged(MouseEvent event) {
        currentState.handleDragged(event);
    }

    public void handleReleased(MouseEvent event) {
        currentState.handleReleased(event);
    }

    ControllerState ready = new ControllerState() {

        @Override
        public void handlePressed(MouseEvent event) {
            prevX = event.getX();
            prevY = event.getY();

            if (model.contains(event.getX(), event.getY())) {
                iModel.setSelected(model.whichBox(event.getX(), event.getY()));
                currentState = dragging;
            }
            else {
                currentState = preparing;
            }
        }

    };

    ControllerState preparing = new ControllerState() {

        @Override
        public void handleDragged(MouseEvent event) {
            model.addBox(event.getX(), event.getY(), 0,0);
            iModel.setSelected(model.whichBox(event.getX(), event.getY()));
            currentState = creating;
        }

        @Override
        public void handleReleased(MouseEvent event) {
            iModel.setSelected(null);
            currentState = ready;
        }

    };

    ControllerState creating = new ControllerState() {

        @Override
        public void handleDragged(MouseEvent event) {
            dX = event.getX() - prevX;
            dY = event.getY() - prevY;
            prevX = event.getX();
            prevY = event.getY();
            iModel.getSelected().changeSize(dX, dY);
        }

        @Override
        public void handleReleased(MouseEvent event) {
            currentState = ready;
        }

    };

    ControllerState dragging = new ControllerState() {

        public void handleDragged(MouseEvent event) {
            dX = event.getX() - prevX;
            dY = event.getY() - prevY;
            prevX = event.getX();
            prevY = event.getY();
            model.moveBox(iModel.getSelected(), dX, dY);
        }

        public void handleReleased(MouseEvent event) {
            currentState = ready;
        }

    };



}
