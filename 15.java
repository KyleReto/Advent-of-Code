import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import java.util.PriorityQueue;

class Fifteen {

    public static void main(String[] args) throws IOException {
        int[][] input = getInput(15);
        Node start = new Node(0, 0, input[0][0]);
        Node goal = new Node(input.length-1, input[0].length-1, input[input.length-1][input[0].length-1]);
        // Recursively move right, down, left, up to every unchecked space
        // Get the risk level of the total path.
        // Save the lowest risk level
        // Once implemented, change BFS to evaluting lowest numbers first.
        System.out.println("Part 1: " + aStar(start, goal, input));

        int[][] biggerMap = multiplyMap(input);
        goal = new Node(biggerMap.length-1, biggerMap[0].length-1, biggerMap[biggerMap.length-1][biggerMap[0].length-1]);
        System.out.println("Part 2: " + aStar(start, goal, biggerMap));
    }

    public static long aStar(Node start, Node goal, int[][] grid){
        // A map of the estimated cost from the start to the end through this node.
        HashMap<Node, Long> fScore = new HashMap<>();
        // The set of all discovered nodes
        PriorityQueue<Node> openSet = new PriorityQueue<>(11, new SortByValue(fScore));
        openSet.add(start);
        // A map from all nodes to their cheapest predecessors
        HashMap<Node, Node> cameFrom = new HashMap<>();
        // A map of the true cost of the path from start to the given node
        // Always default to infinity
        HashMap<Node, Long> gScore = new HashMap<>();
        gScore.put(start, (long) 0);
        fScore.put(start, (long) 0);

        // h = manhattan distance to goal?
        
        // While openset is not empty
        while (!openSet.isEmpty()){
            //current = lowest fScore node in openset
            Node current = openSet.poll();
            // if current = goal
            if (current.equals(goal)){
                //return the found path total
                return gScore.get(current);
            }
            // for each neighbor of current
            ArrayList<Node> nodes = getAdjacentValidSpaces(grid, current.row, current.col);
            for (Node neighbor : nodes){
                // tentative gScore = gScore[current] + neighbor's weight
                long tentativeGScore = gScore.getOrDefault(current, Long.MAX_VALUE) + neighbor.val;
                if (tentativeGScore < 0) tentativeGScore = Integer.MAX_VALUE;
                // if tentGScore < gScore[neighbor]
                if (tentativeGScore < gScore.getOrDefault(neighbor, Long.MAX_VALUE)){
                    // cameFrom[neighbor] = current
                    cameFrom.put(neighbor, current);
                    // gScore[neighbor] = tentativeGScore
                    gScore.put(neighbor, tentativeGScore);
                    // fScore[neighbor] = tentativeGScore + h(neighbor)
                    long h = 0;
                    long currentFScore = tentativeGScore + h;
                    if (currentFScore < 0) currentFScore = Integer.MAX_VALUE;
                    fScore.put(neighbor, currentFScore);
                    // if neighbor is not in openset
                    if (!openSet.contains(neighbor)){
                        //openset add neighbor
                        openSet.add(neighbor);
                    }
                }
            }
        }
        return -1;
    }

    // Returns sorted list of spaces info, space info is row/col/risk.
    public static ArrayList<Node> getAdjacentValidSpaces(int[][] grid, int row, int col){
        ArrayList<Node> spaces = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            int yMod = 0;
            int xMod = 0;
            if (i == 0) xMod = -1;
            if (i == 1) xMod = +1;
            if (i == 2) yMod = -1;
            if (i == 3) yMod = +1;
            try{
                if (grid[row + yMod][col + xMod] != -1){
                    Node space = new Node(row + yMod, col + xMod, grid[row+yMod][col+xMod]);
                    spaces.add(space);
                }
            } catch (IndexOutOfBoundsException e){
                // Do nothing
            }
        }
        return spaces;
    }

    // Given an input file, outputs an array of trimmed strings.
    public static int[][] getInput(int day) throws IOException {
        String textInput = Files.readString(Paths.get("./inputs/" + day + ".txt"));
        String[] rows = textInput.trim().split("\n");
        int[][] output = new int[rows.length][rows[0].trim().length()];
        for (int i = 0; i < rows.length; i++) {
            String[] split = rows[i].trim().split("");
            for (int j = 0; j < output[i].length; j++) {
                output[i][j] = Integer.parseInt(split[j].trim());
            }
        }
        return output;
    }

    public static int[][] multiplyMap(int[][] input){
        int[][] newMap = new int[input.length*5][input[0].length*5];
        // For each tile in the original map
        for (int i = 0; i < input.length; i++){
            for (int j = 0; j < input[0].length; j++){
                // For each of the 25 grids it'll be added to
                for (int k = 0; k < 5; k++){
                    for (int l = 0; l < 5; l++){
                        int rowOffset = input.length * k;
                        int colOffset = input[0].length * l;
                        int val = (input[i][j] + k + l) % 9;
                        if (val == 0) val = 9;
                        newMap[rowOffset + i][colOffset + j] = val;
                    }
                }
            }
        }
        return newMap;
    }
}

class Node{
    public int row;
    public int col;
    public int val;

    Node(int row, int col, int val){
        this.row = row;
        this.col = col;
        this.val = val;
    }

    @Override
    public String toString() {
        return "Row: " + row + ", Col: " + col + ", Val: " + val + "\n";
    }

    @Override
    public boolean equals(Object ob){
        Node compare = (Node) ob;
        return (this.row == compare.row && this.col == compare.col);
    }

    @Override
    public int hashCode(){
        return Objects.hash(row, col);
    }
}

class SortByValue implements Comparator<Node>{
    public HashMap<Node, Long> fScore;
    SortByValue(HashMap<Node, Long> fScore){
        this.fScore = fScore;
    }

    public int compare(Node a, Node b){
        return   (int) (fScore.getOrDefault(a, Long.MAX_VALUE) - fScore.getOrDefault(b, Long.MAX_VALUE));
    }
}