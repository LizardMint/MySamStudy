package com.example.mysamstudy.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mysamstudy.objects.Card;
import com.example.mysamstudy.objects.Set;
import com.example.mysamstudy.objects.User;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String TAG = "TAG";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mySamStudy.db";

    /* Users table */
    private static final String USERS_TABLE = "users";
    private static final String user_id = "user_id";
    private static final String first_name = "first_name";
    private static final String last_name = "last_name";
    private static final String username = "username";
    private static final String password = "password";
    private static final String email = "email";
    private static final String register_date = "register_date";

    /* Cards table */
    private static final String CARDS_TABLE = "cards";
    private static final String card_id = "card_id";
    private static final String question = "question";
    private static final String answer = "answer";

    /* Sets table */
    private static final String SETS_TABLE = "sets";
    private static final String set_id = "set_id";
    private static final String set_name = "set_name";
    private static final String set_size = "set_size";
    private static final String show_answers = "show_answers";
    private static final String loop_set = "loop_set";
    private static final String share_set = "share_set";

    public DatabaseManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: DBMS");
        createUsersTable(db);
        createSetsTable(db);
        createCardsTable(db);
    }

    private void createUsersTable(SQLiteDatabase db) {
        String query = "CREATE TABLE " + USERS_TABLE + "("
                + user_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + first_name + " TEXT NOT NULL, "
                + last_name + " TEXT NOT NULL, "
                + username + " TEXT NOT NULL, "
                + password + " TEXT NOT NULL, "
                + email + " TEXT NOT NULL, "
                + register_date + " DATETIME NOT NULL)";
        db.execSQL(query);
    }

    private void createSetsTable(SQLiteDatabase db) {
        String query = "CREATE TABLE " +
                SETS_TABLE + "(" +
                set_id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                set_name + " TEXT NOT NULL, " +
                set_size + " INTEGER NOT NULL DEFAULT (0), " +
                show_answers + " BIT NOT NULL DEFAULT (0), " +
                loop_set + " BIT NOT NULL DEFAULT (0), " +
                share_set + " BIT NOT NULL DEFAULT (0), " +
                user_id + " INTEGER NOT NULL REFERENCES " + USERS_TABLE + "(" + user_id + "))";
        db.execSQL(query);
    }

    private void createCardsTable(SQLiteDatabase db) {
        String query = "CREATE TABLE " +
                CARDS_TABLE + "(" +
                card_id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                question + " TEXT NOT NULL, " +
                answer + " TEXT NOT NULL, " +
                set_id + " INTEGER NOT NULL REFERENCES " + SETS_TABLE + "(" + set_id + "))";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SETS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CARDS_TABLE);
        onCreate(db);
    }

    public void getUserById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + " FROM " + "USER_TABLE";

    }

    public User getUser(String mUsername, String mPassword){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + USERS_TABLE + " WHERE " + username + " = '" + mUsername +
                "' AND " + password + " = '" + mPassword + "'";
        Cursor c = database.rawQuery(query, null);
        if (!(c.moveToFirst()) || c.getCount() == 0){
            Log.d(TAG, "getUser: NULL");
            return null;
        }
        User user = new User();
        c.moveToFirst();
        do {
            user.setUser_id(c.getInt(c.getColumnIndex(user_id)));
            user.setFirst_name(c.getString(c.getColumnIndex(first_name)));
            user.setLast_name(c.getString(c.getColumnIndex(last_name)));
            user.setEmail(c.getString(c.getColumnIndex(email)));
            user.setPassword(c.getString(c.getColumnIndex(password)));
            user.setUsername(c.getString(c.getColumnIndex(username)));
            user.setRegister_date(c.getString(c.getColumnIndex(register_date)));
        }
        while(c.moveToNext());
        c.close();
        return user;
    }

    public boolean checkUsername(String mUsername){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + username +
                " FROM " + USERS_TABLE +
                " WHERE " + username + " = '" + mUsername + "'";
        Cursor c = db.rawQuery(query, null);
        if (!(c.moveToFirst()) || c.getCount() == 0){
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public long addUser(User user){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(first_name, user.getFirst_name());
        values.put(last_name, user.getLast_name());
        values.put(username, user.getUsername());
        values.put(email, user.getEmail());
        values.put(password, user.getPassword());
        values.put(register_date, user.getRegister_date());
        long id = database.insert(USERS_TABLE, null, values);
        return id;
    }

    public void removeUser(){
        /*
            The statement of code that calls this method will be located in the 'AccountActivity'.
            Obtain the currently logged in users information via the shared preferences and create a query
            that searches the database for that instance or tuple containing that users information.
            Delete that tuple if the correct values are obtained. You will need to give this method (removeUser)
            a parameter list of those values and pass them whenever you call this method from another activity or class.
            You can pass the unique user_id to be sure you're deleting the correct user, but also pass some other
            information about the user for verification like their username or email.

        */

        // YOUR CODE GOES HERE










    }

    public ArrayList<Set> getSets(){
        SQLiteDatabase database = this.getWritableDatabase();
        ArrayList<Set> setsList = new ArrayList<>();
        String query = "SELECT * FROM " + SETS_TABLE;
        Cursor c = database.rawQuery(query, null);
        if (!(c.moveToFirst()) || c.getCount() == 0){
            c.close();
            return null;
        }
        c.moveToFirst();
        do {
            setsList.add(new Set(
                    c.getInt(c.getColumnIndex(set_id)),
                    c.getString(c.getColumnIndex(set_name)),
                    c.getInt(c.getColumnIndex(set_size)),
                    (c.getInt(c.getColumnIndex(show_answers)) != 0),
                    (c.getInt(c.getColumnIndex(loop_set)) != 0),
                    (c.getInt(c.getColumnIndex(share_set)) != 0),
                    c.getInt(c.getColumnIndex(user_id))
            ));
        }
        while (c.moveToNext());

        return setsList;
    }

    public long addSet(Set set){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(set_name, set.getSetName());
        values.put(set_size, 0);
        values.put(show_answers, 0);
        values.put(loop_set, 0);
        values.put(share_set, 0);
        values.put(user_id, set.getFK());
        return database.insert(SETS_TABLE, null, values);
    }

    public void deleteSet(Set set){

        /*
            There are two places in the application where this method can be called. One in the 'MainActivity' when
            the user long-pressed one of the listview items in which a selection-mode is presented for deletion, and
            again in the 'SetActivity' for which there will be a deletion option when the user presses the edit button
            (pencil icon). In the MainActivity, the option of multiple set deletion will be an option.
            The statement of code that calls this method from the MainActivity will be in a for loop in which each iteration
            will call this method passing a 'Set' object to be deleted. Create a query that locates the set and
            removes it from the database. You will have to consider who owns the set (HINT: Foreign Key),
            and the id of the set (Hint: primary key). Verify that the correct set is selected or located
            in the database and delete it. For the SetActivity, only the set that is currently being viewed will need
            to be deleted. Regardless though, this method will be called one at a time by passing a single set object.
        */

        // YOUR CODE GOES HERE










    }

    public ArrayList<Card> getCards(int setID){
        ArrayList<Card> cardList = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + CARDS_TABLE + " WHERE " + set_id + " = '" + setID + "'";
        Cursor c = database.rawQuery(query, null);
        if (!(c.moveToFirst()) || c.getCount() == 0){
            c.close();
            return null;
        }
        c.moveToFirst();
        do {
            cardList.add(new Card(
                    c.getInt(c.getColumnIndex(card_id)),
                    c.getString(c.getColumnIndex(question)),
                    c.getString(c.getColumnIndex(answer)),
                    c.getInt(c.getColumnIndex(set_id))
            ));
        }
        while(c.moveToNext());
        return cardList;
    }

    public long addCard(Card card){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(question, card.getCardQuestion());
        values.put(answer, card.getCardAnswer());
        values.put(set_id, card.getFK());
        return db.insert(CARDS_TABLE, null, values);
    }

    public  void removeCard(Card card){

        /*
            This method should be called within the 'SetActivity' class. When the user long-presses a listview
            item, they will be presented with a selection-mode in which they can select multiple listview items (cards)
            and then press the delete button. The statement of code that calls this method will be in a for loop in which
            each iteration will call this method passing a 'Card' object to be deleted. Create a query that locates the Card
            and removes it from the database. You will have to consider who owns the set for which the card belongs to
            (HINT: Foreign Key), and the id of the Card (Hint: primary key). Verify that the correct Card is selected or located
            in the database and delete it.
        */

        // YOUR CODE GOES HERE










    }

}