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
    Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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
