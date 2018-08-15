package com.example.administrator.secondlogin;

import android.content.Context;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Batch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.zip.Inflater;

public class creditActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    FirebaseFirestore db;
    int i =0;
    int sum_int=0;
    String sum_string="";

    JSONArray jArray = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ;
        setContentView(R.layout.activity_credit);
        final LinearLayout con = (LinearLayout)findViewById(R.id.con);
        final View v = getLayoutInflater().inflate(R.layout.activity_credit, null);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        db.collection("user_cart").document(mAuth.getUid()).collection(mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                System.out.println(document.getId() + " => " + document.getData());

// JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
// jArray.put(document.getData());
//화면표시
                                sub n_layout = new sub(getApplicationContext(),v);

                                con.addView(n_layout);

// System.out.println("jArray = "+jArray.getString(i) + " ==== "+(String)document.get("P_PRICE"));
                                TextView tv_price= con.getChildAt(i).findViewById(R.id.tv_P_PRICE);
                                TextView tv_pname= con.getChildAt(i).findViewById(R.id.tv_P_NAME);
                                TextView tv_quntity= con.getChildAt(i).findViewById(R.id.tv_P_QUNTITY);
                                TextView tv_quntity2= con.getChildAt(i).findViewById(R.id.tv_P_QUNTITY2);
                                System.out.println("fisrt1114"+ document.get("P_QUNTITY"));

//System.out.println("id is = "+con.getChildAt(i));
                                tv_price.setText((String)document.get("P_PRICE"));
                                tv_pname.setText((String)document.get("P_NAME"));
                                tv_quntity.setText((String)document.get("P_ID"));
                                tv_quntity2.setText((String)document.get("P_QUNTITY"));
                                System.out.println("fisrt1115");
                                int p_price= Integer.parseInt((String)document.get("P_PRICE"));
                                int p_quntity = Integer.parseInt((String)document.get("P_QUNTITY"));
                                int multiply = p_price*p_quntity;
// sum_string = (String)document.get("P_PRICE");
                                sum_int += multiply;
                                TextView tv_sum = findViewById(R.id.tv_sum);
                                tv_sum.setText(""+sum_int);
// jArray.get
                                i++;
                            }
                        } else {
// Log.d(TAG, "Error getting documents: ", task.getException());
                            System.out.println("yyyyyyyyyyy");
                        }
                    }
                });
    }
}
