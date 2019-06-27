package topViewDE.modelNPC;

import java.util.List;

public interface Model{
 void repaint();
 ModelMap getMap();
 List<Npc> getNpcs();
 default void initModel() {
   Npc first=new Npc(0.2,150,150,70,StatsConsts.human);
   first.markMeOnMap(getMap());
   getNpcs().add(first);
 }
 default void ping() {
   for(Npc npc:getNpcs())npc.ping(this);
   repaint();
 }
 default double boundPos(double pos) {return Math.max(0,pos);}
 default void goWest() {
   getNpcs().get(0).speedX=-0.1d;
   getNpcs().get(0).speedY=0d;
   }
 default void goEast() {
   getNpcs().get(0).speedX=0.1d;
   getNpcs().get(0).speedY=0d;
   }
 default void goSouth() {
   getNpcs().get(0).speedY=0.1d;
   getNpcs().get(0).speedX=0d;
   }
 default void goNorth() {
   getNpcs().get(0).speedX=0d;
   getNpcs().get(0).speedY=-0.1d;
   }
 default void stop(){
   getNpcs().get(0).speedX=0d;
   getNpcs().get(0).speedY=0d;   
   }
 }

