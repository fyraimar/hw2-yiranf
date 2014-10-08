package edu.cmu.yiranf.hw2.util;

public class IntPair {
  int x = -1;
  int y = -1;
  
  public IntPair(int nx, int ny) {
    x = nx;
    y = ny;
  }
  
  public int getX() {
    return x;
  }
  
  public int getY() {
    return y;
  }
  
  public boolean equals(IntPair opair) {
    return opair.x == x && opair.y == y;
  }
}