package edu.cmu.yiranf.hw2.util;

public class intPair {
  int x = -1;
  int y = -1;
  
  public intPair(int nx, int ny) {
    x = nx;
    y = ny;
  }
  
  public int getX() {
    return x;
  }
  
  public int getY() {
    return y;
  }
  
  public boolean equals(intPair opair) {
    return opair.x == x && opair.y == y;
  }
}