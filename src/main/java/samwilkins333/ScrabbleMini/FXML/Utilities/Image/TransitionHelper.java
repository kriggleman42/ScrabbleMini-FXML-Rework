package main.java.samwilkins333.ScrabbleMini.FXML.Utilities.Image;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class TransitionHelper {

  public static FadeTransition flash(Node target, double pulseDuration) {
    FadeTransition flash = new FadeTransition(Duration.seconds(pulseDuration), target);
    flash.setFromValue(1.0);
    flash.setToValue(0.0);
    flash.setAutoReverse(true);
    flash.setCycleCount(Animation.INDEFINITE);
    return flash;
  }

  public static ScaleTransition scale(Node target, double duration, double byX, double byY) {
    ScaleTransition scale = new ScaleTransition(Duration.seconds(duration), target);
    scale.setByX(byX);
    scale.setByY(byY);
    scale.setAutoReverse(true);
    return scale;
  }

}
