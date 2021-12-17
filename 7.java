import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Seven {
    
    public static void main(String[] args) throws IOException{
        int[] input = getInput(7);
        long cheapestFuel = Long.MAX_VALUE;
        int bestPos = 0;
        int maxPos = 10000;
        for (int i = 0; i <= maxPos; i++){
            // System.out.println("Testing X = " + i);
            long fuelSum = 0;
            for (int j = 0; j < input.length; j++){
                int dist = (Math.abs(i - input[j]));
                // calculate triangular number by explicit formula
                fuelSum += (dist * (dist + 1)) / 2;
                // System.out.println("crabPos = [" + input[j] + "], fuelCost = [" + (dist * (dist + 1)) / 2 
                // + "], sum = [" + fuelSum + "]");
            }
            if (fuelSum < cheapestFuel){
                bestPos = i;
                cheapestFuel = fuelSum;
            }
        }
        System.out.println("Optimal position is [" + bestPos + "], at [" + cheapestFuel + "] fuel.");
    }

    // Given an input file, outputs an array of trimmed strings.
    public static int[] getInput(int day) throws IOException{
        String textInput = Files.readString(Paths.get("./inputs/" + day + ".txt"));
        String[] inputs = textInput.split(",");
        int[] output = new int[inputs.length];
        for (int i = 0; i < inputs.length; i++){
            output[i] = Integer.parseInt(inputs[i].trim());
        }
        return output;
    }
}