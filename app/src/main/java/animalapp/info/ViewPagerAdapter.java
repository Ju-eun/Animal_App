package animalapp.info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;


public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Integer> imageList;

    //각 데이터 항목에 해당하는 뷰를 생성하기위해 어댑터 생성(MainActivity와 연결)
    public ViewPagerAdapter(Context context, ArrayList<Integer> imageList)
    {
        this.mContext = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    //데이터 리스트에서 인자로 넘어온 position에 해당하는
    //아이템 항목에 대한 페이지 생성
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewpager_activity, null);

        ImageView imageView = view.findViewById(R.id.img_view);
        imageView.setImageResource(imageList.get(position));

        container.addView(view);

        return view;
    }

    //Adpater가 관리하는 데이터 리스트의 총개수
    @Override
    public int getCount() {
        return imageList.size();
    }


    //Adapter가 관리하는 데이터 리스트에서 인자로 넘어온 position에 해당하는
    //데이터 항목을 생성된 페이지 제거
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }


    //페이지가 특정 키와 연관되는지 체크
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (View)o);
    }
}
