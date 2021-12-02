import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Two {
    
    public static void main(String[] args) throws IOException{
        String[] inputs = getInput();
        // Process the strings into two arrays with shared indices.
        int[] distances = new int[inputs.length];
        String[] directions = new String[inputs.length];
        for (int i = 0; i < inputs.length; i++){
            distances[i] = Integer.parseInt(inputs[i].substring(inputs[i].length()-1));
            directions[i] = inputs[i].substring(0, inputs[i].length()-1).trim();
            System.out.println(distances[i]);
            System.out.println("\'" + directions[i] + "\'");
        }
        int xPos = 0;
        int depth = 0;
        int aim = 0;
        // Calculate out the x position, depth, and aim according to directions.
        for (int i = 0; i < distances.length; i++){
            if (directions[i].equals("forward")){
                xPos += distances[i];
                depth += (aim*distances[i]);
            }
            if (directions[i].equals("down")){
                aim += distances[i];
            }
            if (directions[i].equals("up")){
                aim -= distances[i];
            }
        }
        System.out.println(xPos * depth);
    }

    // Given an input file, outputs an array of trimmed strings.
    public static String[] getInput() throws IOException{
        String textInput = Files.readString(Paths.get("./inputs/2.txt"));
        String[] inputs = textInput.split("\n");
        for (int i = 0; i < inputs.length; i++){
            inputs[i] = inputs[i].trim();
        }
        return inputs;
    }
}
