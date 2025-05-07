package com.chess.online.entities;

import com.chess.online.game.Piece;

public class GameState {
  Piece[][] board = new Piece[8][8];
  String activeColor = "w";
  String castleWhite = "";
  String castleBlack = "";
  String enpassant = "";
  int halfMoveCount = 0;
  int fullMoveCount = 0;
  String blackKing = "00";
  String whiteKing = "00";
  boolean blackKingUnderCheck = false;
  boolean whiteKingUnderCheck = false;

  public boolean isBlackKingUnderCheck() {
    return blackKingUnderCheck;
  }

  public void setBlackKingUnderCheck(boolean blackKingUnderCheck) {
    this.blackKingUnderCheck = blackKingUnderCheck;
  }

  public boolean isWhiteKingUnderCheck() {
    return whiteKingUnderCheck;
  }

  public void setWhiteKingUnderCheck(boolean whiteKingUnderCheck) {
    this.whiteKingUnderCheck = whiteKingUnderCheck;
  }


  public String getWhiteKing() {
    return whiteKing;
  }

  public void setWhiteKing(String whiteKing) {
    this.whiteKing = whiteKing;
  }

  public String getBlackKing() {
    return blackKing;
  }

  public void setBlackKing(String blackKing) {
    this.blackKing = blackKing;
  }

  public Piece[][] getBoard() {
    return board;
  }

  public void setBoard(Piece[][] board) {
    this.board = board;
  }

  public String getActiveColor() {
    return activeColor;
  }

  public void setActiveColor(String activeColor) {
    this.activeColor = activeColor;
  }

  public String getCastleWhite() {
    return castleWhite;
  }

  public void setCastleWhite(String castleWhite) {
    this.castleWhite = castleWhite;
  }

  public String getCastleBlack() {
    return castleBlack;
  }

  public void setCastleBlack(String castleBlack) {
    this.castleBlack = castleBlack;
  }

  public String getEnpassant() {
    return enpassant;
  }

  public void setEnpassant(String enpassant) {
    this.enpassant = enpassant;
  }

  public int getHalfMoveCount() {
    return halfMoveCount;
  }

  public void setHalfMoveCount(int halfMoveCount) {
    this.halfMoveCount = halfMoveCount;
  }

  public int getFullMoveCount() {
    return fullMoveCount;
  }

  public void setFullMoveCount(int fullMoveCount) {
    this.fullMoveCount = fullMoveCount;
  }
}
