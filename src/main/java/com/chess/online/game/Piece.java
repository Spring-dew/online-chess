package com.chess.online.game;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.chess.online.entities.GameState;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.parameters.P;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Piece {
  private final ChessEnum.PieceColor color;
  private final AbstractPiece abstractPiece;

  private boolean foundOpponentPiece = false;


  private Location location;

  private Set<Location> moves;
  private final ChessEnum.PieceType type;

  public Piece(AbstractPiece abstractPiece, ChessEnum.PieceType type, ChessEnum.PieceColor color) {
    this.color = color;
    this.abstractPiece = abstractPiece;
    this.type = type;
  }

  public ChessEnum.PieceColor getColor() {
    return color;
  }

  public Location getLocation() {
    return location;
  }

  public Set<Location> getMoves() {
    if (this.moves == null) {
      Set<Location> set = new HashSet<>();
      this.abstractPiece.getMoves().stream().map(m -> m.stream().map(set::add)).close();
      return set;
    }
    return this.moves;
  }

  @Override
  protected Piece clone() throws CloneNotSupportedException {
    return new Piece(this.abstractPiece, this.type, this.color);
  }

  public AbstractPiece getAbstractPiece() {
    return this.abstractPiece;
  }

  public ChessEnum.PieceType getPieceType() {
    return this.type;
  }

  public void setMoves(Set<Location> moves) {
    this.moves = moves;
  }

  public static void updateKing(@NotNull GameState state) {
    String wk = state.getWhiteKing();
    Piece whiteKing = state.getBoard()[Integer.parseInt(String.valueOf(wk.charAt(0)))][Integer.parseInt(String.valueOf(wk.charAt(1)))];

    String bk = state.getBlackKing();
    Piece blackKing = state.getBoard()[Integer.parseInt(String.valueOf(bk.charAt(0)))][Integer.parseInt(String.valueOf(bk.charAt(1)))];

    Set<Location> whiteMoves = whiteKing.getMoves();
    Set<Location> blackMoves = blackKing.getMoves();

    for (int i = 0; i < state.getBoard().length; i++) {
      for (int j = 0; j < state.getBoard().length; j++) {
        if (state.getBoard()[i][j].getColor() == ChessEnum.PieceColor.WHITE) {
          if (state.getBoard()[i][j].getPieceType() == ChessEnum.PieceType.PAWN) {
            blackMoves.remove(new Location(i - 1, j - 1));
            blackMoves.remove(new Location(i - 1, j + 1));
          } else {
            blackMoves.removeAll(state.getBoard()[i][j].getMoves());
          }
        }
        if (state.getBoard()[i][j].getColor() == ChessEnum.PieceColor.BLACK) {
          if (state.getBoard()[i][j].getPieceType() == ChessEnum.PieceType.PAWN) {
            whiteMoves.remove(new Location(i + 1, j - 1));
            whiteMoves.remove(new Location(i + 1, j + 1));
          } else {
            whiteMoves.removeAll(state.getBoard()[i][j].getMoves());
          }
        }
      }
    }
    blackKing.setMoves(blackMoves);
    whiteKing.setMoves(whiteMoves);
  }

  public static void updateCastlingMoves(@NotNull GameState state) {
    // Queen Side White Castle
    if (state.getCastleWhite().contains("Q")) {
      boolean canCastle = true;
      // Only Space is allowed between King and Rook
      for (int i : List.of(3, 2, 1)) {
        if (state.getBoard()[7][i].getPieceType() != ChessEnum.PieceType.SPACE) {
          canCastle = false;
          break;
        }
      }
      if (canCastle) {
        // King should not be under Check and also King cannot move into or move through squares controlled by enemy piece
        for (int i : List.of(4, 3, 2)) {
          boolean isSafeLocation = Piece.isKingSafe(state.getBoard(), ChessEnum.PieceColor.WHITE, new Location(7, i));
          if (!isSafeLocation) {
            canCastle = false;
            break;
          }
        }
      }
      if (canCastle) {
        // King is eligible to castle in Queen Side
        Set<Location> set = new HashSet<>();
        set = state.getBoard()[7][4].getMoves();
        set.add(new Location(7, 2));
        state.getBoard()[7][4].setMoves(set);
      }
    }

    // King Side White Castle
    if (state.getCastleWhite().contains("K")) {
      boolean canCastle = true;
      // Only Space is allowed between King and Rook
      for (int i : List.of(5, 6)) {
        if (state.getBoard()[7][i].getPieceType() != ChessEnum.PieceType.SPACE) {
          canCastle = false;
          break;
        }
      }
      if (canCastle) {
        // King should not be under Check and also King cannot move into or move through squares controlled by enemy piece
        for (int i : List.of(4, 5, 6)) {
          boolean isSafeLocation = Piece.isKingSafe(state.getBoard(), ChessEnum.PieceColor.WHITE, new Location(7, i));
          if (!isSafeLocation) {
            canCastle = false;
            break;
          }
        }
      }
      if (canCastle) {
        // King is eligible to castle in King Side
        Set<Location> set = new HashSet<>();
        set = state.getBoard()[7][4].getMoves();
        set.add(new Location(7, 6));
        state.getBoard()[7][4].setMoves(set);
      }
    }

    // Queen Side Black Castle
    if (state.getCastleBlack().contains("q")) {
      boolean canCastle = true;
      // Only Space is allowed between King and Rook
      for (int i : List.of(3, 2, 1)) {
        if (state.getBoard()[0][i].getPieceType() != ChessEnum.PieceType.SPACE) {
          canCastle = false;
          break;
        }
      }
      if (canCastle) {
        // King should not be under Check and also King cannot move into or move through squares controlled by enemy piece
        for (int i : List.of(4, 3, 2)) {
          boolean isSafeLocation = Piece.isKingSafe(state.getBoard(), ChessEnum.PieceColor.BLACK, new Location(0, i));
          if (!isSafeLocation) {
            canCastle = false;
            break;
          }
        }
      }
      if (canCastle) {
        // King is eligible to castle in Queen Side
        Set<Location> set = new HashSet<>();
        set = state.getBoard()[0][4].getMoves();
        set.add(new Location(0, 2));
        state.getBoard()[0][4].setMoves(set);
      }
    }

    // King Side Black Castle
    if (state.getCastleBlack().contains("k")) {
      boolean canCastle = true;
      // Only Space is allowed between King and Rook
      for (int i : List.of(5, 6)) {
        if (state.getBoard()[0][i].getPieceType() != ChessEnum.PieceType.SPACE) {
          canCastle = false;
          break;
        }
      }
      if (canCastle) {
        // King should not be under Check and also King cannot move into or move through squares controlled by enemy piece
        for (int i : List.of(4, 5, 6)) {
          boolean isSafeLocation = Piece.isKingSafe(state.getBoard(), ChessEnum.PieceColor.BLACK, new Location(0, i));
          if (!isSafeLocation) {
            canCastle = false;
            break;
          }
        }
      }
      if (canCastle) {
        // King is eligible to castle in King Side
        Set<Location> set = new HashSet<>();
        set = state.getBoard()[0][4].getMoves();
        set.add(new Location(0, 6));
        state.getBoard()[0][4].setMoves(set);
      }
    }
  }

  public static boolean updateSafeMoves(@NotNull GameState state, String kingColor) {
    final boolean[] isAnyMoveAvailable = {false};
    Location kl;
    ChessEnum.PieceColor color;
    if ("w".equals(kingColor)) {
      String wk = state.getWhiteKing();
      color = ChessEnum.PieceColor.WHITE;
      kl = new Location(Integer.parseInt(String.valueOf(wk.charAt(0))), Integer.parseInt(String.valueOf(wk.charAt(1))));
      boolean isKingSafeWhite = Piece.isKingSafe(state.getBoard(), color, kl);
      state.setWhiteKingUnderCheck(!isKingSafeWhite);
    } else {
      color = ChessEnum.PieceColor.BLACK;
      String bk = state.getBlackKing();
      kl = new Location(Integer.parseInt(String.valueOf(bk.charAt(0))), Integer.parseInt(String.valueOf(bk.charAt(1))));
      boolean isKingSafeBlack = Piece.isKingSafe(state.getBoard(), color, kl);
      state.setBlackKingUnderCheck(!isKingSafeBlack);
    }

    Arrays.stream(state.getBoard()).sequential().forEach(m -> {
      Arrays.stream(m).sequential().forEach(e -> {
        if (e.getColor() == color
//          && e.getPieceType() != ChessEnum.PieceType.KING
        ) {
          Set<Location> safeMoves = new HashSet<>();
          for (Location l : e.getMoves()) {
            Piece[][] copy = Piece.getCopyOfBoard(state.getBoard());
            try {
              copy[l.getX()][l.getY()] = e.clone();
              copy[e.location.getX()][e.location.getY()] = PieceFactory.getPiece(ChessEnum.PieceType.SPACE, null, new Location(e.location.getX(), e.location.getY()));
              boolean safe = false;
              if (e.getPieceType() == ChessEnum.PieceType.KING) {
                safe = Piece.isKingSafe(copy, color, l);
              } else {
                safe = Piece.isKingSafe(copy, color, kl);
              }
              if (safe) {
                isAnyMoveAvailable[0] = true;
                safeMoves.add(l);
              }
            } catch (CloneNotSupportedException ex) {
              // do nothing;
            }
          }
          e.setMoves(safeMoves);
        }
      });
    });
    return isAnyMoveAvailable[0];
  }

  private static boolean isKingSafe(Piece[][] copy, ChessEnum.PieceColor color, Location kl) {
    // translate up
    for (int i = kl.getX() - 1; i >= 0; i--) {
      if (copy[i][kl.getY()].getPieceType() == ChessEnum.PieceType.SPACE) continue;
      if (copy[i][kl.getY()].getColor() == color) break;
      ChessEnum.PieceType type = copy[i][kl.getY()].getPieceType();
      if (type == ChessEnum.PieceType.ROOK || type == ChessEnum.PieceType.QUEEN) return false;
      else break;
    }
    // translate down
    for (int i = kl.getX() + 1; i <= 7; i++) {
      if (copy[i][kl.getY()].getPieceType() == ChessEnum.PieceType.SPACE) continue;
      if (copy[i][kl.getY()].getColor() == color) break;
      ChessEnum.PieceType type = copy[i][kl.getY()].getPieceType();
      if (type == ChessEnum.PieceType.ROOK || type == ChessEnum.PieceType.QUEEN) return false;
      else break;
    }
    // translate left
    for (int i = kl.getY() - 1; i >= 0; i--) {
      if (copy[kl.getX()][i].getPieceType() == ChessEnum.PieceType.SPACE) continue;
      if (copy[kl.getX()][i].getColor() == color) break;
      ChessEnum.PieceType type = copy[kl.getX()][i].getPieceType();
      if (type == ChessEnum.PieceType.ROOK || type == ChessEnum.PieceType.QUEEN) return false;
      else break;
    }
    // translate right
    for (int i = kl.getY() + 1; i <= 7; i++) {
      if (copy[kl.getX()][i].getPieceType() == ChessEnum.PieceType.SPACE) continue;
      if (copy[kl.getX()][i].getColor() == color) break;
      ChessEnum.PieceType type = copy[kl.getX()][i].getPieceType();
      if (type == ChessEnum.PieceType.ROOK || type == ChessEnum.PieceType.QUEEN) return false;
      else break;
    }
    // upper left diagonal
    for (int i = kl.getX() - 1, j = kl.getY() - 1; i >= 0 && j >= 0; i--, j--) {
      if (copy[i][j].getPieceType() == ChessEnum.PieceType.SPACE) continue;
      if (copy[i][j].getColor() == color) break;
      ChessEnum.PieceType type = copy[i][j].getPieceType();
      if (type == ChessEnum.PieceType.BISHOP || type == ChessEnum.PieceType.QUEEN) return false;
      else break;
    }
    // upper right diagonal
    for (int i = kl.getX() - 1, j = kl.getY() + 1; i >= 0 && j <= 7; i--, j++) {
      if (copy[i][j].getPieceType() == ChessEnum.PieceType.SPACE) continue;
      if (copy[i][j].getColor() == color) break;
      ChessEnum.PieceType type = copy[i][j].getPieceType();
      if (type == ChessEnum.PieceType.BISHOP || type == ChessEnum.PieceType.QUEEN) return false;
      else break;
    }
    // lower left diagonal
    for (int i = kl.getX() + 1, j = kl.getY() - 1; i <= 7 && j >= 0; i++, j--) {
      if (copy[i][j].getPieceType() == ChessEnum.PieceType.SPACE) continue;
      if (copy[i][j].getColor() == color) break;
      ChessEnum.PieceType type = copy[i][j].getPieceType();
      if (type == ChessEnum.PieceType.BISHOP || type == ChessEnum.PieceType.QUEEN) return false;
      else break;
    }
    // lower right diagonal
    for (int i = kl.getX() + 1, j = kl.getY() + 1; i <= 7 && j <= 7; i++, j++) {
      if (copy[i][j].getPieceType() == ChessEnum.PieceType.SPACE) continue;
      if (copy[i][j].getColor() == color) break;
      ChessEnum.PieceType type = copy[i][j].getPieceType();
      if (type == ChessEnum.PieceType.BISHOP || type == ChessEnum.PieceType.QUEEN) return false;
      else break;
    }
    // knight moves
    Location[] knightMoves = new Location[]{new Location(kl.getX() - 2, kl.getY() - 1), new Location(kl.getX() - 2, kl.getY() + 1), new Location(kl.getX() + 2, kl.getY() - 1), new Location(kl.getX() + 2, kl.getY() + 1), new Location(kl.getX() - 1, kl.getY() - 2), new Location(kl.getX() + 1, kl.getY() - 2), new Location(kl.getX() - 1, kl.getY() + 2), new Location(kl.getX() + 1, kl.getY() + 2),};
    for (Location l : knightMoves) {
      if (l.isValid() && copy[l.getX()][l.getY()].getColor() != color && copy[l.getX()][l.getY()].getPieceType() == ChessEnum.PieceType.KNIGHT)
        return false;
    }
    // pawn moves
    Location[] pawnMoves;
    if (color == ChessEnum.PieceColor.WHITE) {
      pawnMoves = new Location[]{new Location(kl.getX() - 1, kl.getY() - 1), new Location(kl.getX() - 1, kl.getY() + 1)};
    } else {
      pawnMoves = new Location[]{new Location(kl.getX() + 1, kl.getY() - 1), new Location(kl.getX() + 1, kl.getY() + 1)};
    }
    for (Location l : pawnMoves) {
      if (l.isValid() && copy[l.getX()][l.getY()].getColor() != color && copy[l.getX()][l.getY()].getPieceType() == ChessEnum.PieceType.PAWN)
        return false;
    }
    // king is safe
    return true;
  }

  public void update(Location pieceLocation, Piece[][] board, GameState state) {
    this.location = pieceLocation;
    List<LinkedHashSet<Location>> copy = this.abstractPiece.getMoves().stream().map(m -> {
      this.foundOpponentPiece = false;
      return m.stream().map(l -> l.add(pieceLocation)).takeWhile(l -> {
        if (!l.isValid()) return false;

        // PAWN MOVES
        if (board[pieceLocation.getX()][pieceLocation.getY()].getPieceType() == ChessEnum.PieceType.PAWN) {
          if (board[pieceLocation.getX()][pieceLocation.getY()].getColor() == ChessEnum.PieceColor.BLACK) {
            if (pieceLocation.getX() < l.getX()) {
              if (l.getY() == pieceLocation.getY()) {
                if (l.getX() - pieceLocation.getX() == 2) {
                  return pieceLocation.getX() == 1 && board[l.getX()][l.getY()].getPieceType() == ChessEnum.PieceType.SPACE;
                }
                return board[l.getX()][l.getY()].getPieceType() == ChessEnum.PieceType.SPACE;
              } else {
                return board[l.getX()][l.getY()].getColor() == ChessEnum.PieceColor.WHITE;
              }
            }
          } else {
            if (pieceLocation.getX() > l.getX()) {
              if (l.getY() == pieceLocation.getY()) {
                if (pieceLocation.getX() - l.getX() == 2) {
                  return pieceLocation.getX() == 6 && board[l.getX()][l.getY()].getPieceType() == ChessEnum.PieceType.SPACE;
                }
                return board[l.getX()][l.getY()].getPieceType() == ChessEnum.PieceType.SPACE;
              } else {
                return board[l.getX()][l.getY()].getColor() == ChessEnum.PieceColor.BLACK;
              }
            }
          }
          return false;
        }
        if (!this.foundOpponentPiece && board[l.getX()][l.getY()].getPieceType() == ChessEnum.PieceType.SPACE)
          return true;
        if (!this.foundOpponentPiece && board[l.getX()][l.getY()].getColor() == this.getOpponentColor()) {
          this.foundOpponentPiece = true;
          return true;
        }
        return false;
      }).collect(Collectors.toCollection(LinkedHashSet::new));
    }).toList();
    Set<Location> set = new HashSet<>();
    copy.forEach(set::addAll);
    this.moves = set;
  }

  private ChessEnum.PieceColor getOpponentColor() {
    if (this.color == ChessEnum.PieceColor.WHITE) return ChessEnum.PieceColor.BLACK;
    return ChessEnum.PieceColor.WHITE;
  }

  private static Piece[][] getCopyOfBoard(Piece[][] board) {
    Piece[][] copy = new Piece[8][8];
    try {
      for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board.length; j++) {
          copy[i][j] = board[i][j].clone();
        }
      }
    } catch (CloneNotSupportedException e) {
      // do nothing;
    }
    return copy;
  }

}
