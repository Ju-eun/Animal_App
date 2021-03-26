package animalapp.info;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

public class EventDecorator implements DayViewDecorator {//날짜 아이콘 표시,DayViewDecorator Interface를 구현해서 두개의 shouldDecorate , decorate 오버라이드 함수를 가진다.
    private  Drawable drawable;
    private int color;
    private HashSet<CalendarDay> dates;
    private TextView textView;
    public EventDecorator(Collection<CalendarDay> dates, Activity context, int state) {

        if(state==1)
        {
            drawable = context.getResources().getDrawable(R.drawable.sick);
        }
        else if(state==2)
        {
            drawable = context.getResources().getDrawable(R.drawable.mok);
        }
        else if(state==3)
        {
            drawable= context.getResources().getDrawable(R.drawable.sickmok);
        }
        else if(state==4)
        {
            drawable=context.getResources().getDrawable(R.drawable.spe);
        }
        else if(state==5)
        {
            drawable=context.getResources().getDrawable(R.drawable.dogfoot);
        }
        this.dates = new HashSet<>(dates);

    }



    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }
    //캘린더의 모든 날짜를 띄울 때 decoration이 필요한 지 판단하고 띄움
    @Override
    public void decorate(DayViewFacade view) {// decorate를 이용해 커스터마이징
        view.setSelectionDrawable(drawable);//아이콘 그림 띄우기
        view.addSpan(new DotSpan(5,color));//날짜 밑에 점
    }
    public void setText(String text){
        textView.setText(text);
    }
}
