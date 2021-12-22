import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.IntStream;

class Eleven {

    public static void main(String[] args) throws IOException {
        int[][] input = getInput(11);
        printGrid(input);
        final int NUM_STEPS = 1000;
        int totalFlashes = 0;
        int firstFlashedStep = -1;
        for (int i = 0; i < NUM_STEPS; i++){
            totalFlashes += stepGrid(input);
            if (firstFlashedStep == -1 && isFlashSynced(input)){
                firstFlashedStep = i+1;
            }
        }
        System.out.println("Total Flashes: " + totalFlashes);
        System.out.println("First Synchronized Step: " + firstFlashedStep);
    }

    // Given an input file, outputs an array of trimmed strings.
    public static int[][] getInput(int day) throws IOException {
        String textInput = Files.readString(Paths.get("./inputs/" + day + ".txt"));
        String[] inputs = textInput.trim().split("\n");
        int[][] grid = new int[inputs.length][inputs[0].trim().length()];
        for (int i = 0; i < grid.length; i++) {
            String[] row = inputs[i].trim().split("");
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = Integer.parseInt(row[j].trim());
            }
        }
        return grid;
    }

    public static void printGrid(int[][] grid) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 0) {
                    output.append("\u001B[32m" + grid[i][j] + "\u001B[0m");
                } else {
                    output.append(grid[i][j]);
                }
            }
            output.append("\n");
        }
        System.out.println(output);
    }

    public static int stepGrid(int[][] grid){
        int flashes = 0;
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[0].length; j++){
                flashes += incrementEnergy(grid, i, j);
            }
        }
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[0].length; j++){
                // Reset flashed octopuses
                if (grid[i][j] == -1) grid[i][j] = 0;
            }
        }
        return flashes;
    }

    public static int incrementEnergy(int[][] grid, int row, int col){
        int flashes = 0;
        // Skip the octopus entirely if it has already flashed this pass.
        if (grid[row][col] == -1) return 0;
        // Bump this one's energy up
        grid[row][col]++;
        if (grid[row][col] > 9){
            flashes += 1;
            // Mark octopus as flashed
            grid[row][col] = -1;
            // Bump and evaluate a 3x3 grid of octopuses, centered on row/col
            for (int i = row - 1; i <= row + 1; i++){
                for (int j = col - 1; j <= col + 1; j++){
                    try{
                        // Recursively bump this octopus. Note that this *does* evaluate the center.
                        flashes += incrementEnergy(grid, i, j);
                    } catch (ArrayIndexOutOfBoundsException e){
                        // Do nothing
                    }
                }
            }
            return flashes;
        } else {
            return 0;
        }
    }

    public static boolean isFlashSynced(int[][] grid){
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[0].length; j++){
                if (grid[i][j] != 0) return false;
            }
        }
        return true;
    }
}