import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

class Thirteen {
    
    public static void main(String[] args) throws IOException{
        String[] input = getInput(13);
        ArrayList<String> dots = new ArrayList<>();
        ArrayList<String> folds = new ArrayList<>();
        // Process the input into dot and fold instruction lists.
        boolean isFold = false;
        for (String str : input){
            if (isFold){
                folds.add(str);
            } else if (str.equals("")){
                // If we hit the empty spot, then we can switch to adding folds
                isFold = true;
            } else {
                dots.add(str);
            }
        }
        boolean[][] grid = makeGrid(dots);
        boolean[][] foldedOnce = fold(grid, folds.get(0));
        System.out.println("First fold dot count: " + countDots(foldedOnce));
        for (String instruction : folds){
            grid = fold(grid, instruction);
        }
        System.out.println("Final grid:");
        printGrid(grid);
    }

    // Given an input file, outputs an array of trimmed strings.
    public static String[] getInput(int day) throws IOException{
        String textInput = Files.readString(Paths.get("./inputs/" + day + ".txt"));
        String[] inputs = textInput.trim().split("\n");
        for (int i = 0; i < inputs.length; i++){
            inputs[i] = inputs[i].trim();
        }
        return inputs;
    }

    public static boolean[][] fold(boolean[][] grid, String command){
        String[] split = command.split("=");
        char axis = split[0].charAt(split[0].length()-1);
        int foldVal = Integer.parseInt(split[1]);
        int ySize = grid.length;
        int xSize = grid[0].length;
        if (axis == 'y'){
            ySize = foldVal;
        } else {
            xSize = foldVal;
        }
        boolean[][] newGrid = new boolean[ySize][xSize];
        
        // Reprint unmoved dots
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[0].length; j++){
                if (i > ySize){
                    // Mirror Vertically
                    int newY = 2 * ySize - i;
                    newGrid[newY][j] = newGrid[newY][j] || grid[i][j];
                }else if (j > xSize){
                    // Mirror Horizontally
                    int newX = 2 * xSize - j;
                    newGrid[i][newX] = newGrid[i][newX] || grid[i][j];
                } else if (i == ySize || j == xSize){
                    // Do nothing if we're on a fold line
                } else {
                    newGrid[i][j] = newGrid[i][j] || grid[i][j];
                }
            }
        }
        // Print all mirrored dots
        return newGrid;
    }

    // Outputs a grid of arbitrarty size given the dot parameter
    public static boolean[][] makeGrid(ArrayList<String> dots){
        int[] xArr = new int[dots.size()];
        int[] yArr = new int[dots.size()];
        int xMax = 0;
        int yMax = 0;
        // Create the grid and parse the ints
        for (int i = 0; i < dots.size(); i++){
            String[] split = dots.get(i).split(",");
            xArr[i] = Integer.parseInt(split[0]);
            yArr[i] = Integer.parseInt(split[1]);
            if (xArr[i] > xMax){
                xMax = xArr[i];
            }
            if (yArr[i] > yMax){
                yMax = yArr[i];
            }
        }
        boolean[][] grid = new boolean[yMax+1][xMax+1];
        // Populate the grid
        for (int i = 0; i < xArr.length; i++){
            grid[yArr[i]][xArr[i]] = true;
        }
        return grid;
    }

    public static void printGrid(boolean[][] grid){
        StringBuilder sb = new StringBuilder();
        for (boolean[] row : grid){
            for (boolean cell : row){
                if (cell){
                    sb.append("#");
                } else {
                    sb.append(".");
                }
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    public static int countDots(boolean[][] grid){
        int sum = 0;
        for (boolean[] row : grid){
            for (boolean cell : row){
                if (cell){
                    sum++;
                }
            }
        }
        return sum;
    }
}