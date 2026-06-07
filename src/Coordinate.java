// Represents the x and y positions of each square on the checkerboard
// Notates these positions using a letter and a number ranging from A1 - H8
class Coordinate {
    private final int y;
    private final int x;

    final static char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
    final static char[] numbers = {'8', '7', '6', '5', '4', '3', '2', '1'};

    // Basic Constructor
    private Coordinate(int x, int y) {
        this.y = y;
        this.x = x;
    }

    // Creates a new Coordinate using the provided x and y values
    // Returns null if the x or y < 0 or > 7
    public static Coordinate newCoordinate(int x, int y) {
        if (y < 0 || x < 0 || y > 7 || x > 7) return null;
        else return new Coordinate(x, y);
    }

    // Converts the String coordinate provided to its array positions then creates a new Coordinate object
    // Returns null if the string is not a valid coordinate
    public static Coordinate newCoordinate(String s) {
        if (!isValidCoordinate(s)) return null;
        else {
            int letterIndex = -1;
            int numberIndex = -1;

            for (int i = 0; i < 8; i++) {
                if (s.charAt(0) == letters[i]) {
                    letterIndex = i;
                }
                if (s.charAt(1) == numbers[i]) {
                    numberIndex = i;
                }
            }

            return new Coordinate(letterIndex, numberIndex);
        }
    }

    // Returns the string form of the Coordinate
    @Override
    public String toString() {
        return new String(new char[]{letters[x], numbers[y]});
    }

    public boolean equals(Coordinate c) {
        if (c == null) return false;
        return (this.x == c.x && this.y == c.y);
    }

    // Returns the coordinate at double the distance from this coordinate to coordinate c
    public Coordinate getCoordinateAfter(Coordinate c) {
        int cX = c.getX();
        int cY = c.getY();

        int xDifference = cX - this.x;
        int yDifference = cY - this.y;

        return newCoordinate(cX + xDifference, cY + yDifference);
    }

    // Returns the y coordinate value
    public int getY() {
        return y;
    }

    // Returns the x coordinate value
    public int getX() {
        return x;
    }

    // Checks if the provided String is exactly any letter between A and H followed by any number between 1 and 8.
    public static boolean isValidCoordinate(String c) {
        String regex = "^[A-H][1-8]$";
        return c.matches(regex);
    }
}
