package com.example.administrator.secondlogin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class sub extends LinearLayout implements View.OnClickListener {

    private Button mDeleteButton;
    private LinearLayout mLinear;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    int sum_int=0;
    static TextView tv_sum;

    //setContentView(R.layout.activity_credit);
    static View rootView;
    public sub(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public sub(Context context,View v) {
        super(context);
        LayoutInflater layoutInFlater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        rootView = layoutInFlater.inflate(R.layout.activity_credit, null);

        tv_sum = rootView.findViewById(R.id.tv_sum);
        init(context);
    }
    @SuppressLint("ResourceType")
    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sub1,this,true);
        mLinear = (LinearLayout) findViewById(R.id.child_linear);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        mDeleteButton = (Button) findViewById(R.id.btn_cart_delete);




        mDeleteButton.setOnClickListener(this);



    }

    public void onClick(final View v) {
        switch(v.getId()) {
            case R.id.btn_cart_delete :

                int childSize = mLinear.getChildCount();

                TextView pNUM = mLinear.findViewById(R.id.tv_P_QUNTITY);
                final String pnum = (String)pNUM.getText();

                if(childSize != 0) {
                    mLinear.removeAllViews();
                }

                db.collection("user_cart").document(mAuth.getUid()).collection(mAuth.getUid())
                        .whereEqualTo("P_ID",pnum)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        document.getReference().delete();

                                    }
                                } else {

                        //            System.out.println("yyyyyyyyyyy");
                                }
                            }
                        });
                //삭제클릭시 밑에 총액 새로고침하기위해 추가(전체액티비티 다시실행)
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent mintent = new Intent();
                mintent.setClass(getContext(),creditActivity.class);
                mintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getContext().startActivity(mintent);
                break;
        }



    }
}
