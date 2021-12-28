import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Seventeen {
    
    public static void main(String[] args) throws IOException{
        int[] target = getInput(17);
        int maxY = 0;
        long validVelCount = 0;
        for (int i = 0; i < 1000; i++){
            for (int j = 0; j < 1000; j++){
                // sure could go for some indian food right about now
                int currY = testVelocity(target, i-500, j-500);
                if (currY > maxY) maxY = currY;
                if (currY != -1) validVelCount++;
            }
        }
        System.out.println("Maximum Y Vel: " + maxY);
        System.out.println("Number of Valid Velocities: " + validVelCount);
    }

    // Given an input file, outputs the target range, [x1, x2, y1, y2]. 1 is always less than 2.
    public static int[] getInput(int day) throws IOException{
        String textInput = Files.readString(Paths.get("./inputs/" + day + ".txt"));
        String[] split = textInput.trim().split("x=|\\.\\.|, y=");
        int[] input = new int[4];
        for (int i = 0; i < input.length; i++){
            input[i] = Integer.parseInt(split[i+1]);
        }
        return input;
    }

    public static int testVelocity(int[] target, int xVel, int yVel){
        int yPos = 0;
        int xPos = 0;
        int lastYPos = 0;
        int maxY = 0;
        boolean isMovingUp = true;
        while (yPos > target[2] || isMovingUp){
            lastYPos = yPos;
            yPos += yVel;
            xPos += xVel;
            if (yPos > maxY) maxY = yPos;
            if (xPos >= target[0] && xPos <= target[1] && yPos >= target[2] && yPos <= target[3]){
                return maxY;
            }
            yVel -= 1;
            int dragNum = 0;
            if (xVel < 0) dragNum = 1;
            if (xVel > 0) dragNum = -1;
            xVel += dragNum;
            isMovingUp = yPos > lastYPos;
        }
        return -1;
    }
}