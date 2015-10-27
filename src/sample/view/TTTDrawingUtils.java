package sample.view;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.util.Random;

/**
 * Created by Δενθρ on 17.10.2015.
 */
public class TTTDrawingUtils {
    public void drawCross(double leftX, double leftY, double fieldSize, Group context) {
        final int DRAWING_RECT_WIDTH = 10;
        final Color CROSS_COLOR = Color.DARKBLUE;
        final double DURATION = 1.2;

        Rectangle drawingRect = new Rectangle(0, 0, DRAWING_RECT_WIDTH, DRAWING_RECT_WIDTH);
        drawingRect.setArcHeight(10);
        drawingRect.setArcWidth(10);
        drawingRect.setVisible(false);
        context.getChildren().add(drawingRect);

        ChangeListener changeListener = (ov, t, t1) -> {
            Rectangle newrect = new Rectangle(drawingRect.getTranslateX(), drawingRect.getTranslateY(), drawingRect.getWidth(), drawingRect.getHeight());
            newrect.setFill(CROSS_COLOR);
            newrect.setRotate(drawingRect.getRotate());
            context.getChildren().add(newrect);
        };

        Path path = new Path();
        path.setStrokeWidth(0);
        path.getElements().add(new MoveTo(leftX, leftY));
        drawingRect.translateYProperty().addListener(changeListener);
        path.getElements().add(new LineTo(leftX + fieldSize, leftY + fieldSize));
        path.getElements().add(new MoveTo(leftX, leftY + fieldSize));
        path.getElements().add(new LineTo(leftX + fieldSize, leftY));

        context.getChildren().add(path);

        PathTransition pt = new PathTransition();
        pt.setDuration(Duration.seconds(DURATION));
        pt.setPath(path);
        pt.setNode(drawingRect);
        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pt.play();
    }

    public void drawToe(double centerX, double centerY, double radius, Group context) {
        final int DRAWING_RECT_WIDTH = 10;
        final int ENDPOINT_ERROR_BOUNDS_X = 15;
        final int ENDPOINT_ERROR_BOUNDS_Y = 15;
        final double DURATION = 0.9;
        final double ROTATION_BOUNDS = 70;
        final Color TOE_COLOR = Color.DARKRED;

        Rectangle drawingRect = new Rectangle(0, 0, DRAWING_RECT_WIDTH, DRAWING_RECT_WIDTH);
        drawingRect.setArcHeight(10);
        drawingRect.setArcWidth(10);
        drawingRect.setVisible(false);
        context.getChildren().add(drawingRect);

        ChangeListener changeListener = (ov, t, t1) -> {
            Rectangle newrect = new Rectangle(drawingRect.getTranslateX(), drawingRect.getTranslateY(), drawingRect.getWidth(), drawingRect.getHeight());
            newrect.setFill(TOE_COLOR);
            newrect.setRotate(drawingRect.getRotate());
            context.getChildren().add(newrect);
        };

        ArcTo arcTo = new ArcTo();
        arcTo.setX(centerX);
        arcTo.setY(centerY + radius);
        arcTo.setRadiusX(radius);
        arcTo.setRadiusY(radius);

        Random rnd = new Random();

        ArcTo arcTo2 = new ArcTo();
        arcTo2.setX(centerX - (rnd.nextDouble() * (2 * ENDPOINT_ERROR_BOUNDS_X) - ENDPOINT_ERROR_BOUNDS_X));
        arcTo2.setY(centerY - radius - (rnd.nextDouble() * (2 * ENDPOINT_ERROR_BOUNDS_Y) - ENDPOINT_ERROR_BOUNDS_Y));
        arcTo2.setRadiusX(12);
        arcTo2.setRadiusY(10);

        Path path = new Path();
        path.setStrokeWidth(0);
        path.setRotate(rnd.nextDouble() * (2 * ROTATION_BOUNDS) - ROTATION_BOUNDS);
        path.getElements().add(new MoveTo(centerX, centerY - radius));
        drawingRect.rotateProperty().addListener(changeListener);
        path.getElements().add(arcTo);
        path.getElements().add(arcTo2);

        context.getChildren().add(path);

        PathTransition pt = new PathTransition();
        pt.setDuration(Duration.seconds(DURATION));
        pt.setPath(path);
        pt.setNode(drawingRect);
        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pt.setInterpolator(Interpolator.TANGENT(Duration.seconds(0.5), 1));
        pt.play();
    }
}
