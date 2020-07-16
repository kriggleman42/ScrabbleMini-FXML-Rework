package main.java.samwilkins333.ScrabbleMini.Logic.GameAgents.Players;

import ScrabbleBase.Board.Location.TilePlacement;
import ScrabbleBase.Vocabulary.Trie;
import main.java.samwilkins333.ScrabbleMini.FXML.Scenes.Bindings.Composite.ImageBindings;
import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Board.Board;
import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Board.BoardScore;
import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.GameContext;
import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Rack.RackLayoutManager;
import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Rack.RackView;
import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Tiles.TileBag;
import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Tiles.TileView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Board.BoardLayoutManager.squarePixels;
import static main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Rack.RackLayoutManager.leftOriginLeftPixels;
import static main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Rack.RackLayoutManager.rightOriginLeftPixels;

/**
 * A template that models a Scrabble player. Each player
 * is given a rack of tiles to manipulate, maintains a moves
 * and, in some implementer-specified way, makes a move in the game.
 * @param <T> the type of data structure that holds the entire vocabulary
 *           of valid words in the game
 */
public abstract class Player<T extends Trie> {
  private int playerNumber;
  protected RackView rack;
  protected Map<Integer, BoardScore> score = new HashMap<>();
  protected int moves = 0;

  Player() {
    this.rack = new RackView();
  }

  /**
   * Register's this player's 1-indexed
   * number in the match.
   * @param number player number
   */
  public void setPlayerNumber(int number) {
    playerNumber = number;
  }

  public GameContext<T> initializeContext(GameContext<T> context) {
    context.setRack(this.rack);
    return context;
  }

  /**
   * Both logically and graphically replenishes the missing
   * tiles in a player's rack. Triggers TileBag shaking animation
   * assuming any new <code>Tile</code>s are in fact drawn.
   * @param board allows the Tile to add itself graphically to the board's node
   * @param tileBag the <code>TileBag</code> from which the new tiles
   *                will be drawn
   */
  public boolean fillRack(Board board, TileBag tileBag) {
    if (rack.isFull()) {
      return false;
    }

    rack.consolidate();
//    rack.animationsInProgress = RackView.CAPACITY - rack.size();

    tileBag.shake();
    while (!rack.isFull()) {
      TileView drawn = tileBag.draw(this instanceof HumanPlayer);

      if (drawn == null) {
        break;
      }

      double initialX = playerNumber == 1
              ? leftOriginLeftPixels : rightOriginLeftPixels;
      double initialY = RackLayoutManager.originTopPixels
              + squarePixels * rack.size();

      ImageBindings bindings = drawn.observableImage().bindings();
      bindings.layoutX(initialX);
      bindings.layoutY(initialY);
      bindings.opacity(1);
      drawn.setRackPosition(initialX, initialY);

      rack.add(drawn);

      drawn.render(board);
//      TransitionHelper.pause(RackView.DELAY * rack.size(), e -> {
//        rack.animationsInProgress--;
//      }).play();
    }

    return true;
  }

  /**
   * Updates the current user's score and stores
   * the full <code>BoardScore</code> associated with
   * a given move number.
   * @param result the resultant score of the played word
   */
  public void apply(BoardScore result) {
    System.out.printf("Player %s played %s for %d points\n",
            playerNumber, result.word().toUpperCase(), result.score());
    score.put(++moves, result);
  }

  /**
   * Removes a tile from the player's rack and
   * returns it. Occurs when permanently playing
   * a tile.
   * @param tile the tile to remove and then play
   * @return the removed tile, ready for playing
   */
  public TileView transfer(TileView tile) {
    rack.remove(tile);
    return tile;
  }

  /**
   * A mutator that allows the showing and
   * hiding of a rack, visibly.
   * @param visible whether or not the given
   *                rack's tile should be visible
   */
  public void setRackVisible(boolean visible) {
    rack.setVisible(visible);
  }

  /**
   * Graphically and logically shuffles the tiles
   * within a player's rack, allowing new combinations
   * to become more apparent.
   * @param board used to determine whether or not
   *              the rack can be shuffled
   */
  public void shuffleRack(Board board) {
    rack.shuffle(board);
  }

  /**
   * A method invoked whenever the player must make
   * the next play on the board.
   * @param context the game board, which holds the current
   *              state of the game at the time of invocation,
   *                and the data structure containing the lexicon
   */
  public abstract List<TilePlacement> move(GameContext<T> context);
}
