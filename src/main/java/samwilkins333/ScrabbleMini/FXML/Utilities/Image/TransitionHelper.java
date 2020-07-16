package main.java.samwilkins333.ScrabbleMini.FXML.Utilities.Image;

import javafx.animation.FillTransition;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

/**
 * A convenience class that acts as a factory
 * for <code>Transition</code>s.
 */
public final class TransitionHelper {
  private TransitionHelper() {
    //prevents instantiation
  }

  /**
   * Produces a flash with the given <code>Node</code>
   * as the target, customized by parameter specifications.
   * @param target the Node to flash
   * @param duration the duration of each cycle
   * @param cycles the number of cycles to execute
   * @return the generated <code>FadeTransition</code>
   */
  public static FadeTransition flash(
          Node target, double duration, int cycles) {
    Duration dur = Duration.seconds(duration);
    FadeTransition flash = new FadeTransition(dur, target);
    flash.setFromValue(1.0);
    flash.setToValue(0.0);
    flash.setAutoReverse(true);
    flash.setCycleCount(cycles);
    return flash;
  }

  /**
   * Produces a scaling effect with the given <code>Node</code>
   * as the target, customized by parameter specifications.
   * @param target the Node to scale
   * @param duration the duration of each cycle
   * @param byX the ratio by which to scale the width
   * @param byY the ratio by which to scale the height
   * @return the generated <code>ScaleTransition</code>
   */
  public static ScaleTransition scale(
          Node target, double duration, double byX, double byY) {
    Duration dur = Duration.seconds(duration);
    ScaleTransition scale = new ScaleTransition(dur, target);
    scale.setByX(byX);
    scale.setByY(byY);
    scale.setAutoReverse(true);
    return scale;
  }

  /**
   * Effectively a scheduled task, occurring after the given
   * latency.
   * @param seconds the latency before execution
   * @param onFinished the action to execute after the duration
   *                   has elapsed
   * @return the generated <code>PauseTransition</code>
   */
  public static PauseTransition pause(
          double seconds, EventHandler<ActionEvent> onFinished) {
    Duration dur = Duration.seconds(seconds);
    PauseTransition pause = new PauseTransition(dur);
    pause.setOnFinished(onFinished);
    return pause;
  }

  /**
   * Produces a rotation animation with the given <code>Node</code>
   * as the target, customized by parameter specifications.
   * @param target the Node to rotate
   * @param duration the time within which to perform the rotation
   * @param angle the degrees by which the Node should rotate
   * @param cycles how many such rotations should occur
   * @param rev whether or not the rotation should automatically
   *            reverse itself for each cycle
   * @return the generated <code>RotateTransition</code>
   */
  public static RotateTransition rotate(
          Node target, double duration, double angle, int cycles, boolean rev) {
    Duration dur = Duration.seconds(duration);
    RotateTransition rotate = new RotateTransition(dur, target);
    rotate.setByAngle(angle);
    rotate.setCycleCount(cycles);
    rotate.setAutoReverse(rev);
    return rotate;
  }

  /**
   * Produces an animation that translates the given Node by
   * the specified parameters within the specified time.
   * @param target the Node to translate
   * @param duration the time within which to perform the translation
   * @param byX the amount by which to translate in the x direction
   * @param byY the amount by which to translate in the y direction
   * @return the generated <code>TranslateTransition</code>
   */
  public static TranslateTransition translate(
          Node target, double duration, double byX, double byY) {
    Duration dur = Duration.seconds(duration);
    TranslateTransition translate = new TranslateTransition(dur, target);
    translate.setByX(byX);
    translate.setByY(byY);
    return translate;
  }

  /**
   * Produces an animation that alters the given Node's
   * fill, gradating between the from color to the to color.
   * @param target the Node to which to apply the color gradient
   * @param duration the time within which to perform the gradient
   * @param from the initial color of the gradient
   * @param to the final color of the gradient
   * @return the generated <code>FillTransition</code>
   */
  public static FillTransition gradient(
          Shape target, double duration, Color from, Color to) {
    Duration dur = Duration.seconds(duration);
    FillTransition gradient = new FillTransition(dur, target);
    gradient.setFromValue(from);
    gradient.setToValue(to);
    return gradient;
  }
}
