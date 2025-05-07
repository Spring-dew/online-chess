package com.chess.online.services;

import com.chess.online.dto.MovesDto;
import com.chess.online.dto.MovesResponseDto;
import com.chess.online.entities.GameState;
import com.chess.online.game.ChessEnum;
import com.chess.online.game.Location;
import com.chess.online.game.Piece;
import com.chess.online.game.PieceFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {

  private Random rand = new Random();
  private Set<String> blitzUsers = new HashSet<>();
  private Set<String> bulletUsers = new HashSet<>();
  private Set<String> rapidUsers = new HashSet<>();
  private Set<String> classicUsers = new HashSet<>();

  public Map<String, String> addUserInBlitz(String userID) {
    this.blitzUsers.add(userID);
    if (this.blitzUsers.size() >= 2) {
      this.blitzUsers.remove(userID);
      String opponent = this.blitzUsers.stream().findAny().get();
      this.blitzUsers.remove(opponent);
      Map<String, String> map = new HashMap<>();
      map.put(userID, "w");
      map.put(opponent, "b");
      map.put("gameID", String.valueOf(rand.nextLong()));
      return map;
    }
    return null;
  }

  public Map<String, String> addUserInBullet(String userID) {
    this.bulletUsers.add(userID);
    if (this.bulletUsers.size() >= 2) {
      this.bulletUsers.remove(userID);
      String opponent = this.bulletUsers.stream().findAny().get();
      this.bulletUsers.remove(opponent);
      Map<String, String> map = new HashMap<>();
      map.put(userID, "w");
      map.put(opponent, "b");
      map.put("gameID", String.valueOf(rand.nextLong()));
      return map;
    }
    return null;
  }

  public Map<String, String> addUserInRapid(String userID) {
    this.rapidUsers.add(userID);
    if (this.rapidUsers.size() >= 2) {
      this.rapidUsers.remove(userID);
      String opponent = this.rapidUsers.stream().findAny().get();
      this.rapidUsers.remove(opponent);
      Map<String, String> map = new HashMap<>();
      map.put(userID, "w");
      map.put(opponent, "b");
      map.put("gameID", String.valueOf(rand.nextLong()));
      return map;
    }
    return null;
  }

  public Map<String, String> addUserInClassic(String userID) {
    this.classicUsers.add(userID);
    if (this.classicUsers.size() >= 2) {
      this.classicUsers.remove(userID);
      String opponent = this.classicUsers.stream().findAny().get();
      this.classicUsers.remove(opponent);
      Map<String, String> map = new HashMap<>();
      map.put(userID, "w");
      map.put(opponent, "b");
      map.put("gameID", String.valueOf(rand.nextLong()));
      return map;
    }
    return null;
  }

  /**
   * takes fen string as an argument and returns the parsed object.
   */
  public GameState fenParser(String fen, GameState state) {

    int i = 0, row = 0, col = 0, s = 0, spaceCount = 0;
    String hm = "", fm = "";

    while (i < fen.length()) {
      if (s == 0 && '/' == fen.charAt(i)) {
        row++;
        i++;
        col = 0;
        continue;
      }
      if (' ' == fen.charAt(i)) {
        i++;
        s++;
        continue;
      }
      if (s == 0) {
        Piece piece;
        switch (fen.charAt(i)) {
          case 'r':
            piece = PieceFactory.getPiece(ChessEnum.PieceType.ROOK, ChessEnum.PieceColor.BLACK, new Location(row, col));
            break;
          case 'n':
            piece = PieceFactory.getPiece(ChessEnum.PieceType.KNIGHT, ChessEnum.PieceColor.BLACK, new Location(row, col));
            break;
          case 'b':
            piece = PieceFactory.getPiece(ChessEnum.PieceType.BISHOP, ChessEnum.PieceColor.BLACK, new Location(row, col));
            break;
          case 'q':
            piece = PieceFactory.getPiece(ChessEnum.PieceType.QUEEN, ChessEnum.PieceColor.BLACK, new Location(row, col));
            break;
          case 'k':
            piece = PieceFactory.getPiece(ChessEnum.PieceType.KING, ChessEnum.PieceColor.BLACK, new Location(row, col));
            state.setBlackKing("" + row + col);
            break;
          case 'p':
            piece = PieceFactory.getPiece(ChessEnum.PieceType.PAWN, ChessEnum.PieceColor.BLACK, new Location(row, col));
            break;
          case 'P':
            piece = PieceFactory.getPiece(ChessEnum.PieceType.PAWN, ChessEnum.PieceColor.WHITE, new Location(row, col));
            break;
          case 'R':
            piece = PieceFactory.getPiece(ChessEnum.PieceType.ROOK, ChessEnum.PieceColor.WHITE, new Location(row, col));
            break;
          case 'N':
            piece = PieceFactory.getPiece(ChessEnum.PieceType.KNIGHT, ChessEnum.PieceColor.WHITE, new Location(row, col));
            break;
          case 'B':
            piece = PieceFactory.getPiece(ChessEnum.PieceType.BISHOP, ChessEnum.PieceColor.WHITE, new Location(row, col));
            break;
          case 'Q':
            piece = PieceFactory.getPiece(ChessEnum.PieceType.QUEEN, ChessEnum.PieceColor.WHITE, new Location(row, col));
            break;
          case 'K':
            piece = PieceFactory.getPiece(ChessEnum.PieceType.KING, ChessEnum.PieceColor.WHITE, new Location(row, col));
            state.setWhiteKing("" + row + col);
            break;
          default:
            piece = null;
            spaceCount = Integer.parseInt(String.valueOf(fen.charAt(i)));
        }
        if (spaceCount == 0) {
          state.getBoard()[row][col++] = piece;
        } else {
          for (int j = 0; j < spaceCount; j++) {
            state.getBoard()[row][col++] = PieceFactory.getPiece(ChessEnum.PieceType.SPACE, null, new Location(row, col));
          }
          spaceCount = 0;
        }
        i++;
      } else if (s == 1) {
        if (fen.charAt(i) == 'w') {
          state.setActiveColor("w");
        } else {
          state.setActiveColor("b");
        }
        i++;
      } else if (s == 2) {
        switch (fen.charAt(i)) {
          case 'K':
            state.setCastleWhite(state.getCastleWhite() + "K");
            break;
          case 'Q':
            state.setCastleWhite(state.getCastleWhite() + "Q");
            break;
          case 'k':
            state.setCastleBlack(state.getCastleBlack() + "k");
            break;
          case 'q':
            state.setCastleBlack(state.getCastleBlack() + "q");
            break;
        }
        i++;
      } else if (s == 3) {
        if (!"-".equals(String.valueOf(fen.charAt(i)))) {
          state.setEnpassant(state.getEnpassant() + fen.charAt(i));
        }
        i++;
      } else if (s == 4) {
        hm += fen.charAt(i);
        i++;
      } else if (s == 5) {
        fm += fen.charAt(i);
        i++;
      }
    }

    state.setHalfMoveCount(Integer.parseInt(hm));
    state.setFullMoveCount(Integer.parseInt(fm));

    return state;
  }

  private GameState update(GameState state) {
    Piece[][] board = state.getBoard();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board.length; j++) {
        board[i][j].update(new Location(i, j), board, state);
      }
    }
    return state;
  }

  public MovesResponseDto getMoves(String fen) {
    List<MovesDto> availableMoves = new ArrayList<>();
    GameState state = new GameState();
    boolean isAnyMoveAvailable = false;


    state = this.fenParser(fen, new GameState());
    this.update(state);
    Piece.updateKing(state);
    isAnyMoveAvailable = Piece.updateSafeMoves(state, state.getActiveColor());
    Piece.updateCastlingMoves(state);
    System.out.println("Active Color: " + state.getActiveColor());
    //    this.updateKing(state);
    Piece[][] board = state.getBoard();
    availableMoves = new ArrayList<>();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board.length; j++) {
        ChessEnum.PieceType type = board[i][j].getPieceType();
        availableMoves.add(new MovesDto(new Location(i, j), board[i][j].getMoves()));
      }
    }


    return new MovesResponseDto(
      fen,
      availableMoves,
      !isAnyMoveAvailable,
      state.getActiveColor().equalsIgnoreCase("w") ? "b" : "w",
      state.getActiveColor(),
      state.isWhiteKingUnderCheck(),
      state.isBlackKingUnderCheck()
    );
  }

  public MovesResponseDto makeMove(String fen) {
    return getMoves(fen);
  }
}

