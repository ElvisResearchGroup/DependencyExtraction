package topViewDE.blocks;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

public enum ImgResources {
  Smile("smile2.png");//, //
  //WHEAT("Wheat.png");
  
  public final Image img;
  
  ImgResources(String resourceName) {
    try{ img = ImageIO.read(ImgResources.class.getResource(resourceName)); }
    catch (IOException e){ throw new Error(e); }
  }
}
