import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

class Eight {

    public static void main(String[] args) throws IOException {
        String[] input = getInput(8);
        String[][] signals = new String[input.length][10];
        String[][] outputs = new String[input.length][4];
        for (int i = 0; i < input.length; i++) {
            String[] inputsWithOutputs = input[i].split("\\|");
            signals[i] = inputsWithOutputs[0].split("\\s+");
            outputs[i] = inputsWithOutputs[1].trim().split("\\s+");
        }

        int outputSum = 0;

        // For each signal
        for (int i = 0; i < signals.length; i++) {
            // Generate a key for decoding, placing numbers into their slots
            String[] key = new String[10];
            // Place the known elements in their positions
            for (int j = 0; j < signals[i].length; j++) {
                switch (signals[i][j].length()) {
                    case 2:
                        key[1] = signals[i][j];
                        break;
                    case 3:
                        key[7] = signals[i][j];
                        break;
                    case 4:
                        key[4] = signals[i][j];
                        break;
                    case 7:
                        key[8] = signals[i][j];
                        break;
                    default:
                        // Do nothing
                        break;
                }
            }
            // Now that those are in place, we do a second pass to add some more
            for (int j = 0; j < signals[i].length; j++) {
                // 3 is the only 5 len num that shares 7's numbers.
                if (signals[i][j].length() == 5 &&
                 compareNums(signals[i][j], key[7])) {
                    key[3] = signals[i][j];
                }
                // 6 is the only 6 len num that does *not* share 7's numbers
                if (signals[i][j].length() == 6 &&
                 !compareNums(signals[i][j], key[7])) {
                    key[6] = signals[i][j];
                }
                // 9 is the only 6 len that contains 4
                if (signals[i][j].length() == 6 &&
                 compareNums(signals[i][j], key[4])) {
                    key[9] = signals[i][j];
                }
            }
            // Third pass to grab the last few
            for (int j = 0; j < signals[i].length; j++) {
                // If it's 6 len and not 6 or 9, then it's zero
                if (signals[i][j].length() == 6 &&
                 !signals[i][j].equals(key[6]) &&
                 !signals[i][j].equals(key[9])){
                    key[0] = signals[i][j];
                }
                // If the number is 3, It can't be 2 or 5.
                if(signals[i][j].equals(key[3])){
                    continue;
                }
                // if the number is 2, place it
                if (signals[i][j].length() == 5 &&
                 isTwo(signals[i][j], key[8], key[6])){
                    key[2] = signals[i][j];
                }
                // If it's 5 len and not 2, then it's 5.
                if (signals[i][j].length() == 5 &&
                 !isTwo(signals[i][j], key[8], key[6])){
                    key[5] = signals[i][j];
                }
            }
            // We now have our key.
            int result = 0;
            for (int j = 0; j < outputs[i].length; j++) {
                // Add the result at the appropriate place value.
                int multi = (int) Math.pow(10, (3 - j));
                result += decode(outputs[i][j], key) * multi;

            }
            outputSum += result; 

        }

        int sum = 0;
        for (int i = 0; i < outputs.length; i++) {
            for (int j = 0; j < outputs[0].length; j++) {
                int len = outputs[i][j].length();
                if (len == 2 || len == 4 || len == 3 || len == 7) {
                    sum++;
                }

            }
        }
        System.out.println("Unique Digit Sum = " + sum);
        System.out.println("Ouput Total = " + outputSum);
    }

    // Given the key, decode a given target string to its value.
    static int decode(String target, String[] key){
        char[] charArr = target.toCharArray();
        Arrays.sort(charArr);
        for (int i = 0; i < key.length; i++){
            char[] keyArr = key[i].toCharArray();
            Arrays.sort(keyArr);
            if (Arrays.equals(charArr, keyArr)){
                return i;
            }
        }
        return -1;
    }

    // Given a known eight and six, check if a 5 len str is 2. Otherwise, it's 7.
    static boolean isTwo(String str, String eight, String six){
        String[] eightArr = eight.split("");
        String uncommonLetter = "";
        for (String letter : eightArr){
            if (!six.contains(letter)){
                uncommonLetter = letter;
                break;
            }
        }
        return str.contains(uncommonLetter);
    }

    // Check if the larger num has all of the smaller num's letters
    static boolean compareNums(String largerNum, String smallerNum) {
        String[] arr = smallerNum.split("");
        // if any smallerNum letter is missing in largerNum, return false
        for (String letter : arr) {
            if (!largerNum.contains(letter)) {
                return false;
            }
        }
        // Otherwise, all are present, so return true.
        return true;
    }

    // Given an input file, outputs an array of trimmed strings.
    public static String[] getInput(int day) throws IOException {
        String textInput = Files.readString(Paths.get("./inputs/" + day + ".txt"));
        String[] inputs = textInput.split("\n");
        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = inputs[i].trim();
        }
        return inputs;
    }
}