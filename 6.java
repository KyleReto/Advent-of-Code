import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

class Six {

    public static void main(String[] args) throws IOException{
        String[] input = getInput(6);
        long[] lanternfish = new long[9];
        // Initialize everything to 0
        for (int i = 0; i < lanternfish.length; i++){
            lanternfish[i] = 0;
        }
        // Add all lanternfish to the array
        for (int i = 0; i < input.length; i++){
            lanternfish[Integer.parseInt(input[i])] += 1;
        }

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter number of days: ");
        int days = scan.nextInt() + 1;
        scan.close();
        for (int i = 1; i < days; i++){
            // Move all values down one, moving the lowest to the back.
            for (int j = 0; j < lanternfish.length-1; j++){
                long temp = 0;
                temp = lanternfish[j];
                lanternfish[j] = lanternfish[j+1];
                lanternfish[j+1] = temp;
            }

            // Add the spawned fish back to the list.
            lanternfish[6] += lanternfish[8];

            // Sum all the fish.
            long sum = 0;
            for (int j = 0; j < lanternfish.length; j++){
                sum += lanternfish[j];
            }
            System.out.println("Day = " + i + ", Fish Count = " + sum);
        }
    }

    // Given an input file, outputs an array of trimmed strings.
    public static String[] getInput(int day) throws IOException{
        String textInput = Files.readString(Paths.get("./inputs/" + day + ".txt"));
        String[] inputs = textInput.split(",");
        for (int i = 0; i < inputs.length; i++){
            inputs[i] = inputs[i].trim();
        }
        return inputs;
    }
}