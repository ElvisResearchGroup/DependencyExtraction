package topViewDE.modelNPC;
import static general.General.*;
public interface Action {
  void apply(Model m,Npc npc);
  Action fireSpirit=(m,npc)->{
    Npc s=new Npc(0.2  , npc.x,npc.y,npc.z+10, StatsConsts.human);
    s.speedX=npc.speedX;
    s.speedY=npc.speedY;
    m.getNpcs().add(s);
  };
  Action dig=(m,n)->{
    if(n.speedX==0d && n.speedY==0d)
      n.markMapXY(m.getMap(),(xl,xi,yl,yi)->{
        for(int i:range(n.height*2))
          m.getMap().dig(xi,yi,n.z-1+i);
        });
    double oldX=n.x;
    double oldY=n.y;
    n.moveEthereal(n.stats.speed,1);
    n.markMapXY(m.getMap(),(xl,xi,yl,yi)->{
      for(int i:range(n.height*2))
        m.getMap().dig(xi,yi,n.z+i);
      });
    n.x=oldX;
    n.y=oldY;
    };
    Action build=(m,n)->{
      double oldZ=n.z;
      n.unmarkMeOnMap(m.getMap());
      try{
        n.z+=1;
        if(n.isContactOnMap(m.getMap())){n.z=oldZ;}
        else n.markMapXY(m.getMap(),(xl,xi,yl,yi)->{
            m.getMap().build(xi,yi,oldZ,Item.rock);
          });
      }
      finally{n.markMeOnMap(m.getMap());}
      };
  Action north=(m,n)->{n.speedX=0d;n.speedY=-1d;};
  Action south=(m,n)->{n.speedX=0d;n.speedY=+1d;};
  Action east=(m,n)->{n.speedX=+1d;n.speedY=0d;};
  Action west=(m,n)->{n.speedX=-1d;n.speedY=0d;};
  Action northWest=(m,n)->{n.speedX=-1d;n.speedY=-1d;};
  Action southWest=(m,n)->{n.speedX=-1d;n.speedY=+1d;};
  Action northEast=(m,n)->{n.speedX=+1d;n.speedY=-1d;};
  Action southEast=(m,n)->{n.speedX=+1d;n.speedY=+1d;};
  Action stop=(m,n)->{n.speedX=0d;n.speedY=0d;};
}
