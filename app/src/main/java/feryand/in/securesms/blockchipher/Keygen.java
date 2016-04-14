package feryand.in.securesms.blockchipher;

/**
 * Created by yoga on 4/14/2016.
 */
public class Keygen {

    static boolean created = false;
    static char[] rev_byte_table= null;
    static char[] rev_hex_table=null;

    public Keygen(){
        if(!created) {
            init();
            created = true;
        }
    }
    public void init(){
        rev_byte_table = new char[256];
        rev_hex_table = new char[16];
        for(int i = 0; i < 256; i++) rev_byte_table[Global.byte_table[i]] = (char)i;
        for(int i = 0; i < 16; i++) rev_hex_table[Global.hex_table[i]] = (char)i;
    }
    char[] nextHexKey(char[] key){
        char[] p = new char[Global.BLOCK_SIZE];
        for(int i = 0; i < Global.BLOCK_SIZE; i++) {
            p[i] = Global.hex_table[key[i]];
        }
        return p;
    }
    char[] nextByteKey(char[] key){
        char[] p = new char[Global.BLOCK_SIZE];
        for(int i = 0; i < Global.BLOCK_SIZE; i++) {
            p[i] = Global.byte_table[key[i]];
        }
        return p;
    }
    /**
     * bit_size : 4 (hexa key)
     *            8 (byte key)
     */
    char[] nextKey(char[] key, int bit_size) {
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
    char[] prevHexKey(char[] key) {
        char[] p = new char[Global.BLOCK_SIZE];
        for(int i = 0; i < Global.BLOCK_SIZE; i++) {
            p[i] = rev_hex_table[key[i]];
        }
        return p;
    }

/* generate previous byte key */
    char[] prevByteKey(char[] key) {
        char[] p = new char[Global.BLOCK_SIZE];
        for(int i = 0; i < Global.BLOCK_SIZE; i++) {
            p[i] = rev_byte_table[key[i]];
        }
        return p;
    }

/**
 * bit_size : 4 (hexa key)
 *            8 (byte key)
 */
    char[] prevKey(char[] key, int bit_size) {
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
