package topViewDE.modelHeightMap;

import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import general.Direction;
import topViewDE.blocks.Blocks;
import topViewDE.blocks.Drawable;
import topViewDE.blocks.DrawableConsts;
import topViewDE.simpleController.Controller;
import topViewDE.view.View;
import topViewDE.view.Viewport;

class Game extends JFrame implements 
Model,
View<ModelMap,Drawable>,
Controller,
Blocks<Viewport<ModelMap,Drawable>>
{
private static final long serialVersionUID = 1L;
double cameraZ=30;
private Map<Character,Runnable> actionMap=makeKeyMap();
ModelMap m;
public Game(ModelMap m) {this.m=m;updateCurrentViewPort();}
@Override public JFrame getFrame() {return this;}
private volatile Viewport<ModelMap,Drawable> currentVP;
@Override public void setCurrentViewPort(Viewport<ModelMap, Drawable> v) {currentVP=v;}
@Override public Viewport<ModelMap, Drawable> getCurrentViewPort() {assert currentVP!=null;return currentVP;}
@Override public Drawable get(Viewport<ModelMap,Drawable> view, int coord) {return view.get(coord);}
@Override public void set(Viewport<ModelMap,Drawable> view, Drawable elem, int coord) {view.set(elem,coord);}
@Override public int coordDs(Viewport<ModelMap,Drawable> view, int x, int y, int z) {return view.coordDs(x,y,z);}
@Override public int coordPs(Viewport<ModelMap,Drawable> view, int x, int y, int z) {return view.coordPs(x,y,z);}
@Override public int pixelX(Viewport<ModelMap,Drawable> view, int coord) {return view.pixelX(coord);}
@Override public int pixelY(Viewport<ModelMap,Drawable> view, int coord) {return view.pixelY(coord);}
@Override public double centerX() {return getMap().centerX;}
@Override public double centerY() {return getMap().centerY;}
@Override public void cameraUp() {cameraZ+=0.2d;updateCurrentViewPort();}
@Override public void cameraDown() {cameraZ-=0.2d;updateCurrentViewPort();}
@Override public int maxX(Viewport<ModelMap, Drawable> view) {return view.maxX;}
@Override public int maxY(Viewport<ModelMap, Drawable> view) {return view.maxY;}
@Override public int maxZ(Viewport<ModelMap, Drawable> view) {return view.maxZ;}
@Override public Map<Character, Runnable> getKeyMap() {return this.actionMap;}
@Override public ModelMap getMap() {return m;}
@Override public Drawable get(ModelMap m, Viewport<ModelMap, Drawable> v, int x, int y, int z) {
  return itemToDrawable(m.map.get(x,y,z));
}
//only interesting method
protected Drawable itemToDrawable(Item item){
if(item==Item.nope)return DrawableConsts.air;
if(item==Item.forest)return DrawableConsts.treeL;
if(item==Item.trunk)return DrawableConsts.treeT;
if(item==Item.water)return DrawableConsts.water;
if(item==Item.grass)return DrawableConsts.grass;
if(item==Item.ground)return DrawableConsts.ground;
return DrawableConsts.rock;
}

@Override public void stop() {}
@Override public double getCameraZ() {return cameraZ;}

@Override public void goDir(Direction dir) {Model.super.goDir(dir);} 
@Override public KeyListener getKeyListener() {return Controller.super.getKeyListener();}
@Override public void updateCurrentViewPort() {View.super.updateCurrentViewPort();}
}
public class Main {
  public static void main(String[] args) {
    ModelMap m=new ModelMap(64,new Random(19));
    SwingUtilities.invokeLater(()->{
      Game g=new Game(m);
      g.initializeApp();
      g.setVisible(true);
    });
  }
}