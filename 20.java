import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

class Twenty {
    
    public static void main(String[] args) throws IOException{
        String[] input = getInput(20);
        final int NUM_ENHANCE = 50;
        EnhancementAlgorithm algo = new EnhancementAlgorithm(input[0]);
        Image img = new Image(input, 2);
        boolean fill = false;
        for (int i = 0; i < NUM_ENHANCE; i++){
            img = img.enhance(algo, fill);
            // Account for all possible empty space fills (0 and 511 values)
            if (fill){
                fill = algo.getValue(511);
            } else {
                fill = algo.getValue(0);
            }
            if (i == 1) System.out.println("Number of Pixels Lit After Two Enhancements: " + img.getLitPixelCount());
        }
        System.out.println("Number of Pixels Lit After Fifty Enhancements: " + img.getLitPixelCount());
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

class Image{

    boolean[][] pixels;

    Image(String[] arr, int startIndex){
        pixels = new boolean[arr.length - startIndex][arr[startIndex].length()];
        for (int i = 0; i < pixels.length; i++){
            for (int j = 0; j < pixels[i].length; j++){
                pixels[i][j] = (arr[i+startIndex].charAt(j) == '#');
            }
        }
    }

    Image(boolean[][] pixels){
        this.pixels = pixels;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pixels.length; i++){
            for (int j = 0; j < pixels[i].length; j++){
                sb.append(pixels[i][j] ? '#' : '.');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public Image enhance(EnhancementAlgorithm algo, boolean fill){
        boolean[][] newPixels = new boolean[this.pixels.length+2][this.pixels[0].length+2];
        for (int i = 0; i < newPixels.length; i++){
            for (int j = 0; j < newPixels[i].length; j++){
                newPixels[i][j] = algo.getValue(getFocusVal(i-1, j-1, fill));
            }
        }
        return new Image(newPixels);
    }

    public long getLitPixelCount(){
        long sum = 0;
        for (int i = 0; i < pixels.length; i++){
            for (int j = 0; j < pixels[i].length; j++){
                if (pixels[i][j]) sum++;
            }
        }
        return sum;
    }

    public int getFocusVal(int row, int col, boolean fill){
        StringBuilder binaryNum = new StringBuilder();
        // For the 3x3 grid centered on row, col
        for (int i = row-1; i < row+2; i++){
            for (int j = col-1; j < col+2; j++){
                try{
                    binaryNum.append(pixels[i][j] ? 1 : 0);
                } catch (IndexOutOfBoundsException oob){
                    binaryNum.append(fill ? 1 : 0);
                }
            }
        }
        return Integer.parseInt(binaryNum.toString(), 2);
    }
}

class EnhancementAlgorithm{
    private HashMap<Integer, Boolean> map = new HashMap<>();
    EnhancementAlgorithm(String algo){
        for (int i = 0; i < algo.length(); i++){
            map.put(i, algo.charAt(i) == '#');
        }
    }

    boolean getValue(int key){
        return this.map.get(key);
    }
}