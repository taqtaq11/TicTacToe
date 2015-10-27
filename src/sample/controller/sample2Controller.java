package sample.controller;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.model.FieldState;
import sample.model.TTTModel;
import sample.view.TTTDrawingUtils;

public class sample2Controller {
    private final int SCREEN_WIDTH = 800;
    private final int SCREEN_HEIGHT = 600;

    private final double CELL_SIZE = 100;
    private final double CELL_BORDER_SIZE = 4;
    private final double CELL_OUTER_BORDER_SIZE = 4;

    private TTTDrawingUtils drawingUtils;

    public Group mainGroup;
    public GridPane mainGridPane;
    public Group drawingGroup;
    private TTTModel model;

    public sample2Controller() {
        drawingUtils = new TTTDrawingUtils();
        model = new TTTModel();
    }

    public void initialize() {
        mainGroup.setTranslateX(SCREEN_WIDTH / 2 - CELL_SIZE * 1.5);
        mainGroup.setTranslateY(SCREEN_HEIGHT * 0.2);
    }

    public void field_mouseClicked(Event event) {
        Rectangle field = (Rectangle)event.getSource();
        int fieldId = Integer.parseInt(field.getId());
        int i = (fieldId - 1) % 3;
        int j = (fieldId - 1) / 3;

        switch (model.trySetField(i, j)) {
            case x:
                double width = field.getWidth();
                double leftX = CELL_OUTER_BORDER_SIZE + (CELL_SIZE  + CELL_BORDER_SIZE) * i;
                double leftY = CELL_OUTER_BORDER_SIZE + (CELL_SIZE  + CELL_BORDER_SIZE) * j;
                drawingUtils.drawCross(leftX, leftY, width, drawingGroup);
                break;
            case o:
                double radius = field.getWidth() / 2;
                double centerX = CELL_OUTER_BORDER_SIZE + (CELL_SIZE  + CELL_BORDER_SIZE) * i + radius;
                double centerY = CELL_OUTER_BORDER_SIZE + (CELL_SIZE  + CELL_BORDER_SIZE) * j + radius;
                drawingUtils.drawToe(centerX, centerY, radius - 10, drawingGroup);
                break;
        }

        if (model.isFinished()) {
            if (model.isTie()) {

            }
            else {
                highlightWonFields(model.getWonFields()[0][0] * 3 + model.getWonFields()[0][1] + 1,
                        model.getWonFields()[1][0] * 3 + model.getWonFields()[1][1] + 1,
                        model.getWonFields()[2][0] * 3 + model.getWonFields()[2][1] + 1);
            }
        }
    }

    private void highlightWonFields(int i, int j, int k) {
        ObservableList<Node> children = mainGridPane.getChildren();
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
}
