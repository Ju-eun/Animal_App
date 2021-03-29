package animalapp.info;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<Integer> imageList; //슬라이드 이미지 넣을 배열
    private static final int DP = 24; //ui 컴포넌트의 크기 24로 지정
    /*
    * px(Pixel) : 화면 해상도와 관계 없이 컴포넌트의 크기를 절대값으로 세팅하는 단위
    * dp(Density-independent Pixels) : 화면의 해상도에 따라 크기가 조정되는 단위
    * sp(Scale-independent Pixels) : dp와 비슷한 원리로 동작, 사용자의 글자 크기 조정할 때 사용되는 단위
    * */
    Toolbar toolbar;
    ViewPager viewPager;
    ImageButton btn_cal, btn_map, btn_notice, btn_exit;
    Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent =new Intent(this, introActivity.class);
        startActivity(intent);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("하이"); //타이틀 없음


        this.initializeData();

        viewPager = findViewById(R.id.pager);
        viewPager.setClipToPadding(false); //좌우 화면이 약간 보이게 하기위한 메서드

        float density = getResources().getDisplayMetrics().density;//https://selfish-developer.com/tag/getDisplayMetrics%28%29.density
                                                                   //위에 사이트 참조ㅎㅎ 어떻게 요약할지 몰라서,, 귀찮아서 그런거 아님,,
        int margin = (int) (DP * density);
        viewPager.setPadding(margin, 0, margin, 0);//뷰페이저의 패딩, 마진 주기
        viewPager.setPageMargin(margin/2);
        viewPager.setAdapter(new ViewPagerAdapter(this, imageList)); //ViewPagerAdapter 연결

        btn_cal=findViewById(R.id.img_btn_cal);
        btn_notice=findViewById(R.id.img_btn_notice);
        btn_map=findViewById(R.id.img_btn_map);
        btn_exit=findViewById(R.id.img_btn_exit);

        btn_notice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                intent = new Intent(MainActivity.this,NoticeActivity.class);
                startActivity(intent);
            }
        });


        btn_map.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        btn_cal.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //버튼 눌렀을 때 알림창 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("정말로 종료하시겠습니까?");
                builder.setTitle("종료 알림창")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("종료 알림창");
                alert.show();
            }
        });

    }

    //이미지배열에 이미지추가
    private void initializeData() {
        imageList = new ArrayList();

        imageList.add(R.drawable.slider1);
        imageList.add(R.drawable.slider2);
        imageList.add(R.drawable.slider3);
    }

    //옵션 메뉴(우측 상단) 인플레이트
    public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu, menu);
            return true;
        }


        //해당 메뉴 눌렀을 때 작동
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.action_mypage:
                    intent = new Intent(this,MyPage.class);
                    startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Test222", Toast.LENGTH_LONG).show();
                                break;

                case R.id.action_logout:
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    FirebaseAuth.getInstance().signOut(); //로그아웃


                    return true;

            }
            return super.onOptionsItemSelected(item);
        }

    }
