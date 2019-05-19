package main.java.samwilkins333.ScrabbleMini.Logic.Rack;

import main.java.samwilkins333.ScrabbleMini.Logic.Tiles.Tile;

import java.util.ArrayList;
import java.util.List;

public class Rack {
  private final int capacity;
  private List<Tile> internalState = new ArrayList<>();

  public Rack(int capacity) {
    this.capacity = capacity;
  }

  public void add(Tile tile) {
    internalState.add(tile);
  }

  public boolean isFull() {
    return internalState.size() == capacity;
  }
}
