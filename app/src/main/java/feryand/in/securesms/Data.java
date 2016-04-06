package feryand.in.securesms;

/**
 * Created by Feryandi on 24/09/2015.
 * Modified by Feryandi on 06/04/2016.
 */
public class Data {
    private int _id;
    private String _identifier;
    private String _value;

    public Data() {}

    public Data(int id, String identifier, String value) {
        this._id = id;
        this._identifier = identifier;
        this._value = value;
    }

    public Data(String identifier, String value) {
        this._identifier = identifier;
        this._value = value;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public void setIdentifier(String identifier) {
        this._identifier = identifier;
    }

    public String getIdentifier() {
        return this._identifier;
    }

    public void setValue(String value) {
        this._value = value;
    }

    public String getValue() {
        return this._value;
    }


}