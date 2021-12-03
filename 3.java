import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

class Three {
    
    public static void main(String[] args) throws Exception{
        String[] inputs = getInput();
        String gammaString = getGamma(inputs);
        int gamma = Integer.parseInt(gammaString, 2);
        int episilon = getEpisilon(gamma, gammaString.length());
        System.out.println(gamma);
        System.out.println(episilon);
        System.out.println(gamma * episilon);

        String O2String = getO2(inputs);
        int O2 = Integer.parseInt(O2String, 2);
        System.out.println(O2);
        String CO2String = getCO2(inputs);
        int CO2 = Integer.parseInt(CO2String, 2);
        System.out.println(CO2);
        System.out.println(O2 * CO2);
    }

    static String getO2(String[] input) throws RuntimeException{
        List<String> workingSet = Arrays.asList(input);
        //For each digit in the number
        for (int digit = 0; digit < workingSet.get(0).length(); digit++){
            int oneCount = 0;
            int zeroCount = 0;
            // For each number in the set
            for (int num = 0; num < workingSet.size(); num++){
                if (workingSet.get(num).substring(digit, digit+1).equals("1")){
                    oneCount++;
                } else {
                    zeroCount++;
                }
            }
            String criteria = "";
            if (oneCount >= zeroCount){
                criteria = "1";
            } else{
                criteria = "0";
            }
            List<String> newSet = new ArrayList<>();
            // eliminate the bad numbers
            for (int i = 0; i < workingSet.size(); i++){
                if (workingSet.get(i).substring(digit, digit+1).equals(criteria)){
                    newSet.add(workingSet.get(i));
                }
            }
            workingSet = newSet;
        }
        return workingSet.get(0);
    }

    static String getCO2(String[] input) throws RuntimeException{
        List<String> workingSet = Arrays.asList(input);
        System.out.println(workingSet);
        //For each digit in the number
        for (int digit = 0; digit < workingSet.get(0).length(); digit++){
            System.out.println(digit + ", " + workingSet.get(0));
            int oneCount = 0;
            int zeroCount = 0;
            // For each number in the set
            for (int num = 0; num < workingSet.size(); num++){
                if (workingSet.get(num).substring(digit, digit+1).equals("1")){
                    oneCount++;
                } else {
                    zeroCount++;
                }
            }
            String criteria = "";
            if (oneCount >= zeroCount){
                criteria = "0";
            } else{
                criteria = "1";
            }
            List<String> newSet = new ArrayList<>();
            // eliminate the bad numbers
            for (int i = 0; i < workingSet.size(); i++){
                if (workingSet.get(i).substring(digit, digit+1).equals(criteria)){
                    newSet.add(workingSet.get(i));
                }
            }
            workingSet = newSet;
            if (workingSet.size() <= 1){
                break;
            }
        }
        return workingSet.get(0);
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