package main.java.samwilkins333.ScrabbleMini.Logic.GameAgents.Referee;

import ScrabbleBase.Vocabulary.Trie;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import main.java.samwilkins333.ScrabbleMini.FXML.Utilities.Image.TransitionHelper;
import main.java.samwilkins333.ScrabbleMini.Logic.DataStructures.Utility.PlayerList;
import main.java.samwilkins333.ScrabbleMini.Logic.GameAgents.Players.Player;
import main.java.samwilkins333.ScrabbleMini.Logic.GameAgents.Players.SimulatedPlayer;
import main.java.samwilkins333.ScrabbleMini.Logic.GameAgents.Referee.Initializer.DictionaryInitializer;
import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Board.Board;
import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.GameContext;
import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Tiles.OverlayType;
import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Tiles.TileBag;
import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Word.Axis;
import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Word.Word;

import java.util.List;

import static main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Board.Board.DURATION;
import static main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Tiles.OverlayType.*;

/**
 * Models a referee capable of mediating the Scrabble match at hand.
 * It possesses the references to the <code>Board</code> and
 * <code>TileBag</code> which it controls in addition to player
 * movement sequencing and move validation.
 * @param <T> the data structure the referee will use to store the lexicon
 */
public abstract class Referee<T extends Trie> {
  private static final double DELAY = 0.65;
  protected final PlayerList<T> players;
  protected final Board board;
  private final TileBag tileBag;
  public final T lexicon;

  protected int movesInitiated = 0;

  /**
   * Constructor.
   * @param players the list of player instances involved in the match
   * @param board the fully initialized board on which moves will be played
   * @param tileBag the fully initialized TileBag used to populate
   *                the players' racks
   */
  Referee(PlayerList<T> players, Board board, TileBag tileBag,
          DictionaryInitializer<T> initializer) {
    this.players = players;
    this.board = board;
    this.tileBag = tileBag;
    this.lexicon = initializer.initialize();

    players.forEach(p -> p.fillRack(board, tileBag));
    players.forEach(p -> p.setRackVisible(p == players.current()));

    players.previous();
    nextMove();
  }

  protected abstract Axis analyzeAxis(Word placements);

  protected abstract boolean isPositioned(Word placements, Axis axis);

  protected abstract boolean isValid(Word word);

  /**
   * Enables this <code>Referee</code> instance to
   * react to key events from the user.
   * @param e the key event received
   */
  public void notify(KeyEvent e) {
    if (this.players.current() instanceof SimulatedPlayer) {
      return;
    }
    switch (e.getCode()) {
      case ENTER:
        this.evaluateHumanPlacements();
        break;
      case ESCAPE:
        board.resetPlacements();
        break;
      case SPACE:
        players.current().shuffleRack(board);
        break;
      default:
    }
  }

  /**
   * Registers the next player as the new current,
   * hides the old rack and displays the new,
   * and invokes the new current's move().
   */
  private void nextMove() {
    Player<T> current = players.current();
    current.setRackVisible(false);
    current = players.next();
    current.setRackVisible(true);
    current.fillRack(board, tileBag);
    movesInitiated++;
    if (current instanceof SimulatedPlayer) {
      TransitionHelper.pause(2, e -> {
        GameContext<T> context = new GameContext<>(board, lexicon, movesInitiated - 1);
        players.current().move(players.current().initializeContext(context));
        nextMove();
      }).play();
    }
  }

  private void evaluateHumanPlacements() {
    Word word = board.placements();

    // ensure we have a non-empty...
    if (word.isEmpty() || word.internalOverlap()) {
      return;
    }

    // ...properly oriented and appropriately complete (no gaps) word
    Axis axis;
    if ((axis = analyzeAxis(word)) == Axis.UNDEFINED) {
      return;
    }

    if (!(isPositioned(word, axis) && board.complete(word, axis))) {
      return;
    }

    // order the tiles from left to right or top to bottom ('read' them)
    word.sort(Word.reader(axis));
    boolean wordValid = isValid(word);

    List<Word> crosses = board.crosses(word, axis.perpendicular());
    boolean allValid = true;

    if (!crosses.isEmpty()) {
      for (int i = 0; i < crosses.size(); i++) {
        Word cross = crosses.get(i);
        boolean crossValid = isValid(cross);
        allValid &= crossValid;
        OverlayType overlay = crossValid
                ? (wordValid ? SUCCESS : QUALIFIED_FAILURE) : FAILURE;
        EventHandler<ActionEvent> flash = e -> cross.flash(overlay);
        TransitionHelper.pause((i + 1) * DELAY, flash).play();
      }
      if (!allValid) {
        word.flash(wordValid ? QUALIFIED_FAILURE : FAILURE);
        return;
      }
    }

    if (!wordValid) {
      word.flash(FAILURE);
    } else {
      Player<T> current = players.current();
      if (crosses.isEmpty()) {
        crosses.add(word);
      } else {
        crosses.add(0, word);
      }

      word.forEach(tile -> board.play(current.transfer(tile)));
      TransitionHelper.pause(2 * DURATION, e -> board.correctShadows()).play();
      crosses.forEach(cross -> current.apply(board.score(cross, true)));
      nextMove();
    }
  }
}
