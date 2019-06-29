package topViewDE.modelNPC;

import java.util.function.BiConsumer;

import general.Int4;

import static general.General.*;
public class Npc {
  public Npc(double radius, double x, double y, double z,Stats stats) {
    this.radius = radius;
    this.x = x;
    this.y = y;
    this.z = z;
    this.stats=stats;
    int size=(int)Math.ceil(radius)*2;
    holder=new NpcItem[size][size];
    for(int xi:range(size))for(int yi:range(size))holder[xi][yi]=new NpcItem(this);
  }
  double radius;
  double x;
  double y;
  double z;
  Stats stats;
  double speedX=0;
  double speedY=0;
  public static final BiConsumer<Npc,Model> intentNone=(self,model)->{};
  public static final BiConsumer<Npc,Model> intentGo=(self,model)->{
    self.unmarkMeOnMap(model.getMap());
    double oldX=self.x;
    double oldY=self.y;
  };
  void fall(ModelMap map) {try {
    this.unmarkMeOnMap(map);
    double oldZ=this.z;
    assert !isContactOnMap(map);
    this.z-=1;
    boolean impact=isContactOnMap(map);
    if(impact)this.z=oldZ;
  }finally {this.markMeOnMap(map);}}
  private static final double diagonalReduction=Math.sqrt(0.5d);
  private static final double tiny=0.000001d;
  double go(ModelMap map,double energy){
    double xd=energy*this.speedX;
    double yd=energy*this.speedY;
    if(this.speedX!=0 && this.speedY!=0) {
      xd*=diagonalReduction;
      yd*=diagonalReduction;
    }
    double oldX=this.x;
    double oldY=this.y;
    double oldZ=this.z;
    this.x=oldX+xd;
    this.y=oldY+yd;
    boolean impact=isContactOnMap(map);
    if(!impact)return 0d;
    this.x=oldX+xd/4d;
    this.y=oldY+yd/4d;
    impact=isContactOnMap(map);//to avoiding 'jumping up' if we do not even reach the next tile
    if(!impact) return 0d;
    this.z=oldZ+1;
    impact=isContactOnMap(map);
    if(!impact) return 0d;
    //handle semi movement
    if(xd>0d && Math.floor(this.x+this.radius)!=Math.floor(oldX+this.radius))
      this.x=Math.floor(this.x+this.radius)-this.radius-tiny;
    if(xd<0d && Math.ceil(this.x-this.radius)!=Math.ceil(oldX-this.radius))
       this.x=Math.ceil(this.x-this.radius)+this.radius+tiny;
    if(yd>0d && Math.floor(this.y+this.radius)!=Math.floor(oldY+this.radius))
      this.y=Math.floor(this.y+this.radius)-this.radius-tiny;
    if(yd<0d && Math.ceil(this.y-this.radius)!=Math.ceil(oldY-this.radius))
       this.y=Math.ceil(this.y-this.radius)+this.radius+tiny;
    this.z=oldZ;
    double distX=this.x-oldX;
    double distY=this.y-oldY;
    double dist=Math.sqrt(distX*distX+distY*distY);
    assert energy>=dist;
    assert !isContactOnMap(map);
    return energy-dist;
    }

  void go(ModelMap map){
    //for(int i:range(1))
    go1(map);//go1 need to go slow to never skip a tile
  }
  void go1(ModelMap map){
    if(this.speedX==0d && this.speedY==0d)return;
    double oldSX=this.speedX;
    double oldSY=this.speedY;
    try{out:{
      this.unmarkMeOnMap(map);
      double energy=this.stats.speed;
      energy=go(map,energy);
      if(energy<tiny)break out;
      this.speedX=0;
      energy=go(map,energy);
      if(energy<tiny)break out;
      this.speedX=oldSX;
      this.speedY=0;
      energy=go(map,energy);
    }}
    finally {
      this.speedX=oldSX;
      this.speedY=oldSY;
      //assert !isContactOnMap(map);
      this.markMeOnMap(map);
      }
    }

  BiConsumer<Npc,Model> intent=intentNone;
  void ping(Model m) {
    fall(m.getMap());
    go(m.getMap());
    intent.accept(this,m);
    }
  NpcItem[][] holder;
  void markMeOnMap(ModelMap map){
    _markMap(map,(xl,xi,yl,yi)->{
      holder[xi-xl][yi-yl].update(map.get(xi,yi,(int)z));
      map.set(xi,yi,(int)z,holder[xi-xl][yi-yl]);      
    });
  }
  void unmarkMeOnMap(ModelMap map){
    _markMap(map,(xl,xi,yl,yi)->{
      map.set(xi,yi,(int)z,holder[xi-xl][yi-yl].get());      
    });
  }
  boolean isContactOnMap(ModelMap map){
    boolean[] res= {false};
    _markMap(map,(xl,xi,yl,yi)->{
      Item there=map.get(xi,yi,(int)z);
      if(there instanceof Item.Full)res[0]|=true;
    });
    return res[0];
  }
  void _markMap(ModelMap map,Int4 action){
    int xl=(int)(this.x-radius-1);
    int yl=(int)(this.y-radius-1);
    int xh=(int)(this.x+radius);
    int yh=(int)(this.y+radius);
    assert xh-xl<=holder.length;
    for(int xi :range(xl,xh))for(int yi :range(yl,yh)){
      action.apply(xl,xi,yl,yi);
    }
  }
}
class NpcItem implements Item{
  public NpcItem(Npc npc) {this.npc = npc;}
  Npc npc;
  Item item=Item.air;
  void update(Item item) {this.item=item;}
  Item get() {return this.item;}  
}
class Stats{
  public Stats(double speed, double acceleration) {
    this.speed = speed;
    this.acceleration = acceleration;
  }
  double speed;
  double acceleration;
  //double resistance, strenght, so on...
}
class StatsConsts{
  public static final Stats human=new Stats(0.1,1);
}