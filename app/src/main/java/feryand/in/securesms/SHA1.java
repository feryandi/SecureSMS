package feryand.in.securesms;

import java.math.BigInteger;

/**
 * SHA-1 Hash Function
 * @author Feryandi
 */

public class SHA1 {

    BigInteger h0 = new BigInteger("1732584193");
    BigInteger h1 = new BigInteger("4023233417");
    BigInteger h2 = new BigInteger("2562383102");
    BigInteger h3 = new BigInteger("271733878");
    BigInteger h4 = new BigInteger("3285377520");

    BigInteger textBit;

    String digest;

    SHA1 (String text) {
        /* Count real bit length */
        int size = text.length() * 8;

        /* Message length */
        long ml = text.length() * 8;

        /* Change it to ASCII bit */
        textBit = new BigInteger(text.getBytes());

        /* Add 1 in the back */
        textBit = textBit.shiftLeft(1);
        textBit = textBit.add(new BigInteger("1"));
        size += 1;

        /* Add padding 0 to make congruent to 448(mod 512) */
        int padding = getCongruentPadding(size);
        textBit = textBit.shiftLeft(padding);
        size += padding;

        /* Add padding 64-bit for message length */
        textBit = textBit.shiftLeft(64);
        textBit = textBit.add(BigInteger.valueOf(ml));
        size += 64;

        /* Break message to 512-bit chunks */
        int nc = (int)Math.ceil(size/512);
        BigInteger[] chunks = new BigInteger[nc];

        for ( int i = nc - 1; i >= 0; i-- ) {
            int ha = nc - 1;
            chunks[ha - i] = textBit.shiftRight(i * 512);
            textBit = textBit.subtract(chunks[ha - i].shiftLeft(i * 512));

            /* Remember:
               In BigInteger, leading 0 is in-significant and not printed
            */
        }

        /* Break every chunks to Word (32-bit) */
        BigInteger[][] wchunks = new BigInteger[nc][80]; // 80 here because next step
        for ( int i = 0; i < nc; i++ ) {
            for ( int j = 15; j >= 0; j-- ) {
                wchunks[i][15-j] = chunks[i].shiftRight(j * 32);
                chunks[i] = chunks[i].subtract(wchunks[i][15-j].shiftLeft(j * 32));

                /* Remember:
                   In BigInteger, leading 0 is in-significant and not printed
                */
            }
        }

        /* Extend to 80 words */
        for (int x = 0; x < nc; x++) {
            int iext = 16;
            while (iext <= 79) {
                /* XOR */
                wchunks[x][iext] = (((wchunks[x][iext-3]).xor(wchunks[x][iext-8])).xor(wchunks[x][iext-14])).xor(wchunks[x][iext-16]);
                /* Rotate Shift */
                wchunks[x][iext] = rotateLeft(wchunks[x][iext]);
                ++iext;
            }

            /* The Main Loop */
            BigInteger A = h0;
            BigInteger B = h1;
            BigInteger C = h2;
            BigInteger D = h3;
            BigInteger E = h4;

            BigInteger F;
            BigInteger K;

            for (int i = 0; i < 80; i++) {
                if ( ( 0 <= i ) && ( i <= 19 ) ) {
                    /* Function 1 */
                    /* 0 - 19 */
                    F = (B.and(C)).or((B.not()).and(D));
                    K = BigInteger.valueOf(0x5A827999);
                } else if ( ( 20 <= i ) && ( i <= 39 ) ) {
                    /* Function 2 */
                    /* 20 - 39 */
                    F = (B.xor(C)).xor(D);
                    K = BigInteger.valueOf(0x6ED9EBA1);
                } else if ( ( 40 <= i ) && ( i <= 59 ) ) {
                    /* Function 3 */
                    /* 40 - 59 */
                    F = ((B.and(C)).or((B.and(D)))).or(C.and(D));
                    K = BigInteger.valueOf(0x8F1BBCDC);
                } else {
                    /* Function 4 */
                    /* 60 - 79 */
                    F = (B.xor(C)).xor(D);
                    K = BigInteger.valueOf(0xCA62C1D6);
                }

                BigInteger temp = (rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(A)))))); //Sudah Benar
                temp = temp.add(F);
                temp = temp.add(E);
                temp = temp.add(K);
                temp = temp.add(wchunks[x][i]);

                E = D;
                D = C;
                C = rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(B))))))))));
                C = rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(C))))))))));
                C = rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(rotateLeft(C))))))))));
                B = A;
                A = temp;
            }

            h0 = h0.add(A);
            h1 = h1.add(B);
            h2 = h2.add(C);
            h3 = h3.add(D);
            h4 = h4.add(E);

        }

        digest = "";
        digest += Integer.toHexString((h0).intValue());
        digest += Integer.toHexString((h1).intValue());
        digest += Integer.toHexString((h2).intValue());
        digest += Integer.toHexString((h3).intValue());
        digest += Integer.toHexString((h4).intValue());

    }

    private static BigInteger rotateLeft(BigInteger bi) {
        BigInteger ret = bi.shiftLeft(1);
        if (ret.testBit(32)) {
            ret = ret.clearBit(32).setBit(0);
        }
        return ret;
    }

    private int getCongruentPadding(int size) {
        int tailLength = size % 512;
        int padLength = 0;
        if ((512 - tailLength >= 64)) {
            padLength = 512 - tailLength;
        } else {
            padLength = 1024 - tailLength;
        }

        return (padLength-64);
    }

    public String getDigest() {
        return digest;
    }

}
