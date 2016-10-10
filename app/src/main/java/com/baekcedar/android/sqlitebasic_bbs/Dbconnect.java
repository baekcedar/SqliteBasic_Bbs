package com.baekcedar.android.sqlitebasic_bbs;


import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * Created by HM on 2016-10-10.
 */
public class Dbconnect {
    SQLiteDatabase db;
    String fileName;
    String targetFile;
    Context context;
    Dbconnect(Context context, String fileName){
        this.context = context;
        this.fileName = fileName;
    }
    public void init(){
        Log.i("TEST init",getFullpath(fileName) );
        File file = new File(getFullpath(fileName));
        if(!file.exists()){
            assetToDisk(fileName);
            Log.i("TEST init","assetToDisk ");
        }


    }
    public SQLiteDatabase openDb(){
        return  SQLiteDatabase.openDatabase(getFullpath(fileName),null,0);
    }
    public String getFullpath(String filename){
        return context.getFilesDir().getAbsolutePath() + File.separator + filename;
    }
    public void assetToDisk(String filename){
        // 외부에서 작성된 sqlite db 파일 사용하기
        // 1. assets 에 담아둔 파일을 internal 혹은 external 공간으로 복사한다.

        InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            targetFile = getFullpath(fileName);
            db = openDb();
            AssetManager manager = context.getAssets();
            is = manager.open(filename);
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
    public void dbUpdate(Data data){

        SQLiteDatabase db = null;
        try {
            db = openDb();
            if (db != null) {
                db.execSQL("update bbs " +
                        "set name='"+data.name+"' " +
                        ",title='"+data.title+"'" +
                        ",contents='"+data.contents+"' " +
                        " where no="+data.no);
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

    // 입력
    public void dbInsert(Data data) {
        SQLiteDatabase db = null;
        try {
            db = openDb();
            if (db != null) {
                // 쿼리를 실행해준다. select 문을 제외한 모든 쿼리에 사용
                db.execSQL("insert into bbs(name,title,contents) values('"+data.name+"','"+data.title+"','"+data.contents+"')");
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

}
