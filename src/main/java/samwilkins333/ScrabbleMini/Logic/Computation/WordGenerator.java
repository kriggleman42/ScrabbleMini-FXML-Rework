package main.java.samwilkins333.ScrabbleMini.Logic.Computation;

import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Board.Board;
import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Word.Word;

import java.util.List;

/**
 * An implementor of <code>CandidateGenerator</code>, produces
 * a list of all possible words that can be played
 * on the given board.
 */
public class WordGenerator implements CandidateGenerator<Word, Board> {

  @Override
  public List<Word> generate(Board board) {
    return null;
  }
}
