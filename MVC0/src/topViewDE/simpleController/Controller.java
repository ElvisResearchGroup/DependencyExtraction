package topViewDE.simpleController;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import general.Direction;

public interface Controller{
  default KeyListener getKeyListener() {
    return new KeyListener() {
      @Override public void keyTyped(KeyEvent e) {
        Controller.this.handleKeyEvent(e.getKeyChar());}
      @Override public void keyReleased(KeyEvent arg0) {}
      @Override public void keyPressed(KeyEvent arg0) {}
    };
  }
  default void handleKeyEvent(Character c){
    var mapped=getKeyMap().get(c);
    if(mapped!=null)mapped.run();
  }
  void goDir(Direction dir);
  void stop();
  void cameraUp();
  void cameraDown();
  void repaint();
  Map<Character,Runnable> getKeyMap();
  default Map<Character,Runnable> makeKeyMap(){
    var res=new HashMap<Character,Runnable>();
    res.put('8',()->goDir(Direction.North));
    res.put('2',()->goDir(Direction.South));
    res.put('6',()->goDir(Direction.East));
    res.put('4',()->goDir(Direction.West));
    res.put('5',()->{cameraUp();repaint();});
    res.put('0',()->{cameraDown();repaint();});
    return res;
    }
  }