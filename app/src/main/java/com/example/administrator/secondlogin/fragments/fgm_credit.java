package com.example.administrator.secondlogin.fragments;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.administrator.secondlogin.R;
import com.example.administrator.secondlogin.activities.MainActivity;
import com.example.administrator.secondlogin.sub;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class fgm_credit extends Fragment {

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    int i =0;
    int sum_int=0;
    FirebaseStorage storage;
    StorageReference storageRef;
    public RequestManager mGlideRequestManager;
    LinearLayout con;
    View root;
    View v;
    TextView tv5;
    BootstrapButton btn_pay;

    public fgm_credit() {
        // Required empty public constructor
        i = 0;
        sum_int = 0;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root= inflater.inflate(R.layout.fragment_credit, container, false);
        ( (MainActivity)getActivity()).updateToolbarTitle("pay");
        ////
        TypefaceProvider.registerDefaultIconSets();
        con = (LinearLayout)root.findViewById(R.id.con);
         v = getActivity().getLayoutInflater().inflate(R.layout.activity_credit, null);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mGlideRequestManager = Glide.with(this);
        tv5 = (TextView)root.findViewById(R.id.textView5);
        tv5.setVisibility(View.INVISIBLE);
        btn_pay = (BootstrapButton)root.findViewById(R.id.btn_pay);
        btn_pay.setVisibility(View.INVISIBLE);
        return root;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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
                                TextView tv_quantity= con.getChildAt(i).findViewById(R.id.tv_P_QUNTITY);
                                TextView tv_quntity2= con.getChildAt(i).findViewById(R.id.tv_P_COUNT);
                                ImageView imgview2 = con.getChildAt(i).findViewById(R.id.imageView2);
                                //imgview2.setImageDrawable(R.drawable.blue_camera);

                                StorageReference pathReference = storageRef.child("/user_store/Trustone_store/product/"+(String)document.get("P_ID"));
                                //StorageReference pathReference = storageRef.child("/user_store/Trustone_store/product/113");


                                tv_price.setText((String)document.get("P_PRICE"));
                                tv_pname.setText((String)document.get("P_NAME"));
                                tv_quantity.setText((String)document.get("P_ID"));
                                tv_quntity2.setText((String)document.get("P_QUNTITY"));



                                int p_price= Integer.parseInt((String)document.get("P_PRICE"));
                                int p_quntity = Integer.parseInt((String)document.get("P_QUNTITY"));
                                int multiply = p_price*p_quntity;
                                sum_int += multiply;
                                TextView tv_sum = root.findViewById(R.id.tv_sum);

                                DecimalFormat myFormatter = new DecimalFormat("###,###");
                                String formattedStringPrice = myFormatter.format(sum_int);
                                tv_sum.setText(""+formattedStringPrice+"원");

                                mGlideRequestManager
                                        .using(new FirebaseImageLoader())
                                        .load(pathReference)
                                        .into(imgview2);
                                i++;
                            }
                        btn_pay.setVisibility(View.VISIBLE);
                            tv5.setVisibility(View.VISIBLE);

                        } else {

                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
