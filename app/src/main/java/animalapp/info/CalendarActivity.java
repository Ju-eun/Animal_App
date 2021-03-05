package animalapp.info;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.w3c.dom.Document;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CalendarActivity extends AppCompatActivity {

    ArrayList<String> index;
    ArrayList<String> index_icon;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    MaterialCalendarView calendarView;
    TextView user_name,tv;
    FirebaseAuth mAuth;


    ArrayList<CalendarDay> calendarDayList;
    String DATE, DATE1,DB_DATE;
    RadioButton[] radio;
    RadioGroup radioGroup;
    EditText memo_et;
    int year;
    int month;
    int day;
    int state;

    String str_month;
    String str_day;
   String pet_state;
   String doc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        user_name=(TextView)findViewById(R.id.user_name);

        firebaseAuth = FirebaseAuth.getInstance();

        LayoutInflater inflater= getLayoutInflater();
        View dialogView= inflater.inflate(R.layout.dialog,null);
        radio= new RadioButton[]
                {
                        (RadioButton) dialogView.findViewById(R.id.sick_state),
                        (RadioButton)dialogView.findViewById(R.id.mok_state),
                        (RadioButton) dialogView.findViewById(R.id.sick_mok_state),
                        (RadioButton) dialogView.findViewById(R.id.special_state)
                };
        radioGroup= (RadioGroup)dialogView.findViewById(R.id.radiogroup);
        memo_et=(EditText)dialogView.findViewById(R.id.et);
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
        callData();// 달력 이미지 표시

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
                if(month<10)
                {
                   str_month="0"+month+"";
                }
                else
                {
                    str_month=month+"";
                }
                if(day<10)
                {
                    str_day= "0"+day+"";
                }
                else
                {
                    str_day=day+"";
                }

                DB_DATE=(year+""+str_month+str_day);
                calendarDayList=new ArrayList<>();
                calendarDayList.add(CalendarDay.from(year,month-1,day));//월-1


                AlertDialog.Builder builder= new AlertDialog.Builder(CalendarActivity.this);

                builder.setTitle(DATE1);
                builder.setMessage("메모");
                if (dialogView.getParent() != null)
                    ((ViewGroup) dialogView.getParent()).removeView(dialogView);
                builder.setView(dialogView);


                db_check();// 라디오 버튼, text 불러오기


                builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(radio[0].isChecked())
                        {
                            state=1;
                           pet_state="아픔";
                        }
                        else if(radio[1].isChecked())
                        {
                            state=2;
                            pet_state="목욕";

                        }
                        else if(radio[2].isChecked())
                        {
                            state=3;
                            pet_state="둘다";

                        }
                        else if(radio[3].isChecked()){
                            state=4;
                            pet_state="특별한 날";
                        }
                        else {
                            state=5;
                            Toast.makeText(CalendarActivity.this, "애견 상태를 체크 안하셨습니다.", Toast.LENGTH_SHORT).show();
                        }

                        EventDecorator eventDecorator= new EventDecorator(calendarDayList,CalendarActivity.this,state);
                        calendarView.addDecorator(eventDecorator);

                        update();
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();

            }
        });
        getData();//달력이름






    }
    private void getData(){
        user= FirebaseAuth.getInstance().getCurrentUser();//현재 로그인한 유저 ->Authentication(?)
        db=FirebaseFirestore.getInstance();//firestore db
        if(user.getEmail()!=null)
        {
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
        else{
            finish();
        }

    }

    private void putData()
    {

        Map<String, Object> calendar = new HashMap<>();
        calendar.put("date",DB_DATE);
        calendar.put("memo",memo_et.getText().toString());
        calendar.put("pet_state",pet_state);
        calendar.put("icon",state+"");


        if(user != null) {

            db.collection("calendar").document(user.getUid()).collection("date").document()
                    .set(calendar)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(CalendarActivity.this, "success", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CalendarActivity.this, "fail", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }




    private void callData(){

        user= FirebaseAuth.getInstance().getCurrentUser();//현재 로그인한 유저 ->Authentication(?)
        db=FirebaseFirestore.getInstance();//firestore db
            if(firebaseAuth.getCurrentUser()!=null)
            {
                db.collection("calendar")
                        .document(user.getUid())
                        .collection("date")
                        .get()//가져와
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                  index = new ArrayList<>();
                                  index_icon= new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        index.add(document.get("date").toString());
                                        index_icon.add(document.get("icon").toString());
                                        Toast.makeText(CalendarActivity.this, index.get(0), Toast.LENGTH_SHORT).show();
                                        if(index.get(0)==null)
                                        {
                                            Toast.makeText(CalendarActivity.this, "초기상태입니다.", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(CalendarActivity.this, "초기상태가 아닙니다.", Toast.LENGTH_SHORT).show();
                                        }
                                        String[] array= index.toArray(new String[index.size()]);
                                        String[] array_icon= index_icon.toArray(new String[index_icon.size()]);
                                        int cal_year[]=new int[index.size()];
                                        int cal_month[]=new int[index.size()];
                                        int cal_day[]=new int[index.size()];
                                        calendarDayList=new ArrayList<>();
                                        for(int j=0; j<array.length;j++)
                                        {
                                            calendarDayList=new ArrayList<>();
                                            cal_day[j]= Integer.parseInt(array[j].substring(6));
                                            cal_month[j]=Integer.parseInt(array[j].substring(4,6));
                                            cal_year[j]=Integer.parseInt(array[j].substring(0,4));
                                            calendarDayList.add(CalendarDay.from(cal_year[j],cal_month[j]-1, cal_day[j]));//월-1
                                            EventDecorator eventDecorator= new EventDecorator(calendarDayList,CalendarActivity.this, Integer.parseInt(array_icon[j]));
                                            calendarView.addDecorator(eventDecorator);

                                        }
                                    }

                                }

                            }
                        });
            }

            else{
                finish();
            }




    }
    private void db_check()
    {
        radioGroup.clearCheck();
        memo_et.setText("");
        user= FirebaseAuth.getInstance().getCurrentUser();//현재 로그인한 유저 ->Authentication(?)
        db=FirebaseFirestore.getInstance();//firestore db
        if(firebaseAuth.getCurrentUser()!=null)
        {
            db.collection("calendar")
                    .document(user.getUid())
                    .collection("date")
                    .whereEqualTo("date",DB_DATE)
                    .get()//가져와
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override

                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot documentSnapshot : task.getResult()){
                                        doc= documentSnapshot.getId();
                                        memo_et.setText((CharSequence)documentSnapshot.get("memo"));
                                        radio[(Integer.parseInt(documentSnapshot.get("icon").toString()))-1].setChecked(true);
                                }

                            }

                        }
                    });
        }

        else{
            finish();
        }

    }
    private void update()
    {
        if(firebaseAuth.getCurrentUser()!=null)
        {
                db.collection("calendar").document(user.getUid())
                        .collection("date")
                        .document(doc+"")
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CalendarActivity.this, "업데이트 성공", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CalendarActivity.this, "업데이트 실패", Toast.LENGTH_SHORT).show();
                            }
                        });

                putData();


        }

        else{
            finish();
        }

    }
}
