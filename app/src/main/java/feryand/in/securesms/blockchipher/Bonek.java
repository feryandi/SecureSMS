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

    public static int[] hexa_to_byte(String s) {
        int n = s.length();
        int[] ret = new int[n / 2];
        for (int i = 0; i < n; i += 2) {
            int a = ('0' <= s.charAt(i) && s.charAt(i) <= '9' ? s.charAt(i) - '0' : s.charAt(i) - 'a' + 10);
            int b = ('0' <= s.charAt(i+1) && s.charAt(i+1) <= '9' ? s.charAt(i+1) - '0' : s.charAt(i+1) - 'a' + 10);
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
}
