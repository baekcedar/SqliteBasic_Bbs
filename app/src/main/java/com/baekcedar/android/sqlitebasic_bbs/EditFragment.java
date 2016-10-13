package com.baekcedar.android.sqlitebasic_bbs;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class EditFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int REQUEST_CODE = 0;

    TextView textNo;
    ImageView imageView;
    EditText name, editTextTitle, editTextContents;
    Button cancelBtn,saveBtn,imageBtn;

    private String mParam1;
    private String mParam2;
    private static final int REQ_CODE_IMAGE = 99;
    private OnFragmentInteractionListener mListener;

    public EditFragment() {
        // Required empty public constructor
    }



    @Override // 이미지 결과 출력
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == REQ_CODE_IMAGE && data != null){
            Uri imageUri = data.getData();    // Intent에서 받아온 갤러리 URI
            String selections[] = { MediaStore.Images.Media.DATA}; // 실제 이미지 패스 데이터
            if( Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            }
            else {
                checkPermissions();
            }
            Cursor cursor = getContext().getContentResolver().query(imageUri, selections, null,null,null);

            if(cursor.moveToNext()){
                String imagePath = cursor.getString(0);
                Log.i("imagePath : ", imagePath);
                name.setText(imagePath);
                // 사이즈 지정 옵션
                // 스케일 지정 옵션
                BitmapFactory.Options options = new BitmapFactory.Options();

                imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            }
        }
    }

    // TODO: Rename and change types and number of parameters
    public static EditFragment newInstance(String param1, String param2) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit, container, false);

        textNo          = (TextView) view.findViewById(R.id.textNo);
        name            = (EditText) view.findViewById(R.id.textName);
        editTextTitle   = (EditText) view.findViewById(R.id.editTextTitle);
        editTextContents= (EditText) view.findViewById(R.id.editTextContents);
        imageView       = (ImageView) view.findViewById(R.id.imageView);
        imageBtn = (Button) view.findViewById(R.id.imageBtn);
        cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
        saveBtn = (Button) view.findViewById(R.id.saveBtn);



        // 이미지 버튼 클릭시
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 이미지를 호출하는 action intent
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                //결과값을 넘겨받기 위해 호출
                startActivityForResult(intent, REQ_CODE_IMAGE);



            }
         });
        // cancel 버튼 클릭시
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyBoardOff();
                mListener.listRefresh(10);
                mListener.onFragmentInteraction(MainActivity.LIST_PAGE);

            }
        });
        // Save 버튼 클릭시
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data data = new Data();
                data.contents = editTextContents.getText().toString();
                data.title = editTextTitle.getText().toString();
                data.name = name.getText().toString();



                //필드 공백 체크
                if(data.contents.equals("")){
                    Toast.makeText(getActivity(),"contents 를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if(data.title.equals("")){
                    Toast.makeText(getActivity(),"title 을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if(data.name.equals("")){
                    Toast.makeText(getActivity(),"name 을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(mListener.getUpdateFlag()==1) { // 업데이트
                        data.no = Integer.parseInt(textNo.getText().toString());
                        mListener.setUpdate(data);
                    }else if (mListener.getUpdateFlag()==0){ // 일반 쓰기
                        mListener.setInsert(data);
                    }
                    keyBoardOff();
                    mListener.listRefresh(10);
                    mListener.onFragmentInteraction(MainActivity.LIST_PAGE);
                }




            }
        });


        return view ;
    }
    public void setUpdateMode(Data data){
        editTextTitle.setText(data.title);
        editTextContents.setText(data.contents);
        name.setText(data.name);
        textNo.setText(Integer.toString(data.no));
    }
    public void setWirteMode(){
        editTextTitle.setText("");
        editTextContents.setText("");
        name.setText("");
        textNo.setText("");

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 부모 activity에 interface가 구현되지 않았으면 Exception을 발생시켜 강제로 App을 종료시킨다
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    protected void keyBoardOff() {  // 키보드 내리기
        View view = getActivity().getCurrentFocus();
        InputMethodManager mgr = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }
    // 권한 허용  시스템 팝업창
    @TargetApi(Build.VERSION_CODES.M)
    private Boolean checkPermissions(){
        if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // 쓰기 권한이 없으면 로직 처리
            // 중간에 권한 내용에 대한 알림을 처리하는 함수
            // shouldShowRequestPermissionRationale();
            //TODO 권한처리 리턴값으로 실행 할지 결정하는 로직 처리
            String permissionArray[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissionArray, REQUEST_CODE);
        }else{
            return true;

        }
        return false;
    }

    // Generate -> override Methods -> onRequestPermissionsResult
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case  REQUEST_CODE :
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                break;
        }
    }
}
