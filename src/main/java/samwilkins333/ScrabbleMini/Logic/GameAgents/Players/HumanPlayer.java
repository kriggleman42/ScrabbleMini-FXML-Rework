package main.java.samwilkins333.ScrabbleMini.Logic.GameAgents.Players;

import ScrabbleBase.Board.Location.TilePlacement;
import ScrabbleBase.Vocabulary.Trie;
import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.GameContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Models a human scrabble player. In effect, it is a
 * lightweight wrapper whose <code>move()</code> method
 * just nominally encapsulates the manual dragging and
 * checking of tiles by an actual human player.
 */
public class HumanPlayer extends Player<Trie> {

  /**
   * Constructor.
   */
  public HumanPlayer() {
    super();
  }

  @Override
  public List<TilePlacement> move(GameContext<Trie> context) {
    return new ArrayList<>();
  }
}
