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
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentAdapter adapter;

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
        tabLayout.getTabAt(1).setText("게시판");
        tabLayout.getTabAt(2).setText("지도");

//        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1) ;
//        tabHost1.setup() ;
//
//        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
//        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1") ;
//        ts1.setContent(R.id.content1) ;
//        ts1.setIndicator("TAB 1") ;
//        tabHost1.addTab(ts1)  ;
//
//        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
//        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2") ;
//        ts2.setContent(R.id.content2) ;
//        ts2.setIndicator("TAB 2") ;
//        tabHost1.addTab(ts2) ;
//
//        // 세 번째 Tab. (탭 표시 텍스트:"TAB 3"), (페이지 뷰:"content3")
//        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3") ;
//        ts3.setContent(R.id.content3) ;
//        ts3.setIndicator("TAB 3") ;
//        tabHost1.addTab(ts3) ;
    }





        public boolean onCreateOptionsMenu(Menu menu) {

            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu, menu);
            return true;
        }


        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_settings1:
                    Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
                    return true;

            }
            return super.onOptionsItemSelected(item);
        }

    }
