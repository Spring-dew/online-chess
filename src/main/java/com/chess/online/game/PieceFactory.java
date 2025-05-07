package com.chess.online.game;

import java.util.Set;
import java.util.stream.Collectors;

public class PieceFactory {

  private PieceFactory() { }

  public static Piece getPiece(ChessEnum.PieceType pieceType, ChessEnum.PieceColor color, Location location) {
      return new Piece(AbstractPieceFactory.getInstance().createAbstractPiece(pieceType), pieceType, color);
  }

}
