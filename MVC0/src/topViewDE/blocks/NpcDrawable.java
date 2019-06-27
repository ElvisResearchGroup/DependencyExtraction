package topViewDE.blocks;

import java.awt.Color;
import java.awt.Image;

import topViewDE.blocks.Drawable.Transparent;

public class NpcDrawable implements Transparent{
  public NpcDrawable(Drawable over,Image img,
    int side, int x,int y) {
    this.over = over;
    this.img=img;
    this.side=side;
    this.x=x;
    this.y=y;
  }
  Drawable over;
  Image img;
  int side;
  int x;
  int y;
  public<V> void draw(Blocks<V> b,V v,int x, int y, int z) {
    //Decoration.super.draw(b,v,x,y,z);
    over.draw(b,v,x,y,z);
    Cube.fill4(b,v,new Color(100,100,100,100),
        b.coordPs(v,x,y,z+1), b.coordPs(v,x+1,y,z+1), b.coordPs(v,x+1,y+1,z+1), b.coordPs(v,x,y+1,z+1));

    b.getGraphics(v).drawImage(img,this.x,this.y,side,side,null);
  }
}