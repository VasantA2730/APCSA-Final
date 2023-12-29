import java.awt.*;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;

public class Flag extends Entity 
  {
    private Image image;

    public Flag(int xP, int yP)
    {
      super(xP,yP,40,40);
      try {
            URL url = getClass().getResource("images/flag.png");
            image = ImageIO.read(url);

        } catch (Exception e) {
            System.out.println("Couldn't locate image file");
        }
    }
    /*
    public boolean didCollide(Entity other)
    {
      if(this.getX()+this.getWidth()<other.getX() || other.getX()+other.getWidth()<this.getX())
        return false;
      if(this.getY()+this.getHeight()<other.getY() || other.getY()+other.getHeight()<this.getY())
        return false;
      return true;
    }
    */

    public void checkIsDead()
    {
     
    }

    public void draw(Graphics window)
    {
      window.drawImage(image, getX(), getY(), getWidth(), getHeight(), null);
    }

    public String toString() {
      return getX() + " : " + getY() + " : " + getWidth() + " : " + getHeight();
    }
  }