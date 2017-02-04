package com.theunheard.habitking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by aimanmduslim on 2/2/17.
 */

public class DBHandler extends SQLiteOpenHelper {
    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "habits.db";
    public static final String TABLE_HABITS="habits";
    public static final String TABLE_PIT="persons"; // person interacted

    public static final String COL_ID = "_id";
    public static final String COL_OWNID = "_ownerid";
    public static final String COL_NAME = "habitname";
    public static final String COL_DATELP = "dateLastPerformed";
    public static final String COL_CATEGORY = "category";
    public static final String COL_FREQUENCY = "frequency";


    public static final String COL_PITID = "_pitid";
    public static final String COL_PITNAME = "personInteracted";



    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryHabits = "CREATE TABLE " + TABLE_HABITS+ "(" +

                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_OWNID + " INTEGER, " +
                COL_NAME + " TEXT, " +
                COL_DATELP + " INTEGER, " + // date has to be stored as an integer
                COL_CATEGORY + " TEXT, " +
                COL_FREQUENCY + " INTEGER " +

                ");";

//        String queryPersonsInteracted = "CREATE TABLE " + TABLE_PIT + "(" +
//
//                COL_PITID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COL_OWNID + " TEXT," +
//                COL_PITNAME + " TEXT " +
//                ");";

        db.execSQL(queryHabits);
//        db.execSQL(queryPersonsInteracted);





    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+TABLE_HABITS);
        db.execSQL("DROP TABLE IF EXISTS"+TABLE_PIT);
        onCreate(db);
    }

    public ArrayList<String> getNames(String colName) {
        ArrayList<String> names = new ArrayList<String>();
        SQLiteDatabase db= getReadableDatabase();
        String query = "SELECT " + colName + " FROM " + TABLE_HABITS;
        Cursor c = db.rawQuery(query ,null);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            if(c.getString(c.getColumnIndex(colName))!=null) {
                String name = c.getString(c.getColumnIndex(colName));
                names.add(name);
            }
            c.moveToNext();

        }
        db.close();
        return names;
    }

    public void addHabits(Habit habit) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, habit.getName());
        values.put(COL_OWNID, habit.getOwnerUid());
        values.put(COL_CATEGORY, habit.getCategory());
        values.put(COL_FREQUENCY, 0);
        values.put(COL_DATELP, habit.getDateLastPerformed().getTime());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_HABITS, null, values);
        db.close();

//        addPersonInteracted(habit);
    }

    public void addPersonInteracted(Habit habit) {
        String habitID = habit.getOwnerUid();
        SQLiteDatabase db = getWritableDatabase();

        for (String personName :habit.getPersonsInteracted()
             ) {
            ContentValues values = new ContentValues();
            values.put(COL_PITNAME, personName);
            values.put(COL_OWNID, habitID);
            db.insert(TABLE_PIT, null, values);
        }

        db.close();
    }

    public void deleteHabit(String habitname, String ownerId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_HABITS + " WHERE " + COL_NAME + "=\"" + habitname + "\"" +
//                " AND " + COL_OWNID + "=\"" + ownerId + "\"" +
                ";");
        db.close();
    }

    public String databasetostring(){

        String dbString="";

        SQLiteDatabase db= getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_HABITS;

        Cursor c =db.rawQuery(query,null);

        c.moveToFirst();

        while (!c.isAfterLast())

        {

            if(c.getString(c.getColumnIndex(COL_NAME))!=null)

            {

                dbString+= c.getString(c.getColumnIndex(COL_NAME));

                dbString+="\n";

            }

            c.moveToNext();

        }

        db.close();

        return dbString;

    }

    public void deleteTables() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_HABITS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PIT);
        db.close();
    }

}
