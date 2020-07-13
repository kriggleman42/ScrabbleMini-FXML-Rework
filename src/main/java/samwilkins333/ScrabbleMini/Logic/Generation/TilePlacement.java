package main.java.samwilkins333.ScrabbleMini.Logic.Generation;

public class TilePlacement {

  private final int x;
  private final int y;
  private final Tile tile;

  public TilePlacement(int x, int y, Tile tile) {
    this.x = x;
    this.y = y;
    this.tile = tile;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Tile getTile() {
    return tile;
  }

}
