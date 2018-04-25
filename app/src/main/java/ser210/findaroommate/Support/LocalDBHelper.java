package ser210.findaroommate.Support;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LocalDBHelper {
    private MySQLiteHelper dbhelper;
    private SQLiteDatabase database;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,MySQLiteHelper.COLUMN_EMAIL,MySQLiteHelper.COLUMN_PASSWORD};
    public LocalDBHelper(Context context){
        this.dbhelper = new MySQLiteHelper(context);
    }
    public void open(){
        this.database = dbhelper.getWritableDatabase();
    }
    public void close(){
        this.database.close();
    }
    public SavedLoginUser getSavedUser(){
        if(this.database==null){
            Log.e("Database Error","Database not found! Did you open the connection?");
            return null;
        }
        Cursor cursor = database.query(MySQLiteHelper.TABLE_LOGIN, allColumns,null,null,null,null,null);
        cursor.moveToFirst();
        Log.i("Cursor Pos",String.valueOf(cursor.getPosition()));
        if(cursor.isFirst()){
            //we only need to return the top. Only one user should be stored in our database.
            String email = cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_EMAIL));
            String password = cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_PASSWORD));
            return new SavedLoginUser(email,password);
        }
        return null;
    }
    public void setSavedUser(String email, String password){
        if(this.database==null){
            Log.e("Database Error","Database not found! Did you open the connection?");
            return;
        }
        this.removeAllUsers();
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_EMAIL,email);
        values.put(MySQLiteHelper.COLUMN_PASSWORD,password);
        int rows = database.update(MySQLiteHelper.TABLE_LOGIN,values,MySQLiteHelper.COLUMN_ID + "= 1",null);
        Log.i("RESULT: Rows",String.valueOf(rows));
        if(rows==0){
            Log.i("Database","No Entry, Entering New User");
           long result = database.insert(MySQLiteHelper.TABLE_LOGIN,null,values);
            Log.i("RESULT",String.valueOf(result));
        }
    }
    public void removeAllUsers(){
        if(this.database==null){
            Log.e("Database Error","Database not found! Did you open the connection?");
            return;
        }
        database.execSQL("delete from "+ MySQLiteHelper.TABLE_LOGIN);
    }

    public class SavedLoginUser{
        public String email;
        public String password;
        public SavedLoginUser(String email, String password){
            this.password=password;
            this.email = email;
        }
    }

}
