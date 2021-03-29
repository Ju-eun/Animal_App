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
import android.util.Log;
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
    private FloatingActionButton notice_write_btn; //화면이 움직여도 화면의 최상위에 고정되어 있도록 하려고 사용
    private RecyclerView.LayoutManager mLayoutmanager;
    private NoticeAdapter mNoticeAdapter;
    private List<Board> mBoardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        mNoticeRecyclerView = findViewById(R.id.notice_recycler_view);
        notice_write_btn = findViewById(R.id.notice_write_btn);

        mLayoutmanager=new LinearLayoutManager(this);
        mNoticeRecyclerView.setHasFixedSize(true); //리사이클러뷰 안 아이템들의 크기를 가변적으로 바꿀지 일정한 크기를 사용할지 결정
        mNoticeRecyclerView.setLayoutManager(mLayoutmanager);//리사이클러뷰에 적용


        //버튼 눌렀을 때 WriteActivity로 이동(글쓰기)
        notice_write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),WriteActivity.class);
                startActivity(intent);
            }
        });

    }

    //게시판 초기화면(목록)
    @Override
    protected void onStart() {
        super.onStart();
        mBoardList = new ArrayList<>(); //게시판 목록 저장하는 배열
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
                            mNoticeRecyclerView.setAdapter(mNoticeAdapter); //어댑터 연결
                        }
                    }

                });



    }

    //데이터 집합을 관리하고 뷰를 생성하는 부분
    private class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>{

        private List<Board> mBoardList;

        public NoticeAdapter(List<Board> mBoardList) {
            this.mBoardList = mBoardList;
        }

        @NonNull
        @Override
        //xml파일을 inflate하여 ViewHolder 생성
        public NoticeAdapter.NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new NoticeAdapter.NoticeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noice,parent,false));
        }


        //한 행에 들어갈 데이터 set
        @Override
        public void onBindViewHolder(@NonNull NoticeAdapter.NoticeViewHolder holder, int position) {
            Board data = mBoardList.get(position);
            Glide.with(holder.itemView.getContext())
                    .load(mBoardList.get(position).getView())
                    .into(holder.write_image); //이미지 가져오기 (이미지는 Glide.with로 가져와야함)
            holder.mTitleTextView.setText(data.getTitle()); //글제목 가져오기
            holder.mNameTextView.setText("작성자 : "+data.getId()); //작성자 가져오기
        }

        //게시판에 존재하는 행의 총 개수
        @Override
        public int getItemCount() {
            return mBoardList.size();
        }

        //각 list에 들어갈 객체의 멤버변수
        class NoticeViewHolder extends RecyclerView.ViewHolder{

            //한 행에 보여지는 데이터
            private ImageView write_image;
            private TextView mTitleTextView;
            private TextView mNameTextView;
            public NoticeViewHolder(View itemView){
                super(itemView);

                mTitleTextView = itemView.findViewById(R.id.notice_item_title_text);
                mNameTextView = itemView.findViewById(R.id.notice_item_name_text);
                write_image=itemView.findViewById(R.id.write_image);

//               }

                itemView.setClickable(true); //리스트 클릭할 수 있도록 함
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        db= FirebaseFirestore.getInstance();
                        db.collection("board").document();
                        int pos = getAdapterPosition(); //클릭한 아이템의 위치값 저장하는 변수
                        if(pos != RecyclerView.NO_POSITION){
                            //해당 줄에 입력되어 있던 데이터를 불러와 SelectBoardActivity로 값 넘겨줌
                            Intent intent = new Intent(getApplicationContext(),SelectBoardActivity.class);
                            intent.putExtra("title",mBoardList.get(pos).getTitle());
                            intent.putExtra("contents",mBoardList.get(pos).getContents());
                            intent.putExtra("id",mBoardList.get(pos).getId());
                            intent.putExtra("Uid",mBoardList.get(pos).getUid());
                            intent.putExtra("view",mBoardList.get(pos).getView());
                            intent.putExtra("board_fileName",mBoardList.get(pos).getBoard_fileName());

                            startActivity(intent);
                        }
                    }
                });
            }
        }
    }
}