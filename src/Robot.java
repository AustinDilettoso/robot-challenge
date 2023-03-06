/**
 * Robot class, contains basic info about a single robot.
 * its X position
 * its Y position
 * its direction its facing
 */
public class Robot {
    private int x;
    private int y;
    private direction direction;


    public Robot(int x, int y, direction direction){
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getX() {return x;};
    public int getY() {return y;};

    public void setX(int x) {this.x = x;};
    public void setY(int y) {this.y = y;};


    public direction getDirection() {
        return direction;
    }
    public void setDirection(direction direction) {
        this.direction = direction;
    }
}
