package main.java.samwilkins333.ScrabbleMini.Logic.Computation;

public class Tile {

  private final char letter;
  private final int score;
  private Character letterProxy;

  public Tile(char letter, int score, Character letterProxy) {
    this.letter = letter;
    this.score = score;
    this.letterProxy = letterProxy;
  }

  public char getLetter() {
    return letter;
  }

  public int getScore() {
    return score;
  }

  public Character getLetterProxy() {
    return letterProxy;
  }

  public char getResolvedLetter() {
    return this.letterProxy != null ? this.letterProxy : this.letter;
  }

  public void setLetterProxy(char letterProxy) {
    this.letterProxy = letterProxy;
  }

}
