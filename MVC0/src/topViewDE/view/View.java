package topViewDE.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public interface View<M,D> extends Visit<M,D>{
  double centerX();//always positive
  double centerY();//always positive
  KeyListener getKeyListener();
  D get(M m,Viewport<M,D> v,int x,int y, int z);
  //from visit: void drawCell(Viewport<M,D> v,Graphics2D g,int x,int y,int z);
  M getMap();
  double getCameraZ();
  JFrame getFrame();
  static int max=51;
  static int maxZ=300;
  static int half=26;
  static double scaleZ=0.5d;
  void setCurrentViewPort(Viewport<M,D> v);
  Viewport<M,D> getCurrentViewPort();
  default void updateCurrentViewPort() {
    //assert !SwingUtilities.isEventDispatchThread();
    assert centerX()>=0d;
    assert centerY()>=0d;
    double elevation=getCameraZ();
    int offX=(int)centerX();
    int offY=(int)centerY();
    double extraX=centerX()-offX;//only ok if centerX>=0
    double extraY=centerY()-offY;
    var v=new Viewport<M,D>(max+1,max+1,maxZ+1);
    for(int z=0;z<maxZ+1;z++) for(int x=0;x<max+1;x++)for(int y=0;y<max+1;y++)
      v.cachePoint(x, y, z, elevation, x+1d-extraX, y+1d-extraY,z*scaleZ);
    for(int z=0;z<maxZ;z++) for(int x=0;x<max;x++)for(int y=0;y<max;y++) {
      D d=get(getMap(),v,x+offX-half,y+offY-half,z);
      v.set(d,v.coordDs(x,y,z));
    }
    setCurrentViewPort(v);
  }
  default void renderViewPort(Graphics2D g, M m,double elevation) {
    assert SwingUtilities.isEventDispatchThread();
    visitQuadrants(getCurrentViewPort(),g,max,max,maxZ);
  }
  default void onClose() {}
  default void initializeApp() {
    JFrame f=getFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){View.this.onClose();}
      });
    f.getContentPane().add(new GameMap<M,D>(this));
    f.pack();
    f.addKeyListener(getKeyListener());
    }
}
class GameMap<M,D> extends JComponent{
  private static final long serialVersionUID = 1L;
  @Override public Dimension getPreferredSize() {return new Dimension(800,800);}
  View<M,D> game;
  GameMap(View<M,D> game){this.game=game;setDoubleBuffered(true);}
  @Override public void paintComponent(Graphics g) {
    game.renderViewPort((Graphics2D)g,game.getMap(),game.getCameraZ());
  }
}
interface Visit<M,D>{
  void drawCell(Viewport<M,D> map,Graphics2D g,int x,int y,int z);
  default void visitQuadrants(Viewport<M,D> map,Graphics2D g,int maxX,int maxY,int maxZ) {
    assert maxX%2!=0;
    assert maxY%2!=0;
    maxX=maxX/2;
    maxY=maxY/2;
    for(int z = 0; z < maxZ; z+=1){
      for(int y = 0; y < maxY; y+=1)
        for (int x = 0; x < maxX; x+=1)
          drawCell(map,g,x,y,z);
      for(int y = 0; y < maxY; y+=1)
        for (int x = maxX; x > 0; x-=1)
          drawCell(map,g,maxX+x,y,z); 
      for(int y = maxY; y > 0; y-=1)
        for (int x = maxX; x > 0; x-=1)
          drawCell(map,g,maxX+x,maxY+y,z);
      for(int y = maxY; y > 0; y-=1)
        for (int x = 0; x < maxX; x+=1)
          drawCell(map,g,x,maxY+y,z);
      for (int x = 0; x < maxX; x+=1)
        drawCell(map,g,x,maxY,z);
      for (int x = maxX; x > 0; x-=1)
        drawCell(map,g,maxX+x,maxY,z);
      for (int y = 0; y < maxY; y+=1)
        drawCell(map,g,maxX,y,z);
      for (int y = maxY; y > 0; y-=1)
        drawCell(map,g,maxX,maxY+y,z);
      drawCell(map,g,maxX,maxY,z);
    }
  }
}