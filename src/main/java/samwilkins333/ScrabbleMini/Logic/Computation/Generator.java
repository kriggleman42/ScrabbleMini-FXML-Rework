package main.java.samwilkins333.ScrabbleMini.Logic.Computation;

import main.java.samwilkins333.ScrabbleMini.Logic.GameElements.Rack.RackView;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains logic for exhaustive move generation
 * given game context.
 */
public class Generator {
  public static final Generator Instance = new Generator();
  private TrieNode root;

  public void setRoot(TrieNode root) {
    this.root = root;
  }

  public List<ScoredCandidate> computeAllCandidates(GameContext<Trie> context)
  {
    List<ScoredCandidate> all = new ArrayList<>();
    Set<String> unique = new HashSet<>();
    LinkedList<Tile> rack = context.getRack();
    BoardStateUnit[][] played = context.board();

    java.util.function.BiConsumer<Integer, Integer> generateAtHook = (x, y) -> {
      this.generate(x, y, x, y, rack, new LinkedList<>(), 0, all, unique, this.root, Direction.RIGHT, played);
      this.generate(x, y, x, y, rack, new LinkedList<>(), 0, all, unique, this.root, Direction.DOWN, played);
    };

    if (context.moveCount() == 1) {
      generateAtHook.accept(7, 7);
    } else {
      for (int y = 0; y < 15; y++) {
        for (int x = 0; x < 15; x++) {
          generateAtHook.accept(x, y);
        }
      }
    }

    all.sort((one, two) -> two.getScore() - one.getScore());
    return all;
  }

  private void generate(
          int hX, int hY, int x, int y, LinkedList<Tile> rack, LinkedList<EnrichedTilePlacement> placed,
          final int accumulated, List<ScoredCandidate> all, Set<String> unique, TrieNode node,
          Direction d, BoardStateUnit[][] played)
  {
    Tile tile = played[y][x].getTile();
    Direction i = d.inverse();
    TrieNode childNode;

    java.util.function.BiConsumer<TrieNode, Integer> evaluateAndProceed = (child, accumulatedScore) -> {
      if (child.getTerminal() && d.nextTile(x, y, played) == null) {
        int scored;
        if ((scored = this.applyScorer(played, placed, accumulatedScore)) > 0) {
          List<TilePlacement> placements = placed.stream().map(EnrichedTilePlacement::getRoot).collect(Collectors.toList());
          SerializationResult result = this.contextSerialize(placements, d);
          if (!unique.contains(result.getSerialized())) {
            unique.add(result.getSerialized());
            all.add(new ScoredCandidate(placements, result.getNormalized(), scored));
          }
        }
      }
      Coordinates next;
      TrieNode crossAnchor;
      if ((next = d.nextCoordinates(x, y)) != null) {
        this.generate(hX, hY, next.getX(), next.getY(), rack, placed, accumulatedScore, all, unique, child, d, played);
      } else if ((crossAnchor = child.getChild(Trie.DELIMITER)) != null && (next = i.nextCoordinates(hX, hY)) != null) {
        this.generate(hX, hY, next.getX(), next.getY(), rack, placed, accumulatedScore, all, unique, crossAnchor, i, played);
      }
    };

    if (tile == null) {
      int currentPlacedCount = placed.size();
      int rackCount = rack.size();

      if (rackCount > 0) {
        Set<Character> visited = new HashSet<>();

        for (int r = 0; r < rackCount; r++) {
          Tile toPlace = rack.removeFirst();

          java.util.function.BiConsumer<Character, Boolean> tryLetterPlacement = (letter, isBlank) -> {
            Tile resolvedTile = isBlank ? new Tile(toPlace.getLetter(), toPlace.getScore(), letter) : toPlace;
            TrieNode child;
            List<TilePlacement> cross;
            if ((child = node.getChild(letter)) != null && (cross = this.computeCrossWord(x, y, resolvedTile, d, played)) != null) {
              TilePlacement placement = new TilePlacement(x, y, resolvedTile);
              List<TilePlacement> resolvedCross = cross.size() > 0 ? cross : null;
              placed.add(new EnrichedTilePlacement(placement, resolvedCross));
              evaluateAndProceed.accept(child, accumulated);
              while (placed.size() > currentPlacedCount) {
                placed.removeLast();
              }
            }
          };

          char letter = toPlace.getLetter();
          if (!visited.contains(letter)) {
            visited.add(letter);
            if (letter == '*') {
              char[] alphabet = Alphabet.letters;
              for (int l = 1; l <= 26; l++) {
                tryLetterPlacement.accept(alphabet[l], true);
              }
            } else {
              tryLetterPlacement.accept(letter, false);
            }
          }

          rack.add(toPlace);
        }
      }

      TrieNode crossAnchor;
      Coordinates next;
      if (currentPlacedCount > 0 && (crossAnchor = node.getChild(Trie.DELIMITER)) != null && (next = i.nextCoordinates(hX, hY)) != null) {
        this.generate(hX, hY, next.getX(), next.getY(), rack, placed, accumulated, all, unique, crossAnchor, i, played);
      }
    } else if (node != null && (childNode = node.getChild(tile.getResolvedLetter())) != null) {
      evaluateAndProceed.accept(childNode, accumulated + tile.getScore());
    }
  }

  private List<TilePlacement> computeCrossWord(int sX, int sY, Tile toPlace, Direction d, BoardStateUnit[][] played)
  {
    d = d.perpendicular();
    if (d.nextTile(sX, sY, played) == null && d.inverse().nextTile(sX, sY, played) == null) {
      return Collections.emptyList();
    }
    List<TilePlacement> placements = new ArrayList<>();
    Tile tile = toPlace;
    TrieNode node = this.root;
    int x = sX;
    int y = sY;
    Direction original = d;

    while (tile != null) {
      placements.add(new TilePlacement(x, y, tile));
      if ((node = node.getChild(tile.getResolvedLetter())) == null) {
        break;
      }
      NextTile next;
      if ((next = d.nextTile(x, y, played)) != null) {
        x = next.getX();
        y = next.getY();
        tile = next.getTile();
      } else {
        d = d.inverse();
        if (d.equals(original) || (next = d.nextTile(sX, sY, played)) == null) {
          break;
        }
        x = next.getX();
        y = next.getY();
        tile = next.getTile();
        if (tile != null && (node = node.getChild(Trie.DELIMITER)) == null) {
          break;
        }
      }
    }

    if (node != null && node.getTerminal()) {
      placements.sort(Direction.along(d));
      return placements;
    }
    return null;
  }

  private int applyScorer(BoardStateUnit[][] played, List<EnrichedTilePlacement> placements, int accumulated)
  {
    List<List<TilePlacement>> flattened = new ArrayList<>();
    flattened.add(new ArrayList<>(placements.size()));
    for (EnrichedTilePlacement placement : placements) {
      flattened.get(0).add(placement.getRoot());
      if (placement.getCross() != null) {
        flattened.add(placement.getCross());
      }
    }

    int sum = 0;
    for (List<TilePlacement> word : flattened) {
      sum += this.computeScoreOf(played, word, accumulated);
      accumulated = 0;
    }

    return sum;
  }

  private SerializationResult contextSerialize(List<TilePlacement> placements, Direction direction)
  {
    Direction normalized = direction.normalize();
    placements.sort(Direction.along(normalized));
    String serialized = placements.stream().map(p -> {
      Tile tile = p.getTile();
      char resolved =  tile.getLetterProxy() != null ? tile.getLetterProxy() : Character.MIN_VALUE;
      return String.format("%c:%c%d,%d", resolved, tile.getLetter(), p.getX(), p.getY());
    }).collect(Collectors.joining(","));
    return new SerializationResult(serialized, normalized.name());
  }

  private int computeScoreOf(BoardStateUnit[][] played, List<TilePlacement> placements, int sum)
  {
    int wordMultiplier = 1;
    int newTiles = 0;

    for (TilePlacement placement : placements) {
      Tile tile = placement.getTile();
      BoardStateUnit state = played[placement.getY()][placement.getX()];
      if (state.getTile() == null) {
        newTiles++;
      }
      if (state.getMultiplier() == null || state.getTile() != null) {
        sum += tile.getScore();
      } else {
        int letterValue = state.getMultiplier().getLetterValue();
        int wordValue = state.getMultiplier().getWordValue();
        sum += (letterValue * tile.getScore());
        wordMultiplier *= wordValue;
      }
    }

    int total = wordMultiplier * sum;
    if (newTiles == RackView.CAPACITY) {
      total += 50;
    }
    return total;
  }

}
