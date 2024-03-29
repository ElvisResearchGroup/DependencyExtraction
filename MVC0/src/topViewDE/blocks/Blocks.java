package topViewDE.blocks;

import java.awt.Graphics2D;

public interface Blocks<V>{
  Drawable get(V view, int coord);
  void set(V view, Drawable elem, int coord);
  int maxX(V view);
  int maxY(V view);
  int maxZ(V view);
  int coordDs(V view, int x, int y, int z);
  int coordPs(V view, int x, int y, int z);
  int pixelX(V view, int coord);
  int pixelY(V view, int coord);
  double getCameraZ();
  default void drawCell(V view,Graphics2D g,int x,int y,int z) {
    var d=get(view,coordDs(view,x,y,z));
    assert d!=null: x+" "+y+" "+z;
    d.draw(this,view,g,x,y,z);
  }
}
