package feryand.in.securesms.blockchipher;

import java.util.ArrayList;

/**
 * Created by yoga on 4/14/2016.
 */
public class Cbc {
    public Keygen keygen;

    public Cbc() {

    }

    ArrayList<Block> encrypt(ArrayList<Block> plain, int[] k) {
        Block K= new Block(8, k);
        int[] key = K.bit;
        ArrayList<Block> cip= new ArrayList<>();
        Keygen keygen=new Keygen();
        int[] pre = key;
        for(int i = 0; i < plain.size(); i++) {
            key = keygen.nextByteKey(key);
            plain.set(i, plain.get(i).xor(new Block(8, pre)));
            cip.add(plain.get(i).e_encrypt(new Block(8, key)));
            pre = cip.get(cip.size()-1).bit;
        }
        return cip;
    }

    ArrayList<Block> decrypt(ArrayList<Block> cipher, int[] k) {
        Block K=new Block(8, k);
        int[] key = K.bit;
        ArrayList<Block> pres=new ArrayList<>();
        ArrayList<Block> keys= new ArrayList<>();
        ArrayList<Block> plain= new ArrayList<>();
        Keygen keygen=new Keygen();
        pres.add(new Block(8, key));
        for(int i = 0; i + 1 < cipher.size(); i++) {
            pres.add(cipher.get(i));
        }
        for(int i = 0; i < cipher.size(); i++) {
            key = keygen.nextByteKey(key);
            keys.add(new Block(8, key));
        }
        for(int i = 0; i < cipher.size(); i++) {
            Block tmp = cipher.get(i).e_decrypt(keys.get(i));
            plain.add(tmp.xor( pres.get(i)));
        }
        return plain;
    }
}
