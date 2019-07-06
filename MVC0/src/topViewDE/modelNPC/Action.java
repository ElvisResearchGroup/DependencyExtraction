package topViewDE.modelNPC;

public interface Action {
  void apply(Model m,Npc npc);
  Action fireSpirit=(m,npc)->{
    //double x=npc.speedX*npc.radius
    Npc s=new Npc(0.2  , npc.x,npc.y,npc.z+10, StatsConsts.human);
    s.speedX=npc.speedX;
    s.speedY=npc.speedY;
    m.getNpcs().add(s);
  };
  Action dig=(m,n)->{
    /*if(n.speedX==0 && n.speedY==0){m.getMap().dig(n.x,n.y,n.z-1);}
    if(n.speedX==0 && n.speedY>0) {
      m.getMap().dig(n.x,n.y+1,n.z);
    }*/
    //TODO: npc.spawnX(r)=ceil(x+radius+r+tiny) if speedX>0
    //dig height*2 tall, 
    //build +1 up to height*3 tall
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
