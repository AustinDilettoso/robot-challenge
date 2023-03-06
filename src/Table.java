import java.util.ArrayList;
import java.util.List;

/**
 * Table class contains information on the board size and a list of robots on the table.
 */
public class Table {
    private int[][] tableArray;
    private List<Robot> robots;

    public Table(){
        this.tableArray = new int[5][5];
        this.robots = new ArrayList<Robot>();
    }

    public List<Robot> getRobots(){
        return robots;
    }

}
