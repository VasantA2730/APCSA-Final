import java.util.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.net.URL;
import javax.imageio.ImageIO;

public class Player extends Entity {
    private boolean movingLeft;
    private boolean movingRight;
    private boolean isAirborne;
    public final static int SPEED = 5;
    public final static int JUMP_STRENGTH = 15;

    // determines score; less jumps gives a better score
    private int numJumps; 
    
    private Image image;
    private int moveCount;
    private int shootDirection;
    private Bullets bullets;
    public final static int ATK_CD = 25;
    private int atkCDTimer;
    public final static int IMMUNE_TIME = 30;
    private int immuneTimer;

    // constructors
    public Player(int x, int y) {
        this(x, y, 13, 32, 3);
    }

    public Player(int x, int y, int w, int h) {
        this(x, y, w, h, 3);
    }

    public Player(int x, int y, int w, int h, int hp) {
        super(x, y, w, h, hp);
        movingLeft = false;
        movingRight = false;
        isAirborne = true;
        moveCount = 0;
        bullets = new Bullets();
        shootDirection = 0;
        atkCDTimer = 0;
        immuneTimer = 0;
        try {
            URL url = getClass().getResource("standing.png");
            image = ImageIO.read(url);

        } catch (Exception e) {
            System.out.println("Couldn't locate image file");
        }
    }

    // getters & setters
    public boolean isMovingLeft() {
        return movingLeft;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public void setMovingLeft(boolean m) {
        movingLeft = m;
    }

    public void setMovingRight(boolean m) {
        movingRight = m;
    }

    public Bullets getBullets() {
        return bullets;
    }

    // make the player jump when standing on solid ground
    public void jump() {
        if (!isAirborne) {
            setYVel(-JUMP_STRENGTH);
            numJumps++;
        }
    }

    public int getJumps() {
        return numJumps;
    }

    // shoots a bullet according to the player's direction
    public void attack() {
        if (atkCDTimer <= 0) {
            bullets.add(new Bullet(this, shootDirection == 1 ? "LEFT" : "RIGHT"));
            atkCDTimer = ATK_CD;
        }
    }

    // update and draw methods to be called each frame
    public void update() {
        // movements
        if ((movingLeft && movingRight) || (!movingLeft && !movingRight)) {
            setXVel(0);
            setDirection(0);
            moveCount = -1;
        } else if (movingLeft) {
            setXVel(-SPEED);
            setDirection(1);
            shootDirection = 1;
            if (moveCount < 8)
                moveCount++;
            else
                moveCount = 0;
        } else if (movingRight) {
            setXVel(SPEED);
            setDirection(2);
            shootDirection = 2;
            if (moveCount < 8)
                moveCount++;
            else
                moveCount = 0;
        }
        
        if (atkCDTimer > 0)
            atkCDTimer--;
        if (immuneTimer > 0)
            immuneTimer--;
        
        bullets.move();
        bullets.cleanUpEdges();
    }

    public void draw(Graphics g) {
        try {
            // flashes when player is in immune time
            ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    (float) ((immuneTimer > 0 && immuneTimer % 6 < 3) ? 0.5 : 1.0)));

            // creates animation when moving
            if (getDirection() == 1) {
                if (moveCount == 0 || moveCount == 1)
                    image = ImageIO.read(getClass().getResource("/images/player_left1.png"));
                else if (moveCount == 2 || moveCount == 3)
                    image = ImageIO.read(getClass().getResource("/images/player_left2.png"));
                else if (moveCount == 4 || moveCount == 5)
                    image = ImageIO.read(getClass().getResource("/images/player_left3.png"));
                else if (moveCount == 6 || moveCount == 7)
                    image = ImageIO.read(getClass().getResource("/images/player_left4.png"));
            } else if (getDirection() == 2) {
                if (moveCount == 0 || moveCount == 1)
                    image = ImageIO.read(getClass().getResource("/images/player_right1.png"));
                else if (moveCount == 2 || moveCount == 3)
                    image = ImageIO.read(getClass().getResource("/images/player_right2.png"));
                else if (moveCount == 4 || moveCount == 5)
                    image = ImageIO.read(getClass().getResource("/images/player_right3.png"));
                else if (moveCount == 6 || moveCount == 7)
                    image = ImageIO.read(getClass().getResource("/images/player_right4.png"));
            } else
                image = ImageIO.read(getClass().getResource("standing.png"));
            
            g.drawImage(image, getX(), getY(), getWidth(), getHeight(), null);
        } catch (Exception e) {
        }
        bullets.draw(g);
    }

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
            Entity platform = collidingObjects.get(0);
            if (getYVel() > 0) {
                setY(platform.getY() - getHeight() - 1);
                isAirborne = false;
            } else if (getYVel() < 0) {
                setY(platform.getY() + platform.getHeight() + 1);
            }
            setYVel(0);
        } else
            isAirborne = true;

        // calculate physics x
        setX(getX() + getXVel());
        collidingObjects = collisionList(platforms);
        if (collidingObjects.size() > 0) {
            Entity platform = collidingObjects.get(0);
            if (getXVel() > 0) {
                setX(platform.getX() - getWidth() - 1);
            } else if (getXVel() < 0) {
                setX(platform.getX() + platform.getWidth() + 1);
            }
        }
    }

    // returns all platforms in target that player collided with
    private List<Entity> collisionList(List<Platform> target) {
        List<Entity> collided = new ArrayList<Entity>();
        for (Entity e : target)
            if (didCollide(e))
                collided.add(e);
        return collided;
    }

    @Override
    public void decrementHP() {
        if (immuneTimer <= 0) {
            super.decrementHP();
            immuneTimer = IMMUNE_TIME;
        }
    }

    // changes isDead to true if HP is equal to 0
    public void checkIsDead() {
        if (getHP() <= 0)
            setIsDead(true);
    }

    // checks bullet collision with enemies, returns indices of hit enemies
    public ArrayList<Integer> check(ArrayList<Enemy> enemies) {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        ArrayList<Bullet> bulletsList = bullets.getList();
        for (int i = 0; i < enemies.size(); i++) {
            for (int j = 0; j < bulletsList.size(); j++) {
                if (enemies.get(i).didCollide(bulletsList.get(j))) {
                    bullets.deleteBullet(j);
                    ret.add(i);
                }
            }
        }
        return ret;
    }
}