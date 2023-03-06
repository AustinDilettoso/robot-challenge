import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Game {

    private static final String COMMANDS =
            "Commands: " +
                    "\n'PLACE 0-4,0-4,NORTH|EAST|SOUTH|WEST' - Must be done first " +
                    "\n'MOVE'" +
                    "\n'LEFT'" +
                    "\n'RIGHT'" +
                    "\n'REPORT'" +
                    "\n'END' - Ends the robot challenge" +
                    "\n MOVE,LEFT and RIGHT can start with 'ROBOT number' to command specific robots - eg: 'ROBOT 3 MOVE \n";

    private Table gameTable = new Table();
    private boolean robotPlaced = false;
    private Robot currentRobot;


    /**
     * Primary function that reads the input and handles the resulting operations of the users input
     * @param input The users input
     */
    public void readInput(String input){

        if (!input.isEmpty()){

            // Checks to see if the users input passes the regex expression checks
            if (regexInputChecks(input)){

                // Split the input command into relevant parts
                // Split by space to retrieve the command eg: PLACE x,y,z
                String[] inputArray = input.split(" ");
                String command = inputArray[0];
                command = command.toUpperCase(Locale.ROOT);

                if (!command.equals("END")){

                    // ensure that a robot has already been placed or the first command read is "PLACE"
                    if (robotPlaced == true || command.equals("PLACE")){

                        // Check to see if a ROBOT was specified
                        if (command.equals("ROBOT")){
                            // Check to see if the number of the robot exists within the robots Array
                            if (gameTable.getRobots().size() >= Integer.parseInt(inputArray[1])){
                                // make the current robot the robot specified
                                currentRobot = gameTable.getRobots().get(Integer.parseInt(inputArray[1]) -1);

                                // make the command = the input specified after robot # eg: ROBOT 1 MOVE
                                command = inputArray[2].toUpperCase(Locale.ROOT);
                            }
                        }

                        // Switch statement for various commands
                        switch (command){
                            case "PLACE":
                                handlePlace(inputArray);
                                break;
                            case "MOVE":
                                handleMove(currentRobot);
                                break;
                            case "LEFT":
                                currentRobot.setDirection(rotateDirection(currentRobot.getDirection(), "LEFT"));
                                break;
                            case "RIGHT":
                                currentRobot.setDirection(rotateDirection(currentRobot.getDirection(), "RIGHT"));
                                break;
                            case "REPORT":
                                handleReport();
                                break;
                            default:
                                System.out.println("Invalid command");
                                break;
                        }
                    }
                    else {
                        System.out.println("PLACE command must be used first");
                    }
                }
                else{
                    System.out.println("Robot Challenge Ended");
                }
            }
            else{
                System.out.println("Invalid Command Syntax");
            }
        }

    }
    /**
     * Compares the users input with various regex statements to determine that the input is valid
     * @param input The users input
     * @return true of false depending on if the users input passes the regex checks
     */
    private boolean regexInputChecks(String input) {
        // Ensures PLACE command is "PLACE *SPACE* number[0-4] *comma* number[0-4] *comma* DIRECTION"
        Pattern placeRegexPatternCheck = Pattern.compile("^PLACE\s[0-4]+,[0-4]+,(NORTH|SOUTH|EAST|WEST)$", Pattern.CASE_INSENSITIVE);
        Matcher placeMatcher = placeRegexPatternCheck.matcher(input);
        boolean placeMatchFound = placeMatcher.find();

        // Ensures MOVE, LEFT, RIGHT commands are "MOVE", "LEFT", "RIGHT" or "ROBOT robotNumber MOVE/LEFT/RIGHT"
        Pattern moveLeftRightRegexCheck = Pattern.compile("^(ROBOT\\s\\d+\\s)?(MOVE|LEFT|RIGHT)$",  Pattern.CASE_INSENSITIVE);
        Matcher moveLeftRightMatcher = moveLeftRightRegexCheck.matcher(input);
        boolean moveLeftRightMatchFound = moveLeftRightMatcher.find();

        // Ensures REPORT command is "REPORT" and an added "END" command to end the game input loop
        Pattern reportEndRegexCheck = Pattern.compile("^(REPORT|END)$",  Pattern.CASE_INSENSITIVE);
        Matcher reportEndMatcher = reportEndRegexCheck.matcher(input);
        boolean reportEndMatchFound = reportEndMatcher.find();

        if (placeMatchFound || moveLeftRightMatchFound || reportEndMatchFound) {
            return true;
        }
        return false;

    }

    /**
     * Handles creating a new robot and adding them to the gameTable
     * @param inputArray The array containing the split version of the users input
     */
    private void handlePlace(String[] inputArray) {
        String[] coordinateDirection = inputArray[1].split(",");

        // retrieve X and Y coordinate
        int x = Integer.parseInt(coordinateDirection[0]);
        int y = Integer.parseInt(coordinateDirection[1]);

        // create a new robot with the specified x,y and direction and add it to the gameTable
        gameTable.getRobots().add(new Robot(x, y, readDirection(coordinateDirection[2])));

        // set the current robot to the first robot in the list and state that 1 robot is on the board
        currentRobot = gameTable.getRobots().get(0);
        robotPlaced = true;
    }

    /**
     * Handles incrementing or decrementing of the robots x or y variable based on the direction moved.
     * Also checks that the robot isn't going off the board.
     *
     * @param robot the currently active robot
     */
    private void handleMove(Robot robot){

        int currentX = robot.getX();
        int currentY = robot.getY();

        switch (robot.getDirection()) {
            case NORTH:
                if (currentY + 1 > 4){
                    System.out.println("You cant move any further NORTH");
                    break;
                }
                robot.setY(currentY + 1);
                break;
            case EAST:
                if (currentX + 1 > 4){
                    System.out.println("You cant move any further EAST");
                    break;
                }
                robot.setX(currentX + 1);
                break;
            case SOUTH:
                if (currentY -1 < 0){
                    System.out.println("You cant move any further SOUTH");
                    break;
                }
                robot.setY(currentY - 1);
                break;
            default:
                if (currentX - 1 < 0){
                    System.out.println("You cant move any further WEST");
                    break;
                }
                robot.setX(currentX - 1);
                break;
        }

    }

    /**
     * rotates the robot based on the inputs.
     * @param direction the current direction of the robot
     * @param rotation the direction the robot is rotating
     * @return the new direction
     */
    private direction rotateDirection(direction direction, String rotation) {
        switch(rotation){
            // Turning left
            case "LEFT":
                switch(direction){
                    case NORTH: return direction.WEST;
                    case EAST: return direction.NORTH;
                    case SOUTH: return direction.EAST;
                    default: return direction.SOUTH;
                }
            // turning right
            default:
                switch(direction){
                    case NORTH: return direction.EAST;
                    case EAST: return direction.SOUTH;
                    case SOUTH: return direction.WEST;
                    default: return direction.NORTH;
                }
        }
    }


    /**
     * Helper function that reads the direction in string format and converts it to the direction enum value
     * @param directionStr the direction in string format
     * @return the translated direction
     */
    private direction readDirection(String directionStr){

        switch(directionStr.toUpperCase(Locale.ROOT)){
            case "EAST":
                return direction.EAST;
            case "SOUTH":
                return direction.SOUTH;
            case "WEST":
                return direction.WEST;
            default:
                return direction.NORTH;
        }
    }

    /**
     * Handles printing the report
     */
    private void handleReport(){
        // If only 1 robot print standard output
        if (gameTable.getRobots().size() == 1){
            System.out.println("\nOutput: " + currentRobot.getX() + "," + currentRobot.getY() + "," + currentRobot.getDirection().toString());
        }

        // If multiple report all robots positions on new lines
        else {
            System.out.println("\nOutput: "+ gameTable.getRobots().size() + " Robots present");
            for (int i = 0; i < gameTable.getRobots().size(); i++){
                System.out.println("Robot " + (i + 1) +": " + gameTable.getRobots().get(i).getX() + "," + gameTable.getRobots().get(i).getY() + "," + gameTable.getRobots().get(i).getDirection().toString() + (i == 0 ? "(Active)" : ""));

            }
        }
    }

    public static void main(String[] args) {

        Game robotGame = new Game();
        System.out.println(COMMANDS);
        System.out.println("Enter a place command:");

        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        robotGame.readInput(userInput);

        while (!userInput.equals("END")) {
            userInput = scanner.nextLine();
            robotGame.readInput(userInput);
        }
        scanner.close();
    }
}























