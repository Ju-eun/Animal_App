package animalapp.info;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    private ViewPager viewPager;
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

        viewPager = findViewById(R.id.pager);
        btn_cal=findViewById(R.id.img_btn_cal);
        btn_notice=findViewById(R.id.img_btn_notice);
        btn_map=findViewById(R.id.img_btn_map);
        btn_exit=findViewById(R.id.img_btn_exit);

        btn_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),NoticeActivity.class);
                startActivity(intent);
            }
        });

        btn_map.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

            }
        });
       btn_cal.setOnClickListener(listener);


    }
        View.OnClickListener listener= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent= new Intent(MainActivity.this,CalendarActivity.class);
                startActivity(intent);
            }
        };

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
