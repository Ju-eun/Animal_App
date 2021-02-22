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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;
import java.util.Collection;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth firebaseAuth;
    EditText email_et, password_et;
    Button login_btn, signup_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth= FirebaseAuth.getInstance();

        email_et= (EditText)findViewById(R.id.email_et);
        password_et=(EditText)findViewById(R.id.password_et);

        login_btn= (Button)findViewById(R.id.login_btn);
        signup_btn=(Button)findViewById(R.id.signup_btn);
        signup_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        if(firebaseAuth.getCurrentUser()!=null)
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
            startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
        }
    }
    private void userLogin(){
        String email=email_et.getText().toString().trim();//trim 공백x
        String password= password_et.getText().toString().trim();

        if(TextUtils.isEmpty((email))){//null check
            Toast.makeText(this, "email을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "로그인을 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "로그인을 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}