package main.java.samwilkins333.ScrabbleMini.Logic.DataStructures.Gaddag;

import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Rack.Rack;
import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Word.Word;

import java.util.HashSet;
import java.util.Set;

/**
 * A specialized Trie data structure
 * that calculates valid board moves
 * extremely efficiently.
 */
public class Gaddag extends HashSet<String> {
  private static final String DELIMITER = "#";
  private Set<String> raw = new HashSet<>();

  @Override
  public boolean contains(Object o) {
    return raw.contains(o);
  }

  @Override
  public boolean add(String word) {
    boolean success = true;
    Set<String> representations = new HashSet<>();
    for (int i = 1; i < word.length() + 1; i++) {
      String rev = new StringBuilder(word.substring(0, i)).reverse().toString();
      String result = rev + DELIMITER + word.substring(i);
      representations.add(result);
      success &= super.add(result);
    }
    if (!success) {
      representations.forEach(super::remove);
    } else {
      raw.add(word);
    }
    return success;
  }

  /**
   * One of two recursive backtracking co-routines
   * used to generate all possible words.
   * @param pos the offset from the anchor square
   * @param word the target word
   * @param rack the player's current rack of letters
   * @param arc the arc to be considered
   */
  public void generate(int pos, Word word, Rack rack, Arc arc) {
  }

}