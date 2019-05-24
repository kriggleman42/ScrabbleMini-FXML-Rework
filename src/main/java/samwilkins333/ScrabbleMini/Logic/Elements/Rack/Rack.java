package main.java.samwilkins333.ScrabbleMini.Logic.Elements.Rack;

import main.java.samwilkins333.ScrabbleMini.FXML.Utilities.Image.TransitionHelper;
import main.java.samwilkins333.ScrabbleMini.Logic.Elements.Rack.Board.Board;
import main.java.samwilkins333.ScrabbleMini.Logic.Elements.Rack.Tiles.Tile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static main.java.samwilkins333.ScrabbleMini.Logic.Elements.Rack.Board.BoardLayoutManager.squarePixels;

/**
 * Models a player's rack of 7 tiles, encompassing both
 * the logic and the graphical state.
 */
public class Rack extends ArrayList<Tile> {
  public static final double DELAY = 0.25;
  public static final int CAPACITY = 7;
  public int animationsInProgress = 0;

  /**
   * @return whether or not the rack is at capacity
   */
  public boolean isFull() {
    return size() == CAPACITY;
  }

  /**
   * Either displays or conceals all the tiles
   * present in the rack.
   * @param visible whether or not the tiles should be visible
   */
  public void setVisible(boolean visible) {
    forEach(tile -> tile.observableImage().bindings().opacity(visible ? 1 : 0));
  }

  /**
   * Takes the tiles remaining in the rack after placement
   * and positions them in an immediate sequence beginning
   * at the top of the rack (with an animation).
   */
  public void consolidate() {
    IntConsumer placeTile =
        i -> TransitionHelper.pause(DELAY * i, e -> placeAt(i)).play();
    IntStream.range(0, size()).forEach(placeTile);
  }

  private void placeAt(int pos) {
    double layoutY = RackLayoutManager.originTopPixels + (pos * squarePixels);
    get(pos).set(layoutY);
  }

  /**
   * Logically and graphically shuffles the vertical
   * ordering of the tiles in the rack, non-deterministically.
   * @param board state indicates whether or not the rack can be
   *              shuffled.
   */
  public void shuffle(Board board) {
    if (!board.placements().isEmpty() || animationsInProgress > 0) {
      return;
    }

    sort(Comparator.comparing(Tile::rackPlacement));

    for (int i = 0; i < size() - 1; i++) {
      Tile upperTile = get(i);
      Tile lowerTile = get(i + 1);

      switch ((int) (Math.random() * 3)) {
        case 0:
        case 1:
          upperTile.shift(1);
          lowerTile.shift(-1);
          set(i + 1, upperTile);
          set(i, lowerTile);
          break;
        default:
        case 2:
          break;
      }
    }
  }
}
