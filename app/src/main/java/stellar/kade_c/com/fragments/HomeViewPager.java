package stellar.kade_c.com.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import stellar.kade_c.com.stellar.R;

/**
 * Handles the Slide View
 */
public class HomeViewPager extends Fragment {

    public interface FragmentSwipeItf {
        void fragmentBecameVisible();
    }

    View view;

    ViewPager mViewPager;
    HomePagerAdapter mHomePagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.home_vp, container, false);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);

        createViewPager(mViewPager);

        return view;
    }

    /**
     * Creates and sets our slides in our ViewPager.
     */
    private void createViewPager(ViewPager viewPager) {
        mHomePagerAdapter = new HomePagerAdapter(getActivity().getSupportFragmentManager());
        mHomePagerAdapter.addFrag(new APD(), "APD");
        mHomePagerAdapter.addFrag(new APGallery(), "Gallery");

        viewPager.setAdapter(mHomePagerAdapter);

        // Handles the catch of swipes in order to update views.
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int i, final float v, final int i2) {
            }
            @Override
            public void onPageSelected(final int i) {
                FragmentSwipeItf fragment = (FragmentSwipeItf) mHomePagerAdapter.instantiateItem(mViewPager, i);
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                }
            }
            @Override
            public void onPageScrollStateChanged(final int i) {
            }
        });

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Our ViewPager adapter, handles the change of pages.
     */
    public class HomePagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
}
