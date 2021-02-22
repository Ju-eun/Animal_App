package animalapp.info;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentAdapter adapter;
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
        tabLayout = findViewById(R.id.tab_layout);
        adapter=new FragmentAdapter(getSupportFragmentManager(),2);

        adapter.addFragment(new fragment_calendar());
        adapter.addFragment(new fragment_notice());
        adapter.addFragment(new fragment_map());

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("달력");
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_calendar);
        tabLayout.getTabAt(1).setText("게시판");
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_notice);
        tabLayout.getTabAt(2).setText("지도");
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_map);


        if (savedInstanceState == null) {

            fragment_map mapFragment = new fragment_map();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.pager, mapFragment, "main")
                    .commit();
        }



    }


        public boolean onCreateOptionsMenu(Menu menu) {

            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu, menu);
            return true;
        }


        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_settings1:
                    intent= new Intent(this, LoginActivity.class);
                    startActivity(intent);
//                    Toast.makeText(getApplicationContext(), "Test111", Toast.LENGTH_LONG).show();
                    return true;

                case R.id.action_settings2:
                    intent = new Intent(this, SignUpActivity.class);
                    startActivity(intent);
//                                Toast.makeText(getApplicationContext(), "Test222", Toast.LENGTH_LONG).show();

                case R.id.action_settings3:
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    FirebaseAuth.getInstance().signOut();
//                    finish();

                    return true;

            }
            return super.onOptionsItemSelected(item);
        }

    }
