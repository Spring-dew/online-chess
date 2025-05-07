package com.chess.online.dto;

import java.util.List;

public class MovesResponseDto {

  boolean isBlackKingUnderCheck;
  boolean isWhiteKingUnderCheck;
  List<MovesDto> availableMoves;
  boolean isGameEnded = false;
  String winnerColor = "w";
  String activeColor = "w";
  String fen = "";

  public MovesResponseDto(String fen, List<MovesDto> availableMoves, boolean isGameEnded,
                          String winnerColor, String activeColor, boolean isWhiteKingUnderCheck, boolean isBlackKingUnderCheck) {
    this.fen = fen;
    this.availableMoves = availableMoves;
    this.isGameEnded = isGameEnded;
    this.winnerColor = winnerColor;
    this.activeColor = activeColor;
    this.isWhiteKingUnderCheck = isWhiteKingUnderCheck;
    this.isBlackKingUnderCheck = isBlackKingUnderCheck;
  }

  public MovesResponseDto() {
  }

  public String getFen() {
    return fen;
  }

  public void setFen(String fen) {
    this.fen = fen;
  }

  public List<MovesDto> getAvailableMoves() {
    return availableMoves;
  }

  public void setAvailableMoves(List<MovesDto> availableMoves) {
    this.availableMoves = availableMoves;
  }

  public boolean isGameEnded() {
    return isGameEnded;
  }

  public void setGameEnded(boolean gameEnded) {
    isGameEnded = gameEnded;
  }

  public String getWinnerColor() {
    return winnerColor;
  }

  public void setWinnerColor(String winnerColor) {
    this.winnerColor = winnerColor;
  }

  public String getActiveColor() {
    return activeColor;
  }

  public void setActiveColor(String activeColor) {
    this.activeColor = activeColor;
  }

  public boolean isBlackKingUnderCheck() {
    return isBlackKingUnderCheck;
  }

  public void setBlackKingUnderCheck(boolean blackKingUnderCheck) {
    isBlackKingUnderCheck = blackKingUnderCheck;
  }

  public boolean isWhiteKingUnderCheck() {
    return isWhiteKingUnderCheck;
  }

  public void setWhiteKingUnderCheck(boolean whiteKingUnderCheck) {
    isWhiteKingUnderCheck = whiteKingUnderCheck;
  }
}
