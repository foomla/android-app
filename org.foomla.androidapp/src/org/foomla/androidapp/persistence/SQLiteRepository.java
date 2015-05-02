package org.foomla.androidapp.persistence;

import org.foomla.androidapp.data.EntityBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteRepository extends SQLiteOpenHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SQLiteRepository.class);

    private static final String DATABASE_NAME = "foomla.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TBL_RATING = "CREATE TABLE rating ("
            + "id             INTEGER PRIMARY KEY AUTOINCREMENT," + "exercise_id    INTEGER," + "value          INTEGER"
            + ");";

    public static final String TBL_RATING = "rating";
    public static final String QUERY_RATINGS = "SELECT id, exercise_id, value FROM rating";
    public static final String QUERY_RATING_BY_EXERCISE = "SELECT id, exercise_id, "
            + "value FROM rating WHERE exercise_id = ?";

    private static SQLiteRepository INSTANCE;

    private SQLiteRepository(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteRepository getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SQLiteRepository(context);
        }

        return INSTANCE;
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        LOGGER.info("Create new SQLite database");

        db.beginTransaction();
        db.execSQL(CREATE_TBL_RATING);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        LOGGER.info("Requested an upgrade from " + oldVersion + " to " + newVersion);
    }

    public void select(final EntityBuilder builder, final String query, final String... args) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery(query, args);

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            builder.addRow(cursor);
        }
    }

    public long insert(final String table, final ContentValues values) {
        SQLiteDatabase writeableDatabase = getWritableDatabase();
        writeableDatabase.beginTransaction();

        long insert = writeableDatabase.insert(table, null, values);

        writeableDatabase.setTransactionSuccessful();
        writeableDatabase.endTransaction();

        return insert;
    }
}
