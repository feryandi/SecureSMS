package feryand.in.securesms.blockchipher;

/**
 * Created by yoga on 4/14/2016.
 */
public class Keygen {

    static boolean created = false;
    static int[] rev_byte_table;
    static int[] rev_hex_table;

    public Keygen(){
        if(!created) {
            init();
            created = true;
        }
    }
    public void init(){
        rev_byte_table = new int[256];
        rev_hex_table = new int[16];
        for(int i = 0; i < 256; i++) rev_byte_table[Global.byte_table[i]] = (int)i;
        for(int i = 0; i < 16; i++) rev_hex_table[Global.hex_table[i]] = (int)i;
    }
    int[] nextHexKey(int[] key){
        int[] p = new int[Global.BLOCK_SIZE];
        for(int i = 0; i < Global.BLOCK_SIZE; i++) {
            p[i] = Global.hex_table[key[i]];
        }
        return p;
    }
    int[] nextByteKey(int[] key){
        int[] p = new int[Global.BLOCK_SIZE];
        for(int i = 0; i < Global.BLOCK_SIZE; i++) {
            p[i] = Global.byte_table[key[i]];
        }
        return p;
    }
    /**
     * bit_size : 4 (hexa key)
     *            8 (byte key)
     */
    int[] nextKey(int[] key, int bit_size) {
        if(bit_size == 4) {
            return nextHexKey(key);
        }
        if(bit_size == 8) {
            return nextByteKey(key);
        }
        assert(false);
        return null;
    }

/* generate previous hexa key */
    int[] prevHexKey(int[] key) {
        int[] p = new int[Global.BLOCK_SIZE];
        for(int i = 0; i < Global.BLOCK_SIZE; i++) {
            p[i] = rev_hex_table[key[i]];
        }
        return p;
    }

/* generate previous byte key */
    int[] prevByteKey(int[] key) {
        int[] p = new int[Global.BLOCK_SIZE];
        for(int i = 0; i < Global.BLOCK_SIZE; i++) {
            p[i] = rev_byte_table[key[i]];
        }
        return p;
    }

/**
 * bit_size : 4 (hexa key)
 *            8 (byte key)
 */
    int[] prevKey(int[] key, int bit_size) {
        if(bit_size == 4) {
            return prevHexKey(key);
        }
        if(bit_size == 8) {
            return prevByteKey(key);
        }
        assert(false);
        return null;
    }
}
