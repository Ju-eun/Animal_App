package animalapp.info;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class PasswordActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email_et, phone_et;
    Button email_send_btn, phone_send_btn;
    FirebaseFirestore db;
    String password,user_name;
    Intent intent;
    InputMethodManager imm;
    PendingIntent sentPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        email_et= (EditText)findViewById(R.id.email_et);
        phone_et= (EditText)findViewById(R.id.phone_et);
        email_send_btn= (Button)findViewById(R.id.email_send_btn);
        phone_send_btn= (Button)findViewById(R.id.phone_send_btn);
        email_send_btn.setOnClickListener(this);
        phone_send_btn.setOnClickListener(this);
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);


    }
    @Override
    public void onClick(View view) {
        if(view==email_send_btn)
        {
            //user_info();
            intent=new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"ssdam123@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"쓰담 비밀번호 안내");
            //intent.putExtra(Intent.EXTRA_TEXT,user_name+"님의 비밀번호는"+password+"입니다.");

            startActivity(intent);




        }
        if(view==phone_send_btn)
        {
           // user_info();
           //sendSMS("01053243981",user_name+"님의 비밀번호는 "+password+"입니다");
            sendSMS("01053243981","안녕");

        }

    }
    private void user_info()
    {
        db=FirebaseFirestore.getInstance();

        db.collection("users")
                .whereEqualTo("id",email_et.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(DocumentSnapshot documentSnapshot : task.getResult()) {
                                if(documentSnapshot.get("id").toString()==email_et.getText().toString()&& documentSnapshot.get("phone").toString()==phone_et.getText().toString())
                                {
                                    password= documentSnapshot.get("pwd").toString();
                                    user_name= documentSnapshot.get("name").toString();
                                }
                                else
                                    Toast.makeText(PasswordActivity.this, "이메일과 전화번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }

                        }


                    }
                });
    }
    private void sendSMS(String phoneNumber, String message) {

        // 권한이 허용되어 있는지 확인한다
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
            Toast.makeText(this, "권한을 허용하고 재전송해주세요", Toast.LENGTH_LONG).show();
        } else {
            SmsManager sms = SmsManager.getDefault();

            // 아래 구문으로 지정된 핸드폰으로 문자 메시지를 보낸다
            sms.sendTextMessage(phoneNumber, null, message, sentPI, null);
            Toast.makeText(this, "전송을 완료하였습니다", Toast.LENGTH_LONG).show();
        }
    }

}