package main.java.samwilkins333.ScrabbleMini.Logic.Generation;

public class NextTile extends Coordinates {

  private final Tile tile;

  public NextTile(int x, int y, Tile tile) {
    super(x, y);
    this.tile = tile;
  }

  public Tile getTile() {
    return tile;
  }

}
