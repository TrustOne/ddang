package com.example.administrator.secondlogin;

import android.Manifest;
import android.content.Context;
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
import android.app.Activity;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import static com.facebook.login.widget.ProfilePictureView.TAG;

public class Comment_Activity_1 extends Activity {
    private final int GALLERY_CODE=1112;
    private final int MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE = 1113;
    FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    StorageReference storageRef;
    FirebaseStorage storage;

    ArrayList<Comment> al = new ArrayList<Comment>();
    public RequestManager mGlideRequestManager;

    private ListView mList_Lv;
    private ScrollView mScroll_Sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment__1);
        db = FirebaseFirestore.getInstance();
        final String comment;
        int img;
        mGlideRequestManager = Glide.with(this);
        System.out.println("0809_1");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        iNit();








        db.collection("user_store").document("TrustOne").collection("Comment")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                al.add(new Comment((String)document.get("COMMENT") ,(String)document.get("TIME"), (String)document.get("UID"),(String)document.get("PHOTO") ));
                                System.out.println("0809_1.5" + (String)document.get("COMMENT"));


                            }
                            MyAdapter adapter = new MyAdapter(
                                    getApplicationContext(),
                                    R.layout.listview_layout,
                                    al);
                            ListView lv = (ListView)findViewById(R.id.listview);
                       //     setListViewHeightBasedOnItems(lv);
                            lv.setAdapter(adapter);
                       //     setListViewHeightBasedOnChildren(lv);


                        } else {
                      //      Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


     //   StorageReference pathReference = storageRef.child("user_store/Trustone_store/comment");
        System.out.println("0809_2");

        System.out.println("0809_3");
    }

///////

    private void iNit(){
        mScroll_Sv = (ScrollView)findViewById(R.id.sss);
        mList_Lv = (ListView) findViewById(R.id.listview);


        mList_Lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScroll_Sv.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }


class MyAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Comment> al;
    LayoutInflater inf;
    public MyAdapter(Context context, int layout, ArrayList<Comment> al) {
        this.context = context;
        this.layout = layout;
        this.al = al;
        this.inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() { // 총 데이터의 개수
        return al.size();
    }
    @Override
    public Object getItem(int position) { // 해당 행의 데이터
        return al.get(position);
    }
    @Override
    public long getItemId(int position) { // 해당 행의 유니크한 id
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("0809_7");
        if (convertView == null)
            convertView = inf.inflate(layout, null);


        TextView tv2 = (TextView) convertView.findViewById(R.id.tv_comment_list);
        ImageView iv = (ImageView) convertView.findViewById(R.id.imageView_list);

        Comment s = al.get(position);

        tv2.setText(s.comment);
        System.out.println("0809_8"+s.comment+s.img);
        StorageReference pathReference = storageRef.child(s.img);
        //iv.setImageResource(Integer.parseInt(s.img.toString()));
        mGlideRequestManager
                .using(new FirebaseImageLoader())
                .load(pathReference)
                .into(iv);

        return convertView;
    }
}
//////
    public void image_click(View view) {
        requestReadExternalStoragePermission();
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);

    //    intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case GALLERY_CODE:
                    System.out.println("imageTest1");


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
        System.out.println("imageTest2");
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL); System.out.println("imageTest2.5");
        int exifDegree = exifOrientationToDegrees(exifOrientation); System.out.println("imageTest2.7");
        System.out.println("imageTest3");
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

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE);
                // MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
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
        System.out.println("upload Method123");
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssFFF");
        final String getTime = sdf.format(date);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference mountainsRef = storageRef.child("user_store/Trustone_store/comment/"+getTime+"_"+currentUser.getUid());

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                System.out.println("upload fail123");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                System.out.println("upload success123");
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
}

class Comment { // 자바빈
    String comment = "";
    String time = "";
    String uid = "";
    String img; // 이미지
    public Comment(String comment, String time, String uid, String img) {
        this.comment = comment;
        this.time = time;
        this.uid = uid;
        this.img = img;
    }
    //이미지 없을떄
    public Comment(String comment, String time, String uid) {
        this.comment = comment;
        this.time = time;
        this.uid = uid;
    }
    public Comment() {} // 기본생성자 : 생성자 작업시 함께 추가하자
}
