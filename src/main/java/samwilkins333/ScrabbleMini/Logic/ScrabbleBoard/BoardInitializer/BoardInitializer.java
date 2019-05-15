package main.java.samwilkins333.ScrabbleMini.Logic.ScrabbleBoard.BoardInitializer;

import javafx.geometry.Point2D;

import java.util.Map;

public interface BoardInitializer<D, C> {
  BoardAttributes<D, C> initialize();

  class BoardAttributes<D, C> {
    int squareCount;
    int squareSize;
    Map<Point2D, D> locationMapping;
    Map<D, C> attributeMapping;

    BoardAttributes(int squareCount, int squareSize, Map<Point2D, D> locationMapping, Map<D, C> attributeMapping) {
      this.squareCount = squareCount;
      this.squareSize = squareSize;
      this.locationMapping = locationMapping;
      this.attributeMapping = attributeMapping;
    }

    public int squareCount() {
      return squareCount;
    }

    public int squareSize() {
      return squareSize;
    }

    public Map<Point2D, D> locationMapping() {
      return locationMapping;
    }

    public Map<D, C> attributeMapping() {
      return attributeMapping;
    }
  }
}
