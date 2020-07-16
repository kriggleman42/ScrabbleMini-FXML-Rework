package main.java.samwilkins333.ScrabbleMini.Logic.GameAgents.Players;

import ScrabbleBase.Board.Location.TilePlacement;
import ScrabbleBase.Board.State.Tile;
import ScrabbleBase.Generation.Generator;
import ScrabbleBase.Generation.Objects.ScoredCandidate;
import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.GameContext;

import ScrabbleBase.Vocabulary.Trie;
import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Tiles.TileView;

import java.util.ArrayList;
import java.util.List;

/**
 * Models an artificially intelligent player
 * computing all possible moves with a GADDAG
 * and applying a heuristic for move selection.
 */
public class SimulatedPlayer extends Player<Trie> {

  @Override
  public List<TilePlacement> move(GameContext<Trie> context) {
    if (this.rack.isEmpty()) {
      return null;
    }
    List<ScoredCandidate> candidates = Generator.Instance.computeAllCandidates(context.getRack(), context.getBoard());
    if (candidates.size() == 0) {
      return new ArrayList<>();
    }
    System.out.println(candidates.get(0));
    for (TilePlacement placement : candidates.get(0).getPlacements()) {
      for (TileView tileView : this.rack) {
        if (tileView.getTile().getLetter() == placement.getTile().getLetter()) {
          if (placement.getTile().getLetter() == Tile.BLANK) {
            tileView.getTile().setLetterProxy(placement.getTile().getLetterProxy());
          }
          tileView.playAt(placement.getX(), placement.getY(), true);
          this.rack.remove(tileView);
          break;
        }
      }
    }
    return candidates.get(0).getPlacements();
  }

}
