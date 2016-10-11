package com.baekcedar.android.sqlitebasic_bbs;

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



    public final static int LIST_PAGE   = 0;
    public final static int EDIT_PAGE   = 1;
    public final static int WRITE_PAGE  = 2;
    ArrayList<Data> datas;
    EditFragment editf; // 쓰기 페이지
    ListFragment listf; // 목록 페이지
    ViewPager pager;
    int updateFlag = 0;

    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datas = Datas.newInstance();

        Dbconnect.init(this);
        db = Dbconnect.openDb(this);
        datas = Dbconnect.listPrint(this);

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

    
    @Override
    public void onFragmentInteraction(int actionFlag) {

        switch (actionFlag) {
            case LIST_PAGE :    updateFlag = 0;
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
        Data data = datas.get(position);
        editf.setUpdateMode(Dbconnect.detailSelect(this, data.no));
    }

    @Override  // 리스트 갱신
    public void listRefresh() {
        datas = null;
        editf.setWirteMode();
        datas = Dbconnect.listPrint(this);
        listf.adapter.listRefresh(getDatas());


    }

    @Override
    public int getUpdateFlag() {
        return updateFlag;
    }
    @Override
    public void setInsert(Data data) {
        Dbconnect.dbInsert(this,data);
    }
    @Override
    public void setUpdate(Data data) {
        Dbconnect.dbUpdate(this,data);
    }
    @Override
    public void setDelete(int position) {
        Dbconnect.dbDelete(this, datas.get(position).no);
    }
    @Override
    public ArrayList<Data> getDatas() {
        datas = Dbconnect.listPrint(this);
        return datas;
    }
    @Override
    public Data getData(int position){
        return datas.get(position);
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


