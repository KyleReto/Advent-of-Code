import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

class TwentyTwo {
    
    public static void main(String[] args) throws IOException{
        int[][] input = getInput(22);
        ArrayList<Cuboid> cuboids = new ArrayList<>();
        for (int[] command : input){
            if (command[1] < -50 || command[2] > 50 || command[3] < -50 || command[4] > 50 ||
            command[5] < -50 || command[6] > 50) continue;
            Cuboid cuboid = new Cuboid(command[1], command[2], command[3], 
            command[4], command[5], command[6], command[0] == 1);
            // Make a list of all cuboids, on and off
            // When adding a new cuboid to the list, add its intersection with all on cuboids as off cuboids,
            // and its intersection with all off cuboids as on cuboids
            ArrayList<Cuboid> toAdd = new ArrayList<>();
            for (Cuboid existing : cuboids){
                Cuboid newCuboid = cuboid.getIntersection(existing);
                if (newCuboid != null && newCuboid.getSize() > 0){
                    toAdd.add(newCuboid);
                }
            }
            cuboids.addAll(toAdd);
            if (cuboid.isOn) cuboids.add(cuboid);
            // Sum all on minus all off to get final result.
        }
        long sum = 0;
        for (Cuboid cuboid : cuboids){
            sum += cuboid.isOn ? cuboid.getSize() : cuboid.getSize() * -1;
        }
        System.out.println("Part 1: " + sum);

        cuboids = new ArrayList<>();
        for (int[] command : input){
            Cuboid cuboid = new Cuboid(command[1], command[2], command[3], 
            command[4], command[5], command[6], command[0] == 1);
            ArrayList<Cuboid> toAdd = new ArrayList<>();
            for (Cuboid existing : cuboids){
                Cuboid newCuboid = cuboid.getIntersection(existing);
                if (newCuboid != null && newCuboid.getSize() > 0){
                    toAdd.add(newCuboid);
                }
            }
            cuboids.addAll(toAdd);
            if (cuboid.isOn) cuboids.add(cuboid);
        }
        sum = 0;
        for (Cuboid cuboid : cuboids){
            sum += cuboid.isOn ? cuboid.getSize() : cuboid.getSize() * -1;
        }
        System.out.println("Part 2: " + sum);
    }

    // Given an input file, outputs an array of trimmed strings.
    public static int[][] getInput(int day) throws IOException{
        String textInput = Files.readString(Paths.get("./inputs/" + day + ".txt"));
        String[] inputs = textInput.trim().split("\n");
        for (int i = 0; i < inputs.length; i++){
            inputs[i] = inputs[i].trim();
        }
        // isOn(1/0), x1, x2, y1, y2, z1, z2
        int[][] steps = new int[inputs.length][7];
        for (int i = 0; i < steps.length; i++){
            String[] arr = inputs[i].split(" x=|\\.\\.|,y=|,z=");
            steps[i][0] = arr[0].equals("on") ? 1 : 0;
            for (int j = 1; j < arr.length; j++){
                steps[i][j] = Integer.parseInt(arr[j]);
            }
        }
        return steps;
    }
}

class Cuboid{
    // Cuboid coordinates are inclusive
    public int x1, x2, y1, y2, z1, z2;
    boolean isOn;

    Cuboid(int x1, int x2, int y1, int y2, int z1, int z2, boolean isOn){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
        this.isOn = isOn;
    }

    // Copy an existing cuboid
    Cuboid(Cuboid existing){
        this.x1 = existing.x1;
        this.x2 = existing.x2;
        this.y1 = existing.y1;
        this.y2 = existing.y2;
        this.z1 = existing.z1;
        this.z2 = existing.z2;
        this.isOn = existing.isOn;
    }

    // Check if this cuboid entirely contains another.
    public boolean contains(Cuboid existing){
        boolean isXContained = existing.x1 >= this.x1 && existing.x2 <= this.x2;
        boolean isYContained = existing.y1 >= this.y1 && existing.y2 <= this.y2;
        boolean isZContained = existing.z1 >= this.z1 && existing.z2 <= this.z2;
        return isXContained && isYContained && isZContained;
    }

    // Get a cuboid representing the overlapping space between the two cuboids, if present.
    public Cuboid getIntersection(Cuboid existing){
        int[] xIntersect = getLineIntersection(existing.x1, existing.x2, this.x1, this.x2);
        int[] yIntersect = getLineIntersection(existing.y1, existing.y2, this.y1, this.y2);
        int[] zIntersect = getLineIntersection(existing.z1, existing.z2, this.z1, this.z2);
        if (xIntersect == null || yIntersect == null || zIntersect == null) return null;
        return new Cuboid(xIntersect[0], xIntersect[1], yIntersect[0], yIntersect[1], zIntersect[0], zIntersect[1], !existing.isOn);
    }

    // Get the intersection between two lines (p1, p2; and p3, p4, each inclusive) in 1D space.
    // returns {p1, p2}, which is a line from the smaller to the larger number
    private static int[] getLineIntersection(int p1, int p2, int p3, int p4){
        int[] result = new int[2];
        // There is no overlap
        if (p2 < p3 || p1 > p4) return null;
        int[] points = {p1, p2, p3, p4};
        Arrays.sort(points);
        result[0] = points[1];
        result[1] = points[2];
        return result;
    }

    public long getSize(){
        return (long)(x2-x1+1) * (long)(y2-y1+1) * (long)(z2-z1+1);
    }

    public String toString(){
        return "[x1: " + x1 + ", x2: " + x2 + ", y1: " + y1 + ", y2: " + y2 + ", z1: " + z1 + ", z2: " + z2 + "]";
    }
}