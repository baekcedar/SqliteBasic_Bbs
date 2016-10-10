package com.baekcedar.android.sqlitebasic_bbs;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener{
    public ArrayList<Data> datas = new ArrayList<>();
    Dbconnect dbClass;
    public final static int LIST_PAGE   = 0;
    public final static int EDIT_PAGE   = 1;
    public final static int WRITE_PAGE  = 2;

    EditFragment editf; // 쓰기 페이지
    ListFragment listf; // 목록 페이지
    ViewPager pager;
    int updateFlag = 0;

    SQLiteDatabase db;
    final String dbFileName= "bbs.sqlite";
    private int position=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbClass = new Dbconnect(getApplicationContext(),dbFileName);

        dbClass.init();
        db = dbClass.openDb();
        datas = listPrint();
        if (db == null) {
            Log.i("TEST", "DB NULL");
            System.exit(0);
        }else {
            Log.i("TEST", db.toString());
        }
        listf = ListFragment.newInstance("","");
        editf = EditFragment.newInstance("","");
        pager = (ViewPager) findViewById(R.id.pager);
        CusotmAdapter adapter = new CusotmAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);







    }

    public ArrayList<Data> listPrint() {
        ArrayList<Data> datas = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbClass.openDb();
            if (db != null) {
                // 기본쿼리
                String query = "select * from bbs";
                cursor = db.rawQuery(query, null);
                while (cursor.moveToNext()) {
                    Data data = new Data();
                    int idx = cursor.getColumnIndex("no"); // 컬럼명에 해당하는 순서를 가져온다
                    data.no = cursor.getInt(idx); // 순서로 컬럼을 가져온다
                    idx = cursor.getColumnIndex("name");
                    data.name = cursor.getString(idx);
                    idx = cursor.getColumnIndex("title");
                    data.title = cursor.getString(idx);
                    idx = cursor.getColumnIndex("contents");
                    data.contents = cursor.getString(idx);
                    idx = cursor.getColumnIndex("ndate");
                    data.ndate = cursor.getString(idx);
                    datas.add(data);
                    Log.i("TEST DB",data.ndate );
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
    @Override
    public void onFragmentInteraction(int ActionFlag) {

        switch (ActionFlag) {
            case LIST_PAGE :    updateFlag = 0;
                datas = listPrint();
                listf.onDestroyView();
                pager.setCurrentItem(LIST_PAGE); break;
            case EDIT_PAGE :    updateFlag = 1;

                pager.setCurrentItem(EDIT_PAGE); break;
            case WRITE_PAGE :   updateFlag = 0;
                editf.setWirteMode();
                pager.setCurrentItem(EDIT_PAGE); break;
        }


    }
    @Override
    public void setBox(int position){
        editf.setUpdateMode(position);
    }
    @Override
    public int getUpdateFlag() {
        return updateFlag;
    }
    @Override
    public void setInsert(Data data) {
        dbClass.dbInsert(data);
    }
    @Override
    public void setUpdate(Data data) {
        dbClass.dbUpdate(data);
    }
    @Override
    public ArrayList<Data> getDatas() {
        datas = listPrint();
        return datas;
    }
    @Override
    public Data getData(int position){
        return datas.get(position);
    }
    @Override
    public void setPosition(int position){
        this.position = position;
    }


    //CusotmAdapter class ( Fragment pager adapter )
    class CusotmAdapter extends FragmentStatePagerAdapter{


        public CusotmAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0 : fragment = listf; break;
                case 1 : fragment = editf; break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

}


