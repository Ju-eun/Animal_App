package animalapp.info;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class WriteActivity extends AppCompatActivity {
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();


    private Task<QuerySnapshot> id;
    private EditText mWriteTitleText;
    private EditText mWriteContentsText;
    private EditText mWriteNameText;
    private Button mwrite_upload_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        mWriteTitleText = findViewById(R.id.write_title_text);
        mWriteContentsText = findViewById(R.id.write_contents_text);
        mWriteNameText = findViewById(R.id.write_name_text);
        mwrite_upload_btn = (Button)findViewById(R.id.write_upload_btn);

        mwrite_upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String current = user.getEmail();
                id = mStore.collection("users")
                        .whereEqualTo("id",current)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                                       documentSnapshot.get("pet_name");
                                    }
                                }
                            }
                        });
                Map<String, Object> post = new HashMap<>();
                post.put("id",id.toString());
                post.put("title",mWriteTitleText.getText().toString());
                post.put("contents",mWriteContentsText.getText().toString());
                post.put("name",mWriteNameText.getText().toString());

                mStore.collection("board").document(user.getUid()).set(post)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(WriteActivity.this,"성공",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(WriteActivity.this,"실패",Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });
    }
}