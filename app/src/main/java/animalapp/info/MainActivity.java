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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<Integer> imageList;
    private static final int DP = 24;
    Toolbar toolbar;
    ViewPager viewPager;
    ImageButton btn_cal, btn_map, btn_notice, btn_exit;
    Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("하이"); //타이틀 없음


        this.initializeData();

        viewPager = findViewById(R.id.pager);
        viewPager.setClipToPadding(false);

        float density = getResources().getDisplayMetrics().density;
        int margin = (int) (DP * density);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin/2);
        viewPager.setAdapter(new ViewPagerAdapter(this, imageList));


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
//       btn_cal.setOnClickListener(listener);

    }

    private void initializeData() {
        imageList = new ArrayList();

        imageList.add(R.drawable.slider1);
        imageList.add(R.drawable.slider2);
        imageList.add(R.drawable.slider3);
    }
//
//    View.OnClickListener listener= new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                intent= new Intent(MainActivity.this,CalendarActivity.class);
//                startActivity(intent);
//            }
//        };

    public boolean onCreateOptionsMenu(Menu menu) {

            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu, menu);
            return true;
        }


        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.action_mypage:
                    intent = new Intent(this, SignUpActivity.class);
                    startActivity(intent);
//                                Toast.makeText(getApplicationContext(), "Test222", Toast.LENGTH_LONG).show();

                case R.id.action_logout:
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    FirebaseAuth.getInstance().signOut();
//                    finish();

                    return true;

            }
            return super.onOptionsItemSelected(item);
        }

    }
