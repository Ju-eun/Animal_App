package animalapp.info;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import java.util.Date;

public class onDatDecorator implements DayViewDecorator{//오늘날짜 표시, DayViewDecorator Interface를 구현해서 두개의 shouldDecorate , decorate 오버라이드 함수를 가진다.
    private CalendarDay date;

    public onDatDecorator(){
        date = CalendarDay.today();
    }//오늘날짜 저장
    @Override
    public boolean shouldDecorate(CalendarDay day) {//캘린더의 모든 날짜를 띄울 때 decoration이 필요한 지 판단하고 띄움
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {//shouldDecorate에서 넘어온 view를 decorate에서 받아 커스텀하게 뿌려줌
        view.addSpan(new StyleSpan(Typeface.BOLD));//스타일 설정
        view.addSpan(new RelativeSizeSpan(1.4f));//크기 설정
        view.addSpan(new ForegroundColorSpan(Color.GREEN));//오늘날짜 녹색
    }
    public void setDate(Date date) {
        this.date = CalendarDay.from(date);
    }
}
