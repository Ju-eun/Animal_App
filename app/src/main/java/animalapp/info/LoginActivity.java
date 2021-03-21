package animalapp.info;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;
import java.util.Collection;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    EditText email_et, password_et;
    Button login_btn, signup_btn, password_find_btn;
    String doc;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth= FirebaseAuth.getInstance();

        email_et= (EditText)findViewById(R.id.email_et);
        password_et=(EditText)findViewById(R.id.password_et);

        login_btn= (Button)findViewById(R.id.login_btn);
        signup_btn=(Button)findViewById(R.id.signup_btn);
        password_find_btn=(Button)findViewById(R.id.password_btn);
        signup_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        password_find_btn.setOnClickListener(this);
        if(firebaseAuth.getCurrentUser()!=null)//현재 로그인한 유저가 있을 때 메인 액티비티로 보냄
        {
            Toast.makeText(this, "이미 로그인을 하셨습니다", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }


    }

    @Override
    public void onClick(View view) {
        if(view==login_btn){
                userLogin();
        }
        if(view==signup_btn){
            startActivity(new Intent(LoginActivity.this,SignUpActivity.class));//회원가입
        }
        if(view==password_find_btn){
            startActivity(new Intent(LoginActivity.this,PasswordActivity.class));//비밀번호 찾기
        }
    }
    private void userLogin(){
        email=email_et.getText().toString().trim();//trim 공백x
        String password= password_et.getText().toString().trim();

        if(TextUtils.isEmpty((email))){//null check
            Toast.makeText(this, "email을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email,password)//auhtnetication 로그인 (이메일, 비밀번호)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent1= getIntent();
                            String str=intent1.getStringExtra("password");//비밀번호 찾기 액티비티에서 이메일로 변경했을 경우 값을 받아옴
                            if(TextUtils.isEmpty(str))//받아올 값이 없을 때
                            {
                                Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);

                            }
                            else{// 변경했을 시
                                db=FirebaseFirestore.getInstance();
                                db.collection("users")
                                        .whereEqualTo("id",email)//users콜렉션의 id 필드와 editText의 email이 같은 곳
                                        .get()// 가져옴
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                                        doc= documentSnapshot.getId();//document id를 저장
                                                        db.collection("users").document(doc+"").update("pwd",password_et.getText().toString());
                                                        //authentication은 비밀번호 재설정 시 업데이트가 되었지만 firestore의 user의 pwd 필드는 업데이트가 안되므로 여기서 해준다.
                                                    }
                                                }
                                            }
                                        });

                                Intent intent= new Intent(LoginActivity.this,MainActivity.class);//로그인 성공시 메인액티비티로 보냄
                                startActivity(intent);
                            }
                            Toast.makeText(LoginActivity.this, "로그인을 성공하셨습니다.", Toast.LENGTH_SHORT).show();


                        }
                        else {
                            Toast.makeText(LoginActivity.this, "로그인을 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}