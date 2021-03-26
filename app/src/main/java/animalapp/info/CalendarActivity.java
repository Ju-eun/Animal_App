package animalapp.info;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.AlteredCharSequence;
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
    TextView user_name;



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

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        getData();//달력이름
        firebaseAuth = FirebaseAuth.getInstance();//firebaseAuthetication 값을 가져옴

        LayoutInflater inflater= getLayoutInflater();//LayoutInflater: Layout xml 리소스 파일을 View 객체로 부불려 주는(inflate) LayoutInflater 객체 생성

        View dialogView= inflater.inflate(R.layout.dialog,null);// 인플레이트 함
        radio= new RadioButton[]
                {
                        (RadioButton) dialogView.findViewById(R.id.sick_state),
                        (RadioButton) dialogView.findViewById(R.id.mok_state),
                        (RadioButton) dialogView.findViewById(R.id.sick_mok_state),
                        (RadioButton) dialogView.findViewById(R.id.special_state),
                        (RadioButton) dialogView.findViewById(R.id.not_applicable)
                };
        radioGroup= (RadioGroup)dialogView.findViewById(R.id.radiogroup);
        memo_et=(EditText)dialogView.findViewById(R.id.et);

        calendarView= (MaterialCalendarView)findViewById(R.id.materialCalendar);
        calendarView.state().edit()//달력의 시작과 끝 지정
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        calendarView.addDecorators(//데코레이터 추가
                new SundayDecorator(),//일요일-> 빨간색
                new SaturdayDecorator(),//토요일-> 파란색
                new onDatDecorator()// 오늘 날짜표시 데코레이터

        );
        callData();// 달력 이미지 표시

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {//날짜 클릭시
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {


                DATE=date.toString();//선택한 날짜값을 스트링 값으로 변수에 저장
                String[] parsedDATE= DATE.split("[{]");// CalendarDay{0000-00-00} 으로 나와서 split함
                parsedDATE = parsedDATE[1].split("[}]");
                parsedDATE = parsedDATE[0].split("-");
                year = Integer.parseInt(parsedDATE[0]);//각각의 string 변수에 년 월 일 저장
                month = Integer.parseInt(parsedDATE[1])+1;
                day = Integer.parseInt(parsedDATE[2]);
                DATE1=(year+""+"년"+" "+month+""+"월"+" "+day+""+"일"+" ");//dialog 제목을 지정: string 변수에 (0000년 00월 00일)형태로 저장
                if(month<10)//월에 해당하는 숫자가 10보다 작을 경우
                {
                   str_month="0"+month+"";//스트링 0 추가 후 스트링 형으로 변환
                }
                else
                {
                    str_month=month+"";//아닐경우 그냥 스트링으로 변환
                }
                if(day<10)//일에 해당하는 숫자가 10보다 작을 경우
                {
                    str_day= "0"+day+"";//위와 같음
                }
                else
                {
                    str_day=day+"";
                }

                DB_DATE=(year+""+str_month+str_day);//db에 추가될 정보
                calendarDayList=new ArrayList<>();// 클릭한 날짜를 저장할 배열 선언
                calendarDayList.add(CalendarDay.from(year,month-1,day));//월-1


                AlertDialog.Builder builder= new AlertDialog.Builder(CalendarActivity.this);//AlertDialog.Builder 객체 생성

                builder.setTitle(DATE1);//dialog 제목
                builder.setMessage("메모");//dialog message
                if (dialogView.getParent() != null)// dialog를 show 하기 전에 전에 있는 dialog를 지워줌
                    ((ViewGroup) dialogView.getParent()).removeView(dialogView);
                builder.setView(dialogView);//위에서 inflater가 만든 dialogView 객체 세팅 (Customize)

                db_check();// 해당날짜의 firestore db값 가져오기(라디오 버튼, 메모)


                builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(radio[0].isChecked())//아픔을 체크했을 때
                        {
                            state=1;// 달력 이미지 변수
                            pet_state="아픔";
                        }
                        else if(radio[1].isChecked())//목욕을 체크했을 때
                        {
                            state=2;
                            pet_state="목욕";

                        }
                        else if(radio[2].isChecked())//목욕, 아픔 상태일 때
                        {
                            state=3;
                            pet_state="둘다";

                        }
                        else if(radio[3].isChecked()){//특별한 날 체크했을 때
                            state=4;
                            pet_state="특별한 날";
                        }
                        else if(radio[4].isChecked()){// 해당없음을 체크했을 때
                            state=5;
                            pet_state="해당 없음";
                        }
                        else {
                            state=5;// 체크를 안했을 때
                            Toast.makeText(CalendarActivity.this, "애견 상태를 체크 안하셨습니다.", Toast.LENGTH_SHORT).show();
                        }

                        EventDecorator eventDecorator= new EventDecorator(calendarDayList,CalendarActivity.this,state); //EventDecorator 객체 생성 매개변수 넣어줌 (날짜 배열, 액티비티, 아이콘 값)
                        calendarView.addDecorator(eventDecorator);

                        update();//전에 있던 데이터 지우기
                        putData();// 현재 체크한 값과 메모 저장
                    }
                });
                builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        update();//데이터 지우기
                    }
                });
                builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();//경고창 생성

            }
        });







    }
    private void getData(){//firestore에서 달력 이름(pet name field)가져오기
        user_name=(TextView)findViewById(R.id.user_name);
        user= FirebaseAuth.getInstance().getCurrentUser();//현재 로그인한 유저 ->Authentication(?)
        db=FirebaseFirestore.getInstance();//firestore db
        if(user==null)//로그인한 유저가 없을 때 로그인 액티비티로 보냄
        {
            intent= new Intent(CalendarActivity.this,LoginActivity.class);
        }
        else{

            final String current= user.getEmail();//로그인할 때 그 이메일 가져옴
            db.collection("users")//firestore users
                    .whereEqualTo("id",current)//firestore id필드와 user email같은 곳
                    .get()//가져와
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot documentSnapshot : task.getResult()){
                                    user_name.setText((CharSequence) documentSnapshot.get("pet_name").toString()+"'s Calendar");//달력이름 가져옴
                                }
                            }

                        }
                    });
        }

    }

    private void putData()//firestore 에 저장하는 메서드
    {

        Map<String, Object> calendar = new HashMap<>();//Map: 인터페이스 HashMap: 자식 객체생성
                                                        //key와 value를 쌍으로 저장
        calendar.put("date",DB_DATE);
        calendar.put("memo",memo_et.getText().toString());
        calendar.put("pet_state",pet_state);
        calendar.put("icon",state+"");


        if(user != null) {

            db.collection("calendar").document(user.getUid()).collection("date").document()//collection(calendar)->document(uid)->collection("date")->document(자동) 문서 이름 자동생성
                    .set(calendar)//calendar 생성
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




    private void callData(){//firestore에서 달력 아이콘 가져오는 메서드

        user= FirebaseAuth.getInstance().getCurrentUser();//현재 로그인한 유저 ->Authentication
        db=FirebaseFirestore.getInstance();//firestore db
            if(firebaseAuth.getCurrentUser()!=null)//로그인한 유저가 있을 때
            {
                db.collection("calendar")
                        .document(user.getUid())
                        .collection("date")
                        .get()//가져와
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                  index = new ArrayList<>();//arraylist 초기화
                                  index_icon= new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        index.add(document.get("date").toString());//date 필드를 arraylist에 넣음
                                        index_icon.add(document.get("icon").toString());// icon 필드를 arraylist에 넣음
                                        if(index.get(0)==null)//저장되어있는 값이 없으면
                                        {
                                            Toast.makeText(CalendarActivity.this, "초기상태입니다.", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(CalendarActivity.this, "초기상태가 아닙니다.", Toast.LENGTH_SHORT).show();
                                        }
                                        String[] array= index.toArray(new String[index.size()]);//arraylist를 배열로 변환하여 array배열에 저장
                                        String[] array_icon= index_icon.toArray(new String[index_icon.size()]);//arraylist를 배열로 변환하여 array_icon배열에 저장-> icon
                                        int cal_year[]=new int[index.size()];// int형 배열 선언  ->(calendarDayList에 저장할 값)
                                        int cal_month[]=new int[index.size()];
                                        int cal_day[]=new int[index.size()];

                                        for(int j=0; j<array.length;j++)//저장되어있는 값들의 수 까지 반복
                                        {
                                            calendarDayList=new ArrayList<>();//arraylist 초기화
                                            cal_day[j]= Integer.parseInt(array[j].substring(6));//000000/ 00-> 뒤에 두자리 자르고 int형으로 변환하여 저장
                                            cal_month[j]=Integer.parseInt(array[j].substring(4,6));// 0000/00/00-> 가운데 두자리 잘라 int형으로 변환하여 저장
                                            cal_year[j]=Integer.parseInt(array[j].substring(0,4));// 0000/0000-> 앞 네자리 자르고 int형으로 변환하여 저장
                                            calendarDayList.add(CalendarDay.from(cal_year[j],cal_month[j]-1, cal_day[j]));//월-1 eventdecorator 매개변수가 arraylist이기 때문에 arraylist에 저장
                                            EventDecorator eventDecorator= new EventDecorator(calendarDayList,CalendarActivity.this, Integer.parseInt(array_icon[j]));//날짜, 액티비티, icon값
                                            calendarView.addDecorator(eventDecorator);//decorator추가

                                        }
                                    }

                                }

                            }
                        });
            }

            else{
                intent= new Intent(this,LoginActivity.class);//로그인한 유저 없으면 로그인 액티비티로 감
                startActivity(intent);
            }




    }
    private void db_check()//메모 부분과 라디오 버튼 정보 db에서 가져오는 메서드
    {
        radioGroup.clearCheck();//라디오 버튼 클리어
        memo_et.setText("");// 메모 초기화
        user= FirebaseAuth.getInstance().getCurrentUser();//현재 로그인한 유저 ->Authentication
        db=FirebaseFirestore.getInstance();//firestore db
        if(firebaseAuth.getCurrentUser()!=null)
        {
            db.collection("calendar")//캘린더 collection
                    .document(user.getUid())//현재 로그인한 유저의 id에 해당하는 문서 가져옴
                    .collection("date")//date collection
                    .whereEqualTo("date",DB_DATE)// 클릭한 날짜와 date 필드가 같은 곳을 찾아냄
                    .get()//가져와
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override

                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot documentSnapshot : task.getResult()){
                                        doc= documentSnapshot.getId();//변수에 클릭한 날짜의 id를 가져옴 <--변수에 저장해서 삭제때 쓰일 예정
                                        memo_et.setText((CharSequence)documentSnapshot.get("memo"));//메모필드에서 값 가져와서 setText

                                            radio[(Integer.parseInt(documentSnapshot.get("icon").toString()))-1].setChecked(true);//아이콘 필드에서 값 가져와서 라디오버튼 체크


                                }

                            }

                        }
                    });
        }

        else{
            intent= new Intent(this,LoginActivity.class);//로그인한 유저가 없다면 로그인 액티비티로 보냄
            startActivity(intent);
        }

    }
    private void update()//문서 지우기
    {
        if(firebaseAuth.getCurrentUser()!=null)
        {
                db.collection("calendar").document(user.getUid())
                        .collection("date")
                        .document(doc+"")//날짜를 클릭했을 때 db_check에서 받아온 문서 값을 넣음
                        .delete()//삭제
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




        }

        else{
            intent= new Intent(this,LoginActivity.class);//로그인 한 유저 없을 때 로그인 액티비티로 감
            startActivity(intent);
        }

    }
}
