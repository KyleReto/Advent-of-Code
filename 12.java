import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Twelve {
    
    public static void main(String[] args) throws IOException{
        String[] input = getInput(12);
        CaveMap cave = new CaveMap();
        for (String str : input){
            cave.addPath(str);
        }
        cave.addMirrorPaths();
        System.out.println(cave.countPaths("start", new ArrayList<String>(), false)[0]);
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

class CaveMap{
    public HashMap<String, ArrayList<String>> map;
    CaveMap(){
        map = new HashMap<>();
    }

    public void addPath(String str){
        String from = str.substring(0, str.indexOf("-"));
        String to = str.substring(str.indexOf("-")+1, str.length());
        ArrayList<String> list = new ArrayList<>();
        if (map.containsKey(from)){
            list = map.get(from);
        }
        list.add(to);
        map.put(from, list);
    }

    public void addMirrorPaths(){
        HashMap<String, ArrayList<String>> oldMap = clone(this.map);
        for (Map.Entry<String,ArrayList<String>> entry : oldMap.entrySet()){
            String from = entry.getKey();
            for (String to : entry.getValue()){
                String newPath = to + "-" + from;
                this.addPath(newPath);
            }
        }
    }

    public HashMap<String, ArrayList<String>> clone(HashMap<String, ArrayList<String>> oldMap){
        HashMap<String, ArrayList<String>> newMap = new HashMap<>();
        for (Map.Entry<String,ArrayList<String>> entry : oldMap.entrySet()){
            String key = entry.getKey();
            ArrayList<String> value = entry.getValue();
            newMap.put(key, (ArrayList<String>) value.clone());
        }
        return newMap;
    }

    // Creates a copy of the map and arraylist, but shallowly copies the strings to save memory.
    public CaveMap clone(){
        CaveMap newMap = new CaveMap();
        newMap.map = new HashMap<>();
        for (Map.Entry<String,ArrayList<String>> entry : this.map.entrySet()){
            String key = entry.getKey();
            ArrayList<String> value = entry.getValue();
            newMap.map.put(key, (ArrayList<String>) value.clone());
        }
        return newMap; 
    }

    private boolean isBig(String node){
        return node.toUpperCase().equals(node);
    }

    // Returns array of an int and an explored map
    public Object[] countPaths(String node, ArrayList<String> explored, boolean hasRevisited){
        Object[] output = new Object[2];
        int pathCount = 0;
        ArrayList<String> visitedNodes = (ArrayList<String>) explored.clone();
        // Mark the current node as visited
        visitedNodes.add(node);
        // If we've hit the end, mark this as a successful path.
        if (node.equals("end")){
            output[0] = 1;
            return output;
        }
        // Otherwise, find the number of connected, successful paths.
        for (String destination : this.map.get(node)){
            if (destination.equals("start")) continue;
            if (isBig(destination) || !visitedNodes.contains(destination) || !hasRevisited){
                // If we used our revisit this time
                boolean willRevisit = hasRevisited;
                if (!isBig(destination) && visitedNodes.contains(destination) && !hasRevisited){
                    willRevisit = true;
                }
                Object[] object = countPaths(destination, visitedNodes, willRevisit);
                pathCount += (int) object[0];
            }
        }
        output[0] = pathCount;
        output[1] = visitedNodes;
        return output;
    }
}