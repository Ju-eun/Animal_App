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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.grpc.Context;


public class SelectBoardActivity extends AppCompatActivity {

    private EditText title;
    private EditText contents;
    private TextView id;
    private Button select_board_btn_save, select_board_btn_del, select_board_btn_img;
    private ImageView select_img_view;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user= firebaseAuth.getCurrentUser();;
    FirebaseFirestore mStore;
    String uid;
    String doc;
    Uri imgUri;
    String id_v;
    String getimage, getfileName, sendfileName;
    Intent intent;
    private FirebaseStorage storage;
    private StorageReference imgRef;
    private Boolean aBoolean=true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_board);




        title = (EditText) findViewById(R.id.select_board_title_text);
        contents = (EditText) findViewById(R.id.select_board_contents_text);
        id = (TextView) findViewById(R.id.select_board_name_text);
        select_img_view = findViewById(R.id.select_img_view);
        select_board_btn_save = findViewById(R.id.select_board_btn_save);
        select_board_btn_del = findViewById(R.id.select_board_btn_del);
        select_board_btn_img = findViewById(R.id.select_img_btn);



        if(user==null){
            showDialog();
        }
        else{
            intent = getIntent();
            title.setText(intent.getStringExtra("title"));
            contents.setText(intent.getStringExtra("contents"));
            id.setText(intent.getStringExtra("name"));
            getfileName=intent.getExtras().getString("board_fileName");
            getimage=intent.getExtras().getString("view");
            storage = FirebaseStorage.getInstance();
            Log.d("확인",getfileName);
            Glide.with(getApplicationContext()).load(getimage).into(select_img_view);
            id_v = intent.getStringExtra("Uid");
            user = FirebaseAuth.getInstance().getCurrentUser();//현재 접속하고 있는 유저
            mStore = FirebaseFirestore.getInstance();

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            uid = firebaseUser.getUid();
            if (user.getUid().equals(id_v)) {
                select_board_btn_del.setVisibility(View.VISIBLE);
                select_board_btn_save.setVisibility(View.VISIBLE);
                select_board_btn_img.setVisibility(View.VISIBLE);
                title.setFocusable(true);
                contents.setFocusable(true);
            }
            else {
                select_board_btn_save.setVisibility(View.GONE);
                select_board_btn_del.setVisibility(View.GONE);
                select_board_btn_img.setVisibility(View.GONE);
                title.setFocusable(false);
                contents.setFocusable(false);
            }
        }




        select_board_btn_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();//현재 접속하고 있는 유저
                mStore = FirebaseFirestore.getInstance();

                if (aBoolean == false) {
                    if(!getimage.equals("null")){
                        storage.getReference().child("board").child(getfileName).delete();
                    }
                    imgRef = storage.getReferenceFromUrl("gs://animalapp-cadbb.appspot.com/board");
                    imgRef = storage.getReference();
                    Uri file = Uri.fromFile(new File(getPath(imgUri)));
                    StorageReference riversRef = imgRef.child("board/" + file.getLastPathSegment());
                    UploadTask uploadTask = riversRef.putFile(file);

                    sendfileName = file.getLastPathSegment();

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            Uri uri1 = uri.getResult();

                            String mWrite_board = String.valueOf(uri1);


                            firebaseAuth = FirebaseAuth.getInstance();
                            if (firebaseAuth.getCurrentUser() != null) {
                                Map<String, Object> board = new HashMap<>();
                                board.put("title", title.getText().toString());
                                board.put("contents", contents.getText().toString());
                                board.put("board_fileName", sendfileName);
                                board.put("view", mWrite_board);

                                firebaseAuth = FirebaseAuth.getInstance();
                                mStore = FirebaseFirestore.getInstance();
                                mStore.collection("board").whereEqualTo("title", intent.getStringExtra("title"))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                                        doc = documentSnapshot.getId();
                                                        imgUri = Uri.parse((String) documentSnapshot.get("view"));
                                                        Glide.with(getApplicationContext()).load(imgUri).into(select_img_view);
                                                    }
                                                    if (firebaseAuth.getCurrentUser() != null) {
                                                        mStore = FirebaseFirestore.getInstance();
                                                        mStore.collection("board").document(doc + "")
                                                                .update(board)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(SelectBoardActivity.this, "수정 성공", Toast.LENGTH_SHORT).show();
                                                                        finish();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(SelectBoardActivity.this, "수정 실패", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                }
                                            }

                                        });
                            }
                        }
                    });

                }
            }
        });

        select_board_btn_img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
                }
                clickSelect();
                aBoolean=false;
            }
        });


        select_board_btn_del.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                mStore = FirebaseFirestore.getInstance();
                mStore.collection("board").whereEqualTo("title", intent.getStringExtra("title"))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        doc = documentSnapshot.getId();
                                    }
                                    if(firebaseAuth.getCurrentUser()!=null)
                                    {
                                        mStore= FirebaseFirestore.getInstance();
                                        mStore.collection("board").document(doc+"")
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(SelectBoardActivity.this, "삭제 성공", Toast.LENGTH_SHORT).show();
                                                        storage.getReference().child("board").child(getfileName).delete();
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(SelectBoardActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            }

                        });



            }
        });


    }

    void clickSelect() {
        //사진을 선탣할 수 있는 Gallery앱 실행
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    //선택한 이미지의 경로 얻어오기
                    imgUri = data.getData();
                    Glide.with(getApplicationContext()).load(imgUri).into(select_img_view);
                    Log.d("확인222", String.valueOf(imgUri));
                }
                break;
        }
    }

    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }

    void showDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(SelectBoardActivity.this)
                .setTitle("알림")
                .setMessage("회원만 열람가능합니다. 로그인또는 회원가입을 해주세요.")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(SelectBoardActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                }) .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(SelectBoardActivity.this,NoticeActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
}

