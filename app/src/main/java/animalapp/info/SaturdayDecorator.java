package animalapp.info;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

public class SaturdayDecorator implements DayViewDecorator {//토요일 날짜 표시, DayViewDecorator Interface를 구현해서 두개의 shouldDecorate , decorate 오버라이드 함수를 가진다.
    private final Calendar calendar = Calendar.getInstance();//현재 날짜 가져옴
    public SaturdayDecorator() {
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {//캘린더의 모든 날짜를 띄울 때 decoration이 필요한 지 판단하고 띄움
        day.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay == Calendar.SATURDAY;//토요일 출력
    }

    @Override
    public void decorate(DayViewFacade view) {// decorate를 이용해 커스터마이징
        view.addSpan(new ForegroundColorSpan(Color.BLUE));//토요일 파란색으로 지정
    }
}
