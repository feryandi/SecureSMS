package feryand.in.securesms;

/**
 * Created by Asus on 21 April 16.
 */
public class SMS {
    String message;
    String sender;

    SMS(String m, String s) {
        message = m;
        sender = s;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getPlainMessage(){
        int startpos = 0;
        int endpos = message.length();

        if (isModifiedMessage()) {
            startpos = message.indexOf("</ss>")+5;
        }

        if (isHaveSignature()) {
            endpos = message.indexOf("<ds>");
        }

        return message.substring(startpos,endpos);
        //return str.substring(11,str.length()-<panjang digital signaturenya>) atau pake panjang fixed
    }

    String getOption(){
        return message.substring(4,6);
    }

    boolean isEncrypted() {
        if (isModifiedMessage()) {
            if ( (getOption().substring(0, 1)).equals("E") ) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    String getDigitalSignature(){
        int startpos = message.indexOf("<ds>")+4+2;
        int endpos = message.indexOf("</ds>");
        return message.substring(startpos,endpos);
    }

    boolean isHaveSignature(){
        if(message.indexOf("<ds>")>=0){
            return true;
        } else {
            return false;
        }
    }

    boolean isModifiedMessage(){
        if(message.indexOf("<ss>")>=0){
            return true;
        } else {
            return false;
        }
    }
}
