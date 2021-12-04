import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

class Card{
    // First num is row, second is column.
    public int[][] board;
    public boolean[][] hits;
    
    public Card(String[][] input){
        board = new int[5][5];
        hits = new boolean[5][5];
        // For each row
        for (int i = 0; i < input.length; i++){
            // And each column
            for (int j = 0; j < input[0].length; j++){
                board[i][j] = Integer.parseInt(input[i][j].trim());
                hits[i][j] = false;
            }
        }
    }

    public String toString(){
        String output = "";
        // For each row
        for (int i = 0; i < board.length; i++){
            // And each column
            for (int j = 0; j < board[0].length; j++){
                output += ("[" + board[i][j] + "]");
                if (hits[i][j]){
                    output += "*";
                }
            }
            output += "\n";
        }
        return output;
    }

    public void markCard(int num){
        // For each row
        for (int i = 0; i < board.length; i++){
            // And each column
            for (int j = 0; j < board[0].length; j++){
                if (board[i][j] == num){
                    hits[i][j] = true;
                }
            }
        }
    }

    public int getScore(int lastNumber){
        int sum = 0;
        for (int i = 0; i < board.length; i++){
            // And each column
            for (int j = 0; j < board[0].length; j++){
                if (!hits[i][j]){
                    sum += board[i][j];
                }
            }
        }
        return sum * lastNumber;
    }

    public boolean checkBingo(){
        // Check each row for a full row.
        for (int i = 0; i < hits.length; i++){
            int rowSum = 0;
            for (int j = 0; j < hits[0].length; j++){
                if (hits[i][j]){
                    rowSum++;
                }
            }
            if (rowSum == 5){
                System.out.println("Won on row " + i);
                return true;
            }
        }
        // Check each column
        for (int i = 0; i < hits[0].length; i++){
            int colSum = 0;
            for (int j = 0; j < hits.length; j++){
                if (hits[j][i]){
                    colSum++;
                }
            }
            if (colSum == 5){
                System.out.println("Won on col " + i);
                return true;
            }
        }
        return false;
    }

}

class Four {
    public static void main(String[] args) throws IOException{
        ArrayList<Card> cards = getCards(4);
        ArrayList<Integer> winnerIndices = new ArrayList<>();
        ArrayList<Integer> scores = new ArrayList<>();
        int[] numbers = getNumbers(4);
        for (int i = 0; i < numbers.length; i++){
            for (int j = 0; j < cards.size(); j++){
                if (winnerIndices.contains(j)){
                    continue;
                }
                cards.get(j).markCard(numbers[i]);
                if (cards.get(j).checkBingo()){
                    winnerIndices.add(j);
                    scores.add(cards.get(j).getScore(numbers[i]));
                }
            }
        }
        System.out.println("The last board to win is " + winnerIndices.get(winnerIndices.size()-1) +
        " with " + scores.get(winnerIndices.size()-1) + " points."
        );
    }

    // Given an input file, outputs an array of bingo cards.
    public static ArrayList<Card> getCards(int day) throws IOException{
        String textInput = Files.readString(Paths.get("./inputs/" + day + ".txt"));
        String[] inputArray = textInput.split("\n");
        ArrayList<Card> cards = new ArrayList<>();
        int currRow = 0;
        String[][] workingBoard = new String[5][5];
        for (int i = 2; i < inputArray.length; i++){
            // If it's a spacing line, stop and make a new card.
            if (currRow == 5){
                cards.add(new Card(workingBoard));
                currRow = 0;
                continue;
            }
            String[] line = inputArray[i].trim().split("\\s+");
            for (int currCol = 0; currCol < workingBoard[0].length; currCol++){
                workingBoard[currRow][currCol] = line[currCol];
            }
            currRow++;
        }
        return cards;
    }

    public static int[] getNumbers(int day) throws IOException{
        String textInput = Files.readString(Paths.get("./inputs/" + day + ".txt"));
        String[] allInputs = textInput.split("\n");
        String[] numbersStrings = allInputs[0].split(",");
        int[] numbers = new int[numbersStrings.length];
        for (int i = 0; i < numbersStrings.length; i++){
            numbers[i] = Integer.parseInt(numbersStrings[i].trim());
        }
        return numbers;
    }
}