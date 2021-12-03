import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Three {
    
    public static void main(String[] args) throws Exception{
        String[] inputs = getInput();
        String gammaString = getGamma(inputs);
        int gamma = Integer.parseInt(gammaString, 2);
        int episilon = getEpisilon(gamma, gammaString.length());
        System.out.println(gamma);
        System.out.println(episilon);
        System.out.println(gamma * episilon);
    }

    static String getGamma(String[] input) throws RuntimeException{
        String result = "";
        //For each digit in the number
        for (int digit = 0; digit < input[0].length(); digit++){
            int oneCount = 0;
            int zeroCount = 0;
            // For each number in the set
            for (int num = 0; num < input.length; num++){
                if (input[num].substring(digit, digit+1).equals("1")){
                    oneCount++;
                } else {
                    zeroCount++;
                }
            }
            if (oneCount > zeroCount){
                result += "1";
            } else if (zeroCount > oneCount){
                result += "0";
            } else {
                throw new RuntimeException("Error: Counts are equal");
            }
        }
        return result;
    }

    static int getEpisilon(int gamma, int length){
        String bitMask = "";
        for (int i = 0; i < length; i++){
            bitMask += "1";
        }
        int maskVal = Integer.parseInt(bitMask, 2);
        return gamma^maskVal;
    }

    // Given an input file, outputs an array of trimmed strings.
    public static String[] getInput() throws IOException{
        String textInput = Files.readString(Paths.get("./inputs/3.txt"));
        String[] inputs = textInput.split("\n");
        for (int i = 0; i < inputs.length; i++){
            inputs[i] = inputs[i].trim();
        }
        return inputs;
    }
}