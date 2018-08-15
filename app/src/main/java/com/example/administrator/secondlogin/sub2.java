package com.example.administrator.secondlogin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class sub2 extends LinearLayout  {

    private LinearLayout mLinear;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;

    //setContentView(R.layout.activity_credit);
    static View rootView;
    public sub2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public sub2(Context context, View v) {
        super(context);
        LayoutInflater layoutInFlater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        rootView = layoutInFlater.inflate(R.layout.activity_comment__1, null);

      //  tv_sum = rootView.findViewById(R.id.tv_sum);
        init(context);
    }
    @SuppressLint("ResourceType")
    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sub2,this,true);
       // mLinear = (LinearLayout) findViewById(R.id.child_linear);
       // db = FirebaseFirestore.getInstance();
       // mAuth = FirebaseAuth.getInstance();
       // FirebaseUser currentUser = mAuth.getCurrentUser();









    }


}
