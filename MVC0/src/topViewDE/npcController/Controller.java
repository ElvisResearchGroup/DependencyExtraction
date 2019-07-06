package topViewDE.npcController;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import general.Direction;

public interface Controller<A>{
  default KeyListener getKeyListener() {
    return new KeyListener() {
      @Override public void keyTyped(KeyEvent e) {
        //Controller.this.handleKeyEvent(e.getKeyChar());
      }
      @Override public void keyReleased(KeyEvent e) {
        Controller.this.handleKeyEvent(e);        
      }
      @Override public void keyPressed(KeyEvent e) {
        Controller.this.handleKeyEvent(e);
      }
    };
  }
  default void handleKeyEvent(KeyEvent e){
    var mapped=getKeyMap().get(e.getKeyChar());
    if(mapped!=null)mapped.accept(e);
  }
  void goDir(Direction dir);
  List<A> allActions();
  void doAction(A a);
  void stop();
  void cameraUp();
  void cameraDown();
  void repaint();
  Map<Character,Consumer<KeyEvent>> getKeyMap();
  private boolean isRelease(KeyEvent e) {return e.getID()==KeyEvent.KEY_RELEASED;}
  default Map<Character,Consumer<KeyEvent>> makeKeyMap(){
    class Local{
      boolean n;
      boolean w;
      boolean s;
      boolean e;
      void act() {
        System.out.println("  "+n);
        System.out.println(w+"  "+e);
        System.out.println("  "+s);
        if(!n&&!s&&!w&&!e){stop();return;}
        if((n&&s)||(w&&e)){stop();return;}
        if(!n&&!s) {
          if(w){goDir(Direction.West);return;}
          assert e;goDir(Direction.East);return;          
        }
        if(!w&&!e) {
          if(n){goDir(Direction.North);return;}
          assert s;goDir(Direction.South);return;          
        }
        if(n&&w){goDir(Direction.NorthWest);return;}
        if(n&&e){goDir(Direction.NorthEast);return;}
        assert s;if(w){goDir(Direction.SouthWest);return;}
        assert e;goDir(Direction.SouthEast);return;
      }}
    Local l=new Local();
    Consumer<KeyEvent> north=e->{l.n=!isRelease(e);l.act();};
    Consumer<KeyEvent> south=e->{l.s=!isRelease(e);l.act();};
    Consumer<KeyEvent> east=e->{l.e=!isRelease(e);l.act();};
    Consumer<KeyEvent> west=e->{l.w=!isRelease(e);l.act();};
    var res=new HashMap<Character,Consumer<KeyEvent>>();
    res.put('8',north);
    res.put('2',south);
    res.put('6',east);
    res.put('4',west);
    res.put('5',e->{cameraUp();repaint();});
    res.put('0',e->{cameraDown();repaint();});
    res.put(' ',e->{if(isRelease(e))doAction(allActions().get(0));});
    return res;
    }
  }