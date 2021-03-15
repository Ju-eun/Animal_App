package animalapp.info;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.audiofx.NoiseSuppressor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

public class NoticeActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView mNoticeRecyclerView;
    private FloatingActionButton notice_write_btn;
    private RecyclerView.LayoutManager mLayoutmanager;
    private NoticeAdapter mNoticeAdapter;
    private List<Board> mBoardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        mNoticeRecyclerView = (RecyclerView)findViewById(R.id.notice_recycler_view);
        notice_write_btn = (FloatingActionButton) findViewById(R.id.notice_write_btn);

        mLayoutmanager=new LinearLayoutManager(this);
        mNoticeRecyclerView.setHasFixedSize(true);//추가
        mNoticeRecyclerView.setLayoutManager(mLayoutmanager);

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
        db = FirebaseFirestore.getInstance();
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
                                String view=(String)shot.get("view");
                                String Uid = (String)shot.get("UID");
                                String board_fileName=(String)shot.get("board_fileName");
                                Board data = new Board(id, title, contents, view, Uid, board_fileName);

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
            Glide.with(holder.itemView.getContext())
                    .load(mBoardList.get(position).getView())
                    .into(holder.write_image);
            holder.mTitleTextView.setText(data.getTitle());
            holder.mNameTextView.setText("작성자 : "+data.getId());
        }

        @Override
        public int getItemCount() {
            return mBoardList.size();
        }

        class NoticeViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

            private ImageView write_image;
            private TextView mTitleTextView;
            private TextView mNameTextView;
            public NoticeViewHolder(View itemView){
                super(itemView);

                mTitleTextView = itemView.findViewById(R.id.notice_item_title_text);
                mNameTextView = itemView.findViewById(R.id.notice_item_name_text);
                write_image=itemView.findViewById(R.id.write_image);

//               if(mBoardList.get(pos).getId().equals(Uid)){
                itemView.setOnCreateContextMenuListener(this);
//               }

                itemView.setClickable(true);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        db= FirebaseFirestore.getInstance();
                        db.collection("board").document();
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            Intent intent = new Intent(getApplicationContext(),SelectBoardActivity.class);
                            intent.putExtra("title",mBoardList.get(pos).getTitle());
                            intent.putExtra("contents",mBoardList.get(pos).getContents());
                            intent.putExtra("id",mBoardList.get(pos).getId());
                            intent.putExtra("Uid",mBoardList.get(pos).getUid());
                            intent.putExtra("view",mBoardList.get(pos).getView());
                            intent.putExtra("board_fileName",mBoardList.get(pos).getBoard_fileName());

                            startActivity(intent);
                            //Toast.makeText(getApplicationContext(),(pos+1) +"번째 아이템 클릭", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }


            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    MenuItem Delete=menu.add(Menu.NONE,1001,1,"삭제");
                    Delete.setOnMenuItemClickListener(onEditMenu);

            }

            public final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch(item.getItemId()){
                        case 1001:
                            break;
                    }
                    return true;
                }
            };
        }
    }
}