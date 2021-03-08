package animalapp.info;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

public class MyPage extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText my_page_name_et, my_page_phone_et, my_page_pwd_ed;
    EditText my_page_pet_name_et, my_page_pet_type_et, my_page_pet_gender_et;
    Button my_btn_save, my_btn_cancel;
    String Email;

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
        my_btn_save = findViewById(R.id.my_btn_save);
        my_btn_cancel = findViewById(R.id.mypage_cancel_btn);

        Email = user.getEmail();
        Log.d("a123", Email);

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
                            }
                        }
                    }
                });

        my_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> post = new HashMap<>();

                post.put("id", Email);
                post.put("name", my_page_name_et.getText().toString());
                post.put("phone", my_page_phone_et.getText().toString());
                post.put("pwd", my_page_pwd_ed.getText().toString());
                post.put("pet_name", my_page_pet_name_et.getText().toString());
                post.put("pet_type", my_page_pet_type_et.getText().toString());
                post.put("pet_gender", my_page_pet_gender_et.getText().toString());

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

        my_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}