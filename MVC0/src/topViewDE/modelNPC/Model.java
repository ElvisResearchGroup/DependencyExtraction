package topViewDE.modelNPC;

import java.util.List;

import general.Direction;

public interface Model{
 void repaint();
 ModelMap getMap();
 List<Npc> getNpcs();
 default void initModel() {
   Npc first=new Npc(0.2,150,150,70,StatsConsts.human);
   first.markMeOnMap(getMap());
   getNpcs().add(first);
   Npc second=new Npc(0.2,154.5,154.5,70,StatsConsts.human);
   second.markMeOnMap(getMap());
   getNpcs().add(second);
 }
 default void ping() {
   for(Npc npc:getNpcs())npc.ping(this);
 }
 default double boundPos(double pos) {return Math.max(0,pos);}
 default void goDir(Direction dir) {
   stop();
   switch(dir) {
     case North:getNpcs().get(0).speedY=-1d;break;
     case South:getNpcs().get(0).speedY=+1d;break;
     case East:getNpcs().get(0).speedX=+1d;break;
     case West:getNpcs().get(0).speedX=-1d;break;
     case NorthWest:
       getNpcs().get(0).speedX=-1d;
       getNpcs().get(0).speedY=-1d;break;
     case SouthWest:
       getNpcs().get(0).speedX=-1d;
       getNpcs().get(0).speedY=+1d;break;
     case NorthEast:
       getNpcs().get(0).speedX=+1d;
       getNpcs().get(0).speedY=-1d;break;
     case SouthEast:
       getNpcs().get(0).speedX=+1d;
       getNpcs().get(0).speedY=+1d;break;
     }
   }
 default void stop(){
   getNpcs().get(0).speedX=0d;
   getNpcs().get(0).speedY=0d;   
   }
 }

