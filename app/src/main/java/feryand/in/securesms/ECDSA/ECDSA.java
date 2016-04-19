package feryand.in.securesms.ECDSA;

import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author Feryandi
 */
public class ECDSA {
    /* Pre-defined */
    /* y^2 = x^3 + ax^2 + b mod prime */  
    BigInteger a;
    BigInteger b;
    BigInteger prime;     
    BigInteger n; /* order */
    Point G; /* base point */
    
    /* Pub - Pri */
    public Point pub;
    public BigInteger pri;
    
    public ECDSA() {
        /* Using secp256k1 */
        prime = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16);
        a = new BigInteger("0000000000000000000000000000000000000000000000000000000000000000", 16);
        b = new BigInteger("0000000000000000000000000000000000000000000000000000000000000007", 16);
        n = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16);
        G = new Point(new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798",16),
                      new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8",16),
                      prime);
        
        
    }
        
    public void generateKey() {
        Random generator = new Random();
        BigInteger iv = new BigInteger("" + (generator.nextInt()*5) + 1);
        pri = iv.mod(n.subtract(BigInteger.valueOf(1)));    
        pub = G.multiply(pri, a);
        Point x = pub.multiply(n, a);
    }

    public void setPri(String newPri) {
        pri = new BigInteger(newPri, 16);
        pub = G.multiply(pri, a);
        Point x = pub.multiply(n, a);
    }
    
    public Point generateSignature(String hashed) {
        /* Masukan string hashed, harus sudah dihash dengan SHA-1 */
        
        Random generator = new Random();
        BigInteger k = BigInteger.valueOf(0);
        
        Point rs = new Point();
        BigInteger r = BigInteger.valueOf(0);
        BigInteger s = BigInteger.valueOf(0);
        
        do {
            do {
                BigInteger iv = new BigInteger("" + (generator.nextInt()*5) + 1);
                k = iv.mod(n.subtract(BigInteger.valueOf(1)));
                Point ry = G.multiply(k, a);
                
                r = (ry.getX()).mod(n);
            } while ( r.equals(BigInteger.valueOf(0)) );
        
            BigInteger hm = new BigInteger(hashed, 16);            
            BigInteger hmdr = hm.add(pri.multiply(r));
            
            s = ((k.modInverse(n)).multiply(hmdr)).mod(n);
            
        } while ( s.equals(BigInteger.valueOf(0)) );
            
        rs = new Point(r, s, prime);
        
        return rs;
    }
    
    public boolean verifySignature(Point rs, String hashed) {
        /* Masukan string hashed, harus sudah dihash dengan SHA-1 */
        
        boolean ret = false;
        
        if ( ( (rs.getX()).compareTo(n) == -1 ) &&
             ( (rs.getY()).compareTo(n) == -1 ) ) {
            
            BigInteger w = (rs.getY()).modInverse(n);
            BigInteger hm = new BigInteger(hashed, 16);
            
            BigInteger u1 = (hm.multiply(w)).mod(n);
            BigInteger u2 = ((rs.getX()).multiply(w)).mod(n);
            
            Point uP = G.multiply(u1, a);
            Point uQ = pub.multiply(u2, a);
            
            Point xy = uP.add(uQ, a);
            
            BigInteger v = (xy.getX()).mod(n);
            
            if ( v.equals(rs.getX()) ) {
                ret = true;
            }
            
        }
        
        return ret;
    }
        
}
