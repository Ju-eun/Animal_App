package animalapp.info;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoticeActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private RecyclerView mNoticeRecyclerView;
    private FloatingActionButton notice_write_btn;

    private NoticeAdapter mNoticeAdapter;
    private List<Board> mBoardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        mNoticeRecyclerView = (RecyclerView)findViewById(R.id.notice_recycler_view);
        notice_write_btn = (FloatingActionButton) findViewById(R.id.notice_write_btn);


//        mBoardList.add(new Board(null,"반갑습니다 여러분",null,"android"));
//        mBoardList.add(new Board(null,"Hello",null,"server"));
//        mBoardList.add(new Board(null,"ok",null,"php"));
//        mBoardList.add(new Board(null,"zzz",null,"java"));



        notice_write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),WriteActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mBoardList = new ArrayList<>();

        db.collection("board").orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value != null) {
                            mBoardList.clear();
                            for (DocumentSnapshot snapshot : value.getDocuments()) {

                                Map<String, Object> shot = snapshot.getData();
                                String id = (String) shot.get("id");
                                String title = (String) shot.get("title");
                                String contents = (String) shot.get("contents");
                                Board data = new Board(id, title, contents);

                                mBoardList.add(data);
                            }
                            mNoticeAdapter = new NoticeAdapter(mBoardList);
                            mNoticeRecyclerView.setAdapter(mNoticeAdapter);
                        }
                    }

                });



    }

    private class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>{

        private List<Board> mBoardList;

        public NoticeAdapter(List<Board> mBoardList) {
            this.mBoardList = mBoardList;
        }

        @NonNull
        @Override
        public NoticeAdapter.NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new NoticeAdapter.NoticeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noice,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull NoticeAdapter.NoticeViewHolder holder, int position) {
            Board data = mBoardList.get(position);
            holder.mTitleTextView.setText(data.getTitle());
            holder.mNameTextView.setText("작성자 : "+data.getId());
        }

        @Override
        public int getItemCount() {
            return mBoardList.size();
        }

        class NoticeViewHolder extends RecyclerView.ViewHolder{

            private TextView mTitleTextView;
            private TextView mNameTextView;
            public NoticeViewHolder(View itemView){
                super(itemView);

                mTitleTextView = itemView.findViewById(R.id.notice_item_title_text);
                mNameTextView = itemView.findViewById(R.id.notice_item_name_text);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            Intent intent = new Intent(getApplicationContext(),SelectBoardActivity.class);
                            startActivity(intent);

                        }
                    }
                });
            }
        }
    }
}