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

public class EventDecorator implements DayViewDecorator {
    private  Drawable drawable;
    private int color;
    private HashSet<CalendarDay> dates;
    private TextView textView;
    public EventDecorator(Collection<CalendarDay> dates, Activity context, int state) {
        //drawable=context.getResources().getDrawable(R.drawable.more);
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
            drawable=context.getResources().getDrawable(R.drawable.clip);
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

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
        view.addSpan(new DotSpan(5,color));
    }
    public void setText(String text){
        textView.setText(text);
    }
}
