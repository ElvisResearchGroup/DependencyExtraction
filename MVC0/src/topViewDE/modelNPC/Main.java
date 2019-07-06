package topViewDE.modelNPC;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import general.Direction;
import topViewDE.blocks.Blocks;
import topViewDE.blocks.Drawable;
import topViewDE.blocks.DrawableConsts;
import topViewDE.blocks.ImgResources;
import topViewDE.blocks.NpcDrawable;
import topViewDE.npcController.Controller;
import topViewDE.view.View;
import topViewDE.view.Viewport;

class Game extends JFrame implements 
Model,
View<ModelMap,Drawable>,
Controller<Action>,
Blocks<Viewport<ModelMap,Drawable>>
{
private static final long serialVersionUID = 1L;
double cameraZ=30;
private Map<Character,Consumer<KeyEvent>> actionMap=makeKeyMap();
private List<Npc> npcs=new ArrayList<>();
ModelMap m;
final ScheduledFuture<?> handle;
private volatile boolean inPing=false;
private volatile Viewport<ModelMap,Drawable> currentVP;
@Override public void setCurrentViewPort(Viewport<ModelMap, Drawable> v) {currentVP=v;}
@Override public Viewport<ModelMap, Drawable> getCurrentViewPort() {assert currentVP!=null;return currentVP;}
public Game(ModelMap m) {
  this.m=m;
  initModel();
  updateCurrentViewPort();
  var scheduler =Executors.newScheduledThreadPool(1);
  handle =scheduler.scheduleAtFixedRate(()->{
    if(inPing) {System.err.println("skipping a ping");return;}
    inPing=true;
    try{
      assert !SwingUtilities.isEventDispatchThread();
      this.ping();
      updateCurrentViewPort();
      repaint();
      }
    catch(Throwable t){
      onClose();
      t.printStackTrace();
      System.exit(1);
      }
    finally {inPing=false;}
    },
    500, 33,TimeUnit.MILLISECONDS);
  }

@Override public void onClose() {handle.cancel(true);}
@Override public JFrame getFrame() {return this;}
@Override public List<Npc>  getNpcs() {return npcs;}
@Override public Drawable get(Viewport<ModelMap,Drawable> view, int coord) {return view.get(coord);}
@Override public void set(Viewport<ModelMap,Drawable> view, Drawable elem, int coord) {view.set(elem,coord);}
@Override public int coordDs(Viewport<ModelMap,Drawable> view, int x, int y, int z) {return view.coordDs(x,y,z);}
@Override public int coordPs(Viewport<ModelMap,Drawable> view, int x, int y, int z) {return view.coordPs(x,y,z);}
@Override public int pixelX(Viewport<ModelMap,Drawable> view, int coord) {return view.pixelX(coord);}
@Override public int pixelY(Viewport<ModelMap,Drawable> view, int coord) {return view.pixelY(coord);}
@Override public double centerX() {return getNpcs().get(0).x;}
@Override public double centerY() {return getNpcs().get(0).y;}
@Override public void cameraUp() {cameraZ+=0.2d;}
@Override public void cameraDown() {cameraZ-=0.2d;}
@Override public int maxX(Viewport<ModelMap, Drawable> view) {return view.maxX;}
@Override public int maxY(Viewport<ModelMap, Drawable> view) {return view.maxY;}
@Override public int maxZ(Viewport<ModelMap, Drawable> view) {return view.maxZ;}
@Override public Map<Character, Consumer<KeyEvent>> getKeyMap() {return this.actionMap;}
@Override public ModelMap getMap() {return m;}
@Override public Drawable get(ModelMap m, Viewport<ModelMap, Drawable> v, int x, int y, int z) {
return itemToDrawable(v,m.get(x,y,z));
}
//only interesting method
protected Drawable itemToDrawable(Viewport<ModelMap, Drawable> v,Item item){
if(item==Item.air)return DrawableConsts.air;
if(item==Item.treeTop)return DrawableConsts.treeL;
if(item==Item.treeTrunk)return DrawableConsts.treeT;
if(item==Item.water)return DrawableConsts.water;
if(item==Item.grass)return DrawableConsts.grass;
if(item==Item.ground)return DrawableConsts.ground;
if(item instanceof NpcItem){
  NpcItem i=(NpcItem)item;
  Drawable d=itemToDrawable(v,i.get());
  double x=i.npc.x-centerX()+View.half;
  double y=i.npc.y-centerY()+View.half;
  double z=i.npc.z*View.scaleZ;
  return v.circleToPixel(this.cameraZ,x,y,z,i.npc.radius,
    (xl,xSide,yl,ySide)->
    new NpcDrawable(d,ImgResources.Smile.img,xSide,xl,yl));
}
return DrawableConsts.rock;
}
@Override public double getCameraZ() {return cameraZ;}
volatile Set<Action>pendingActions=new LinkedHashSet<Action>();
@Override public Set<Action>pendingActions(){
  synchronized(pendingActions){
    Set<Action>pendingLocal=pendingActions;
    pendingActions=new LinkedHashSet<Action>();
    return pendingLocal;
    }
}
@Override public void doAction(Action a) {
  synchronized(pendingActions){pendingActions.add(a);}
}

@Override public void goDir(Direction dir) {Model.super.goDir(dir);}
@Override public void stop() {Model.super.stop();}
@Override public List<Action> allActions() {return Model.super.allActions();}
@Override public KeyListener getKeyListener() {return Controller.super.getKeyListener();}
@Override public void updateCurrentViewPort() {View.super.updateCurrentViewPort();}
}
public class Main {
  public static void main(String[] args) {
    Object unused=ImgResources.Smile.img;//load
    ModelMap m=new ModelMap(new Random(19));
    SwingUtilities.invokeLater(()->{
      Game g=new Game(m);
      g.initializeApp();
      g.setVisible(true);
    });
  }
}