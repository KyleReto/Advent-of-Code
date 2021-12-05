import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class VentMap{
    int dangerPoints = 0;
    int[][] map;

    VentMap(int rowCount, int colCount){
        map = new int[rowCount][colCount];
        // initialize the entire array
        for (int i = 0; i < rowCount; i++){
            for (int j = 0; j < colCount; j++){
                map[i][j] = 0;
            }
        }
    }

    public void markVent(int x1, int y1, int x2, int y2){
        // If the line is diagonal, handle it separately.
        if (!(x2 == x1 || y2 == y1)){
            int j = 0;
            for (int i = 0; i != (x2-x1+Integer.signum(x2-x1)); i+= Integer.signum(x2-x1)){
                if (x2-x1 == 0) break;
                map[y1+j][x1+i] += 1;
                if (map[y1+j][x1+i] == 2){
                    dangerPoints++;
                }
                j += Integer.signum(y2-y1);
            }

            return;
        }
        for (int i = 0; i != (x2-x1+Integer.signum(x2-x1)); i+= Integer.signum(x2-x1)){
            if (x2-x1 == 0) break;
            map[y1][x1+i] += 1;
            // This is ugly
            if (map[y1][x1+i] == 2){
                dangerPoints++;
            }
        }

        for (int i = 0; i != (y2-y1+Integer.signum(y2-y1)); i+= Integer.signum(y2-y1)){
            if (y2-y1 == 0) break;
            map[y1+i][x1] += 1;
            if (map[y1+i][x1] == 2){
                dangerPoints++;
            }
        }

    }

    public String toString(){
        String output = "";
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[0].length; j++){
                output += "[" + map[i][j] + "]";
            }
            output += "\n";
        }
        return output;
    }
}

class Five {

    public static void main(String[] args) throws IOException{
        String[] input = getInput(5);
        VentMap map = new VentMap(1000, 1000);
        for (int i = 0; i < input.length; i++){
            String[] coordinates = input[i].split("->");
            String[] startPos = coordinates[0].trim().split(",");
            String[] endPos = coordinates[1].trim().split(",");
            map.markVent(Integer.parseInt(startPos[0]), Integer.parseInt(startPos[1]), 
            Integer.parseInt(endPos[0]), Integer.parseInt(endPos[1]));
        }
        System.out.println(map.dangerPoints);
    }

    // Given an input file, outputs an array of trimmed strings.
    public static String[] getInput(int day) throws IOException{
        String textInput = Files.readString(Paths.get("./inputs/" + day + ".txt"));
        String[] inputs = textInput.split("\n");
        for (int i = 0; i < inputs.length; i++){
            inputs[i] = inputs[i].trim();
        }
        return inputs;
    }
}