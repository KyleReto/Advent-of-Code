import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

class Fourteen {
    
    public static void main(String[] args) throws IOException{
        String[] input = getInput(14);
        String polymer = input[0];
        HashMap<String, String> rules = new HashMap<>();
        HashMap<String, Long> polymerMap = disassemblePolymer(polymer);
        final int STEPS = 40;
        // Split the input into rules arrays
        for (int i = 2; i < input.length; i++){
            String[] rule = input[i].split(" -> ");
            rules.put(rule[0], rule[1]);
        }
        // Grow the polymer
        for (int i = 0; i < STEPS; i++){
            polymerMap = stepGrowth(polymerMap, rules);
        }
        // Calculate the answer
        long leastCommonQuantity = Long.MAX_VALUE;
        long mostCommonQuantity = 0;
        HashMap<Character, Long> quantities = getElementQuantities(polymerMap, polymer);
        for (Map.Entry<Character, Long> entry : quantities.entrySet()){
            long quantity = quantities.getOrDefault(entry.getKey(), (long) 0);
            if (quantity > mostCommonQuantity) mostCommonQuantity = quantity;
            if (quantity < leastCommonQuantity) leastCommonQuantity = quantity;
        }
        System.out.println(mostCommonQuantity - leastCommonQuantity);
    }

    public static HashMap<Character, Long> getElementQuantities(HashMap<String, Long> polymer, String base){
        HashMap<Character, Long> elements = new HashMap<>();
        for (Map.Entry<String, Long> entry : polymer.entrySet()){
            String pair = entry.getKey();
            Long val = entry.getValue();
            if (elements.putIfAbsent(pair.charAt(1), val) != null){
                elements.put(pair.charAt(1), val + elements.get(pair.charAt(1)));
            }
        }
        // Add 1 to start character because it wasn't counted.
        elements.put(base.charAt(0), elements.get(base.charAt(0)) + 1);
        return elements;
    }

    public static HashMap<String, Long> stepGrowth(HashMap<String, Long> polymer, HashMap<String, String> rules){
        HashMap<String, Long> newPoly = new HashMap<>();
        for (Map.Entry<String, String> rule : rules.entrySet()){
            long pairCount = polymer.getOrDefault(rule.getKey(), (long) 0);
            String firstNewPair = rule.getKey().charAt(0) + rule.getValue();
            String secondNewPair = rule.getValue() + rule.getKey().charAt(1);
            if (newPoly.putIfAbsent(firstNewPair, pairCount) != null){
                newPoly.put(firstNewPair, pairCount + newPoly.get(firstNewPair));
            }
            if (newPoly.putIfAbsent(secondNewPair, pairCount) != null){
                newPoly.put(secondNewPair, pairCount + newPoly.get(secondNewPair));
            }
        }
        return newPoly;
    }

    // Returns a map representing a polymer, of strings to their quantities
    public static HashMap<String, Long> disassemblePolymer(String polymer){
        HashMap<String, Long> map = new HashMap<>();
        for (int i = 1; i < polymer.length(); i++){
            String currPoly = polymer.substring(i-1, i+1);
            Long currentQuantity = map.get(currPoly);
            if (currentQuantity == null){
                map.put(currPoly, (long) 1);
            } else {
                map.put(currPoly, currentQuantity + 1);
            }
        }
        return map;
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