package com.dayal.medlist.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dayal.medlist.Model.Medicine;
import com.dayal.medlist.Util.Constants;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private Context cntxt;
    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME,null ,Constants.DB_VERSION);
        this.cntxt=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       String CREATE_MED_TABLE= "CREATE TABLE " + Constants.TABLE_NAME + "(" + Constants.KEY_ID + " INTEGER PRIMARY KEY," +
               Constants.KEY_MED_ITEM + " TEXT," + Constants.KEY_QTY + " TEXT," + Constants.KEY_DATE_ADDED + " LONG);";
        db.execSQL(CREATE_MED_TABLE);

            }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }


    public void addMed(Medicine medicine){
      SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Constants.KEY_MED_ITEM,medicine.getName());
        values.put(Constants.KEY_QTY,medicine.getQuantity());
        values.put(Constants.KEY_DATE_ADDED,java.lang.System.currentTimeMillis());

        db.insert(Constants.TABLE_NAME,null,values);

        Log.i("Save","Saved to DB");

    }

    public Medicine getMed(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(Constants.TABLE_NAME,new String[]{Constants.KEY_ID,Constants.KEY_MED_ITEM,Constants.KEY_QTY,Constants.KEY_DATE_ADDED},
                Constants.KEY_ID + "=?",new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();
        Medicine medicine=new Medicine();
        medicine.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
        medicine.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_MED_ITEM)));
        medicine.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY)));
        //convert timestamp to something readable
        DateFormat dateFormat=DateFormat.getDateInstance();
        String formattedDate=dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_ADDED))).getTime());
        medicine.setDateAdded(formattedDate);

        return medicine;
    }

    public List<Medicine> getAllMeds(){
       SQLiteDatabase db=this.getReadableDatabase();
        List<Medicine> medicineList=new ArrayList<>();
        Cursor cursor=db.query(Constants.TABLE_NAME,new String[]{Constants.KEY_ID,Constants.KEY_MED_ITEM,Constants.KEY_QTY,Constants.KEY_DATE_ADDED}
                               ,null,null,null,null,Constants.KEY_DATE_ADDED + " DESC");

          if(cursor.moveToFirst()){
              do{
                  Medicine medicine=new Medicine();
                  medicine.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                  medicine.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_MED_ITEM)));
                  medicine.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY)));
                  //convert timestamp to something readable
                  DateFormat dateFormat=DateFormat.getDateInstance();
                  String formattedDate=dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_ADDED))).getTime());
                  medicine.setDateAdded(formattedDate);

                  medicineList.add(medicine);
              }while ((cursor.moveToNext()));
          }
          cursor.close();
          return  medicineList;
    }

    public int updateMed(Medicine medicine){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Constants.KEY_MED_ITEM,medicine.getName());
        values.put(Constants.KEY_QTY,medicine.getQuantity());
        values.put(Constants.KEY_DATE_ADDED,medicine.getDateAdded());
        Log.w("log","updated " + String.valueOf(medicine.getId()));

        return db.update(Constants.TABLE_NAME,values, Constants.KEY_ID + "=?",new String[]{String.valueOf(medicine.getId())});  //returns id (int)
    }

    public void deleteMed(int id){
    SQLiteDatabase db =this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME,Constants.KEY_ID + "=?",new String[]{String.valueOf(id)});
        Log.w("log","deleted " + String.valueOf(id));
        db.close();
            }

    public int getMedCount(){
        String countQuery= "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(countQuery,null);
        return cursor.getCount();
    }

}
