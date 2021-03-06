package animalapp.info;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteActivity extends AppCompatActivity {

    private FirebaseFirestore mStore;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private EditText mWriteTitleText;
    private EditText mWriteContentsText;
    private TextView mWriteNameText;
    private Button mWrite_upload_btn;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference imgRef;
    private ImageView mWrite_img_view;
    private Button mWrite_img_btn;
    private String id;
    private String key="1";
    private int i = 1;
    String mWrite_board;
    String board_fileName;
    Uri imgUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        mWriteTitleText = findViewById(R.id.write_title_text);
        mWriteContentsText = findViewById(R.id.write_contents_text);
        mWriteNameText = findViewById(R.id.write_name_text);
        mWrite_upload_btn = findViewById(R.id.write_upload_btn);
        mWrite_img_btn = findViewById(R.id.write_img_btn);
        mWrite_img_view=findViewById(R.id.write_img_view);




        if(user==null){ //로그인하지 않았을 경우
            showDialog(); //다이얼로그 함수 실행
        }
        else{ //로그인 했을 때
            final String current= user.getEmail();//로그인할 때 그 이메일 가져옴

            mStore = FirebaseFirestore.getInstance();
            mStore.collection("users")//firestore users
                    .whereEqualTo("id",current)//firestore id와 user email같은 곳
                    .get()//가져와
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot documentSnapshot : task.getResult()){

                                    id = (String)documentSnapshot.get("pet_name");
                                }
                            }
                            mWriteNameText.setText("작성자 : "+id);
                        }
                    });
        }


        mStore = FirebaseFirestore.getInstance();
        mStore.collection("board").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                    i++; //추가될때 마다 키값 하나씩 증가
                    key = Integer.toString(i); //키값 생성, 저장
                    Toast.makeText(WriteActivity.this,"성공",Toast.LENGTH_SHORT).show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(WriteActivity.this,"실패",Toast.LENGTH_SHORT).show();
                    }
                });


        mWrite_upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imgRef = storage.getReferenceFromUrl("gs://animalapp-cadbb.appspot.com/board");
                imgRef = storage.getReference();
                if(imgUri==null){ //이미지 미등록시
                    //이미지값이랑 파일이름 저장 (특정이미지 저장법을 몰라서 그냥 파일이름이랑 값 통째로 넣은 변수 사용했수다,,)
                    mWrite_board="https://firebasestorage.googleapis.com/v0/b/animalapp-cadbb.appspot.com/o/board%2Fthumb_l_8FFF68D772B6EA4B8663721F50B4B0E6.jpg?alt=media&token=828db3dc-398c-4be1-b576-89629a9329b5";
                    board_fileName="thumb_l_8FFF68D772B6EA4B8663721F50B4B0E6.jpg";

                        Map<String, Object> post = new HashMap<>();
                        post.put("id",id);
                        post.put("title",mWriteTitleText.getText().toString());
                        post.put("contents",mWriteContentsText.getText().toString());
                        post.put("time", FieldValue.serverTimestamp());
                        post.put("UID",firebaseAuth.getUid());
                        post.put("key",key);
                        post.put("view",mWrite_board);
                        post.put("board_fileName",board_fileName);
                        Log.d("hihi",mWrite_board);

                        mStore = FirebaseFirestore.getInstance();
                        mStore.collection("board").add(post)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(WriteActivity.this,"이미지 없이 업로드 성공",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(WriteActivity.this, "이미지 없이 업로드 실패", Toast.LENGTH_SHORT).show();
                                    }
                                });


                }

                else{ //이미지 등록시
                    Uri file = Uri.fromFile(new File(getPath(imgUri))); // 절대경로uri를 file에 할당

                    // stroage images에 절대경로파일 저장
                    StorageReference riversRef = imgRef.child("board/" + file.getLastPathSegment());
                    UploadTask uploadTask = riversRef.putFile(file);


                    // Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    })
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!uri.isComplete()) ;
                                    Uri uri1 = uri.getResult();

                                    mWrite_board = String.valueOf(uri1); //이미지 절대경로 저장
                                    board_fileName = file.getLastPathSegment(); //이미지 파일이름 저장
                                    Map<String, Object> post = new HashMap<>();
                                    post.put("id",id);
                                    post.put("title",mWriteTitleText.getText().toString());
                                    post.put("contents",mWriteContentsText.getText().toString());
                                    post.put("time", FieldValue.serverTimestamp());
                                    post.put("UID",firebaseAuth.getUid());
                                    post.put("key",key);
                                    post.put("view",mWrite_board);
                                    post.put("board_fileName",board_fileName);
                                    Log.d("hihi",mWrite_board);

                                    mStore = FirebaseFirestore.getInstance();
                                    mStore.collection("board").add(post)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(WriteActivity.this,"업로드 성공",Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(WriteActivity.this, "업로드 실패", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                }

            }
        });

        mWrite_img_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
                }
                clickSelect(); //앨범 실행
            }
        });



    }

    void clickSelect() {
        //사진을 선탣할 수 있는 Gallery앱 실행
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 10);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    //선택한 이미지의 경로 얻어오기
                    imgUri = data.getData();
                    Glide.with(this).load(imgUri).into(mWrite_img_view);
                }
                break;
        }
    }

    //이미지 절대경로
    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }


    //다이얼로그 생성함수
    void showDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(WriteActivity.this)
                .setTitle("알림")
                .setMessage("회원정보가 없습니다. 로그인화면으로 이동하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(WriteActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                }) .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(WriteActivity.this,NoticeActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

}