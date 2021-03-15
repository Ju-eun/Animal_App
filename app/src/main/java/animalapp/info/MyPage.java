package animalapp.info;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MyPage extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference imgRef;
    private Boolean aBoolean = true;

    EditText my_page_name_et, my_page_phone_et, my_page_pwd_ed;
    EditText my_page_pet_name_et, my_page_pet_type_et, my_page_pet_gender_et;
    Button my_btn_save, my_btn_cancel, my_page_photo_btn;
    ImageView my_page_imgview;
    String Email,profile_fileName,profile_fileName_revice, sign_profile;
    Uri imguri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        my_page_name_et = findViewById(R.id.my_page_name_et);
        my_page_phone_et = findViewById(R.id.my_page_phone_et);
        my_page_pwd_ed = findViewById(R.id.my_page_pwd_et);
        my_page_pet_name_et = findViewById(R.id.my_page_pet_name_et);
        my_page_pet_type_et = findViewById(R.id.my_page_pet_type_et);
        my_page_pet_gender_et = findViewById(R.id.my_page_pet_gender_et);
        my_page_photo_btn = findViewById(R.id.my_page_photo_btn);
        my_btn_save = findViewById(R.id.my_btn_save);
        my_btn_cancel = findViewById(R.id.mypage_cancel_btn);
        my_page_imgview = findViewById(R.id.my_page_imgview);

        Email = user.getEmail();

        db.collection("users").whereEqualTo("id", Email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                my_page_name_et.setText((String) documentSnapshot.get("name"));
                                my_page_phone_et.setText((String) documentSnapshot.get("phone"));
                                my_page_pwd_ed.setText((String) documentSnapshot.get("pwd"));
                                my_page_pet_name_et.setText((String) documentSnapshot.get("pet_name"));
                                my_page_pet_type_et.setText((String) documentSnapshot.get("pet_type"));
                                my_page_pet_gender_et.setText((String) documentSnapshot.get("pet_gender"));
                                sign_profile = ((String)documentSnapshot.get("sign_profile"));
                                imguri = Uri.parse((String) documentSnapshot.get("sign_profile"));
                                profile_fileName = (String)documentSnapshot.get("profile_fileName");
                                Glide.with(getApplicationContext()).load(imguri).into(my_page_imgview);
                            }
                        }
                    }
                });


        my_page_photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                aBoolean = false;
            }
        });

        my_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(aBoolean == false) {
                    storage.getReference().child("sign_up_profile").child(profile_fileName).delete();

                    imgRef = storage.getReferenceFromUrl("gs://animalapp-cadbb.appspot.com/sign_up_profile");

                    imgRef = storage.getReference();
                    Uri file = Uri.fromFile(new File(getPath(imguri))); // 절대경로uri를 file에 할당

                    // stroage images에 절대경로파일 저장
                    StorageReference riversRef = imgRef.child("sign_up_profile/" + file.getLastPathSegment());
                    UploadTask uploadTask = riversRef.putFile(file);

                    profile_fileName_revice = file.getLastPathSegment();


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

                                    String profile = String.valueOf(uri1);

                                    Map<String, Object> post = new HashMap<>();

                                    post.put("id", Email);
                                    post.put("name", my_page_name_et.getText().toString());
                                    post.put("phone", my_page_phone_et.getText().toString());
                                    post.put("pwd", my_page_pwd_ed.getText().toString());
                                    post.put("pet_name", my_page_pet_name_et.getText().toString());
                                    post.put("pet_type", my_page_pet_type_et.getText().toString());
                                    post.put("pet_gender", my_page_pet_gender_et.getText().toString());
                                    post.put("sign_profile", profile);
                                    post.put("profile_fileName", profile_fileName_revice);

                                    firebaseAuth.getCurrentUser().updatePassword(my_page_pwd_ed.getText().toString());

                                    db.collection("users").document(user.getUid()).set(post)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(MyPage.this, "수정 성공", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(MyPage.this, "수정 실패", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                    aBoolean = true;
                }
                else {
                    Map<String, Object> post = new HashMap<>();

                    post.put("id", Email);
                    post.put("name", my_page_name_et.getText().toString());
                    post.put("phone", my_page_phone_et.getText().toString());
                    post.put("pwd", my_page_pwd_ed.getText().toString());
                    post.put("pet_name", my_page_pet_name_et.getText().toString());
                    post.put("pet_type", my_page_pet_type_et.getText().toString());
                    post.put("pet_gender", my_page_pet_gender_et.getText().toString());
                    post.put("sign_profile", sign_profile);
                    post.put("profile_fileName", profile_fileName);

                    firebaseAuth.getCurrentUser().updatePassword(my_page_pwd_ed.getText().toString());

                    db.collection("users").document(user.getUid()).set(post)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MyPage.this, "수정 성공", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MyPage.this, "수정 실패", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });


        my_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    //선택한 이미지의 경로 얻어오기
                    imguri = data.getData();
                    Glide.with(getApplicationContext()).load(imguri).into(my_page_imgview);
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