package com.example.smiley;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SmileyCanvas extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private final double rightEyeX = 130;
    private final double rightEyeY = 57.5;
    private final double rightEyeWidth = 10;
    private final double rightEyeHeight = 25;
    private final double rightEyeCenterX = rightEyeX + rightEyeWidth / 2;
    private final double rightEyeCenterY = rightEyeY + rightEyeHeight / 2;

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(200, 200);
        gc = canvas.getGraphicsContext2D();

        // Draw left eye
        gc.setFill(Color.BLACK);
        gc.fillRect(60, 57.5, 10, 25);

        // Draw right eye
        gc.fillRect(rightEyeX, rightEyeY, rightEyeWidth, rightEyeHeight);

        // Draw mouth
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);

        double mouthRadius = 40;
        double mouthCenterX = 100;
        double mouthCenterY = 130;

        gc.beginPath();
        for (double angle = 0; angle <= 180; angle += 5) {
            double x = mouthCenterX + mouthRadius * Math.cos(Math.toRadians(angle));
            double y = mouthCenterY + mouthRadius * Math.sin(Math.toRadians(angle));
            gc.lineTo(x, y);
        }
        gc.stroke();

        // Add canvas to scene and show
        Scene scene = new Scene(new Group(canvas));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        // Add mouse event
        addMouseEvent();
    }

    public void addMouseEvent() {
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            /*
            --clickDistance--
            a² = x distance from the eye to the click squared
            b² = y distance from the eye to the click squared
            c² (clickDistance) = a² + b²

            --validDistance--
            a² = x center of the right eye plus width squared
            b² = y center of the right eye plus height squared
            c² (validDistance) = a² + b²

            Use the pythagorean theorem (a² + b² = c²) to get the distance from the center of the right eye and the click (clickDistance)
            Then check if the click is inside the imaginary circle in right eye (validDistance)
            The validDistance gets calculated by dividing the width and height by 2 and adding them together

            At last clickDistance is checked if it is smaller or equal to the valid distance
            */

            // Get distance in which a click is valid
            double validDistance = Math.pow(rightEyeX - rightEyeCenterX, 2) + Math.pow(rightEyeY - rightEyeCenterY, 2);

            // Get position from the click
            double clickX = e.getX();
            double clickY = e.getY();

            // Get distance between click and right eye
            double clickDistance = Math.pow(clickX - rightEyeCenterX, 2) + Math.pow(clickY - rightEyeCenterY, 2);

            // Check if click distance is smaller or equal to valid distance
            if (clickDistance <= validDistance) {
                rotateEye();
                visualizeValidDistance(validDistance);
            }
        });
    }

    public void rotateEye() {
        // Delete right eye
        gc.clearRect(rightEyeX, rightEyeY, rightEyeWidth, rightEyeHeight);

        // Create a new one rotated 90 degrees (Height and width are switched)
        gc.fillRect((rightEyeCenterX - rightEyeHeight / 2), (rightEyeCenterY - rightEyeWidth / 2), rightEyeHeight, rightEyeWidth);

        // Set timeout with the Timeline class
        Timeline timeline = new Timeline();

        // Add a KeyFrame with a duration of 1 second
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            // Delete new eye
            gc.clearRect((rightEyeCenterX - rightEyeHeight / 2), (rightEyeCenterY - rightEyeWidth / 2), rightEyeHeight, rightEyeWidth);

            // Draw old eye
            gc.fillRect(rightEyeX, rightEyeY, rightEyeWidth, rightEyeHeight);
        });

        // Add the KeyFrame to the Timeline
        timeline.getKeyFrames().add(keyFrame);

        // Start the timeline
        timeline.play();
    }

    public void visualizeValidDistance(double validDistance) {
        gc.setFill(Color.LIGHTBLUE);

        // gc.fillOval(x - r, y - r, 2 * r, 2 * r);
        double radius = Math.sqrt(validDistance);
        double diameter = 2 * radius;
        gc.fillOval(rightEyeCenterX - radius, rightEyeCenterY - radius, diameter, diameter);

        gc.setFill(Color.BLACK);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
