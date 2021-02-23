package animalapp.info;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NoticeActivity extends AppCompatActivity {

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

        mBoardList = new ArrayList<>();
        mBoardList.add(new Board(null,"반갑습니다 여러분",null,"android"));
        mBoardList.add(new Board(null,"Hello",null,"server"));
        mBoardList.add(new Board(null,"ok",null,"php"));
        mBoardList.add(new Board(null,"zzz",null,"java"));

        mNoticeAdapter = new NoticeAdapter(mBoardList);
        mNoticeRecyclerView.setAdapter(mNoticeAdapter);

        notice_write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),WriteActivity.class);
                startActivity(intent);
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
            holder.mNameTextView.setText(data.getName());
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
            }
        }
    }
}