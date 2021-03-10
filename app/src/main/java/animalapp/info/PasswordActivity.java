package animalapp.info;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;




public class PasswordActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email_et, phone_et,email_dialog_name_et, email_dialog_num_et,email_dialog_find_email;
    Button confirm_button, email_find_btn, email_dialog_btn;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    String password,user_name,phone_num, email,phone;
    RadioButton[] radio;

    InputMethodManager imm;
    PendingIntent sentPI;
    View dialogView, dialogView2;
    String doc;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());


        email_et= (EditText)findViewById(R.id.email_et);
        phone_et= (EditText)findViewById(R.id.phone_et);


        confirm_button=(Button)findViewById(R.id.confirm_btn);
        confirm_button.setOnClickListener(this);
        email_find_btn=(Button)findViewById(R.id.email_find_btn);
        email_find_btn.setOnClickListener(this);
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        LayoutInflater inflater= getLayoutInflater();
        dialogView= inflater.inflate(R.layout.password_dialog,null);
        dialogView2=inflater.inflate(R.layout.email_dialog,null);
        radio= new RadioButton[]{
                (RadioButton)dialogView.findViewById(R.id.phone_send),
                (RadioButton)dialogView.findViewById(R.id.email_send)
        };
        email_dialog_name_et= (EditText)dialogView2.findViewById(R.id.email_dialog_name_et);
        email_dialog_num_et=(EditText)dialogView2.findViewById(R.id.email_dialog_num_et);
        email_dialog_find_email=(EditText)dialogView2.findViewById(R.id.email_dialog_find_email);
        email_dialog_btn= (Button)dialogView2.findViewById(R.id.email_dialog_btn);

    }
    @Override
    public void onClick(View view) {

        if(view==confirm_button)
        {

            firebaseAuth= FirebaseAuth.getInstance();
            db=FirebaseFirestore.getInstance();
            email= email_et.getText().toString().trim();
            phone= phone_et.getText().toString().trim();
            db.collection("users")
                    .whereEqualTo("id",email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(DocumentSnapshot documentSnapshot : task.getResult()) {


                                    if(phone.equals(documentSnapshot.get("phone").toString()))
                                    {
                                        doc= documentSnapshot.getId();
                                        phone_num= documentSnapshot.get("phone").toString();
                                        password= documentSnapshot.get("pwd").toString();
                                        user_name= documentSnapshot.get("name").toString();
                                        AlertDialog.Builder builder= new AlertDialog.Builder(PasswordActivity.this);
                                        builder.setTitle("비밀번호를 알아내라!");

                                        if (dialogView.getParent() != null)
                                            ((ViewGroup) dialogView.getParent()).removeView(dialogView);
                                        builder.setView(dialogView);
                                        builder.setPositiveButton("전송", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if(radio[0].isChecked())
                                                {
                                                    sendSMS(phone_num,user_name+"님의 비밀번호는 "+password+"입니다");

                                                    Toast.makeText(PasswordActivity.this, "사용자의 문자로 비밀번호를 전송하였습니다.", Toast.LENGTH_SHORT).show();
                                                    intent= new Intent(PasswordActivity.this, LoginActivity.class);
                                                    startActivity(intent);

                                                }
                                                else if(radio[1].isChecked())
                                                {
                                                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task1 -> {
                                                        if(task1.isSuccessful()){
                                                            Toast.makeText(PasswordActivity.this, "사용자의 이메일로 비밀번호를 전송하였습니다.", Toast.LENGTH_SHORT).show();
                                                            intent = new Intent (PasswordActivity.this, LoginActivity.class);
                                                            intent.putExtra("password","1");
                                                            startActivity(intent);
                                                        }
                                                    });

                                                }
                                            }
                                        });
                                        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                        builder.create().show();
                                    }

                                    else{
                                        Toast.makeText(PasswordActivity.this, "다시 입력해 주세요", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }


                        }
                    });


        }
        if(view==email_find_btn)
        {
            AlertDialog.Builder builder2= new AlertDialog.Builder(PasswordActivity.this);
            builder2.setTitle("이메일을 알아내라");
            if (dialogView2.getParent() != null)
                ((ViewGroup) dialogView2.getParent()).removeView(dialogView2);

            builder2.setView(dialogView2);

            builder2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });


            builder2.create().show();
            email_dialog_btn.setOnClickListener(listener);

        }


    }
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            db=FirebaseFirestore.getInstance();

            db.collection("users")
                    .whereEqualTo("name", email_dialog_name_et.getText().toString().trim())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                    if(email_dialog_num_et.getText().toString().trim().equals(documentSnapshot.get("phone")+""))
                                    {

                                        email_dialog_find_email.setText(documentSnapshot.get("id").toString());

                                    }
                                    else
                                        Toast.makeText(PasswordActivity.this, "이름과 전화번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }
    };

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