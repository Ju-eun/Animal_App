package animalapp.info;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyPage extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference mDatabase;
    EditText my_et_name, my_et_phone;
    EditText my_et_pet_name,my_et_pet_kind, my_et_pet_gen;
    Button my_btn_save;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        my_et_name=findViewById(R.id.my_et_name);
        my_et_phone=findViewById(R.id.my_et_phone);
        my_et_pet_name=findViewById(R.id.my_et_pet_name);
        my_et_pet_kind=findViewById(R.id.my_et_pet_kind);
        my_et_pet_gen=findViewById(R.id.my_et_pet_gen);
        my_btn_save=findViewById(R.id.my_btn_save);

        //database=FirebaseDatabase.getInstance();
        //uid=firebaseUser.getUid();
        //mDatabase=database.getReference("AnimalApp").child("users").child(uid);


    }
}