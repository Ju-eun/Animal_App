package animalapp.info;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
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
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore mStore;
    String uid;
    String doc;
    Uri imgUri;
    String id_v;
    String getimage, getfileName;
    private StorageReference imgRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_board);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();


        title = (EditText) findViewById(R.id.select_board_title_text);
        contents = (EditText) findViewById(R.id.select_board_contents_text);
        id = (TextView) findViewById(R.id.select_board_name_text);
        select_img_view = findViewById(R.id.select_img_view);
        select_board_btn_save = findViewById(R.id.select_board_btn_save);
        select_board_btn_del = findViewById(R.id.select_board_btn_del);
        select_board_btn_img = findViewById(R.id.select_img_btn);



        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        contents.setText(intent.getStringExtra("contents"));
        id.setText(intent.getStringExtra("name"));
        getfileName=intent.getExtras().getString("board_fileName");
        Log.d("a1234",getfileName);
        getimage=intent.getExtras().getString("view");
        storage = FirebaseStorage.getInstance();
        Log.d("확인",getimage);
        Glide.with(getApplicationContext()).load(getimage).into(select_img_view);
        id_v = intent.getStringExtra("Uid");
        user = FirebaseAuth.getInstance().getCurrentUser();//현재 접속하고 있는 유저
        mStore = FirebaseFirestore.getInstance();

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


        user = FirebaseAuth.getInstance().getCurrentUser();//현재 접속하고 있는 유저
        mStore = FirebaseFirestore.getInstance();

        select_board_btn_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                storage.getReference().child("board").child(getfileName).delete();
//                imgRef = storage.getReferenceFromUrl("gs://animalapp-cadbb.appspot.com/board");
//                imgRef = storage.getReference();
//                Uri file = Uri.fromFile(new File(getPath(imgUri)));
//                StorageReference riversRef = imgRef.child("board/" + file.getLastPathSegment());
//                UploadTask uploadTask = riversRef.putFile(file);
//
//                sendfileName=file.getLastPathSegment();

                firebaseAuth = FirebaseAuth.getInstance();
                if (firebaseAuth.getCurrentUser() != null) {
                    Map<String, Object> board = new HashMap<>();
                    board.put("title", title.getText().toString());
                    board.put("contents", contents.getText().toString());
                    //board.put("view",getimage);
//                    board.put("view",select_img_view.ge)

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
//                                            imgUri = Uri.parse((String) documentSnapshot.get("view"));
//                                            Glide.with(getApplicationContext()).load(imgUri).into(select_img_view);
                                        }

                                        if(firebaseAuth.getCurrentUser()!=null)
                                        {
                                            mStore= FirebaseFirestore.getInstance();
                                            mStore.collection("board").document(doc+"")
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

        select_board_btn_img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
                }
                clickSelect();
            }
        });


        select_board_btn_del.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                mStore = FirebaseFirestore.getInstance();
                storage.getReference().child("board").child(getfileName).delete();
                mStore.collection("board").whereEqualTo("title", intent.getStringExtra("title"))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        doc = documentSnapshot.getId();
                                        Log.d("a1234",doc);
                                    }

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
}

