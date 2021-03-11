package animalapp.info;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteActivity extends AppCompatActivity {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();

    private EditText mWriteTitleText;
    private EditText mWriteContentsText;
    private TextView mWriteNameText;
    private Button mWrite_upload_btn;

    private String id;
    private String key="1";
    private int i = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        mWriteTitleText = findViewById(R.id.write_title_text);
        mWriteContentsText = findViewById(R.id.write_contents_text);
        mWriteNameText = findViewById(R.id.write_name_text);
        mWrite_upload_btn = findViewById(R.id.write_upload_btn);

        final String current= user.getEmail();//로그인할 때 그 이메일 가져옴

        mStore.collection("users")//firestore users
                .whereEqualTo("id",current)//firestore id와 user email같은 곳?
                .get()//가져와
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot documentSnapshot : task.getResult()){

                                id = (String)documentSnapshot.get("pet_name");
                            }
                        }
                        mWriteNameText.setText("작성자 : "+id);
                    }
                });

        mStore.collection("board").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                    i++;
                    key = Integer.toString(i);
                    Toast.makeText(WriteActivity.this,"성공",Toast.LENGTH_SHORT).show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(WriteActivity.this,"실패",Toast.LENGTH_SHORT).show();
                    }
                });

        mWrite_upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firebaseAuth.getCurrentUser() != null){
                    Map<String, Object> post = new HashMap<>();
                    post.put("id",id);
                    post.put("title",mWriteTitleText.getText().toString());
                    post.put("contents",mWriteContentsText.getText().toString());
                    post.put("time", FieldValue.serverTimestamp());
                    post.put("UID",firebaseAuth.getUid());
                    post.put("key",key);

                    mStore.collection("board").add(post)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(WriteActivity.this,"업로드 성공",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(WriteActivity.this, "업로드 실패", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });



    }
}