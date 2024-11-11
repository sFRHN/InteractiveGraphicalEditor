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

        Platform.runLater(View::requestFocus);
        this.getChildren().addAll(View, miniView);
    }

}
