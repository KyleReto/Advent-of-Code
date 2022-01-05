import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

class TwentyOne {

    public static void main(String[] args) throws IOException {
        String[] input = getInput(21);
        int p1Score = 0;
        int p2Score = 0;
        // Space is from 0 to 9
        int p1Space = Integer.parseInt(input[0]) - 1;
        int p2Space = Integer.parseInt(input[1]) - 1;
        final int P1_START = p1Space;
        final int P2_START = p2Space;
        int playerTurn = 0;
        int[] dieVal = { 1, 0 };
        while (p1Score < 1000 && p2Score < 1000) {
            int roll = rollDeterministic(dieVal) + rollDeterministic(dieVal) + rollDeterministic(dieVal);
            if (playerTurn == 0) {
                p1Space = (p1Space + roll) % 10;
                p1Score += p1Space + 1;
                playerTurn = 1;
            } else {
                p2Space = (p2Space + roll) % 10;
                p2Score += p2Space + 1;
                playerTurn = 0;
            }
        }
        if (p1Score >= 1000) {
            System.out.println("Part 1: " + p2Score * dieVal[1]);
        } else {
            System.out.println("Part 1: " + p1Score * dieVal[1]);
        }
        long p1Universes = findWinningUniverses(0, P1_START, 0, P2_START, 0);
        long p2Universes = findWinningUniverses(0, P2_START, 0, P1_START, 1);
        long mostUniverses = p1Universes >= p2Universes ? p1Universes : p2Universes;
        System.out.println("Part 2: " + mostUniverses);
    }

    public static int rollDeterministic(int[] dieVal) {
        int result = dieVal[0];
        dieVal[1]++;
        dieVal[0]++;
        if (dieVal[0] > 100){
            dieVal[0] = 1;
        }
        return result;
    }

    public static long findWinningUniverses(int p1Score, int p1Space, int p2Score, int p2Space, int turn) {
        // Frequency of all possible dice rolls, from 0 to 9
        int[] frequencies = { 0, 0, 0, 1, 3, 6, 7, 6, 3, 1 };
        long wins = 0;
        if (turn == 0) {
            for (int i = 3; i <= 9; i++) {
                // Add the roll to the space, adjusting for looping track
                int nextSpace = (p1Space + i) % 10;
                int nextScore = nextSpace + 1;
                if (p1Score + nextScore >= 21) {
                    wins += frequencies[i];
                } else {
                    wins += findWinningUniverses(p1Score+nextScore, nextSpace, p2Score, p2Space, 1) * frequencies[i];
                }
            }
        } else {
            for (int i = 3; i <= 9; i++) {
                // Add the roll to the space, adjusting for looping track
                int nextSpace = (p2Space + i) % 10;
                int nextScore = nextSpace + 1;
                if (p2Score + nextScore >= 21) {
                    // do nothing
                } else {
                    wins += findWinningUniverses(p1Score, p1Space, p2Score+nextScore, nextSpace, 0) * frequencies[i];
                }
            }
        }
        return wins;
    }

    // Given an input file, outputs an array of trimmed strings.
    public static String[] getInput(int day) throws IOException {
        String textInput = Files.readString(Paths.get("./inputs/" + day + ".txt"));
        String[] inputs = textInput.trim().split("\n");
        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = inputs[i].trim().split(": ")[1].trim();
        }
        return inputs;
    }
}