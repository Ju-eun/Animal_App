package animalapp.info;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SelectBoardActivity extends AppCompatActivity {

    private EditText title;
    private EditText contents;
    private TextView id;
    private Button select_board_btn_save, select_board_btn_del;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore mStore;
    String uid;
    String id_v;
    String doc;
    String pet_Name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_board);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();


        title = (EditText) findViewById(R.id.select_board_title_text);
        contents = (EditText) findViewById(R.id.select_board_contents_text);
        id = (TextView) findViewById(R.id.select_board_name_text);
        select_board_btn_save = findViewById(R.id.select_board_btn_save);
        select_board_btn_del = findViewById(R.id.select_board_btn_del);

        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        contents.setText(intent.getStringExtra("contents"));
        id.setText(intent.getStringExtra("name"));
        id_v = intent.getStringExtra("Uid");

        user = FirebaseAuth.getInstance().getCurrentUser();//현재 접속하고 있는 유저
        mStore = FirebaseFirestore.getInstance();

        if (user.getUid().equals(id_v)) {
            select_board_btn_del.setVisibility(View.VISIBLE);
            select_board_btn_save.setVisibility(View.VISIBLE);
        }
        else {
            select_board_btn_save.setVisibility(View.GONE);
            select_board_btn_del.setVisibility(View.GONE);
        }

        select_board_btn_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                if (firebaseAuth.getCurrentUser() != null) {
                    Map<String, Object> board = new HashMap<>();
                    board.put("title", title.getText().toString());
                    board.put("contents", contents.getText().toString());

                    mStore = FirebaseFirestore.getInstance();
                    firebaseAuth = FirebaseAuth.getInstance();
                    mStore = FirebaseFirestore.getInstance();
                    mStore.collection("board").whereEqualTo("title", intent.getStringExtra("title"))
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                            doc = documentSnapshot.getId();
                                        }
                                        if (firebaseAuth.getCurrentUser() != null) {
                                            mStore = FirebaseFirestore.getInstance();
                                            mStore.collection("board").document(doc + "")
                                                    .update(board)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(SelectBoardActivity.this, "수정 성공", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(SelectBoardActivity.this, "수정 실패", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                }

                            });
                }
            }
        });


        select_board_btn_del.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                mStore = FirebaseFirestore.getInstance();
                mStore.collection("board").whereEqualTo("title", intent.getStringExtra("title"))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        doc = documentSnapshot.getId();
                                    }
                                    if (firebaseAuth.getCurrentUser() != null) {
                                        mStore = FirebaseFirestore.getInstance();
                                        mStore.collection("board").document(doc + "")
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(SelectBoardActivity.this, "삭제 성공", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(SelectBoardActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            }

                        });


            }
        });

    }
}

