package com.theunheard.habitking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by aimanmduslim on 2/2/17.
 */

public class DBHandler extends SQLiteOpenHelper {
    private static DBHandler sInstance;
    private static Context myContext;
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
    public static final String COL_PERIOD = "period";
    public static final String COL_MULTIPLIER = "multiplier";


    public static final String COL_PITID = "_pitid";
    public static final String COL_PITNAME = "personInteracted";

//    public static synchronized DBHandler getInstance(Context context) {
//
//        // Use the application context, which will ensure that you
//        // don't accidentally leak an Activity's context.
//        // See this article for more information: http://bit.ly/6LRzfx
//        if (sInstance == null) {
//            sInstance = new DBHandler(context.getApplicationContext());
//        }
//        return sInstance;
//    }

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
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
                COL_PERIOD + " INTEGER " +
                COL_MULTIPLIER + " INTEGER " +


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

    public void refreshDB () {
        deleteTables();
        createTable();

    }


    public void createTable() {
        SQLiteDatabase db = getWritableDatabase();
        String queryHabits = "CREATE TABLE " + TABLE_HABITS+ "(" +

                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_OWNID + " INTEGER, " +
                COL_NAME + " TEXT, " +
                COL_DATELP + " INTEGER, " + // date has to be stored as an integer
                COL_CATEGORY + " TEXT, " +
                COL_FREQUENCY + " INTEGER " +
                COL_PERIOD + " TEXT " +
                COL_MULTIPLIER + " INTEGER " +


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

    public ArrayList<Habit> getAllHabits() {
        ArrayList<Habit> habitList = new ArrayList<Habit>();

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_HABITS;
        Cursor c = db.rawQuery(query ,null);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            Habit habit = new Habit();

            habit.setId(c.getString(c.getColumnIndex(COL_ID)));

            if(c.getString(c.getColumnIndex(COL_NAME)) != null) {
                habit.setName(c.getString(c.getColumnIndex(COL_NAME)));
            }



            if(c.getString(c.getColumnIndex(COL_CATEGORY)) != null) {
                habit.setCategory(c.getString(c.getColumnIndex(COL_CATEGORY)));
            }

            try {
                habit.setDateLastPerformed(new Date(c.getLong(c.getColumnIndex(COL_DATELP))));
                habit.setReminderPeriodProperties(c.getInt(c.getColumnIndex(COL_PERIOD)), c.getInt(c.getColumnIndex(COL_MULTIPLIER)));
                habit.setFrequencyPerformed(c.getInt(c.getColumnIndex(COL_FREQUENCY)));

            } catch (Exception e) {
                Log.d("Habit King", "Date doesn't exist for this row?");
            }
            // TODO: finish this
            habitList.add(habit);








            c.moveToNext();

        }
        db.close();
        return habitList;

    }



    public void addHabit(Habit habit) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, habit.getName());
        values.put(COL_OWNID, habit.getOwnerUid());
        values.put(COL_CATEGORY, habit.getCategory());
        values.put(COL_FREQUENCY, 0);
        values.put(COL_DATELP, habit.getDateLastPerformed().getTime());
        values.put(COL_PERIOD, habit.getReminderPerPeriodLengthMode());
        values.put(COL_MULTIPLIER, habit.getReminderPeriodMultiplier());
        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("ALTER TABLE " + TABLE_HABITS + " ADD COLUMN " + COL_MULTIPLIER + " INTEGER" );
//        db.execSQL("ALTER TABLE " + TABLE_HABITS + " ADD COLUMN " + COL_PERIOD + " INTEGER" );
        db.insert(TABLE_HABITS, null, values);
        db.close();

//        addPersonInteracted(habit);
    }

    public void modifyHabit (Habit habit) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, habit.getName());
        cv.put(COL_CATEGORY, habit.getCategory());
        cv.put(COL_FREQUENCY, habit.getFrequencyPerformed());
        cv.put(COL_DATELP, habit.getDateLastPerformed().getTime());
        cv.put(COL_PERIOD, habit.getReminderPerPeriodLengthMode());
        cv.put(COL_MULTIPLIER, habit.getReminderPeriodMultiplier());



        String ID = habit.getId();
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_HABITS, cv, COL_ID+"="+ID, null);
        db.close();




    }

    public boolean deleteHabit(Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_HABITS, COL_ID + " = " + id.toString(), null) > 0;
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
