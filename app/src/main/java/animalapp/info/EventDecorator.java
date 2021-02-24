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
    private final Drawable drawable;
    private int color;
    private HashSet<CalendarDay> dates;
    private TextView textView;
    public EventDecorator(Collection<CalendarDay> dates, Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.more);

        this.dates = new HashSet<>(dates);
        this.textView = textView;
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
