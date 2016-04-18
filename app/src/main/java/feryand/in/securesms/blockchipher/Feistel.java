package feryand.in.securesms.blockchipher;

/**
 * Created by yoga on 4/14/2016.
 */
public class Feistel {

    Keygen keygen =new Keygen();


    public Feistel() {

    }

    public Block encrypt(Block plain, int[] key) {
        Block newfi= new Block(4);
        Block newse= new Block(4);
        Block[] fise= new Block[2];
        fise = plain.split();
        newfi = fise[1];
        newse = fise[1].f_encrypt(new Block(4, key)).xor( fise[0]);
        Block ret= new Block(8);
        ret.combine(newfi, newse);
        return ret;
    }

    public Block decrypt(Block cipher, int[] key) {
        Block newfi= new Block(4);
        Block newse= new Block(4);
        Block[] fise= new Block[2];
        fise= cipher.split();
        newse = fise[0];
        newfi = fise[0].f_encrypt(new Block(4, key)).xor( fise[1]);
        Block ret= new Block(8);
        ret.combine(newfi, newse);
        return ret;
    }
}
