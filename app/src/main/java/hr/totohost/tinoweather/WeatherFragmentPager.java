package hr.totohost.tinoweather;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class WeatherFragmentPager extends FragmentPagerAdapter {

    /**
     * Contains all the fragments.
     */
    private List<Fragment> fragments = new ArrayList<>();


    /**
     * Creates a new PagerAdapter instance.
     *
     * @param fragmentManager The FragmentManager.
     */
    public WeatherFragmentPager(FragmentManager fragmentManager) {
        super(fragmentManager);
    }


    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "View " + position;
    }

    /**
     * Adds the fragment to the list
     *
     * @param fragment New instance of the Fragment to be associated with this tab.
     */
    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }

    public void clearFragments() {
        fragments.clear();
    }

}