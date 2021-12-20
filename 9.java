import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

class Nine {

    public static void main(String[] args) throws IOException {
        int[][] input = getInput(9);
        int riskSum = 0;
        int[] basins = new int[3];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                int[] neighbors = getNeighbors(input, i, j);
                boolean hasLowerNeighbor = false;
                for (int neighbor : neighbors) {
                    if (neighbor <= input[i][j] && neighbor != -1) {
                        hasLowerNeighbor = true;
                        break;
                    }
                }
                // If it's a low point
                if (!hasLowerNeighbor) {
                    riskSum += 1 + input[i][j];
                    addIfBigger(basins, getBasinSize(input, i, j));
                }
            }
        }
        System.out.println(String.format("Risk sum is [%d]", riskSum));
        int product = 1;
        for (int basin : basins){
            product *= basin;
        }
        System.out.println(String.format("Product is [%d]", product));
    }

    public static void addIfBigger(int[] basins, int newBasin){
        for (int i = 0; i < basins.length; i++){
            System.out.println(newBasin + ", " + basins[i]);
            if (newBasin >= basins[i]){
                basins[i] = newBasin;
                // Sort so that we don't overwrite higher values
                Arrays.sort(basins);
                break;
            }
        }
    }

    public static int getBasinSize(int[][] map, int row, int col) {
        // Create a copy of the map
        // Create a method called getNeighborCount
        // neighborCount = number of neighbors that aren't 9 or -1.
        // neighborCount runs recursively on the neighbor's neighbors as well.
        // neighborCount sets all checked neighbors in the map copy to -1.

        // can't directly clone 2d arrays
        int[][] mapCopy = new int[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            mapCopy[i] = map[i].clone();
        }
        // base case
        mapCopy[row][col] = -1;
        return getNeighborCount(mapCopy, row, col) + 1;
    }

    public static int getNeighborCount(int[][] map, int row, int col){
        int count = 0;
        for (int i = 0; i < 4; i++){
            int y = row;
            int x = col;
            if (i == 0) y -= 1;
            if (i == 1) y += 1;
            if (i == 2) x -= 1;
            if (i == 3) x += 1;
            try{
                if (map[y][x] != 9 && map[y][x] != -1){
                    map[y][x] = -1;
                    count++;
                    count += getNeighborCount(map, y, x);
                }
            } catch (ArrayIndexOutOfBoundsException oob){
                // Do nothing
            }
        }
        return count;
    }

    // Return an array of all of that cell's neighbors.
    // Ordered Top/Bottom/Left/Right, -1 for nonexistent neighbors
    public static int[] getNeighbors(int[][] map, int row, int col) {
        int[] neighbors = new int[4];
        for (int i = 0; i < neighbors.length; i++) {
            int y = row;
            int x = col;
            // There's gotta be a better way...
            if (i == 0)
                y -= 1;
            if (i == 1)
                y += 1;
            if (i == 2)
                x -= 1;
            if (i == 3)
                x += 1;
            try {
                neighbors[i] = map[y][x];
            } catch (ArrayIndexOutOfBoundsException oob) {
                neighbors[i] = -1;
            }
        }
        return neighbors;
    }

    // Given an input file, outputs the array parsed to ints.
    public static int[][] getInput(int day) throws IOException {
        String textInput = Files.readString(Paths.get("./inputs/" + day + ".txt"));
        String[] rows = textInput.trim().split("\n");
        String[][] strs = new String[rows.length][rows[0].length()];
        int[][] inputs = new int[rows.length][rows[0].length() - 1];
        for (int i = 0; i < strs.length; i++) {
            strs[i] = rows[i].trim().split("");
            for (int j = 0; j < rows[0].length() - 1; j++) {
                inputs[i][j] = Integer.parseInt(strs[i][j]);
            }
        }
        return inputs;
    }

    public static void printMap(int[][] map) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                sb.append("[" + map[i][j] + "] ");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }
}