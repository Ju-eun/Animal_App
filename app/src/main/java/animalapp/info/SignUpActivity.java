package animalapp.info;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
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

    private ArrayList<MemInfo> memInfos;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if(Build.VERSION.SDK_INT>=21){
            getWindow().setStatusBarColor(Color.parseColor("#000000"));
        }
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF000000));

        sign_id_ed = (EditText)findViewById(R.id.sign_id_ed);
        sign_password_ed = (EditText)findViewById(R.id.sign_password_ed);
        sign_passwordcheck_ed = (EditText)findViewById(R.id.sign_passwordcheck_ed);
        sign_name_ed = (EditText)findViewById(R.id.sign_name_ed);
        sign_phone_ed = (EditText)findViewById(R.id.sign_phone_ed);
        sign_pet_name_ed=(EditText)findViewById(R.id.sign_pet_name_ed);
        sign_pet_type_ed = (EditText)findViewById(R.id.sign_pet_type_ed);
        sign_pet_gender_ed = (EditText)findViewById(R.id.sign_pet_gender_ed);
        sign_up_btn = (Button)findViewById(R.id.sign_up_btn);
        sign_cancel_btn = (Button)findViewById(R.id.sign_cancel_btn);

        memInfos = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();


        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sign_password_ed.getText().toString().equals(sign_passwordcheck_ed.getText().toString())) {
                    signup();
                }
                else {
                    Toast.makeText(SignUpActivity.this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }


                firebaseAuth.createUserWithEmailAndPassword(sign_id_ed.getText().toString(),sign_password_ed.getText().toString())
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    finish();
                                }else {
                                    Toast.makeText(SignUpActivity.this,"회원가입 에러",Toast.LENGTH_SHORT).show();
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


    }
    void signup(){
        String email = sign_id_ed.getText().toString().trim();
        String pwd = sign_password_ed.getText().toString().trim();
        String name = sign_name_ed.getText().toString().trim();
        String phone = sign_phone_ed.getText().toString().trim();
        String pet_name = sign_pet_name_ed.getText().toString().trim();
        String pet_type = sign_pet_type_ed.getText().toString().trim();
        String pet_gender = sign_pet_gender_ed.getText().toString();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        MemInfo memInfo = new MemInfo(email,pwd,name,phone,pet_name,pet_type,pet_gender);
        if(user != null){
            db.collection("users").document(user.getUid()).set(memInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(SignUpActivity.this,"회원가입 성공",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this,"회원가입 실패",Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
}