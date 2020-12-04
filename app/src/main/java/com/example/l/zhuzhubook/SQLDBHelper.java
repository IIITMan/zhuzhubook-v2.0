package com.example.l.zhuzhubook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLDBHelper extends SQLiteOpenHelper{
    //
    private static String DB_Name="book_db.db";
    private static int DB_V=1;
    private static SQLDBHelper dbHelper=null;
    private SQLiteDatabase db=null;
    @Override
    public void onCreate(SQLiteDatabase dbl) {
        String sql0="drop table if exists books";
        dbl.execSQL(sql0);
        String sql1="create  table if not exists books(id integer primary key autoincrement,bookid text,bookname text,lookchapterid text,lookchapter text,lastchapterid text,lastchapter text,updatetime text,imgurl text,sort int)";
        dbl.execSQL(sql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    private SQLDBHelper(Context context){
        super(context,DB_Name,null,DB_V);
    }
    private SQLDBHelper(Context context, int v){
        super(context,DB_Name,null,v);
    }
    public  static  SQLDBHelper getInstance(Context context,int v){
        if(v>0&&dbHelper==null){
            dbHelper=new SQLDBHelper(context,v);
        }else if(dbHelper==null){
            dbHelper=new SQLDBHelper(context);
        }
        return dbHelper;
    }

    public SQLiteDatabase openLink(){
        if(db==null||!db.isOpen()){
            db=dbHelper.getReadableDatabase();
        }
        return db;
    }
    //查询数据
    public AllClass.Books SelectBooksData(String where, SQLiteDatabase dbr){
        try {
            AllClass.Books books=new AllClass.Books(0,null,null,null,null,null,null,null,null,null);
            Cursor cursor= dbr.query("books",null,where,null,null,null,null);
            if(cursor.getCount()==1){
                cursor.moveToFirst();
                books.id=cursor.getInt(0);
                books.bookid=cursor.getString(1);
                books.bookname=cursor.getString(2);
                books.lookchapterid=cursor.getString(3);
                books.lookchapter=cursor.getString(4);
                books.lastchapterid=cursor.getString(5);
                books.lastchapter=cursor.getString(6);
                books.updatetime=cursor.getString(7);
                books.imgurl=cursor.getString(8);
                books.sort=cursor.getString(9);
            }
            return  books;
        }catch (Exception ex){

        }
        return  null;
    }
    //查询数据
    public List<AllClass.Books> SelectBooksListData(String where, SQLiteDatabase dbr){
        try {
            List<AllClass.Books> list=new ArrayList<>();
            Cursor cursor= dbr.query("books",null,where,null,null,null,"sort");
            if(cursor.getCount()>0){
                while (cursor.moveToNext()) {
                    AllClass.Books books=new AllClass.Books(0,null,null,null,null,null,null,null,null,null);
                    books.id=cursor.getInt(0);
                    books.bookid=cursor.getString(1);
                    books.bookname=cursor.getString(2);
                    books.lookchapterid=cursor.getString(3);
                    books.lookchapter=cursor.getString(4);
                    books.lastchapterid=cursor.getString(5);
                    books.lastchapter=cursor.getString(6);
                    books.updatetime=cursor.getString(7);
                    books.imgurl=cursor.getString(8);
                    books.sort=cursor.getString(9);
                    list.add(books);
                }
            }
            return  list;
        }catch (Exception ex){

        }
        return  null;
    }
    //插入数据
    public void InsertBooksData(SQLiteDatabase dbw, AllClass.Books books){
        try{
            ContentValues cvalue=new ContentValues();
//            cvalue.put("id",books.id);
            cvalue.put("bookid",books.bookid);
            cvalue.put("bookname",books.bookname);
            cvalue.put("lookchapterid",books.lookchapterid);
            cvalue.put("lookchapter",books.lookchapter);
            cvalue.put("lastchapterid",books.lastchapterid);
            cvalue.put("lastchapter",books.lastchapter);
            cvalue.put("updatetime",books.updatetime);
            cvalue.put("imgurl",books.imgurl);
            cvalue.put("sort",books.sort);
            dbw.insert("books",null,cvalue);
        }catch (Exception ex){

        }
    }
    //修改数据
    public void UpdateBooksData(SQLiteDatabase dbw, AllClass.Books books){
        try{
            String where="bookid='"+books.bookid+"'";
            ContentValues cvalue=new ContentValues();
            cvalue.put("id",books.id);
            cvalue.put("bookid",books.bookid);
            cvalue.put("bookname",books.bookname);
            cvalue.put("lookchapterid",books.lookchapterid);
            cvalue.put("lookchapter",books.lookchapter);
            cvalue.put("lastchapterid",books.lastchapterid);
            cvalue.put("lastchapter",books.lastchapter);
            cvalue.put("updatetime",books.updatetime);
            cvalue.put("imgurl",books.imgurl);
            cvalue.put("sort",books.sort);
            dbw.update("books",cvalue,where,null);
        }catch (Exception ex){

        }
    }
    //删除数据
    public void DeleteBooksData(int id,SQLiteDatabase dbw){
        try{
            String where="id="+id;
            dbw.delete("books",where,null);
        }catch (Exception ex){

        }
    }
    //执行sql
    public void execSql(String sql,SQLiteDatabase dbw){
        try{
            dbw.execSQL(sql);
        }catch (Exception ex){

        }
    }
}
