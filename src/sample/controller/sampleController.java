package sample.controller;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import sample.model.FieldState;
import sample.model.TTTModel;

import static sample.model.FieldState.empty;

public class sampleController {
    public GridPane mainGP;
    private TTTModel model;

    public sampleController() {
        model = new TTTModel();
    }

    public void proceed(ActionEvent actionEvent) {
        Button field = (Button)actionEvent.getSource();
        int fieldId = Integer.parseInt(field.getId());
        int i = (fieldId - 1) / 3;
        int j = (fieldId - 1) % 3;

        switch (model.trySetField(i, j)) {
            case empty:
                field.setText("");
                break;
            case x:
                field.setText("X");
                break;
            case o:
                field.setText("O");
                break;
        }

        if (model.isFinished()) {
            highlightWonFields(model.getWonFields()[0][0] * 3 + model.getWonFields()[0][1] + 1,
                    model.getWonFields()[1][0] * 3 + model.getWonFields()[1][1] + 1,
                    model.getWonFields()[2][0] * 3 + model.getWonFields()[2][1] + 1);
        }
    }

    private void highlightWonFields(int i, int j, int k) {
        ObservableList<Node> children = mainGP.getChildren();
        for (int l = 0; l < children.size(); l++) {
            Node child = children.get(l);

            try {
                if ((Integer.parseInt(child.getId()) == i)
                        || (Integer.parseInt(child.getId()) == j)
                        || (Integer.parseInt(child.getId()) == k)) {
                    child.getStyleClass().add("highlighted");
                }
            }
            catch (Exception e)
            {

            }
        }
    }

    public void restart(ActionEvent actionEvent) {
        model.newGame();
        ObservableList<Node> children = mainGP.getChildren();
        for (int l = 0; l < children.size(); l++) {
            Node child = children.get(l);

            if (!child.getId().equals("restart_btn")) {
                String defStyle = child.getStyleClass().get(0);
                ((Button)child).setText("");
                child.getStyleClass().clear();
                child.getStyleClass().add(defStyle);
                child.getStyleClass().add("TTTCell");
            }
        }
    }
}
