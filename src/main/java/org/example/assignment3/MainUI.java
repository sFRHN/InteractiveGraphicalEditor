/*
 * NAME: Sayed Farhaan Rafi Bhat
 * NSID: bcl568
 * Student Number: 11354916
 */

package org.example.assignment3;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;

public class MainUI extends StackPane {

    public MainUI() {
        EntityModel model = new EntityModel();
        InteractionModel iModel = new InteractionModel();
        DetailView View = new DetailView();
        MiniView miniView = new MiniView();
        AppController controller = new AppController();
        MiniController miniController = new MiniController();

        controller.setModel(model);
        controller.setiModel(iModel);

        miniController.setModel(model);
        miniController.setiModel(iModel);

        View.setModel(model);
        View.setiModel(iModel);
        View.setupEvents(controller);

        miniView.setModel(model);
        miniView.setiModel(iModel);
        miniView.setupEvents(miniController);

        model.addSubscriber(View);
        model.addSubscriber(miniView);

        iModel.addSubscriber(View);
        iModel.addSubscriber(miniView);


        // Force the view to be focused when the application starts
        // Resetting the view width and height in the InteractionModel here fixes the initial size of the rectangle
        // inside miniView
        Platform.runLater(() -> {
            View.requestFocus();
            iModel.setViewWidth(View.ViewWidth());
            iModel.setViewHeight(View.ViewHeight());
        });
        this.getChildren().addAll(View, miniView);
    }

}
