package animalapp.info;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class fragment_notice extends Fragment {

    private View mview;
    private RecyclerView mNoticeRecyclerView;
    private FloatingActionButton notice_write_btn;

    private NoticeAdapter mNoticeAdapter;
    private List<Board> mBoardList;

    public fragment_notice() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_notice, container, false);
        mNoticeRecyclerView = mview.findViewById(R.id.notice_recycler_view);
        notice_write_btn = mview.findViewById(R.id.notice_write_btn);

        mBoardList = new ArrayList<>();
        mBoardList.add(new Board(null,"반갑습니다 여러분",null,"android"));
        mBoardList.add(new Board(null,"Hello",null,"server"));
        mBoardList.add(new Board(null,"ok",null,"php"));
        mBoardList.add(new Board(null,"zzz",null,"java"));

        mNoticeAdapter = new NoticeAdapter(mBoardList);
        mNoticeRecyclerView.setAdapter(mNoticeAdapter);

        notice_write_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),WriteActivity.class));
            }
        });


        return mview;


    }
    private  class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>{

        private List<Board> mBoardList;

        public NoticeAdapter(List<Board> mBoardList) {
            this.mBoardList = mBoardList;
        }

        @NonNull
        @Override
        public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new NoticeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noice,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
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