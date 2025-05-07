package com.chess.online.game;

import java.util.Objects;

public class Location {
  private int x;
  private int y;

  public Location(int a, int b) {
    this.x = a;
    this.y = b;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return y;
  }

  public Location add(Location l) {
    return new Location(this.x + l.x, this.y + l.y);
  }

  public boolean isValid() {
    if (this.x < 0 || this.x > 7) return false;
    if (this.y < 0 || this.y > 7) return false;
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Location location = (Location) o;
    return x == location.x && y == location.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    return "Location{" + "x=" + x + ", y=" + y + '}';
  }
}
