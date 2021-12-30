import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

class Eighteen {
    
    public static void main(String[] args) throws Exception{
        String[] input = getInput(18);
        int maxMagnitude = 0;
        for (int i = 0; i < input.length; i++){
            for (int j = 0; j < input.length; j++){
                if (i == j) break;
                SnailfishNumber sfn = new SnailfishNumber(input[i]);
                sfn = SnailfishNumber.add(sfn, new SnailfishNumber(input[j]));
                int magnitude = sfn.getMagnitude();
                if (magnitude > maxMagnitude) maxMagnitude = magnitude;
            }
        }
        System.out.println(maxMagnitude);
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

class SnailfishNumber{
    public SnailfishNumber left = null;
    public SnailfishNumber right = null;
    public SnailfishNumber parent = null;
    public int singleVal = -1;
    public boolean isPair;

    SnailfishNumber(SnailfishNumber left, SnailfishNumber right){
        isPair = true;
        this.left = left;
        this.left.parent = this;
        this.right = right;
        this.right.parent = this;
    }

    SnailfishNumber(int value){
        isPair = false;
        this.singleVal = value;
    }

    SnailfishNumber(String str){
        try{
            singleVal = Integer.parseInt(str);
            isPair = false;
        } catch (NumberFormatException nfe){
            isPair = true;
            // Cut off the brackets on the sides
            String newStr = str.substring(1, str.length()-1);
            // Get the left and right sides, and make them into snailfish.
            String[] split = splitSnailStr(newStr);
            left = new SnailfishNumber(split[0]);
            left.parent = this;
            right = new SnailfishNumber(split[1]);
            right.parent = this;
        }
    }

    public int getMagnitude(){
        if (!isPair) return singleVal;
        return (3*left.getMagnitude() + 2*right.getMagnitude());
    }

    public static SnailfishNumber add (SnailfishNumber first, SnailfishNumber second) throws Exception{
        SnailfishNumber sfn = new SnailfishNumber(first, second);
        //System.out.println("After Addition: " + sfn);
        sfn.reduce();
        return sfn;
    }

    public int getDepth(){
        if (this.parent == null) return 0;
        return this.parent.getDepth()+1;
    }

    public void reduce() throws Exception{
        ArrayList<SnailfishNumber> pairs = this.getPairList();
        for (SnailfishNumber pair : pairs){
            //System.out.println("Pair: " + pair + ", Depth: " + pair.getDepth());
            if (pair.getDepth() >= 4){
                pair.explode();
                //System.out.println("After Explode: " + this);
                this.reduce();
                return;
            }
        }
        ArrayList<SnailfishNumber> nums = this.getNumList();
        for (SnailfishNumber num : nums){
            if (num.singleVal >= 10){
                num.split();
                //System.out.println("After Split: " + this);
                this.reduce();
                return;
            }
        }
    }

    // Returns a list of all single-deep pairs, from left to right.
    public ArrayList<SnailfishNumber> getPairList(){
        ArrayList<SnailfishNumber> list = new ArrayList<>();
        // Recursively add all pair children to list
        if (left.isPair){
            for (SnailfishNumber sfn : left.getPairList()){
                list.add(sfn);
            }
        }
        if (right.isPair){
            for (SnailfishNumber sfn : right.getPairList()){
                list.add(sfn);
            }
        }
        // If both children are single, add self to list.
        if (!left.isPair && !right.isPair){
            list.add(this);
        }
        return list;
    }

    // Returns a list of all single values, from left to right.
    public ArrayList<SnailfishNumber> getNumList(){
        ArrayList<SnailfishNumber> list = new ArrayList<>();
        // Recursively add all pair children to list
        if (isPair){
            for (SnailfishNumber sfn : left.getNumList()){
                list.add(sfn);
            }
            for (SnailfishNumber sfn : right.getNumList()){
                list.add(sfn);
            }
        }
        // If self is not a pair, add to list
        if (!isPair){
            list.add(this);
        }
        return list;
    }

    private void split() throws Exception{
        if (this.singleVal == -1) throw new Exception("Attempted to split " + this + ", which is not a regular number.");
        double splitVal = this.singleVal / 2.0;
        this.left = new SnailfishNumber((int) Math.floor(splitVal));
        this.right = new SnailfishNumber((int) Math.ceil(splitVal));
        this.left.parent = this;
        this.right.parent = this;
        this.isPair = true;
        this.singleVal = -1;
    }

    private void explode() throws Exception{
        try{
            SnailfishNumber leftNeighbor = this.getNeighbor(true);
            SnailfishNumber rightNeigbor = this.getNeighbor(false);
            if (leftNeighbor != null) leftNeighbor.singleVal += this.left.singleVal;
            if (rightNeigbor != null) rightNeigbor.singleVal += this.right.singleVal;
            // Replace this pair with a single 0.
            this.isPair = false;
            this.left = null;
            this.right = null;
            this.singleVal = 0;
        } catch (Exception e){
            throw new Exception("Attempted to explode " + this + ", which is not a single-deep pair.");
        }
    }

    // isLeft refers to if you're looking for the left neighbor or right neighbor
    private SnailfishNumber getNeighbor(boolean isLeft){
        if (this.parent == null) return null;
        if (isLeft){
            if (this.parent.left.equals(this)){
                return this.parent.getNeighbor(true);
            } else {
                return this.parent.left.getDeepest(false);
            }
        } else {
            if (this.parent.right.equals(this)){
                return this.parent.getNeighbor(false);
            } else {
                return this.parent.right.getDeepest(true);
            }
        }
    }

    // isLeft refers to if you're looking for the deepest left or deepest right child
    SnailfishNumber getDeepest(boolean isLeft){
        if (isLeft){
            return isPair ? left.getDeepest(true) : this;
        } else {
            return isPair ? right.getDeepest(false) : this;
        }
    }

    // Split a snailnumber string into its two component number strings
    private String[] splitSnailStr(String str){
        int openBr = 0;
        int closeBr = 0;
        String[] output = new String[2];
        for (int i = 0; i < str.length(); i++){
            if (str.charAt(i) == '[') openBr++;
            if (str.charAt(i) == ']') closeBr++;
            if (openBr == closeBr && str.charAt(i) == ','){
                output[0] = str.substring(0, i);
                output[1] = str.substring(i+1);
                break;
            }
        }
        return output;
    }

    // pooh, that's not honey, you're eating recursion
    public String toString(){
        return isPair ? "[" + left.toString() + "," + right.toString() + "]" : String.valueOf(singleVal);
    }
}