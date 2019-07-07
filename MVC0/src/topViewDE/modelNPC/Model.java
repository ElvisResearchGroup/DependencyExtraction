package topViewDE.modelNPC;

import java.util.List;
import java.util.Set;

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
   //Action.stop.apply(this,getNpcs().get(0));
   for(Action a:pendingActions())
     a.apply(this,getNpcs().get(0));
   for(Npc npc:getNpcs())npc.ping(this);
 }
 default double boundPos(double pos) {return Math.max(0,pos);}
 List<Action> allActions=List.of(Action.fireSpirit,Action.dig,Action.build);
 default List<Action> allActions(){return allActions;}
 Set<Action>pendingActions();//this also clear the pending actions
 void doAction(Action a);
 default void goDir(Direction dir) {
   switch(dir) {
     case North:doAction(Action.north);break;
     case South:doAction(Action.south);break;
     case East:doAction(Action.east);break;
     case West:doAction(Action.west);break;
     case NorthWest:doAction(Action.northWest);break;
     case SouthWest:doAction(Action.southWest);break;
     case NorthEast:doAction(Action.northEast);break;
     case SouthEast:doAction(Action.southEast);break;
     }
   }
 default void stop(){doAction(Action.stop);}
 }

