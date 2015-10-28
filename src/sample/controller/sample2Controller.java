package sample.controller;

import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Main;
import sample.model.FieldState;
import sample.model.TTTModel;
import sample.view.TTTDrawingUtils;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.FileInputStream;
import java.io.InputStream;

public class sample2Controller {
    private final int SCREEN_WIDTH = 800;
    private final int SCREEN_HEIGHT = 600;

    private final double CELL_SIZE = 100;
    private final double CELL_BORDER_SIZE = 4;
    private final double CELL_OUTER_BORDER_SIZE = 4;

    private TTTDrawingUtils drawingUtils;
    private TTTModel model;
    private FadeTransition gamerLabel1FadeIn;
    private FadeTransition gamerLabel2FadeIn;
    private FadeTransition endGameLabelFadeIn;

    public Group mainGroup;
    public GridPane mainGridPane;
    public Group drawingGroup;
    public Label gamerLabel1;
    public Label gamerLabel2;
    public Label endGameLabel;

    public sample2Controller() {
        drawingUtils = new TTTDrawingUtils();
        model = new TTTModel();
    }

    public void initialize() {
        mainGroup.setTranslateX(SCREEN_WIDTH / 2 - CELL_SIZE * 1.5);
        mainGroup.setTranslateY(SCREEN_HEIGHT * 0.2);

        gamerLabel1FadeIn = new FadeTransition(
                Duration.millis(1000)
        );
        gamerLabel1FadeIn.setNode(gamerLabel1);
        gamerLabel1FadeIn.setFromValue(0.0);
        gamerLabel1FadeIn.setToValue(1.0);
        gamerLabel1FadeIn.setCycleCount(1);
        gamerLabel1FadeIn.setAutoReverse(false);

        gamerLabel2FadeIn = new FadeTransition(
                Duration.millis(1000)
        );
        gamerLabel2FadeIn.setNode(gamerLabel2);
        gamerLabel2FadeIn.setFromValue(0.0);
        gamerLabel2FadeIn.setToValue(1.0);
        gamerLabel2FadeIn.setCycleCount(1);
        gamerLabel2FadeIn.setAutoReverse(false);

        endGameLabelFadeIn = new FadeTransition(
                Duration.millis(3000)
        );
        endGameLabelFadeIn.setNode(endGameLabel);
        endGameLabelFadeIn.setFromValue(0.0);
        endGameLabelFadeIn.setToValue(1.0);
        endGameLabelFadeIn.setCycleCount(1);
        endGameLabelFadeIn.setAutoReverse(false);
    }

    public void field_mouseClicked(Event event) {

        Rectangle field = (Rectangle)event.getSource();
        int fieldId = Integer.parseInt(field.getId());
        int i = (fieldId - 1) % 3;
        int j = (fieldId - 1) / 3;

        FieldState fs = model.trySetField(i, j);

        if (fs == null)
            return;

        switch (fs) {
            case x:
                double width = field.getWidth();
                double leftX = CELL_OUTER_BORDER_SIZE + (CELL_SIZE  + CELL_BORDER_SIZE) * i;
                double leftY = CELL_OUTER_BORDER_SIZE + (CELL_SIZE  + CELL_BORDER_SIZE) * j;
                drawingUtils.drawCross(leftX, leftY, width, drawingGroup);

                gamerLabel2FadeIn.setFromValue(0.0);
                gamerLabel2FadeIn.setToValue(1.0);
                gamerLabel2FadeIn.playFromStart();

                gamerLabel1FadeIn.setFromValue(1.0);
                gamerLabel1FadeIn.setToValue(0.0);
                gamerLabel1FadeIn.playFromStart();

                playSound("sounds/paper1.wav", 1000);
                break;
            case o:
                double radius = field.getWidth() / 2;
                double centerX = CELL_OUTER_BORDER_SIZE + (CELL_SIZE  + CELL_BORDER_SIZE) * i + radius;
                double centerY = CELL_OUTER_BORDER_SIZE + (CELL_SIZE  + CELL_BORDER_SIZE) * j + radius;
                drawingUtils.drawToe(centerX, centerY, radius - 10, drawingGroup);

                gamerLabel1FadeIn.setFromValue(0.0);
                gamerLabel1FadeIn.setToValue(1.0);
                gamerLabel1FadeIn.playFromStart();

                gamerLabel2FadeIn.setFromValue(1.0);
                gamerLabel2FadeIn.setToValue(0.0);
                gamerLabel2FadeIn.playFromStart();

                playSound("sounds/paper1.wav", 1000);
                break;
        }

        if (model.isFinished()) {
            if (model.isTie()) {
                endGameLabel.setText("Tie");
                endGameLabel.setTextFill(Color.web("#888"));
            }
            else {
                if (model.getWinner() == FieldState.x) {
                    endGameLabel.setText("The winner is X");
                    endGameLabel.setTextFill(Color.DARKBLUE);
                }
                else {
                    endGameLabel.setText("The winner is O");
                    endGameLabel.setTextFill(Color.DARKRED);
                }
                /*highlightWonFields(model.getWonFields()[0][0] * 3 + model.getWonFields()[0][1] + 1,
                        model.getWonFields()[1][0] * 3 + model.getWonFields()[1][1] + 1,
                        model.getWonFields()[2][0] * 3 + model.getWonFields()[2][1] + 1);*/
            }

            gamerLabel1FadeIn.setFromValue(gamerLabel1.getOpacity());
            gamerLabel1FadeIn.setToValue(0.0);
            gamerLabel1FadeIn.playFromStart();

            gamerLabel2FadeIn.setFromValue(gamerLabel2.getOpacity());
            gamerLabel2FadeIn.setToValue(0.0);
            gamerLabel2FadeIn.playFromStart();

            endGameLabelFadeIn.setFromValue(0.0);
            endGameLabelFadeIn.setToValue(1.0);
            endGameLabelFadeIn.playFromStart();

            playSound("sounds/Fortuna.wav", 14500);
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

    public void newGame(Event event) {
        model.newGame();
        drawingGroup.getChildren().clear();
        gamerLabel1.setOpacity(1.0);
        gamerLabel2.setOpacity(0.0);

        endGameLabelFadeIn.setFromValue(endGameLabel.getOpacity());
        endGameLabelFadeIn.setDuration(Duration.millis(1000));
        endGameLabelFadeIn.setToValue(0.0);
        endGameLabelFadeIn.playFromStart();
        endGameLabelFadeIn.setDuration(Duration.millis(3000));
    }

    public static synchronized void playSound(String filename, int duration) {
        new Thread(() -> {
            try {
                String fullpath = Main.class.getResource(filename).getPath();
                InputStream in = new FileInputStream(fullpath);
                AudioStream as =  new AudioStream(in);
                AudioPlayer.player.start(as);
                Thread.sleep(duration);
                AudioPlayer.player.stop(as);
            } catch(Exception ex) {

            }
        }).start();
    }

}
