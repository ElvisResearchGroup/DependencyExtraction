package topViewDE.modelHeightMap;

import general.Direction;

public interface Model{
 void repaint();
 ModelMap getMap();
 default double boundPos(double pos) {return Math.max(0,pos);}
 default void goDir(Direction dir) {
   switch(dir) {
     case North:getMap().centerY=boundPos(getMap().centerY-0.1d);break;
     case South:getMap().centerY=boundPos(getMap().centerY+0.1d);break;
     case East:getMap().centerX=boundPos(getMap().centerX+0.1d);break;
     case West:getMap().centerX=boundPos(getMap().centerX-0.1d);break;
     default: assert false;
     }
   repaint();
   }
 }

