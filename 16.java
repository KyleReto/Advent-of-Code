import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

class Sixteen {

    public static void main(String[] args) throws Exception {
        // Thoughts on the problem:
        // We need to parse each bit one at a time, and exactly once.
        // The idea here is a pipe-and-filter approach with a stack of bits.
        // We can pop bits off the end as we need them and process accordingly
        // When we run out of bits, we're done.
        Transmission input = new Transmission(getInput(16));
        System.out.println(parsePacket(input));
        // 0, 1, 2 = version
        // 3, 4, 5 = type ID
        // Type ID 4 = literal, single binary number
        // Pad with leading zeroes until multiple of 4, break into groups of 4
        // Prefix all but the last group w/ "1" (last instead prefix w/ "0")
        // There may be excess bits, ignore these.
        // Type ID !4 = operator, operates on subpackets
        // Bit 6 = length type ID
        // If it's 0, next 15 bits (7-21) are the total subpacket length
        // If it's 1, next 11 bits are the *number* of subpackets

    }

    public static long parsePacket(Transmission transmission) throws Exception {
        long version = transmission.popManyAsLong(3);
        long packetType = transmission.popManyAsLong(3);
        if (packetType == 4) {
            return getLiteralVal(transmission);
        } else {
            long lengthType = transmission.popManyAsLong(1);
            ArrayList<Long> vals = new ArrayList<>();
            if (lengthType == 0) {
                long subPacketLength = transmission.popManyAsLong(15);
                long lengthToStopAt = transmission.length() - subPacketLength;
                while (transmission.length() > lengthToStopAt) {
                    vals.add(parsePacket(transmission));
                }
            } else {
                long subPacketCount = transmission.popManyAsLong(11);
                for (int i = 0; i < subPacketCount; i++) {
                    vals.add(parsePacket(transmission));
                }
            }
            return operate(packetType, vals);
        }
    }

    public static long operate(long packetType, ArrayList<Long> vals) throws Exception {
        int type = (int) packetType;
        long result;
        switch (type) {
            case 0:
                result = 0;
                for (long val : vals){
                    result += val;
                }
                break;
            case 1:
                result = 1;
                for (long val : vals){
                    result *= val;
                }
                break;
            case 2:
                result = Long.MAX_VALUE;
                for (long val : vals){
                    if (val < result) result = val;
                }
                break;
            case 3:
                result = 0;
                for (long val : vals){
                    if (val > result) result = val;
                }
                break;
            case 5:
                result = (vals.get(0) > vals.get(1)) ? 1 : 0;
                break;
            case 6:
                result = (vals.get(0) < vals.get(1)) ? 1 : 0;
                break;
            case 7:
                result = (vals.get(0).equals(vals.get(1))) ? 1 : 0;
                break;
            default:
                throw new Exception("Invalid operation " + packetType);
        }
        return result;
    }

    public static long getLiteralVal(Transmission transmission) {
        StringBuilder sb = new StringBuilder();
        boolean isLastBit = false;
        while (!isLastBit) {
            isLastBit = transmission.popManyAsLong(1) == 0;
            sb.append(transmission.popManyAsStr(4));
        }
        return Long.parseLong(sb.toString(), 2);
    }

    // Given an input file, outputs the given string
    public static String getInput(int day) throws IOException {
        String textInput = Files.readString(Paths.get("./inputs/" + day + ".txt"));
        return textInput.trim();
    }
}

class Transmission {
    private ArrayDeque<Boolean> bits = new ArrayDeque<>();

    Transmission(String hex) {
        for (char ch : hex.toCharArray()) {
            for (boolean digit : hexToBoolArr(ch)) {
                bits.addLast(digit);
            }
        }
    }

    public long popManyAsLong(int popCount) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < popCount; i++) {
            sb.append(bits.pop() ? '1' : '0');
        }
        return Long.parseLong(sb.toString(), 2);
    }

    public String popManyAsStr(int popCount) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < popCount; i++) {
            sb.append(bits.pop() ? '1' : '0');
        }
        return sb.toString();
    }

    private static boolean[] hexToBoolArr(char ch) {
        int num = Character.digit(ch, 16);
        String binary = Integer.toBinaryString(num);
        // Add leading zeros
        binary = String.format("%4s", binary).replace(' ', '0');
        boolean[] output = new boolean[binary.length()];
        for (int i = 0; i < binary.length(); i++) {
            output[i] = binary.charAt(i) == '1';
        }
        return output;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Boolean> it = bits.iterator();
        while (it.hasNext()) {
            sb.append(it.next() ? '1' : '0');
        }
        return sb.toString();
    }

    public int length() {
        return this.toString().length();
    }
}