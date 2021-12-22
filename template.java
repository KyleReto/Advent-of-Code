import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Template {
    
    public static void main(String[] args) throws IOException{
        String[] input = getInput(0);
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
}