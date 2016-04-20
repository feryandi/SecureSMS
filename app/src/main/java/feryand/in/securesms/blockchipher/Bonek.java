package feryand.in.securesms.blockchipher;

import java.util.ArrayList;

/**
 * Created by yoga on 4/14/2016.
 */
public class Bonek {

    Keygen keygen = new Keygen();

    public Bonek() {

    }

    public ArrayList<Block> encrypt(ArrayList<Block> plain, int[] K) {
        Block key = new Block(8, K);
        Block ori_key = new Block(key);

        Keygen keygen = new Keygen();
        Cbc cbc = new Cbc();
        Feistel feistel = new Feistel();

        for (int i = 0; i < plain.size(); i++) {
            plain.set(i, plain.get(i).xor(ori_key));
        }

        for (int round = 0; round < 10; round++) {
            key.bit = keygen.nextByteKey(key.bit);
            plain = cbc.encrypt(plain, key.bit);
            for (int f = 0; f < 2; f++) {
                key.bit = keygen.nextByteKey(key.bit);
                Block[] keyfise = new Block[2];
                keyfise = key.split();
                for (int i = 0; i < plain.size(); i++) {
                    plain.set(i, feistel.encrypt(plain.get(i), keyfise[0].bit));
                    plain.set(i, feistel.encrypt(plain.get(i), keyfise[1].bit));
                }
            }
        }

        for (int i = 0; i < plain.size(); i++) {
            plain.set(i, plain.get(i).xor(ori_key));
        }

        return plain;
    }

    public ArrayList<Block> decrypt(ArrayList<Block> cipher, int[] K) {
        Block key = new Block(8, K);
        Block ori_key = new Block(key);
        Keygen keygen = new Keygen();
        Cbc cbc = new Cbc();
        Feistel feistel = new Feistel();
        for (int i = 0; i < 31; i++) {
            key.bit = keygen.nextByteKey(key.bit);
        }
        for (int i = 0; i < cipher.size(); i++) {
            cipher.set(i, cipher.get(i).xor(ori_key));
        }
        for (int round = 0; round < 10; round++) {
            for (int f = 0; f < 2; f++) {
                key.bit = keygen.prevByteKey(key.bit);
                
                Block[] keyfise = new Block[2];
                keyfise = key.split();
               
                for (int i = 0; i < cipher.size(); i++) {
                    cipher.set(i, feistel.decrypt(cipher.get(i), keyfise[1].bit));
                    cipher.set(i, feistel.decrypt(cipher.get(i), keyfise[0].bit));
                }
            }
            key.bit = keygen.prevByteKey(key.bit);
            cipher = cbc.decrypt(cipher, key.bit);

        }

        for (int i = 0; i < cipher.size(); i++) {
            cipher.set(i, cipher.get(i).xor(ori_key));
        }
 
        return cipher;
    }

    public static byte[] hexa_to_byte(String s) {

        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static int[] hexa_to_key(String s) {
        String strkey= s;
        while(strkey.length()<32){
            strkey=strkey.concat(strkey);
        }
        if(strkey.length()>32){
            strkey=strkey.substring(0,32);
        }
        int n = strkey.length();
        int[] ret = new int[n / 2];
        for (int i = 0; i < n; i += 2) {
            int a = ('0' <= strkey.charAt(i) && strkey.charAt(i) <= '9' ? strkey.charAt(i) - '0' : strkey.charAt(i) - 'a' + 10);
            int b = ('0' <= strkey.charAt(i+1) && strkey.charAt(i+1) <= '9' ? strkey.charAt(i+1) - '0' : strkey.charAt(i+1) - 'a' + 10);
            ret[i / 2] = (b << 4) | a;
        }
        return ret;
    }

    public static ArrayList<Block> byte_to_block(ArrayList<Byte> b) {
        assert (b.size() % Global.BLOCK_SIZE == 0);
        ArrayList<Block> ret = new ArrayList();
        for (int i = 0; i * Global.BLOCK_SIZE < b.size(); i++) {
            ret.add(new Block(8));
            for (int j = 0; j < Global.BLOCK_SIZE; j++) {
                int a = (int) b.get(i * Global.BLOCK_SIZE + j);

                ret.get(i).bit[j] = a & 0xff;
            }

        }
        return ret;
    }

    public static ArrayList<Byte> block_to_byte(ArrayList<Block> b) {
        ArrayList<Byte> ret = new ArrayList();
        for (int i = 0; i < b.size(); i++) {
            for (int j = 0; j < Global.BLOCK_SIZE; j++) {
                ret.add((byte) b.get(i).bit[j]);
            }
        }
        return ret;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] listToArray (ArrayList<Byte> ret){
        byte[] bret = new byte[ret.size()];
        for(int i=0; i<ret.size();i++){
            bret[i]=ret.get(i);
        }
        return bret;
    }

    public static ArrayList<Byte> arrayToList (byte[] arrb){
        ArrayList<Byte> b = new ArrayList();
        for (int i = 0; i < arrb.length; i++) {
            b.add(arrb[i]);
        }
        return b;
    }
}
