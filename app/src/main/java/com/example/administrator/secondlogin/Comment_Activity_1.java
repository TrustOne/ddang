package com.example.administrator.secondlogin;

import android.Manifest;
import android.app.Activity;
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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

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

public class Comment_Activity_1 extends Activity {
    private final int GALLERY_CODE=1112;
    private final int MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE = 1113;
    FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    StorageReference storageRef;
    FirebaseStorage storage;
    int i =0;
    public RequestManager mGlideRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment__1);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        TextView tv_name = findViewById(R.id.tv_name);
        String email = mAuth.getCurrentUser().getEmail();
        if(TextUtils.isEmpty(email)) {
            tv_name.setText(mAuth.getCurrentUser().getDisplayName());
        }
        else {
            tv_name.setText(mAuth.getCurrentUser().getEmail());
        }
        final LinearLayout con = (LinearLayout)findViewById(R.id.con);
        final View v = getLayoutInflater().inflate(R.layout.activity_comment__1, null);
        mGlideRequestManager = Glide.with(this);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();



        db.collection("user_store").document("TrustOne").collection("Comment")
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
                                System.out.println("Time is = "+dt1.format(date));

                                sub2 n_layout = new sub2(getApplicationContext(),v);
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
                                i++;
                            }
                        } else {

                        }
                    }
                });
    }

    public void image_click(View view) {
        requestReadExternalStoragePermission();
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        ImageView ivImage = (ImageView)findViewById(R.id.imageView);
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
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }
    private void requestReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void upload(View view) {
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        RatingBar ratingBar = findViewById(R.id.ratingBar1);
        float rating = ratingBar.getRating();
        final String s_rating = String.valueOf(rating);
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssFFF");
        final String getTime = sdf.format(date);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
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

                    EditText edittext = (EditText)findViewById(R.id.editText);
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
                    data.put("ID", currentUser.getEmail());

                    db.collection("user_store").document("TrustOne").collection("Comment")
                            .add(data)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    //         Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
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
            EditText edittext = (EditText)findViewById(R.id.editText);
            edittext.clearFocus();
            String s_edit = edittext.getText().toString();
            db = FirebaseFirestore.getInstance();

            Map<String, Object> data = new HashMap<>();
            data.put("TIME", getTime);
            data.put("UID", currentUser.getUid());
            data.put("COMMENT", s_edit);
            data.put("PHOTO", "NULL");
            data.put("STAR", s_rating);
            data.put("ID", currentUser.getEmail());

            db.collection("user_store").document("TrustOne").collection("Comment")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //         Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //      Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
    }
}
