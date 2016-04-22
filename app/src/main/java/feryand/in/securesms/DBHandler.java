package feryand.in.securesms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Feryandi on 24/09/2015.
 * Modified by Feryandi on 06/04/2016.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "ssms.db";
    private static final String TABLE_DATA = "ssmsData";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IDENTIFIER = "identifier";
    public static final String COLUMN_VALUE = "value";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " +
                TABLE_DATA + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_IDENTIFIER + " TEXT," +
                COLUMN_VALUE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
        onCreate(db);
    }

    public void addData(Data data) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_IDENTIFIER, data.getIdentifier());
        values.put(COLUMN_VALUE, data.getValue());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_DATA, null, values);
        db.close();
    }

    public Data findData (String id) {
        String query = "Select * FROM " + TABLE_DATA + " WHERE " + COLUMN_IDENTIFIER + " =  \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Data data = new Data();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            data.setID(Integer.parseInt(cursor.getString(0)));
            data.setIdentifier(cursor.getString(1));
            data.setValue(cursor.getString(2));
            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    public boolean deleteData(String id) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_DATA + " WHERE " + COLUMN_IDENTIFIER + " =  \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Data data = new Data();

        if (cursor.moveToFirst()) {
            data.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_DATA, COLUMN_IDENTIFIER + " = ?",
                    new String[] { String.valueOf(data.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

}