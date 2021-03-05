package animalapp.info;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class SelectBoardActivity extends AppCompatActivity {



    private EditText title;
    private EditText contents;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_board);

        title = (EditText)findViewById(R.id.select_board_title_text);
        contents = (EditText)findViewById(R.id.select_board_contents_text);
        name = (TextView)findViewById(R.id.select_board_name_text);

        Intent intent=getIntent();
        title.setText(intent.getStringExtra("title"));
        contents.setText(intent.getStringExtra("contents"));
        name.setText(intent.getStringExtra("name"));






    }
}