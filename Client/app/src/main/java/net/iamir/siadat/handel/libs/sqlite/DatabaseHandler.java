package net.iamir.siadat.handel.libs.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.iamir.siadat.handel.models.Record;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "iamir_siadat";

    // Labels table name
    private static final String TABLE_RECORDS = "records";

    // Labels Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_SERIAL = "serial";
    private static final String KEY_NAME = "name";
    private static final String KEY_FAMILY = "family";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_STATUS = "status";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table create query
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_RECORDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SERIAL + " TEXT DEFAULT NULL," + KEY_NAME + " TEXT DEFAULT NULL," + KEY_FAMILY + " TEXT DEFAULT NULL," + KEY_LEVEL + " TEXT DEFAULT NULL," + KEY_STATUS + " TEXT DEFAULT NULL)";
        db.execSQL(CREATE_CATEGORIES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Inserting new lable into lables table
     */
    public void updateRecord(Record record, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERIAL, record.getRecordId());
        values.put(KEY_NAME, record.getRecordName());
        values.put(KEY_FAMILY, record.getRecordFamily());
        values.put(KEY_LEVEL, record.getRecordLevel());
        Cursor cursor = null;
        if  (record.getRecordLocalId() != null){
            cursor = db.query(TABLE_RECORDS, new String[]{KEY_ID}, KEY_ID + "=?",
                    new String[]{String.valueOf(record.getRecordLocalId())}, null, null, null, null);
            if (cursor.getCount() > 0) {
                values.put(KEY_STATUS, status == null ? "update" : status);
                db.update(TABLE_RECORDS, values, KEY_ID + " = ?", new String[]{String.valueOf(record.getRecordLocalId())});
            }
        }else if (record.getRecordId() != null){
            cursor = db.query(TABLE_RECORDS, new String[]{KEY_SERIAL}, KEY_SERIAL + "=?",
                    new String[]{String.valueOf(record.getRecordId())}, null, null, null, null);
            if (cursor.getCount() > 0) {
                values.put(KEY_STATUS, status == null ? "update" : status);
                db.update(TABLE_RECORDS, values, KEY_SERIAL + " = ?", new String[]{String.valueOf(record.getRecordId())});
            }
        }
        if (cursor == null || (cursor != null && cursor.getCount() == 0)){
            values.put(KEY_STATUS, status == null ? "store" : status);
            db.insert(TABLE_RECORDS, null, values);
        }
        db.close();
    }

    public void deleteRecord(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (record.getRecordId() == null)
            db.delete(TABLE_RECORDS, KEY_ID + " = ?",
                    new String[]{String.valueOf(record.getRecordLocalId())});
        else
            db.delete(TABLE_RECORDS, KEY_SERIAL + " = ?",
                    new String[]{String.valueOf(record.getRecordId())});
        db.close();
    }


    public List<Record> getAllRecords(Boolean saved) {
        List<Record> records = new ArrayList<Record>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECORDS;
        if (saved)
            selectQuery += " where " + KEY_STATUS + " != 'delete'";
        else
            selectQuery += " where " + KEY_STATUS + " != 'saved'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Record $record = new Record();
                $record.setRecordLocalId(cursor.getString(0));
                $record.setRecordId(cursor.getString(1));
                $record.setRecordName(cursor.getString(2));
                $record.setRecordFamily(cursor.getString(3));
                $record.setRecordLevel(cursor.getString(4));
                $record.setRecordStatus(cursor.getString(5));
                records.add($record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return records;
    }

    public List<Record> getAllRecordsQuery(String query) {
        List<Record> records = new ArrayList<Record>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECORDS;
        selectQuery += " where " + KEY_STATUS + " != 'delete'";
        selectQuery += " where " + KEY_NAME + " = " + query + " or " + KEY_FAMILY + " = " + query;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Record $record = new Record();
                $record.setRecordLocalId(cursor.getString(0));
                $record.setRecordId(cursor.getString(1));
                $record.setRecordName(cursor.getString(2));
                $record.setRecordFamily(cursor.getString(3));
                $record.setRecordLevel(cursor.getString(4));
                $record.setRecordStatus(cursor.getString(5));
                records.add($record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return records;
    }
}