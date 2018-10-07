package com.example.administrator.secondlogin;


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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

public class CartActivity extends AppCompatActivity {

    int minteger1 = 1;
    int minteger2 = 1;
    int minteger3 = 1;
    int minteger4 = 1;

    private FirebaseAuth mAuth;
    private boolean islogin = false;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        System.out.println("20181007_a");
        mAuth = FirebaseAuth.getInstance();
        AssetManager am = getResources().getAssets() ;
        InputStream is = null ;

        try { // 애셋 폴더에 저장된 field.png 열기.
            is = am.open("exit.png") ;
// 입력스트림 is를 통해 field.png 을 Bitmap 객체로 변환.
            Bitmap bm = BitmapFactory.decodeStream(is) ;
// 만들어진 Bitmap 객체를 이미지뷰에 표시.
            ImageView imageView = (ImageView) findViewById(R.id.img_islogin) ;
            imageView.setImageBitmap(bm) ;
            is.close() ; }
        catch (Exception e) { e.printStackTrace(); }


    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
// Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
// hideProgressDialog();
        if (user != null) {

            String email = user.getEmail();
            TextView mTextView = findViewById(R.id.emailtextView);

            if(TextUtils.isEmpty(email)) {
                mTextView.setText(user.getDisplayName() + "님");

            }
            else {
                mTextView.setText(user.getEmail() + "님");

            }
            islogin = true;

        } else {
            islogin=false;
            TextView mTextView = findViewById(R.id.emailtextView);
            mTextView.setText("로그인");
/*
mStatusTextView.setText(R.string.signed_out);
mDetailTextView.setText(null);

findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);

*/
        }
    }

    public void OnclickM(View view) {
        if(islogin ==false)
        {
            Intent intent = new Intent(CartActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        else{
            AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void add2(View view) {
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
    public void add3(View view) {
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
    public void add4(View view) {
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
    public void add1(View view) {
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
        Intent intent = new Intent(CartActivity.this, creditActivity.class);
// intent.putExtra("key", listContent[position]);

        startActivity(intent);

    }
    ///////
    public void increaseInteger1(View view) {
        minteger1 = minteger1 + 1;
        display1(minteger1);

    }public void decreaseInteger1(View view) {
        minteger1 = minteger1 - 1;
        display1(minteger1);
    }

    private void display1(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.integer_number1);
        displayInteger.setText("" + number);
    }

    public void increaseInteger2(View view) {
        minteger2 = minteger2 + 1;
        display2(minteger2);

    }public void decreaseInteger2(View view) {
        minteger2 = minteger2 - 1;
        display2(minteger2);
    }

    private void display2(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.integer_number2);
        displayInteger.setText("" + number);
    }

    public void increaseInteger3(View view) {
        minteger3 = minteger3 + 1;
        display3(minteger3);

    }public void decreaseInteger3(View view) {
        minteger3 = minteger3 - 1;

        display3(minteger3);
    }

    private void display3(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.integer_number3);

        displayInteger.setText("" + number);
    }

    public void increaseInteger4(View view) {
        minteger4 = minteger4 + 1;
        display4(minteger4);

    }public void decreaseInteger4(View view) {
        minteger4 = minteger4 - 1;
        display4(minteger4);
    }

    private void display4(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.integer_number4);
        displayInteger.setText("" + number);
    }

    public void Comment1_click(View view) {
        Intent intent = new Intent(CartActivity.this, Comment_Activity_1.class);
        startActivity(intent);
    }
///////

}
