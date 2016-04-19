package feryand.in.securesms.ECDSA;

import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author Yoga and Fery
 */
public class Point {
    private BigInteger x,y;
    private BigInteger mod;
    public boolean infinity = false;
    
    public Point(){
        x = BigInteger.valueOf(0);
        y = BigInteger.valueOf(0);
    }
    
    public Point(BigInteger x, BigInteger y, BigInteger mod){
        this.mod = mod;
        this.x = bePositive(x, mod);
        this.y = bePositive(y, mod);
    }
    
    public boolean isEqual(Point p){
        if (this.infinity && p.infinity) 
            return true;
        else if (this.infinity || p.infinity)
            return false;
        else if (x.equals(p.getX()) && y.equals(p.getY()))
            return true;        
        else
            return false;
    }
    
    private BigInteger bePositive(BigInteger a, BigInteger mod) {
        while ( a.compareTo(BigInteger.valueOf(0)) == -1 ) {
            a = a.add(mod);
        }
        return a;
    }
    
    public BigInteger getX(){
        return x;
    }
    public BigInteger getY(){
        return y;
    }
    public void setX(BigInteger x){     
        this.x = bePositive(x, mod);
    }
    public void setY(BigInteger y){
        this.y = bePositive(y, mod);
    }
        
    public Point add(Point p, BigInteger aElips){
        if ( this.infinity ) {
            return p;
        } else if ( p.infinity ) {
            return this;
        } else if ( isEqual(p) ) {
            return kuadrat(aElips);
        } else {
            Point iP = new Point(p.getX(), p.getY().multiply(BigInteger.valueOf(-1)), mod);
            
            if ( isEqual(iP) ) {
                Point r = new Point(BigInteger.valueOf(0), BigInteger.valueOf(0), mod);
                r.infinity = true;
                return r;
            } else {
                BigInteger gradien;
                BigInteger ygradien = (this.getY()).subtract(p.getY());
                BigInteger xgradien = (this.getX()).subtract(p.getX());
                
                if( xgradien.equals(BigInteger.valueOf(0)) ){
                    Point r = new Point(BigInteger.valueOf(0), BigInteger.valueOf(0), mod);
                    r.infinity = true;
                    return r;
                }            
                
                xgradien = xgradien.modInverse(mod);
                ygradien = ygradien.mod(mod);

                gradien = ((ygradien).multiply(xgradien)).mod(mod);

                BigInteger xhasil = (((gradien.pow(2)).subtract(p.getX())).subtract(x)).mod(mod);
                BigInteger yhasil = ((gradien.multiply(p.getX().subtract(xhasil))).subtract(p.getY())).mod(mod);

                Point r= new Point(xhasil,yhasil,mod);
                return r;
            }        
            
        }        
    }
    
    public Point kuadrat(BigInteger aElips){
        if (this.infinity) {
            Point r = new Point(BigInteger.valueOf(0), BigInteger.valueOf(0), mod);
            r.infinity = true;
            return r;
        } else {
            BigInteger gradien;

            BigInteger ygradien = BigInteger.valueOf(2).multiply(y);
            BigInteger xgradien = (BigInteger.valueOf(3).multiply(x.pow(2))).add(aElips);

            ygradien = ygradien.modInverse(mod);
            gradien = (ygradien.multiply(xgradien)).mod(mod);

            BigInteger xhasil = ((gradien.pow(2)).subtract(BigInteger.valueOf(2).multiply(x))).mod(mod);
            BigInteger yhasil = (gradien.multiply(x.subtract(xhasil)).subtract(y)).mod(mod);

            Point r = new Point(xhasil, yhasil, mod);

            return r;
        }
    }
    
    public Point multiply(BigInteger n, BigInteger aElips){
        byte[] b = n.toByteArray();
        String bit = n.toString(2);
        int len = n.bitLength();
        Point p = this;     
                
        int i = 0;        
        
        int binary = (int) bit.charAt(i) - 48;
        Point t = new Point(BigInteger.valueOf(0), BigInteger.valueOf(0), mod);
        if ( binary == 1 ) {
            t = p;   
        }
        
        for ( i = i+1; i < len; i++ ) {
            t = t.kuadrat(aElips);
            binary = (int) bit.charAt(i) - 48;
            if ( binary == 1 ) {
                t = t.add(p, aElips);
            }
        }
        
        return t;
    }
    
    
}
