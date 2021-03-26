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


        confirm_button=(Button)findViewById(R.id.confirm_btn);//확인버튼
        confirm_button.setOnClickListener(this);
        email_find_btn=(Button)findViewById(R.id.email_find_btn);//이메일 찾기 버튼
        email_find_btn.setOnClickListener(this);
    //    imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        LayoutInflater inflater= getLayoutInflater();//LayoutInflater: Layout xml 리소스 파일을 View 객체로 부불려 주는(inflate) LayoutInflater 객체 생성
        dialogView= inflater.inflate(R.layout.password_dialog,null);//인플레이트 함
        dialogView2=inflater.inflate(R.layout.email_dialog,null);
        radio= new RadioButton[]{
                (RadioButton)dialogView.findViewById(R.id.phone_send),
                (RadioButton)dialogView.findViewById(R.id.email_send)
        };
        email_dialog_name_et= (EditText)dialogView2.findViewById(R.id.email_dialog_name_et);//이메일 다이얼로그 관련
        email_dialog_num_et=(EditText)dialogView2.findViewById(R.id.email_dialog_num_et);
        email_dialog_find_email=(EditText)dialogView2.findViewById(R.id.email_dialog_find_email);
        email_dialog_btn= (Button)dialogView2.findViewById(R.id.email_dialog_btn);

    }
    @Override
    public void onClick(View view) {

        if(view==confirm_button)
        {

            firebaseAuth= FirebaseAuth.getInstance();//이메일 재설정을 위해 선언
            db=FirebaseFirestore.getInstance();
            email= email_et.getText().toString().trim();
            phone= phone_et.getText().toString().trim();
            db.collection("users")
                    .whereEqualTo("id",email)//users 콜렉션의 문서 id필드와 EditText email과 같은 곳
                    .get()//가져옴
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(DocumentSnapshot documentSnapshot : task.getResult()) {
                                    if(phone.equals(documentSnapshot.get("phone").toString()))//전화번호와 가져온 phone필드와 같을 경우
                                    {
                                        //doc= documentSnapshot.getId();//가져온 document의 id를 저장
                                        phone_num= documentSnapshot.get("phone").toString();//phone필드 값을 변수에 저장
                                        password= documentSnapshot.get("pwd").toString();// pwd필드 값을 변수에 저장
                                        user_name= documentSnapshot.get("name").toString();//name 필드 값을 변수에 저장
                                        AlertDialog.Builder builder= new AlertDialog.Builder(PasswordActivity.this);//AlertDialog.Builder 객체 생성
                                        builder.setTitle("비밀번호를 알아내라!");//제목 설정

                                        if (dialogView.getParent() != null)// dialog를 show 하기 전에 전에 있는 dialog를 지워줌
                                            ((ViewGroup) dialogView.getParent()).removeView(dialogView);
                                        builder.setView(dialogView);//위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
                                        builder.setPositiveButton("전송", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if(radio[0].isChecked())//휴대폰 전송 눌렀을 시
                                                {
                                                    sendSMS(phone_num,user_name+"님의 비밀번호는 "+password+"입니다");// 전화번호와, 보낼 메세지

                                                    Toast.makeText(PasswordActivity.this, "사용자의 문자로 비밀번호를 전송하였습니다.", Toast.LENGTH_SHORT).show();
                                                    intent= new Intent(PasswordActivity.this, LoginActivity.class);//보낸 후 로그인 액티비티로 감
                                                    startActivity(intent);

                                                }
                                                else if(radio[1].isChecked())//이메일로 비밀번호 재설정 눌렀을 때
                                                {
                                                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task1 -> {//sendPasswordResetEmail 메서드로 사용자에게 비밀번호 재설정 이메일을 보냄
                                                        if(task1.isSuccessful()){
                                                            Toast.makeText(PasswordActivity.this, "사용자의 이메일로 비밀번호를 전송하였습니다.", Toast.LENGTH_SHORT).show();
                                                            intent = new Intent (PasswordActivity.this, LoginActivity.class);//로그인 액티비티로 감
                                                            intent.putExtra("password","1");//인텐트로 값 보내기
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

                                    else{// phone필드와 전화번호 입력한 값이 다를 경우
                                        Toast.makeText(PasswordActivity.this, "다시 입력해 주세요", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }


                        }
                    });


        }
        if(view==email_find_btn)//이메일 찾기 버튼 눌렀을 때
        {
            AlertDialog.Builder builder2= new AlertDialog.Builder(PasswordActivity.this);//AlertDialog.Builder 객체 생성
            builder2.setTitle("이메일을 알아내라");// 제목 설정
            if (dialogView2.getParent() != null)// dialog를 show 하기 전에 전에 있는 dialog를 지워줌
                ((ViewGroup) dialogView2.getParent()).removeView(dialogView2);

            builder2.setView(dialogView2);//위에서 inflater가 만든 dialogView 객체 세팅 (Customize)

            builder2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });


            builder2.create().show();
            email_dialog_btn.setOnClickListener(listener);//이메일 찾기 버튼 누름
        }


    }
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            db=FirebaseFirestore.getInstance();

            db.collection("users")
                    .whereEqualTo("name", email_dialog_name_et.getText().toString().trim())//collection users-> document (field name== EditText email)
                    .get()//가져와
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                    if(email_dialog_num_et.getText().toString().trim().equals(documentSnapshot.get("phone")+""))//전화번호와 phone 필드와 같을 경우
                                    {

                                        email_dialog_find_email.setText(documentSnapshot.get("id").toString());//EditText-> 가져온 email로 set

                                    }
                                    else//다를경우
                                        Toast.makeText(PasswordActivity.this, "이름과 전화번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }
    };

    private void sendSMS(String phoneNumber, String message) {// 전화번호와 메세지를 받아서

        // 권한이 허용되어 있는지 확인한다
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {//권한이 없을 때
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);//권한 요청
            Toast.makeText(this, "권한을 허용하고 재전송해주세요", Toast.LENGTH_LONG).show();
        } else {//SMS 메시지 보냄
            SmsManager sms = SmsManager.getDefault();
            // 아래 구문으로 지정된 핸드폰으로 문자 메시지를 보낸다
            sms.sendTextMessage(phoneNumber, null, message, sentPI, null);
            Toast.makeText(this, "전송을 완료하였습니다", Toast.LENGTH_LONG).show();
        }
    }

}