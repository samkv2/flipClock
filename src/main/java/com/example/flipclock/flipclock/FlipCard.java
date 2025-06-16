package com.example.flipclock.flipclock;

import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.util.Duration;

public class FlipCard extends StackPane {
    private final Text text;
    private Font currentFont;
    private double fontSize;

    public FlipCard(String initialText, double fontSize, Font font) {
        this.fontSize = fontSize;
        this.currentFont = font;

        this.text = new Text(initialText);
        this.text.setFont(Font.font(currentFont.getFamily(), fontSize));
        this.text.setFill(Color.RED);
        this.text.setEffect(glowEffect());
        this.text.setTextAlignment(TextAlignment.CENTER);

        setMinWidth(fontSize * 1.2);
        setStyle("-fx-background-color: black;");
        setAlignment(Pos.CENTER);
        getChildren().add(text);
    }

    public void updateText(String newText) {
        if (!text.getText().equals(newText)) {
            ScaleTransition flipOut = new ScaleTransition(Duration.millis(150), text);
            flipOut.setToY(0);

            flipOut.setOnFinished(e -> {
                text.setText(newText);
                ScaleTransition flipIn = new ScaleTransition(Duration.millis(150), text);
                flipIn.setToY(1);
                flipIn.play();
            });

            flipOut.play();
        }
    }

    public void setFontSize(double newSize) {
        this.fontSize = newSize;
        this.text.setFont(Font.font(currentFont.getFamily(), newSize));
        this.setMinWidth(newSize * 1.2);
    }

    private DropShadow glowEffect() {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.RED);
        glow.setRadius(15);
        glow.setSpread(0.3);
        return glow;
    }
}
