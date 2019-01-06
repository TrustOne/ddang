package com.example.administrator.secondlogin;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Console;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class fgm_cart extends Fragment implements View.OnClickListener{
    int minteger1 = 1;
    int minteger2 = 1;
    int minteger3 = 1;
    int minteger4 = 1;
    TextView mTextView;
    ImageView joinButton;
    Button decrease1,decrease2,decrease3,decrease4,increase1,increase2,increase3,increase4;
    Button add1,add2,add3,add4,btn_comment2,button6;
    private FirebaseAuth mAuth;
    private boolean islogin = false;
    FirebaseFirestore db;
    View root;

    //20181113
//    private long pressedTime = 0;
//
//    public interface OnBackPressedListener {
//        public void onBack();
//    }
//    public void setOnBackPressedListener(OnBackPressedListener listener) {
//        mBackListener = listener;
//    }
//    private OnBackPressedListener mBackListener;
//
//    @Override
//    public void onBackPressed() {
//
//        // 다른 Fragment 에서 리스너를 설정했을 때 처리됩니다.
//        if(mBackListener != null) {
//            mBackListener.onBack();
//            Log.e("!!!", "Listener is not null");
//            // 리스너가 설정되지 않은 상태(예를들어 메인Fragment)라면
//            // 뒤로가기 버튼을 연속적으로 두번 눌렀을 때 앱이 종료됩니다.
//        } else {
//            Log.e("!!!", "Listener is null");
//            if ( pressedTime == 0 ) {
////                Snackbar.make(findViewById(R.id.main_layout),
////                        " 한 번 더 누르면 종료됩니다." , Snackbar.LENGTH_LONG).show();
//                pressedTime = System.currentTimeMillis();
//            }
//            else {
//                int seconds = (int) (System.currentTimeMillis() - pressedTime);
//
//                if ( seconds > 2000 ) {
////                    Snackbar.make(findViewById(R.id.main_layout),
////                            " 한 번 더 누르면 종료됩니다." , Snackbar.LENGTH_LONG).show();
//                    pressedTime = 0 ;
//                }
//                else {
//                    super.onBackPressed();
//                    Log.e("!!!", "onBackPressed : finish, killProcess");
//                    finish();
//                    android.os.Process.killProcess(android.os.Process.myPid());
//                }
//            }
//        }
//    }
    ////////

    public fgm_cart() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_fgm_cart, container, false);



        joinButton = (ImageView) root.findViewById(R.id.img_islogin);
        joinButton.setOnClickListener(this);
        increase1 = (Button) root.findViewById(R.id.increase1);
        increase1.setOnClickListener(this);
        increase2 = (Button) root.findViewById(R.id.increase2);
        increase2.setOnClickListener(this);
        increase3 = (Button) root.findViewById(R.id.increase3);
        increase3.setOnClickListener(this);
        increase4 = (Button) root.findViewById(R.id.increase4);
        increase4.setOnClickListener(this);
        decrease1 = (Button) root.findViewById(R.id.decrease1);
        decrease1.setOnClickListener(this);
        decrease2 = (Button) root.findViewById(R.id.decrease2);
        decrease2.setOnClickListener(this);
        decrease3 = (Button) root.findViewById(R.id.decrease3);
        decrease3.setOnClickListener(this);
        decrease4 = (Button) root.findViewById(R.id.decrease4);
        decrease4.setOnClickListener(this);
        add1 = (Button) root.findViewById(R.id.button10);
        add1.setOnClickListener(this);
        add2 = (Button) root.findViewById(R.id.button9);
        add2.setOnClickListener(this);
        add3 = (Button) root.findViewById(R.id.button8);
        add3.setOnClickListener(this);
        add4 = (Button) root.findViewById(R.id.button7);
        add4.setOnClickListener(this);
        btn_comment2 = (Button) root.findViewById(R.id.btn_comment2);
        btn_comment2.setOnClickListener(this);
        button6 = (Button) root.findViewById(R.id.button6);
        button6.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        AssetManager am = getResources().getAssets();
        InputStream is = null;

        mTextView = (TextView)root.findViewById(R.id.emailtextView);
        try { // 애셋 폴더에 저장된 field.png 열기.
            is = am.open("exit.png") ;
// 입력스트림 is를 통해 field.png 을 Bitmap 객체로 변환.
            Bitmap bm = BitmapFactory.decodeStream(is) ;
// 만들어진 Bitmap 객체를 이미지뷰에 표시.
            ImageView imageView = (ImageView) root.findViewById(R.id.img_islogin) ;
            imageView.setImageBitmap(bm) ;
            is.close() ; }
        catch (Exception e) { e.printStackTrace(); }
        return root;
    }




    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser user) {
// hideProgressDialog();


        if (user != null) {

            String email = user.getEmail();
            if(TextUtils.isEmpty(email)) {
                mTextView.setText(user.getDisplayName() + "님");
            }
            else {
                mTextView.setText(user.getEmail() + "님");
            }
            islogin = true;
        } else {
            islogin=false;
            mTextView.setText("로그인");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_islogin:
                login();
                break;
            case R.id.decrease1:
                decreaseInteger1();
                break;
            case R.id.decrease2:
                decreaseInteger2();
                break;
            case R.id.decrease3:
                decreaseInteger3();
                break;
            case R.id.decrease4:
                decreaseInteger4();
                break;
            case R.id.increase1:
                increaseInteger1();
                break;
            case R.id.increase2:
                increaseInteger2();
                break;
            case R.id.increase3:
                increaseInteger3();
                break;
            case R.id.increase4:
                increaseInteger4();
                break;
            case R.id.button10:
                add1();
                break;
            case R.id.button9:
                add2();
                break;
            case R.id.button8:
                add3();
                break;
            case R.id.button7:
                add4();
                break;
            case R.id.btn_comment2:
                Comment2_click();
                break;
            case R.id.button6:
                Comment1_click();
                break;

    }


    }
    private void login() {
        if (islogin == false) {

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {

            AlertDialog.Builder oDialog = new AlertDialog.Builder(getActivity(),
                    android.R.style.Theme_DeviceDefault_Light_Dialog);

            oDialog.setMessage("로그아웃 하시겠습니까?");
            oDialog.setTitle("일반 Dialog");

            oDialog.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            oDialog.setNeutralButton("예", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    LoginManager.getInstance().logOut();

                    updateUI(null);
                }
            });
            oDialog.setCancelable(false);
            oDialog.show();

        }
    }
//20181111_추가
@RequiresApi(api = Build.VERSION_CODES.N)
public void add2() {
    final FirebaseUser currentUser = mAuth.getCurrentUser();
    db = FirebaseFirestore.getInstance();
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.FFF");
    final String getTime = sdf.format(date);

    db.collection("user_cart").document(currentUser.getUid()).collection(currentUser.getUid())
            .whereEqualTo("P_ID","117")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if(task.getResult().isEmpty()) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("TIME", getTime);
                            data.put("P_ID", "117");
                            data.put("P_PRICE", "16000");
                            data.put("P_NAME", "스와브로스키 사파이어 넥클리스 보급형");
                            data.put("P_QUNTITY", String.valueOf(minteger2));
                            db.collection("user_cart").document(currentUser.getUid()).collection(currentUser.getUid())
                                    .add(data);
                        }
                        else {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().update("P_QUNTITY", String.valueOf(minteger2 + Integer.parseInt((String) document.get("P_QUNTITY"))));
                            }
                        }
                    } else {

                    }
                }
            });


}

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void add3() {
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.FFF");
        final String getTime = sdf.format(date);

        db.collection("user_cart").document(currentUser.getUid()).collection(currentUser.getUid())
                .whereEqualTo("P_ID","113")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if(task.getResult().isEmpty()) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("TIME", getTime);
                                data.put("P_ID", "113");
                                data.put("P_PRICE", "12000");
                                data.put("P_NAME", "포카리스웨트 355ML*24");
                                data.put("P_QUNTITY", String.valueOf(minteger3));
                                db.collection("user_cart").document(currentUser.getUid()).collection(currentUser.getUid())
                                        .add(data);
                            }
                            else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().update("P_QUNTITY", String.valueOf(minteger3 + Integer.parseInt((String) document.get("P_QUNTITY"))));
                                }
                            }
                        } else {
                        }
                    }
                });

// Later...
// newCityRef.set(data);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void add4() {
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.FFF");
        final String getTime = sdf.format(date);

        db.collection("user_cart").document(currentUser.getUid()).collection(currentUser.getUid())
                .whereEqualTo("P_ID","136")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if(task.getResult().isEmpty()) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("TIME", getTime);
                                data.put("P_ID", "136");
                                data.put("P_PRICE", "12000");
                                data.put("P_NAME", "설화수 로션");
                                data.put("P_QUNTITY", String.valueOf(minteger4));
                                db.collection("user_cart").document(currentUser.getUid()).collection(currentUser.getUid())
                                        .add(data);
                            }
                            else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().update("P_QUNTITY", String.valueOf(minteger4 + Integer.parseInt((String) document.get("P_QUNTITY"))));
                                }
                            }
                        } else {
                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void add1() {
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.FFF");
        final String getTime = sdf.format(date);

        db.collection("user_cart").document(currentUser.getUid()).collection(currentUser.getUid())
                .whereEqualTo("P_ID","114")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if(task.getResult().isEmpty())
                            {
                                Map<String, Object> data = new HashMap<>();
                                data.put("TIME", getTime);
                                data.put("P_ID", "114");
                                data.put("P_PRICE", "12000");
                                data.put("P_NAME", "정관장 홈삼");
                                data.put("P_QUNTITY", String.valueOf(minteger1));

                                db.collection("user_cart").document(currentUser.getUid()).collection(currentUser.getUid())
                                        .add(data);
                            }
                            else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().update("P_QUNTITY", String.valueOf(minteger1 + Integer.parseInt((String) document.get("P_QUNTITY"))));
                                }
                            }
                        } else {
                        }
                    }
                });
    }


    public void credit(View view) {
        Intent intent = new Intent(getActivity(), creditActivity.class);
// intent.putExtra("key", listContent[position]);

        startActivity(intent);

    }
    ///////
    public void increaseInteger1() {
        minteger1 = minteger1 + 1;
        display1(minteger1);

    }public void decreaseInteger1() {
        if(minteger1>=2)minteger1 = minteger1 - 1;
        display1(minteger1);
    }

    private void display1(int number) {
        TextView displayInteger = (TextView) root.findViewById(
                R.id.integer_number1);
        displayInteger.setText("" + number);
    }

    public void increaseInteger2() {
        minteger2 = minteger2 + 1;
        display2(minteger2);

    }public void decreaseInteger2() {
        if(minteger2>=2)minteger2 = minteger2 - 1;
        display2(minteger2);
    }

    private void display2(int number) {
        TextView displayInteger = (TextView) root.findViewById(
                R.id.integer_number2);
        displayInteger.setText("" + number);
    }

    public void increaseInteger3() {
        minteger3 = minteger3 + 1;
        display3(minteger3);

    }public void decreaseInteger3() {
        if(minteger3>=2)minteger3 = minteger3 - 1;

        display3(minteger3);
    }

    private void display3(int number) {
        TextView displayInteger = (TextView) root.findViewById(
                R.id.integer_number3);

        displayInteger.setText("" + number);
    }

    public void increaseInteger4() {
        minteger4 = minteger4 + 1;
        display4(minteger4);

    }public void decreaseInteger4() {
        if(minteger4>=2)minteger4 = minteger4 - 1;
        display4(minteger4);
    }

    private void display4(int number) {
        TextView displayInteger = (TextView) root.findViewById(
                R.id.integer_number4);
        displayInteger.setText("" + number);
    }

    public void Comment1_click() {
       /* Intent intent = new Intent(getActivity(), Comment_Activity_1.class);
        startActivity(intent);*/
       fgm_credit fgm_credit = new fgm_credit();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fgm_main_con, fgm_credit);
        fragmentTransaction.addToBackStack(null);
//Transaction 작업 후 마지막에 commit()를 호출 후 적용
        fragmentTransaction.commit();
    }
    ///////
    public void Comment2_click() {

        fgm_comment fgm_comment = new fgm_comment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //위에서 가져온 Transaction을 이용해 밑에 3가지 기능 가능
//add() : Fragment 추가
//remove() : Fragment 제거
//replace() : Fragment 변경

        fragmentTransaction.replace(R.id.fgm_main_con, fgm_comment);
        fragmentTransaction.addToBackStack(null);
//Transaction 작업 후 마지막에 commit()를 호출 후 적용
        fragmentTransaction.commit();

    }





}
