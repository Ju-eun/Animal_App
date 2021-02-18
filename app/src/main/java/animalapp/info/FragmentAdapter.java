package animalapp.info;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList=new ArrayList<>();

    public FragmentAdapter(FragmentManager fm, int behavior){
        super(fm, behavior);
    }

    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);
    }

    public Fragment getItem(int position){
        return fragmentList.get(position);
    }

    public int getCount(){
        return fragmentList.size();
    }
}