package com.example.administrator.secondlogin;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.text.DecimalFormat;

public class creditActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    int i =0;
    int sum_int=0;
    FirebaseStorage storage;
    StorageReference storageRef;
    public RequestManager mGlideRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        TypefaceProvider.registerDefaultIconSets();
        final LinearLayout con = (LinearLayout)findViewById(R.id.con);
        final View v = getLayoutInflater().inflate(R.layout.activity_credit, null);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mGlideRequestManager = Glide.with(this);

        db.collection("user_cart").document(mAuth.getUid()).collection(mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                sub n_layout = new sub(getApplicationContext(),v);
                                con.addView(n_layout);
                                TextView tv_price= con.getChildAt(i).findViewById(R.id.tv_P_PRICE);
                                TextView tv_pname= con.getChildAt(i).findViewById(R.id.tv_P_NAME);
                                TextView tv_quntity= con.getChildAt(i).findViewById(R.id.tv_P_QUNTITY);
                                TextView tv_quntity2= con.getChildAt(i).findViewById(R.id.tv_P_COUNT);
                                ImageView imgview2 = con.getChildAt(i).findViewById(R.id.imageView2);
                          //      imgview2.setImageDrawable(R.drawable.blue_camera);

                                StorageReference pathReference = storageRef.child("/user_store/Trustone_store/product/"+(String)document.get("P_ID"));
                                //StorageReference pathReference = storageRef.child("/user_store/Trustone_store/product/113");


                                tv_price.setText((String)document.get("P_PRICE"));
                                tv_pname.setText((String)document.get("P_NAME"));
                                tv_quntity.setText((String)document.get("P_ID"));
                                tv_quntity2.setText((String)document.get("P_QUNTITY"));

                                System.out.println("imageview load11"+pathReference.getPath()+tv_pname.getId());

                                int p_price= Integer.parseInt((String)document.get("P_PRICE"));
                                int p_quntity = Integer.parseInt((String)document.get("P_QUNTITY"));
                                int multiply = p_price*p_quntity;
                                sum_int += multiply;
                                TextView tv_sum = findViewById(R.id.tv_sum);
                                DecimalFormat myFormatter = new DecimalFormat("###,###");
                                String formattedStringPrice = myFormatter.format(sum_int);
                                tv_sum.setText(""+formattedStringPrice+"원");

                                mGlideRequestManager
                                        .using(new FirebaseImageLoader())
                                        .load(pathReference)
                                        .into(imgview2);


                                i++;
                            }
                        } else {

                        }
                    }
                });
    }
}
