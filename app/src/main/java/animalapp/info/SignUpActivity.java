package animalapp.info;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    Uri imgUri;
    Uri selectedImageUri;
    String sign_profile;
    String profile_fileName;
    private EditText sign_id_ed;
    private EditText sign_password_ed;
    private EditText sign_passwordcheck_ed;
    private EditText sign_name_ed;
    private EditText sign_phone_ed;
    private EditText sign_pet_name_ed;
    private EditText sign_pet_type_ed;
    private EditText sign_pet_gender_ed;
    private Button sign_up_btn;
    private Button sign_cancel_btn;
    private Button sing_up_photo_btn;
    private ImageView sign_imageView;

    private ArrayList<MemInfo> memInfos;

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference imgRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#000000"));
        }
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF000000));

        sign_id_ed = (EditText) findViewById(R.id.sign_id_ed);
        sign_password_ed = (EditText) findViewById(R.id.sign_password_ed);
        sign_passwordcheck_ed = (EditText) findViewById(R.id.sign_passwordcheck_ed);
        sign_name_ed = (EditText) findViewById(R.id.sign_name_ed);
        sign_phone_ed = (EditText) findViewById(R.id.sign_phone_ed);
        sign_pet_name_ed = (EditText) findViewById(R.id.sign_pet_name_ed);
        sign_pet_type_ed = (EditText) findViewById(R.id.sign_pet_type_ed);
        sign_pet_gender_ed = (EditText) findViewById(R.id.sign_pet_gender_ed);
        sign_up_btn = (Button) findViewById(R.id.sign_up_btn);
        sign_cancel_btn = (Button) findViewById(R.id.sign_cancel_btn);
        sing_up_photo_btn = (Button) findViewById(R.id.sign_up_photo_btn);
        sign_imageView = (ImageView) findViewById(R.id.sign_imgview);

        memInfos = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sign_password_ed.getText().toString().equals(sign_passwordcheck_ed.getText().toString())) {

                } else {
                    Toast.makeText(SignUpActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }


                firebaseAuth.createUserWithEmailAndPassword(sign_id_ed.getText().toString(), sign_password_ed.getText().toString())
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
//                                    clickUpload();
                                    signup();
                                    finish();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "회원가입 에러", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });

            }
        });

        sign_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sing_up_photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
                }
                clickSelect();
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
                    Glide.with(this).load(imgUri).into(sign_imageView);
                }

                break;
        }
    }


    void signup() {

        String email = sign_id_ed.getText().toString().trim();
        String pwd = sign_password_ed.getText().toString().trim();
        String name = sign_name_ed.getText().toString().trim();
        String phone = sign_phone_ed.getText().toString().trim();
        String pet_name = sign_pet_name_ed.getText().toString().trim();
        String pet_type = sign_pet_type_ed.getText().toString().trim();
        String pet_gender = sign_pet_gender_ed.getText().toString().trim();


        FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        imgRef = storage.getReferenceFromUrl("gs://animalapp-cadbb.appspot.com/sign_up_profile");

            imgRef = storage.getReference();
            if(imgUri==null)
            {
                sign_profile="null";
                profile_fileName="null";
                MemInfo memInfo = new MemInfo(email, pwd, name, phone, pet_name, pet_type, pet_gender, sign_profile , profile_fileName);

                if (user != null) {
                    db.collection("users").document(user.getUid()).set(memInfo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUpActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                }
                            });


                }
            }
            else
            {
                Uri file = Uri.fromFile(new File(getPath(imgUri))); // 절대경로uri를 file에 할당

            // stroage images에 절대경로파일 저장
            StorageReference riversRef = imgRef.child("sign_up_profile/" + file.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(file);
            profile_fileName = file.getLastPathSegment();


            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uri.isComplete()) ;
                    Uri uri1 = uri.getResult();

                    sign_profile = String.valueOf(uri1);
                    MemInfo memInfo = new MemInfo(email, pwd, name, phone, pet_name, pet_type, pet_gender, sign_profile , profile_fileName);

                    if (user != null) {
                        db.collection("users").document(user.getUid()).set(memInfo)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignUpActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }
                }
            });


//        //참조 객체를 통해 이미지 파일 업로드
////         imgRef.putFile(imgUri);
            }



    }
//사진의 절대 경로 가져오는  메서드
    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }
}