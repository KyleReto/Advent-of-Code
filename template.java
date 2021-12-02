import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Main {
    
    public static void main(String[] args) throws IOException{
    }

    public static String[] getInput(String path) throws IOException{
        String textInput = Files.readString(Paths.get("./inputs/2.txt"));
        String[] inputs = textInput.split("\n");
        for (int i = 0; i < inputs.length; i++){
            inputs[i] = inputs[i].trim();
        }
        return inputs;
    }
}