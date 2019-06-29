package topViewDE.game0;

import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.util.Map;

import javax.swing.JFrame;

import general.Direction;
import topViewDE.model0.*;
import topViewDE.simpleController.*;
import topViewDE.view.*;
import topViewDE.blocks.*;

public class Game extends JFrame implements 
    Model,
    View<ModelMap,Drawable>,
    Controller,
    Blocks<Viewport<ModelMap,Drawable>>
    {
  private static final long serialVersionUID = 1L;
  double cameraZ=30;
  private Map<Character,Runnable> actionMap=makeKeyMap();
  ModelMap m;
  public Game(ModelMap m) {this.m=m;}
  @Override public JFrame getFrame() {return this;}
  @Override public Drawable get(Viewport<ModelMap,Drawable> view, int coord) {return view.get(coord);}
  @Override public void set(Viewport<ModelMap,Drawable> view, Drawable elem, int coord) {view.set(elem,coord);}
  @Override public int coordDs(Viewport<ModelMap,Drawable> view, int x, int y, int z) {return view.coordDs(x,y,z);}
  @Override public int coordPs(Viewport<ModelMap,Drawable> view, int x, int y, int z) {return view.coordPs(x,y,z);}
  @Override public int pixelX(Viewport<ModelMap,Drawable> view, int coord) {return view.pixelX(coord);}
  @Override public int pixelY(Viewport<ModelMap,Drawable> view, int coord) {return view.pixelY(coord);}
  @Override public double centerX() {return getMap().centerX;}
  @Override public double centerY() {return getMap().centerY;}
  @Override public Graphics2D getGraphics(Viewport<ModelMap,Drawable> view) {return view.getGraphics();}
  @Override public void cameraUp() {cameraZ+=0.1d;}
  @Override public void cameraDown() {cameraZ-=0.1d;}
  @Override public int maxX(Viewport<ModelMap, Drawable> view) {return view.maxX;}
  @Override public int maxY(Viewport<ModelMap, Drawable> view) {return view.maxY;}
  @Override public int maxZ(Viewport<ModelMap, Drawable> view) {return view.maxZ;}
  @Override public Map<Character, Runnable> getKeyMap() {return this.actionMap;}
  @Override public ModelMap getMap() {return m;}
  @Override public Drawable get(ModelMap m, Viewport<ModelMap, Drawable> v, int x, int y, int z) {
    return itemToDrawable(m.get(x,y,z));
  }
  //only interesting method
  protected Drawable itemToDrawable(Item item){
    if(item==Item.nope)return DrawableConsts.air;
    if(item==Item.forest)return DrawableConsts.treeL;
    if(item==Item.trunk)return DrawableConsts.treeT;
    return DrawableConsts.rock;
    }
  @Override public void stop() {}
  @Override public double getCameraZ() {return cameraZ;}

  @Override public void goDir(Direction dir) {Model.super.goDir(dir);}
  @Override public KeyListener getKeyListener() {return Controller.super.getKeyListener();}

}