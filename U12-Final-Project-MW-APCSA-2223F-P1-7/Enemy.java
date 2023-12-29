import java.util.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.net.URL;
import javax.imageio.ImageIO;

public class Enemy extends Entity {
    private Image image;
    public final static int SPEED = 3;

    // boundaries limits the enemy's movements on their first touched platform
    private ArrayList<Platform> boundaries;

    // private Bullets bullets;

    // for when the enemy is idling and not spotting the player
    private int idleTimer;
    private int moveTimer;
    private int moveDirection;

    // moves towards player when no platforms are obstructing this line
    private Line2D sightLine;
    public static final int SIGHT_RANGE = 250;

    // constructors
    public Enemy(int x, int y) {
        this(x, y, 14, 40, 3);
    }

    public Enemy(int x, int y, int w, int h) {
        this(x, y, w, h, 3);
    }

    public Enemy(int x, int y, int w, int h, int hp, ArrayList<Platform> boundaries) {
        this(x, y, w, h, hp);
        this.boundaries = boundaries;
    }

    public Enemy(int x, int y, int w, int h, int hp) {
        super(x, y, w, h, hp);
        boundaries = new ArrayList<Platform>();
        // bullets = new Bullets();
        idleTimer = (int) (Math.random() * 120) + 100;
        moveTimer = 0;
        moveDirection = 0;
        setDirection(1);
        sightLine = new Line2D.Double(0, 0, 0, 0);

        try {
            URL url = getClass().getResource("enemy_left.png");
            image = ImageIO.read(url);
        } catch (Exception e) {
        }
    }

    // getters and setters
    public ArrayList<Platform> getBoundaries() {
        return boundaries;
    }

    public void setBoundaries(ArrayList<Platform> bounds) {
        boundaries = bounds;
    }

    // update and draw methods to be called each frame
    public void update(Entity player, List<Platform> platforms) {
        if (!getIsDead()) {
            // enemy's line of sight to the player
            sightLine = new Line2D.Double(getX() + getWidth() / 2, getY() + getHeight() / 2,
                    player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);

            // if player is in sight range and there are no platforms blocking the sight of
            // the enemy
            if (lineLength(sightLine) <= SIGHT_RANGE && !intersectsPlatform(sightLine, platforms)) {
                // player at the left of this enemy
                if (player.getX() + player.getWidth() / 2 < getX() + getWidth() / 2) {
                    setXVel(-SPEED);
                    setDirection(1);
                } else
                // player at the right of this enemy
                if (player.getX() + player.getWidth() / 2 > getX() + getWidth() / 2) {
                    setXVel(SPEED);
                    setDirection(2);
                }
            } else {
                // enemy stays stationary when idleTimer > 0
                if (idleTimer > 0) {
                    setXVel(0);
                    idleTimer--;
                    if (idleTimer <= 0) {
                        moveTimer = (int) (Math.random() * 100);
                        moveDirection = (int) (Math.random() * 2) + 1;
                    }
                }
                // enemy moves at a random direction afterwards
                if (moveTimer > 0) {
                    setXVel(moveDirection == 1 ? -SPEED / 2 : SPEED / 2);
                    setDirection(moveDirection);
                    moveTimer--;
                    if (moveTimer <= 0)
                        idleTimer = (int) (Math.random() * 100) + 40;
                }
            }
        }
        // bullets.move();
        // bullets.cleanUpEdges();
    }

    public void draw(Graphics g) {

        // enemy changes color based on remaining hp
        String color = "";
        if (getHP() > 2 * getMaxHP() / 3)
            color = "/green";
        else if (getHP() > getMaxHP() / 3)
            color = "/yellow";
        else
            color = "/red";

        try {
            if (getDirection() == 1)
                image = ImageIO.read(getClass().getResource(color + "/enemy_left.png"));
            else if (getDirection() == 2)
                image = ImageIO.read(getClass().getResource(color + "/enemy_right.png"));
            g.drawImage(image, getX(), getY(), getWidth(), getHeight(), null);
        } catch (Exception e) {
        }

        // draws line of sight
        /*
         * g.setColor(lineLength(sightLine) <= SIGHT_RANGE ? Color.RED : Color.YELLOW);
         * ((Graphics2D) g).draw(sightLine);
         */

        // bullets.draw(g);
    }

    // shoots a bullet according to the enemy's direction
    /*
    public void attack() {
        bullets.add(new Bullet(this, getDirection() == 1 ? "LEFT" : "RIGHT"));
    }
    */

    // method to calculate physics
    public void move(List<Platform> platforms) {
        // calculate physics y
        setYVel(getYVel() + GRAVITY);
        if (getYVel() > TERMINAL_Y_VEL)
            setYVel(TERMINAL_Y_VEL);
        else if (getYVel() < -TERMINAL_Y_VEL)
            setYVel(-TERMINAL_Y_VEL);
        setY(getY() + getYVel());
        List<Entity> collidingObjects = collisionList(platforms);
        if (collidingObjects.size() > 0) {
            // sets up the boundaries upon landing the first time
            if (boundaries.size() == 0) {
                boundaries.add(
                        new Platform(collidingObjects.get(0).getX() - 10, collidingObjects.get(0).getY() - 50, 10, 50));
                boundaries.add(new Platform(collidingObjects.get(0).getX() + collidingObjects.get(0).getWidth(),
                        collidingObjects.get(0).getY() - 50, 10, 50));
            }

            Entity platform = collidingObjects.get(0);
            if (getYVel() > 0)
                setY(platform.getY() - getHeight() - 1);
            else if (getYVel() < 0)
                setY(platform.getY() + platform.getHeight() + 1);
            setYVel(0);
        }

        // calculate physics x
        setX(getX() + getXVel());
        collidingObjects = collisionList(platforms);
        if (collidingObjects.size() > 0) {
            Entity platform = collidingObjects.get(0);
            if (getXVel() > 0)
                setX(platform.getX() - getWidth() - 1);
            else if (getXVel() < 0)
                setX(platform.getX() + platform.getWidth() + 1);
        }
    }

    // returns all platforms in target that the enemy collided with
    private List<Entity> collisionList(List<Platform> platforms) {

        List<Entity> collided = new ArrayList<Entity>();
        List<Entity> target = new ArrayList<Entity>();

        target.addAll(platforms);
        target.addAll(boundaries);

        for (Entity e : target)
            if (didCollide(e))
                collided.add(e);
        return collided;
    }

    // check if any platform is in a line
    private boolean intersectsPlatform(Line2D line, List<Platform> platforms) {
        for (Entity e : platforms)
            if (line.intersects(e.getX(), e.getY(), e.getWidth(), e.getHeight()))
                return true;
        return false;
    }

    // returns line distance
    private double lineLength(Line2D line) {
        double dX = line.getX1() - line.getX2();
        double dY = line.getY1() - line.getY2();
        return Math.sqrt(dX * dX + dY * dY);
    }

    // changes isDead to true if HP is equal to 0
    public void checkIsDead() {
        if (getHP() <= 0)
            setIsDead(true);
    }
}