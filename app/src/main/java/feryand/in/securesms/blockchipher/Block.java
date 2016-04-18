package feryand.in.securesms.blockchipher;

import java.util.Random;

/**
 * Created by yoga on 4/14/2016.
 */
public class Block {
    public int[] bit = null;         // a container array, consist of byte/hex of Block
    public int BIT_SIZE;             // 8 for byte, 4 for hex

    public Block() {
        BIT_SIZE=8;
        bit = new int[Global.BLOCK_SIZE];
        for(int i=0; i<BIT_SIZE;i++){
            bit[i]=0;
        }
    }

    public Block(int n){
        BIT_SIZE=n;
        bit = new int[Global.BLOCK_SIZE];
        Random rand= new Random();
        for(int i = 0; i < Global.BLOCK_SIZE; i++) bit[i] = (int)rand.nextInt(1 << BIT_SIZE);
    }

    public Block(int n, int[] b){
        BIT_SIZE=n;
        bit = new int[Global.BLOCK_SIZE];
        for(int i = 0; i < Global.BLOCK_SIZE; i++) {
            bit[i] = b[i];
        }
    }

    public Block(Block b){
        BIT_SIZE=b.BIT_SIZE;
        bit = new int[Global.BLOCK_SIZE];
        for(int i = 0; i < Global.BLOCK_SIZE; i++) {
            bit[i] = b.bit[i];
        }
    }

    public int get(int i) {
        return bit[i];
    }

    public void set(int i, int b) {
        bit[i] = b;
    }

    public void assign(Block b) {
        for(int i = 0; i < Global.BLOCK_SIZE; i++) {
            bit[i] = b.bit[i];
        }
    }

    public Block add(Block b){
        Block ret= new Block(BIT_SIZE);
        for(int i = 0; i < Global.BLOCK_SIZE; i++) {
            ret.bit[i] = (int) ((this.bit[i] + b.bit[i]) % (1 << BIT_SIZE));
        }
        return ret;
    }

    public Block substract(Block b) {
        Block ret= new Block(BIT_SIZE);
        for(int i = 0; i < Global.BLOCK_SIZE; i++) {
            ret.bit[i] = (int) ((this.bit[i] - b.bit[i] + (1 << BIT_SIZE)) % (1 << BIT_SIZE));
        }
        return ret;
    }

    public Block xor(Block b){
        Block ret= new Block(BIT_SIZE);
        for(int i = 0; i < Global.BLOCK_SIZE; i++) {
            ret.bit[i] = (int) (this.bit[i] ^ b.bit[i]);
        }
        return ret;
    }

    public Block shiftleft(int num) {
        Block ret= new Block(BIT_SIZE);
        for(int i = 0; i < Global.BLOCK_SIZE; i++) {
            ret.bit[i] = this.bit[(i + num) % Global.BLOCK_SIZE];
        }
        return ret;
    }


    public  Block shiftright(int num)  {
        Block ret= new Block(BIT_SIZE);
        for(int i = 0; i < Global.BLOCK_SIZE; i++) {
            ret.bit[i] = this.bit[(i - num + Global.BLOCK_SIZE) % Global.BLOCK_SIZE];
        }
        return ret;
    }

    /* rotate the byte/hex matrix clockwise 90 degree */
    public Block rotate() {
        Block ret= new Block(BIT_SIZE);
        for(int t = 0; t < Global.BLOCK_SIZE; t++) {
            int i = t / Global.SIDE_SIZE;
            int j = t % Global.SIDE_SIZE;
            int newi = j;
            int newj = Global.SIDE_SIZE - 1 - i;
            ret.bit[newi * Global.SIDE_SIZE + newj] = this.bit[t];
        }
        return ret;
    }

    public Block transpose() {
        Block ret= new Block(BIT_SIZE);
        for(int t = 0; t < Global.BLOCK_SIZE; t++) {
            int i = t / Global.SIDE_SIZE;
            int j = t % Global.SIDE_SIZE;
            ret.bit[i * Global.SIDE_SIZE + j] = this.bit[t];
        }
        return ret;
    }

    /**
     * E function for ciphering byte (8 bit)
     * Used in CBC
     **/
    public Block e_encrypt(Block key) {
        Block ret= new Block(BIT_SIZE);
        Keygen keygen=new Keygen();
        ret = (this.shiftright(6)).xor(key.shiftleft(9));
        ret = ret.rotate();
        ret = ret.add(key);
        ret = ret.rotate().rotate().rotate();
        ret = new Block(BIT_SIZE, keygen.nextKey(ret.bit, BIT_SIZE));
        ret = ret.transpose();
        ret = ret.xor(key);
        ret = ret.rotate().rotate();
        return ret;
    }

    /**
     * E function for deciphering byte (8 bit)
     * Used in CBC
     **/
    public Block e_decrypt(Block key) {
        Block ret= new Block(BIT_SIZE);
        Keygen keygen=new Keygen();
        ret = this.rotate().rotate();
        ret = ret.xor(key) ;
        ret = ret.transpose();
        ret = new Block(BIT_SIZE, keygen.prevKey(ret.bit, BIT_SIZE));
        ret = ret.rotate();
        ret = ret.substract(key);
        ret = ret.rotate().rotate().rotate();
        ret = (ret.xor((key.shiftleft(9)))).shiftleft(6);
        return ret;
    }

    /**
     * F function for ciphering hex (4 bit)
     * Used in Feistel network
     * Basically the algo is the same with E function in CBC so just call the subprogram :)
     **/
    public Block f_encrypt(Block key) {
        return e_encrypt(key);
    }

    /**
     * F function for deciphering hex (4 bit)
     * Used in Feistel network
     **/
    public Block f_decyprt(Block key) {
        return e_decrypt(key);
    }

    public Block[] split() {
        Block fi=new Block(4);
        Block se=new Block(4);
        for(int i = 0; i < Global.BLOCK_SIZE / 2; i++) {
            fi.bit[i * 2] = (int) (bit[i] % (1 << 4));
            fi.bit[i * 2 + 1] = (int) ((bit[i] / (1 << 4)) % (1 << 4));
        }
        for(int i = 0; i < Global.BLOCK_SIZE / 2; i++) {
            se.bit[i * 2] = (int) (bit[i + Global.BLOCK_SIZE/2] % (1 << 4));
            se.bit[i * 2 + 1] = (int) ((bit[i + Global.BLOCK_SIZE/2] >> 4) % (1 << 4));
        }
        Block ret[]= new Block[2];
        ret[0]=fi;
        ret[1]=se;
        return ret;
    }

    public void combine(Block fi, Block se) {
        this.BIT_SIZE = 8;
        for(int i = 0; i < Global.BLOCK_SIZE; i += 2) {
            this.bit[i/2] = (int) (fi.bit[i] | (fi.bit[i + 1] << 4));
            this.bit[i/2 + Global.BLOCK_SIZE/2] = (int) (se.bit[i] | (se.bit[i + 1] << 4));
        }
    }
}
