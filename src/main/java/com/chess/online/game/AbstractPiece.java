package com.chess.online.game;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@FunctionalInterface
public interface AbstractPiece {
  List<LinkedHashSet<Location>> getMoves();
}
