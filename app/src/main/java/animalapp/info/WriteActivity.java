package animalapp.info;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WriteActivity extends AppCompatActivity {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private EditText mWriteTitleText;
    private EditText mWriteContentsText;
    private EditText mWriteNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        mWriteTitleText = findViewById(R.id.write_title_text);
        mWriteContentsText = findViewById(R.id.write_contents_text);
        mWriteNameText = findViewById(R.id.write_name_text);

        Map<String, Object> post = new HashMap<>();
        post.put("id","");
        post.put("title",mWriteTitleText.getText().toString());
        post.put("contents",mWriteContentsText.getText().toString());
        post.put("name",mWriteNameText.getText().toString());

    }
}