import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

class Nineteen {
    
    public static void main(String[] args) throws IOException{
        ArrayList<ArrayList<String>> input = getInput(19);
        ArrayList<Scanner> scanners = new ArrayList<>();
        for (ArrayList<String> al : input){
            scanners.add(new Scanner(al));
        }
        // Put all beacons from Scanner 0 into the true list, and delete it from the overall list
        ArrayList<Beacon> trueBeacons = scanners.get(0).beacons;
        ArrayList<Scanner> trueScanners = new ArrayList<>();
        trueScanners.add(scanners.get(0));
        scanners.remove(0);
        // Evaluate the innermost expression nlogn times
        while(!scanners.isEmpty()){
            boolean finishedChecking = false;
            Scanner toRemove = null;
            for (Scanner scanner : scanners){
                // If finishedChecking is set, jump all the way back to the outer for loop and start from the beginning.
                if (finishedChecking) break;
                // for every possible orientation
                for (Scanner orientation: scanner.getAllOrientations()){
                    if (finishedChecking) break;
                    // For every known beacon/translated beacon pair,
                    // translate to match the points and see if other points also match.
                    for (int i = 0; i < trueBeacons.size(); i++){
                        if (finishedChecking) break;
                        for (int j = 0; j < orientation.beacons.size(); j++){
                            Scanner translation = orientation.translateToMatch(trueBeacons.get(i), orientation.beacons.get(j));
                            // If 12 beacons match truebeacons,
                            if (Scanner.getSharedBeacons(translation.beacons, trueBeacons).size() >= 12){
                                finishedChecking = true;
                                toRemove = scanner;
                                trueBeacons.addAll(Scanner.getUnsharedBeacons(translation.beacons, trueBeacons));
                                trueScanners.add(translation);
                                System.out.println("Current Size is: " + trueBeacons.size());
                                break;
                            }
                        }
                    }
                }
            }
            if (finishedChecking){
                scanners.remove(toRemove);
            }
        }
        System.out.println("Total Size is: " + trueBeacons.size());
        long maxManhattan = 0;
        for (int i = 0; i < trueScanners.size()-1; i++){
            for (int j = i; j < trueScanners.size(); j++){
                long manhattan = Scanner.getManhattanDist(trueScanners.get(i), trueScanners.get(j));
                if (manhattan > maxManhattan) maxManhattan = manhattan;
            }
        }
        System.out.println("Largest Manhattan Distance is: " + maxManhattan);

    }

    // Given an input file, outputs an arrayList of lists of scanner info.
    public static ArrayList<ArrayList<String>> getInput(int day) throws IOException{
        String textInput = Files.readString(Paths.get("./inputs/" + day + ".txt"));
        String[] lines = textInput.trim().split("\n");
        ArrayList<ArrayList<String>> scanners = new ArrayList<>();
        int index = 0;
        for (String line : lines){
            if (line.trim().equals("")) continue;
            if (line.contains("---")){
                String scannerNum = line.substring(12, line.length()-5);
                index = Integer.parseInt(scannerNum);
                scanners.add(index, new ArrayList<>());
            } else {
                scanners.get(index).add(line.trim());
            }

        }
        return scanners;
    }
}

class Scanner{
    public ArrayList<Beacon> beacons = new ArrayList<>();
    public int homeX = 0;
    public int homeY = 0;
    public int homeZ = 0;

    Scanner(ArrayList<String> beaconStrs){
        for (String str : beaconStrs){
            beacons.add(new Beacon(str));
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Beacon beacon : beacons){
            sb.append("[" + beacon.toString() + "] ");
        }
        return sb.toString();
    }

    public ArrayList<Scanner> getAllOrientations(){
        ArrayList<Scanner> orientations = new ArrayList<>();
        Scanner toAdd = this;
        // For each face, get every possible rotation
            for (int i = 0; i < 6; i++){
                for (int j = 0; j < 4; j++){
                    orientations.add(toAdd);
                    toAdd = toAdd.rotate('z');
                }
                toAdd = toAdd.rotate('y');
                // Get the top
                if (i == 3) toAdd = toAdd.rotate('x');
                // and the bottom
                if (i == 4) toAdd = toAdd.rotate('y');
            }
        return orientations;
    }

    // Returns a copy of this scanner with the given axis flipped
    public Scanner flip(char axis){
        ArrayList<String> newBeacons = new ArrayList<>();
        for (Beacon point : beacons){
            newBeacons.add(point.flip(axis).toString());
        }
        return new Scanner(newBeacons);
    }

    // Returns a 90 degree rotated copy along the given axis
    public Scanner rotate(char axis){
        ArrayList<String> newBeacons = new ArrayList<>();
        for (Beacon point : beacons){
            newBeacons.add(point.rotate(axis).toString());
        }
        return new Scanner(newBeacons);
    }

    public Scanner translateToMatch(Beacon theirs, Beacon ours){
        int xDiff = theirs.x - ours.x;
        int yDiff = theirs.y - ours.y;
        int zDiff = theirs.z - ours.z;
        return this.translate(xDiff, yDiff, zDiff);
    }

    public Scanner translate(int x, int y, int z){
        ArrayList<String> newBeacons = new ArrayList<>();
        for (Beacon point : beacons){
            newBeacons.add(new Beacon(point.x + x, point.y + y, point.z + z).toString());
        }
        Scanner sc = new Scanner(newBeacons);
        sc.homeX = x;
        sc.homeY = y;
        sc.homeZ = z;
        return sc;
    }

    // Returns a list of all beacons shared between two lists of beacons
    // Assumes identical orientation and zero point
    public static ArrayList<Beacon> getSharedBeacons(ArrayList<Beacon> first, ArrayList<Beacon> second){
        ArrayList<Beacon> list = (ArrayList<Beacon>) first.clone();
        list.retainAll(second);
        return list;
    }

    public static ArrayList<Beacon> getUnsharedBeacons(ArrayList<Beacon> first, ArrayList<Beacon> second){
        ArrayList<Beacon> list = (ArrayList<Beacon>) first.clone();
        list.removeAll(second);
        return list;
    }

    public static long getManhattanDist(Scanner a, Scanner b){
        long manhattan = 0;
        manhattan += Math.abs(b.homeX - a.homeX);
        manhattan += Math.abs(b.homeY - a.homeY);
        manhattan += Math.abs(b.homeZ - a.homeZ);
        return manhattan;
    }
}

class SortByBeacons implements Comparator<Beacon>{
    public int compare(Beacon a, Beacon b)
    {
        int compareVal;
        compareVal = a.x - b.x;
        if (compareVal == 0) compareVal = a.y - b.y;
        if (compareVal == 0) compareVal = a.z - b.z;
        return compareVal;
    }
}

class Beacon{
    public static final int MAP_SIZE = 10000;
    // Refers to the x, y, and z values of the beacon, relative to it given Scanner.
    public int x, y, z;

    Beacon(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Beacon(String str){
        this(Integer.parseInt(str.split(",")[0]), 
        Integer.parseInt(str.split(",")[1]), 
        Integer.parseInt(str.split(",")[2]));
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof Beacon)) return false;
        Beacon compare = (Beacon) o;
        return (compare.x == this.x && compare.y == this.y && compare.z == this.z);
    }

    public String toString(){
        return (x + "," + y + "," + z);
    }

    // Rotate 90 degrees clockwise along the given axis
    public Beacon rotate(char axis){
        switch(axis){
            case 'x':
                return new Beacon(this.x, this.z, this.y*-1);
            case 'y':
                return new Beacon(this.z*-1, this.y, this.x); 
            case 'z':
                return new Beacon(this.y, this.x*-1, this.z);
            default:
                return null;
        }
    }

    // Flip the point across the given axis
    public Beacon flip(char axis){
        switch(axis){
            case 'x':
                return new Beacon(this.x*-1, this.y, this.z);
            case 'y':
                return new Beacon(this.x, this.y*-1, this.z); 
            case 'z':
                return new Beacon(this.x, this.y, this.z*-1);
            default:
                return null;
        }
    }
}