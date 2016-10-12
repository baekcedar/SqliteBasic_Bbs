package com.baekcedar.android.sqlitebasic_bbs;


import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class Dbconnect {
    public static final String DB_NAME = "sqlite.sqlite";

    public static void init(Context context){ // 파일 확인 및 생성
        File file = new File(getFullpath(context));
        if(!file.exists()){
            assetToDisk(context);
        }
    }
    public static SQLiteDatabase openDb(Context context){ //DB 연결
        return  SQLiteDatabase.openDatabase(getFullpath(context),null,0);
    }

    public static String getFullpath(Context context){ // DB 경로
        return context.getFilesDir().getAbsolutePath() + File.separator + DB_NAME;
    }

    public static void assetToDisk(Context context){
        // 외부에서 작성된 sqlite db 파일 사용하기
        // 1. assets 에 담아둔 파일을 internal 혹은 external 공간으로 복사한다.

        InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            String targetFile = getFullpath(context);

            AssetManager manager = context.getAssets();
            is = manager.open(DB_NAME);
            bis = new BufferedInputStream(is);

            // 2. 저장할 위치에 파일이 없으면 생성해둔다.
            File file = new File(targetFile);
            if(!file.exists()){
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            // 읽어올 데이터를 담아줄 변수
            int read = -1; // 모두 읽어오면 -1이 리턴
            // 한번에 읽은 버퍼의 크기를 지정
            byte buffer[] = new byte[1024];
            // 더 이상 읽어올 데이터가 없을때까지 buffer 단위로 읽어서 쓴다.
            while((read = bis.read(buffer, 0 ,1024)) != -1){
                bos.write(buffer, 0, read);
            }
            bos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) bos.hashCode();
                if (fos != null) fos.hashCode();
                if (bis != null) bis.hashCode();
                if (is != null) is.hashCode();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //수정
    public static void dbUpdate(Context context, Data data){

        SQLiteDatabase db = null;
        try {
            db = openDb(context);
            if (db != null) {
                db.execSQL("update bbs3 " +
                        "set name='"+data.name+"' " +
                        ",title='"+data.title+"'" +
                        ",contents='"+data.contents+"' " +
                        " where no="+data.no);
            }
            Toast.makeText(context,"Update Successful",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (db != null) db.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    // 입력
    public static void dbInsert(Context context, Data data) {
        SQLiteDatabase db = null;
        try {
            db = openDb(context);
            if (db != null) {
                // 쿼리를 실행해준다. select 문을 제외한 모든 쿼리에 사용
                db.execSQL("insert into bbs3(name,title,contents) values('" +
                        data.name +
                        "','" + data.title +
                        "','" + data.contents + "')");
                Toast.makeText(context,"Insert Successful",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (db != null) db.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public static void dbDelete(Context context, int no){
        SQLiteDatabase db = null;
        try {
            db = openDb(context);
            if (db != null) {
                // 쿼리를 실행해준다. select 문을 제외한 모든 쿼리에 사용
                db.execSQL("delete from bbs3 where no ='"+no+"'");

                Toast.makeText(context,"Delete Successful",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (db != null) db.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<Data> listPrint(Context context,int listCount) {
        ArrayList<Data> datas = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openDb(context);
            if (db != null) {
                // 기본쿼리

                String query = "select no,title,name,ndate from bbs3 order by no desc limit "+listCount;
                cursor = db.rawQuery(query, null);
                while (cursor.moveToNext()) {
                    Data data = new Data();
                    int idx = cursor.getColumnIndex("no");
                    data.no = cursor.getInt(idx);
                    idx = cursor.getColumnIndex("name");
                    data.name = cursor.getString(idx);
                    idx = cursor.getColumnIndex("title");
                    data.title = cursor.getString(idx);
                    idx = cursor.getColumnIndex("ndate");
                    data.ndate = cursor.getString(idx);
                    datas.add(data);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (cursor != null) cursor.close();
                if (db != null) db.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return datas;
    }
    public static ArrayList<Data> listPrintScroll(Context context,int listCount) {
        ArrayList<Data> datas = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openDb(context);
            if (db != null) {
                // 기본쿼리
                String query = "select no,title,name,ndate from bbs3 order by no desc limit "+listCount+", "+10;
                cursor = db.rawQuery(query, null);
                while (cursor.moveToNext()) {
                    Data data = new Data();
                    int idx = cursor.getColumnIndex("no");
                    data.no = cursor.getInt(idx);
                    idx = cursor.getColumnIndex("name");
                    data.name = cursor.getString(idx);
                    idx = cursor.getColumnIndex("title");
                    data.title = cursor.getString(idx);
                    idx = cursor.getColumnIndex("ndate");
                    data.ndate = cursor.getString(idx);
                    datas.add(data);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (cursor != null) cursor.close();
                if (db != null) db.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return datas;
    }
    public static ArrayList<Data> SelectAllbyWord(Context context, int listCount, String word) {
        ArrayList<Data> datas = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openDb(context);
            String where = "";
            if(!"".equals(word)){
                where = " where title like '%"+word+"%' ";
            }



            if (db != null) {
                // 기본쿼리
                String query = "select no,title,name,ndate from bbs3"+where+"order by no desc limit "+listCount;
                cursor = db.rawQuery(query, null);
                while (cursor.moveToNext()) {
                    Data data = new Data();
                    int idx = cursor.getColumnIndex("no");
                    data.no = cursor.getInt(idx);
                    idx = cursor.getColumnIndex("name");
                    data.name = cursor.getString(idx);
                    idx = cursor.getColumnIndex("title");
                    data.title = cursor.getString(idx);
                    idx = cursor.getColumnIndex("ndate");
                    data.ndate = cursor.getString(idx);
                    datas.add(data);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (cursor != null) cursor.close();
                if (db != null) db.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return datas;
    }
    // data.no 값을 받아 해당 레코드 select
    public static Data detailSelect(Context context, int no) {
        // 받은 no 값으로 해당 레코드만 select 가져오기
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Data data = new Data();
        try {
            db = openDb(context);
            if (db != null) {
                // 기본쿼리
                Log.i("TEST", no+"");
                String query = "select * from bbs3 where no="+no;

                cursor = db.rawQuery(query, null);

                if (cursor.moveToNext()) {
                    int idx = cursor.getColumnIndex("no");
                    data.no = cursor.getInt(idx);
                    idx = cursor.getColumnIndex("name");
                    data.name = cursor.getString(idx);
                    idx = cursor.getColumnIndex("title");
                    data.title = cursor.getString(idx);
                    idx = cursor.getColumnIndex("contents");
                    data.contents = cursor.getString(idx);
                    idx = cursor.getColumnIndex("ndate");
                    data.ndate = cursor.getString(idx);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (cursor != null) cursor.close();
                if (db != null) db.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return data;
    }
    public static int selectCount(Context context, String word) {
        // 받은 no 값으로 해당 레코드만 select 가져오기
        SQLiteDatabase db = null;
        Cursor cursor = null;
        int count=0;
        String where = "";
        try {
            db = openDb(context);
            if (db != null) {
                // 레코드 개수 출력
                if(!"".equals(word)){
                     where = " where title like '%"+word+"%' ";
                }
                String query = "select count(*) from bbs3"+ where;

                cursor = db.rawQuery(query, null);

                if (cursor.moveToNext()) {
                    count = cursor.getInt(0);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (cursor != null) cursor.close();
                if (db != null) db.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return count;
    }
}
