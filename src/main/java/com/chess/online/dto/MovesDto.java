package com.chess.online.dto;

import com.chess.online.game.Location;

import java.util.Set;

public class MovesDto {
    Location from;
    Set<Location> to;

  public MovesDto(Location from, Set<Location> to) {
    this.from = from;
    this.to = to;
  }

  public MovesDto() { }

  public Location getFrom() {
    return from;
  }

  public void setFrom(Location from) {
    this.from = from;
  }

  public Set<Location> getTo() {
    return to;
  }

  public void setTo(Set<Location> to) {
    this.to = to;
  }
}
