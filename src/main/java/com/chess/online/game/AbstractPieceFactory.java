package com.chess.online.game;

import java.util.*;

public class AbstractPieceFactory {

  private static AbstractPiece ABSTRACT_KING;
  private static AbstractPiece ABSTRACT_QUEEN;
  private static AbstractPiece ABSTRACT_ROOK;
  private static AbstractPiece ABSTRACT_BISHOP;
  private static AbstractPiece ABSTRACT_KNIGHT;
  private static AbstractPiece ABSTRACT_PAWN;
  private static AbstractPiece ABSTRACT_SPACE;

  private static AbstractPieceFactory instance;

  private AbstractPieceFactory() {
  }

  public static AbstractPiece createAbstractPiece(ChessEnum.PieceType type) {
    List<LinkedHashSet<Location>> moves = new ArrayList<>();
    switch (type) {
      case KING -> {
        return ABSTRACT_KING;
      }
      case QUEEN -> {
        return ABSTRACT_QUEEN;
      }
      case ROOK -> {
        return ABSTRACT_ROOK;
      }
      case BISHOP -> {
        return ABSTRACT_BISHOP;
      }
      case KNIGHT -> {
        return ABSTRACT_KNIGHT;
      }
      case PAWN -> {
        return ABSTRACT_PAWN;
      }
      default -> {
        return ABSTRACT_SPACE;
      }
    }
  }

  private static void createInstance() {
    ABSTRACT_KING = () -> {
      List<LinkedHashSet<Location>> moves = new ArrayList<>();
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(-1, -1));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(-1, 0));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(-1, 1));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(0, -1));
        // castling
//        add(new Location(0, -2));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(0, 1));
        // castling
//        add(new Location(0, 2));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(1, -1));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(1, 0));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(1, 1));
      }});
      return moves;
    };


    ABSTRACT_QUEEN = () -> {
      List<LinkedHashSet<Location>> moves = new ArrayList<>();
      // diagonal
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(-1, -1));
        add(new Location(-2, -2));
        add(new Location(-3, -3));
        add(new Location(-4, -4));
        add(new Location(-5, -5));
        add(new Location(-6, -6));
        add(new Location(-7, -7));
      }});

      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(1, 1));
        add(new Location(2, 2));
        add(new Location(3, 3));
        add(new Location(4, 4));
        add(new Location(5, 5));
        add(new Location(6, 6));
        add(new Location(7, 7));
      }});

      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(1, -1));
        add(new Location(2, -2));
        add(new Location(3, -3));
        add(new Location(4, -4));
        add(new Location(5, -5));
        add(new Location(6, -6));
        add(new Location(7, -7));
      }});

      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(-1, 1));
        add(new Location(-2, 2));
        add(new Location(-3, 3));
        add(new Location(-4, 4));
        add(new Location(-5, 5));
        add(new Location(-6, 6));
        add(new Location(-7, 7));
      }});

      // horizontal
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(1, 0));
        add(new Location(2, 0));
        add(new Location(3, 0));
        add(new Location(4, 0));
        add(new Location(5, 0));
        add(new Location(6, 0));
        add(new Location(7, 0));
      }});

      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(-1, 0));
        add(new Location(-2, 0));
        add(new Location(-3, 0));
        add(new Location(-4, 0));
        add(new Location(-5, 0));
        add(new Location(-6, 0));
        add(new Location(-7, 0));
      }});

      // vertical
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(0, 1));
        add(new Location(0, 2));
        add(new Location(0, 3));
        add(new Location(0, 4));
        add(new Location(0, 5));
        add(new Location(0, 6));
        add(new Location(0, 7));
      }});

      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(0, -1));
        add(new Location(0, -2));
        add(new Location(0, -3));
        add(new Location(0, -4));
        add(new Location(0, -5));
        add(new Location(0, -6));
        add(new Location(0, -7));
      }});
      return moves;
    };

    ABSTRACT_ROOK = () -> {
      List<LinkedHashSet<Location>> moves = new ArrayList<>();

      // horizontal
      moves.add(new LinkedHashSet<Location>() {
        {
          add(new Location(1, 0));
          add(new Location(2, 0));
          add(new Location(3, 0));
          add(new Location(4, 0));
          add(new Location(5, 0));
          add(new Location(6, 0));
          add(new Location(7, 0));
        }
      });

      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(-1, 0));
        add(new Location(-2, 0));
        add(new Location(-3, 0));
        add(new Location(-4, 0));
        add(new Location(-5, 0));
        add(new Location(-6, 0));
        add(new Location(-7, 0));
      }});

      // vertical
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(0, 1));
        add(new Location(0, 2));
        add(new Location(0, 3));
        add(new Location(0, 4));
        add(new Location(0, 5));
        add(new Location(0, 6));
        add(new Location(0, 7));
      }});

      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(0, -1));
        add(new Location(0, -2));
        add(new Location(0, -3));
        add(new Location(0, -4));
        add(new Location(0, -5));
        add(new Location(0, -6));
        add(new Location(0, -7));
      }});
      return moves;
    };

    ABSTRACT_BISHOP = () -> {
      List<LinkedHashSet<Location>> moves = new ArrayList<>();
      // diagonal
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(-1, -1));
        add(new Location(-2, -2));
        add(new Location(-3, -3));
        add(new Location(-4, -4));
        add(new Location(-5, -5));
        add(new Location(-6, -6));
        add(new Location(-7, -7));
      }});

      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(1, 1));
        add(new Location(2, 2));
        add(new Location(3, 3));
        add(new Location(4, 4));
        add(new Location(5, 5));
        add(new Location(6, 6));
        add(new Location(7, 7));
      }});

      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(1, -1));
        add(new Location(2, -2));
        add(new Location(3, -3));
        add(new Location(4, -4));
        add(new Location(5, -5));
        add(new Location(6, -6));
        add(new Location(7, -7));
      }});

      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(-1, 1));
        add(new Location(-2, 2));
        add(new Location(-3, 3));
        add(new Location(-4, 4));
        add(new Location(-5, 5));
        add(new Location(-6, 6));
        add(new Location(-7, 7));
      }});

      return moves;
    };

    ABSTRACT_KNIGHT = () -> {
      List<LinkedHashSet<Location>> moves = new ArrayList<>();
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(-1, -2));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(+1, -2));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(-1, 2));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(+1, 2));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(-2, -1));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(-2, +1));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(2, -1));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(2, +1));
      }});
      return moves;
    };

    ABSTRACT_PAWN = () -> {
      List<LinkedHashSet<Location>> moves = new ArrayList<>();
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(-1, 0));
        add(new Location(-2, 0));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(+1, 0));
        add(new Location(+2, 0));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(-1, -1));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(+1, -1));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(-1, +1));
      }});
      moves.add(new LinkedHashSet<Location>() {{
        add(new Location(+1, +1));
      }});
      return moves;
    };

    ABSTRACT_SPACE = Collections::emptyList;

  }

  public static AbstractPieceFactory getInstance() {
    if (instance == null) {
      createInstance();
    }
    return instance;
  }
}
