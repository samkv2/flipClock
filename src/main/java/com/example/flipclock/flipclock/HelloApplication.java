package com.example.flipclock.flipclock;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HelloApplication extends Application {

    private FlipCard hourCard, minuteCard, secondCard;
    private Text colon1, colon2, amPmText, dateText;
    private Font segmentFont;

    private int lastMinute = -1;
    private int lastDay = -1;

    @Override
    public void start(Stage stage) {
        // Load 7-segment font
        segmentFont = Font.loadFont(getClass().getResourceAsStream("/fonts/ds_digital/DS-DIGI.TTF"), 10);

        // Initialize clock digits
        hourCard = new FlipCard("12", 10, segmentFont);
        minuteCard = new FlipCard("00", 10, segmentFont);
        secondCard = new FlipCard("00", 10, segmentFont);

        // Colons
        colon1 = createColon(":");
        colon2 = createColon(":");

        // AM/PM
        amPmText = new Text("AM");
        amPmText.setFill(Color.RED);
        amPmText.setEffect(glowEffect());

        // Horizontal time layout
        HBox timeBox = new HBox(6, hourCard, colon1, minuteCard, colon2, secondCard, amPmText);
        timeBox.setAlignment(Pos.CENTER);

        // Date display
        dateText = new Text(getFormattedDate());
        dateText.setFill(Color.RED);
        dateText.setEffect(glowEffect());
        dateText.setFont(Font.font(segmentFont.getFamily(), 10)); // placeholder

        StackPane datePane = new StackPane(dateText);
        datePane.setMaxWidth(Double.MAX_VALUE);
        datePane.setAlignment(Pos.CENTER);

        // Combine everything
        VBox rootBox = new VBox(20, timeBox, datePane);
        rootBox.setStyle("-fx-background-color: black;");
        rootBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(rootBox);

        // Make clock scale based on width
        DoubleBinding scaleFactor = scene.widthProperty().multiply(0.90).divide(1000);
        scaleFactor.addListener((obs, oldVal, newVal) -> updateFontSizes(newVal.doubleValue()));
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateFontSizes(scaleFactor.get()));

        // Real-time updates
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> updateClock()),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // Show window
        stage.setScene(scene);
        stage.setTitle("Flip Clock- Made with ❤\uFE0F by Shivam");
        stage.setMaximized(true);
        stage.show();

        javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());

        updateFontSizes(scaleFactor.get());

        PreventSleep.activate();


    }

    private void updateClock() {
        LocalTime now = LocalTime.now();

        int hour = now.getHour() % 12;
        if (hour == 0) hour = 12;

        String hh = String.format("%02d", hour);
        String mm = String.format("%02d", now.getMinute());
        String ss = String.format("%02d", now.getSecond());
        String ampm = now.getHour() >= 12 ? "PM" : "AM";

        hourCard.updateText(hh);
        minuteCard.updateText(mm);
        secondCard.updateText(ss);
        amPmText.setText(ampm);

        // Blink seconds colon every second
        colon2.setVisible(now.getSecond() % 2 == 0);

        // Blink minutes colon once on minute change
        if (now.getMinute() != lastMinute) {
            flashColon1();
            lastMinute = now.getMinute();
        }

        // Update date on day change
        int today = LocalDate.now().getDayOfMonth();
        if (today != lastDay) {
            dateText.setText(getFormattedDate());
            lastDay = today;
        }
    }

    private void flashColon1() {
        Timeline blink = new Timeline(
                new KeyFrame(Duration.ZERO, e -> colon1.setFill(Color.BLACK)),
                new KeyFrame(Duration.millis(250), e -> colon1.setFill(Color.RED))
        );
        blink.play();
    }

    private void updateFontSizes(double scale) {
        double fontSize = 120 * scale;

        hourCard.setFontSize(fontSize);
        minuteCard.setFontSize(fontSize);
        secondCard.setFontSize(fontSize);

        colon1.setFont(Font.font(segmentFont.getFamily(), fontSize));
        colon2.setFont(Font.font(segmentFont.getFamily(), fontSize));
        amPmText.setFont(Font.font(segmentFont.getFamily(), fontSize * 0.4));
        dateText.setFont(Font.font(segmentFont.getFamily(), fontSize));
    }

    private Text createColon(String symbol) {
        Text colon = new Text(symbol);
        colon.setFill(Color.RED);
        colon.setEffect(glowEffect());
        colon.setFont(Font.font(segmentFont.getFamily(), 120));
        return colon;
    }

    private String getFormattedDate() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE • dd - MMM • yyyy");
        return formatter.format(date);
    }

    private DropShadow glowEffect() {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.RED);
        glow.setRadius(15);
        glow.setSpread(0.3);
        return glow;
    }

    public static void main(String[] args) {
        launch();
    }
}
