package org.example.assignment3;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class AppController {

    private EntityModel model;
    private InteractionModel iModel;
    private ControllerState currentState;
    private double prevX, prevY, dX, dY, adjustedX, adjustedY;

    public abstract static class ControllerState {
        void handlePressed(MouseEvent event) {}
        void handleDragged(MouseEvent event) {}
        void handleReleased(MouseEvent event) {}
        void handleKeyPressed(KeyEvent event) {}
        void handleKeyReleased(KeyEvent event) {}
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

    public void handleKeyPressed(KeyEvent event) {
        currentState.handleKeyPressed(event);
    }

    public void handleKeyReleased(KeyEvent event) {
        currentState.handleKeyReleased(event);
    }

    ControllerState ready = new ControllerState() {

        @Override
        public void handlePressed(MouseEvent event) {
            prevX = event.getX();
            prevY = event.getY();

            adjustedX = event.getX() - iModel.getViewLeft();
            adjustedY = event.getY() - iModel.getViewTop();

            if (model.contains(adjustedX, adjustedY)) {
                iModel.setSelected(model.whichBox(adjustedX, adjustedY));
                model.notifySubscribers();
                currentState = dragging;
            }
            else {
                currentState = preparing;
            }
        }

        @Override
        public void handleKeyPressed(KeyEvent event) {
            switch (event.getCode()) {
                case DELETE:
                case BACK_SPACE:
                    if (iModel.getSelected() != null) {
                        model.deleteBox(iModel.getSelected());
                        iModel.setSelected(null);
                    }
                    break;
                case SHIFT:
                    currentState = panning;
                    break;
                default:
                    break;
            }
        }

    };

    ControllerState preparing = new ControllerState() {

        @Override
        public void handleDragged(MouseEvent event) {
            model.addBox(adjustedX, adjustedY, 0,0);
            iModel.setSelected(model.whichBox(adjustedX, adjustedY));
            currentState = creating;
        }

        @Override
        public void handleReleased(MouseEvent event) {
            iModel.setSelected(null);
            model.notifySubscribers();
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
            model.notifySubscribers();
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

    ControllerState panning = new ControllerState() {

        @Override
        public void handlePressed(MouseEvent event) {
            prevX = event.getX();
            prevY = event.getY();
        }

        @Override
        public void handleDragged(MouseEvent event) {
            dX = event.getX() - prevX;
            dY = event.getY() - prevY;
            prevX = event.getX();
            prevY = event.getY();
            iModel.moveViewport(dX, dY);
        }

        @Override
        public void handleKeyReleased(KeyEvent event) {
            if (event.getCode() == KeyCode.SHIFT) {
                currentState = ready;
            }
        }

    };



}
