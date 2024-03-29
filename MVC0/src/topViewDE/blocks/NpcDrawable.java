package topViewDE.blocks;

import java.awt.Color;
import java.awt.Graphics2D;
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
  public<V> void draw(Blocks<V> b,V v,Graphics2D g,int x, int y, int z) {
    if(Drawable.underCamera(b,z))return;
    //Decoration.super.draw(b,v,x,y,z);
    over.draw(b,v,g,x,y,z);
    Cube.fill4(b,v,g,new Color(100,100,100,100),
        b.coordPs(v,x,y,z+1), b.coordPs(v,x+1,y,z+1), b.coordPs(v,x+1,y+1,z+1), b.coordPs(v,x,y+1,z+1));

    g.drawImage(img,this.x,this.y,side,side,null);
  }
}