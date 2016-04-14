package feryand.in.securesms.blockchipher;

import java.util.ArrayList;

/**
 * Created by yoga on 4/14/2016.
 */
public class Bonek {
    Keygen keygen=new Keygen();

    public Bonek() {

    }

    public ArrayList<Block> encrypt(ArrayList<Block> plain, char[] K) {
        Block key= new Block(8, K);
        Block ori_key = key;
        Keygen keygen=new Keygen();
        Cbc cbc=new Cbc();
        Feistel feistel=new Feistel();
        for(int i = 0; i < plain.size(); i++) {
            plain.set(i,  plain.get(i).xor(ori_key));
        }
        for(int round = 0; round < 10; round++) {
            key.bit = keygen.nextByteKey(key.bit);
            plain = cbc.encrypt(plain, key.bit);
            for(int f = 0; f < 2; f++) {
                key.bit = keygen.nextByteKey(key.bit);
                Block[] keyfise= new Block[2];
                keyfise = key.split();
                for(int i = 0; i < plain.size(); i++) {
                    plain.set(i, feistel.encrypt(plain.get(i), keyfise[0].bit));
                    plain.set(i, feistel.encrypt(plain.get(i), keyfise[1].bit));
                }
            }
        }
        for(int i = 0; i < plain.size(); i++) {
            plain.set(i, plain.get(i).xor( ori_key));
        }
        return plain;
    }

    public ArrayList<Block> decrypt(ArrayList<Block> cipher, char[] K) {
        Block key=new Block(8, K);
        Block ori_key = key;
        Keygen keygen=new Keygen();
        Cbc cbc=new Cbc();
        Feistel feistel=new Feistel();
        for(int i = 0; i < 31; i++) key.bit = keygen.nextByteKey(key.bit);
        for(int i = 0; i < cipher.size(); i++) {
            cipher.set(i , cipher.get(i).xor( ori_key));
        }
        for(int round = 0; round < 10; round++) {
            for(int f = 0; f < 2; f++) {
                key.bit = keygen.prevByteKey(key.bit);
                Block[] keyfise= new Block[2];
                keyfise= key.split();
                for(int i = 0; i < cipher.size(); i++) {
                    cipher.set(i, feistel.decrypt(cipher.get(i), keyfise[1].bit));
                    cipher.set(i, feistel.decrypt(cipher.get(i), keyfise[0].bit));
                }
            }
            key.bit = keygen.prevByteKey(key.bit);
            cipher = cbc.decrypt(cipher, key.bit);
        }
        for(int i = 0; i < cipher.size(); i++) {
            cipher.set(i, cipher.get(i).xor( ori_key));
        }
        return cipher;
    }
    
    public ArrayList<Block> byte_to_block(ArrayList<Byte> b) {
        assert (b.size() % Global.BLOCK_SIZE == 0);
        ArrayList<Block> ret= new ArrayList();
        for (int i = 0; i * Global.BLOCK_SIZE < b.size(); i++) {
            ret.add(new Block(8));
            for (int j = 0; j < Global.BLOCK_SIZE; j++) {
                
                ret.get(i).bit[j]= (char) b.get(i * Global.BLOCK_SIZE + j).byteValue();
            }
            
        }
        return ret;
    }

   public ArrayList<Byte> block_to_byte(ArrayList<Block> b) {
        ArrayList<Byte> ret= new ArrayList();
        for (int i = 0; i < b.size(); i++) {
            for (int j = 0; j < Global.BLOCK_SIZE; j++) {
                ret.add((byte)b.get(i).bit[j]);
            }
        }
        return ret;
    }
}
