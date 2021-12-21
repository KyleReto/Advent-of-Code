import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class Ten {
    
    public static void main(String[] args) throws Exception{
        String[] input = getInput(10);
        ArrayList<StringBuilder> parsedSBs = new ArrayList<>();
        // For each string
        // iterate over each character
        // For every ]/}/)/> found, backtrack to find its pair.
        // When a pair is found, delete both that character and the paired character.
        // If the final result is empty string, you're done
        // If any pair is not found, it's corrupted.
        // If the result isn't empty, it's incomplete

        HashMap<Character, Integer> syntaxScores = new HashMap();
        syntaxScores.put(')', 3);
        syntaxScores.put(']', 57);
        syntaxScores.put('}', 1197);
        syntaxScores.put('>', 25137);

        int score = 0;
        for (String str : input){
            boolean isStringValid = true;
            StringBuilder sb = new StringBuilder(str);
            for (int i = 0; i < sb.length(); i++){
                try{
                    int pairIndex = findPair(sb, i);
                    if (pairIndex != -1){
                        sb.deleteCharAt(i);
                        sb.deleteCharAt(pairIndex);
                        // Decreasing the size of sb means the iterator needs to be updated.
                        i-= 2;
                    } else {
                        score += syntaxScores.get(sb.charAt(i));
                        isStringValid = false;
                        break;
                    }
                } catch (Exception e){
                    // If findPair throws anything, it's because it's not a }/]/)/>
                }
            }
            if (isStringValid){
                parsedSBs.add(sb);
            }
        }
        System.out.println("Syntax score is " + score);
        // We now have a list of only the starting characters.
        // To match them, we just have to iterate backwards finding their pairs
        // For each pair, we calculate the points as directed
        // Store the points in an array of their own
        // Sort them and grab the middle to find the median.

        long[] completionScores = new long[parsedSBs.size()];
        for (int i = 0; i < parsedSBs.size(); i++){
            long completionScore = 0;
            for (int k = parsedSBs.get(i).length()-1; k >= 0; k--){
                completionScore *= 5;
                switch (parsedSBs.get(i).charAt(k)){
                    case '(':
                    completionScore += 1;
                        break;
                    case '[':
                    completionScore += 2;
                        break;
                    case '{':
                    completionScore += 3;
                        break;
                    case '<':
                    completionScore += 4;
                        break;
                    default:
                        throw new Exception("Char has no valid pair");
                }
            }
            completionScores[i] = completionScore;
        }
        
        Arrays.sort(completionScores);
        System.out.println("Median completion score is " + completionScores[completionScores.length/2]);

    }

    // Given a stringbuilder and the index of an ending character, find its pair's index
    public static int findPair(StringBuilder sb, int charIndex) throws Exception{
        char matchingSymbol;
        switch (sb.charAt(charIndex)){
            case '}':
                matchingSymbol = '{';
                break;
            case ')':
                matchingSymbol = '(';
                break;
            case ']':
                matchingSymbol = '[';
                break;
            case '>':
                matchingSymbol = '<';
                break;
            default:
                throw new Exception("Char has no valid pair");
        }
        // Pair found, delete both parts of pair.
        if (sb.charAt(charIndex - 1) == matchingSymbol){
            return charIndex-1;
        }
        // Pair not found.
        return -1;
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