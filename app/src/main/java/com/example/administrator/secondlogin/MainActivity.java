package com.example.administrator.secondlogin;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private long pressedTime = 0;

    public interface OnBackPressedListener {
        public void onBack();
    }
    public void setOnBackPressedListener(OnBackPressedListener listener) {
        mBackListener = listener;
    }
    private OnBackPressedListener mBackListener;

    //20181112
    @Override
    public void onBackPressed() {

        if(getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
        else
            super.onBackPressed();
        // 다른 Fragment 에서 리스너를 설정했을 때 처리됩니다.
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
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void cart_click(View view) {
        System.out.println("20181111_11");
        fgm_cart fgm_cart = new fgm_cart();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //위에서 가져온 Transaction을 이용해 밑에 3가지 기능 가능
//add() : Fragment 추가
//remove() : Fragment 제거
//replace() : Fragment 변경
        System.out.println("20181111_111");
        fragmentTransaction.replace(R.id.fgm_main_con, fgm_cart).addToBackStack(null).commit();
        //fragmentTransaction.addToBackStack(null);
//Transaction 작업 후 마지막에 commit()를 호출 후 적용
    //    fragmentTransaction.commit();
        System.out.println("20181111_1111");

    }

}