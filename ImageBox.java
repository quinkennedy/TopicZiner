class ImageBox extends ContentBox{
  PImage image;
  public ImageBox(PImage img){
    image = img;
  }
  
  public boolean isResizable(){
    return true;
  }
  
  public Rectangle render(Rectangle area, PGraphics pg, boolean debug){
    if (image == null){
      return new Rectangle(area.x, area.y, 0, 0);
    } else {
      float scale = 1;
      scale = Math.min(scale, area.h / Math.max(image.height, 1));
      scale = Math.min(scale, area.w / Math.max(image.width, 1));
      float sWidth = image.width * scale;
      float sHeight = image.height * scale;
      image.filter(GRAY);
      Rectangle destination = new Rectangle(area.x + (area.w - sWidth) / 2, area.y, sWidth, sHeight);
      pg.image(image, destination.x, destination.y, destination.w, destination.h);
      // Really want to get this blend working, but I'm too tired to figure out the right coordinates...
      //pg.blend(image, 0, 0, image.width, image.height, (int) destination.x, (int)destination.y, (int)destination.w, (int)destination.h, DARKEST); 
      return destination;
    }
  }
}