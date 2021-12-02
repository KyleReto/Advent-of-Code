import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class One{
    public static void main(String[] args) throws IOException{
        // Get and process the input
        String input = Files.readString(Paths.get("./inputs/1.txt"));
        String[] inputs = input.split("\n");
        int[] measurements = new int[2000];
        for (int i = 0; i < inputs.length; i++){
            measurements[i] = Integer.parseInt(inputs[i].trim());
        }
        System.out.println(slidingWindow(measurements));
    }

    // Find the number of times that the measurement increases
    public static int measurementSizeUpCount(int[] measurements){
        int count = 0;
        for (int i = 0; i < measurements.length-1; i++){
            if (measurements[i+1] > measurements[i]){
                count++;
            }
        }
        return count;
    }

    public static int slidingWindow(int[] measurements){
        int count = 0;
        for (int i = 2; i < measurements.length-1; i++){
            // Sum up each window and compare.
            int firstWindow = measurements[i] + measurements[i-1] + measurements[i-2];
            int secondWindow = measurements[i+1] + measurements[i] + measurements[i-1];
            if (secondWindow > firstWindow){
                count++;
            }
        }
        return count;
    }
}