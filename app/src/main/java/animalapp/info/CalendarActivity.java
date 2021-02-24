package animalapp.info;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CalendarActivity extends AppCompatActivity {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
    FirebaseUser user;
    FirebaseFirestore db;
    MaterialCalendarView calendarView;
    TextView user_name,tv;
    FirebaseAuth mAuth;
    ArrayList<CalendarDay> calendarDayList ;
    String DATE, DATE1;
    int year;
    int month;
    int day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        user_name=(TextView)findViewById(R.id.user_name);
        tv=(TextView)findViewById(R.id.tv);
        calendarView= (MaterialCalendarView)findViewById(R.id.materialCalendar);
        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        calendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new onDatDecorator()
        );

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                DATE=date.toString();
                String[] parsedDATE= DATE.split("[{]");// CalendarDay{0000-00-00} 으로 나와서 split함
                parsedDATE = parsedDATE[1].split("[}]");
                parsedDATE = parsedDATE[0].split("-");
                year = Integer.parseInt(parsedDATE[0]);
                month = Integer.parseInt(parsedDATE[1])+1;
                day = Integer.parseInt(parsedDATE[2]);
                DATE1=(year+""+"년"+" "+month+""+"월"+" "+day+""+"일"+" ");


                AlertDialog.Builder builder= new AlertDialog.Builder(CalendarActivity.this);
                builder.setTitle(DATE1);
                builder.setMessage("일정을 추가하세요");
                builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //추가 눌렀을 때
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
               // Toast.makeText(CalendarActivity.this, DATE1, Toast.LENGTH_SHORT).show();

            }
        });
        getData();
        calendarDayList=new ArrayList<>();
       calendarDayList.add(CalendarDay.today());
        calendarDayList.add(CalendarDay.from(2021,02,25));//월-1

        EventDecorator eventDecorator= new EventDecorator(calendarDayList,this);

        calendarView.addDecorator(eventDecorator);
    }
    private void getData(){
        user= FirebaseAuth.getInstance().getCurrentUser();//현재 로그인한 유저 ->Authentication(?)
        db=FirebaseFirestore.getInstance();//firestore db
        final String current= user.getEmail();//로그인할 때 그 이메일 가져옴
        db.collection("users")//firestore users
                .whereEqualTo("id",current)//firestore id와 user email같은 곳?
                .get()//가져와
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot documentSnapshot : task.getResult()){
                                user_name.setText((CharSequence) documentSnapshot.get("pet_name")+"의 달력");
                            }
                        }
                    }
                });
    }
    private void memoData(){

    }



}