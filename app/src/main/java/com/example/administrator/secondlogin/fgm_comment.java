package com.example.administrator.secondlogin;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class fgm_comment extends Fragment implements View.OnClickListener{

    View root;
    private final int GALLERY_CODE=1112;
    private final int MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE = 1113;
    FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    StorageReference storageRef;
    FirebaseStorage storage;
    int i =0;
    public RequestManager mGlideRequestManager;
    private loadinglayout loadinglayout;
    BootstrapButton button3,button2;
    public fgm_comment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_fgm_comment, container, false);
        System.out.println("20181112");
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        System.out.println("20181112_1");
        button3 = (BootstrapButton)root.findViewById(R.id.button3);
        System.out.println("20181112_1");
        button3.setOnClickListener(this);
        button2 = (BootstrapButton)root.findViewById(R.id.button2);
        button2.setOnClickListener(this);

        EditText edittext = (EditText)root.findViewById(R.id.editText);
        edittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.editText) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        TextView tv_name = root.findViewById(R.id.tv_name);
        String email = mAuth.getCurrentUser().getEmail();
        System.out.println("20181112_1");
        if(TextUtils.isEmpty(email)) {

            tv_name.setText(mAuth.getCurrentUser().getDisplayName());
        }
        else {
            tv_name.setText(mAuth.getCurrentUser().getEmail());
        }
        final LinearLayout con = (LinearLayout)root.findViewById(R.id.con);
        final View v = getActivity().getLayoutInflater().inflate(R.layout.activity_comment__1, null);
        mGlideRequestManager = Glide.with(this);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        System.out.println("20181112_1");

        db.collection("user_store").document("TrustOne").collection("Comment").orderBy("TIME", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String date_s = (String)document.get("TIME");
                                SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmmssFFF");
                                Date date = null;
                                try {
                                    date = dt.parse(date_s);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                                sub2 n_layout = new sub2(getActivity().getApplicationContext(),v);
                                con.addView(n_layout);
                                TextView tv_comment= con.getChildAt(i).findViewById(R.id.tv_comment_sub2);
                                ImageView img_comment=con.getChildAt(i).findViewById(R.id.imageView_sub2);
                                TextView tv_name_sub= con.getChildAt(i).findViewById(R.id.tv_name_sub);
                                RatingBar ratingBar_sub = con.getChildAt(i).findViewById(R.id.ratingBar_sub);
                                TextView tv_time_comment= con.getChildAt(i).findViewById(R.id.tv_time_comment);

                                StorageReference pathReference = storageRef.child((String)document.get("PHOTO"));
                                tv_name_sub.setText((String)document.get("ID"));
                                ratingBar_sub.setRating(Float.valueOf((String)document.get("STAR")));
                                tv_comment.setText((String)document.get("COMMENT"));
                                tv_time_comment.setText(dt1.format(date)+"작성");
                                mGlideRequestManager
                                        .using(new FirebaseImageLoader())
                                        .load(pathReference)
                                        .into(img_comment);
                                System.out.println("imageview2" + pathReference.getPath()+img_comment.getTransitionName());
                                i++;
                            }
                        } else {

                        }
                    }
                });


        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button3:
                requestReadExternalStoragePermission();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GALLERY_CODE);
                break;
            case R.id.button2:
                upload();
                break;
        }
    }

    //2018-11-12
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_CODE:
                    sendPicture(data.getData()); //갤러리에서 가져오기
                    break;
                default:
                    break;
            }
        }
    }

    private void sendPicture(Uri imgUri) {

        String imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        ImageView ivImage = (ImageView)root.findViewById(R.id.imageView);
        ivImage.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기
    }
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree) {

// Matrix 객체 생성
        Matrix matrix = new Matrix();
// 회전 각도 셋팅
        matrix.postRotate(degree);
// 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getApplicationContext().getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }
    private void requestReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    //20181112
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void upload() {
        ImageView imageView = (ImageView)root.findViewById(R.id.imageView);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        RatingBar ratingBar = root.findViewById(R.id.ratingBar1);
        float rating = ratingBar.getRating();
        final String s_rating = String.valueOf(rating);
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssFFF");
        final String getTime = sdf.format(date);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        loadinglayout = new loadinglayout(getActivity());
        loadinglayout.show();
        final StorageReference mountainsRef = storageRef.child("user_store/Trustone_store/comment/"+getTime+"_"+currentUser.getUid());

        if(null!=imageView.getDrawable()) {
            imageView.isSelected();
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 15, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...

                    EditText edittext = (EditText)root.findViewById(R.id.editText);
                    //       edittext.setFocusable(false);
                    edittext.clearFocus();
                    String s_edit = edittext.getText().toString();
                    db = FirebaseFirestore.getInstance();

                    Map<String, Object> data = new HashMap<>();
                    data.put("TIME", getTime);
                    data.put("UID", currentUser.getUid());
                    data.put("COMMENT", s_edit);
                    data.put("PHOTO", mountainsRef.getPath());
                    data.put("STAR", s_rating);
                    if(TextUtils.isEmpty(currentUser.getEmail())) {

                        data.put("ID", currentUser.getDisplayName());
                    }
                    else {
                        data.put("ID", currentUser.getEmail());

                    }

                    db.collection("user_store").document("TrustOne").collection("Comment")
                            .add(data)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    //         Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    EditText edittext = (EditText)root.findViewById(R.id.editText);
                                    edittext.setText("");
                                    ImageView imageView = (ImageView)root.findViewById(R.id.imageView);
                                    imageView.setImageDrawable(null);
                                    getActivity().recreate();
                                    loadinglayout.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //      Log.w(TAG, "Error adding document", e);
                                }
                            });
                }
            });
        }
        else{
            EditText edittext = (EditText)root.findViewById(R.id.editText);
            edittext.clearFocus();
            String s_edit = edittext.getText().toString();
            db = FirebaseFirestore.getInstance();

            Map<String, Object> data = new HashMap<>();
            data.put("TIME", getTime);
            data.put("UID", currentUser.getUid());
            data.put("COMMENT", s_edit);
            data.put("PHOTO", "NULL");
            data.put("STAR", s_rating);


            if(TextUtils.isEmpty(currentUser.getEmail())) {

                data.put("ID", currentUser.getDisplayName());
            }
            else {
                data.put("ID", currentUser.getEmail());

            }




            db.collection("user_store").document("TrustOne").collection("Comment")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //         Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            EditText edittext = (EditText)root.findViewById(R.id.editText);
                            edittext.setText("");
                            ImageView imageView = (ImageView)root.findViewById(R.id.imageView);
                            imageView.setImageDrawable(null);
                            getActivity().recreate();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //      Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
        //up로드 끝나는시점

    }


}
