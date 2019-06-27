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
  void go(ModelMap map){
    if(this.speedX==0d && this.speedY==0d)return;
    try{
      this.unmarkMeOnMap(map);
      double oldX=this.x;
      double oldY=this.y;
      double oldZ=this.z;
      assert !isContactOnMap(map);
      this.x+=this.speedX;
      this.y+=this.speedY;
      boolean impact=isContactOnMap(map);
      if(!impact)return;
      //can I go up 1 and not impact?
      this.z+=1;
      impact=isContactOnMap(map);
      if(!impact) return;
      this.x=oldX;
      this.y=oldY;
      this.z=oldZ;
      this.speedX=0;
      this.speedY=0;
      }
    finally {this.markMeOnMap(map);}
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
  public static final Stats human=new Stats(10,1);
}