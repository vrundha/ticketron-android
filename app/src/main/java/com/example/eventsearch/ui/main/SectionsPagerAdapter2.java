package com.example.eventsearch.ui.main;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.os.Bundle;

        import androidx.annotation.Nullable;
        import androidx.annotation.StringRes;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentManager;
        import androidx.fragment.app.FragmentPagerAdapter;

        import com.example.eventsearch.ArtistTabFragment;
        import com.example.eventsearch.EventTabFragment;
        import com.example.eventsearch.FavoriteFragment;
        import com.example.eventsearch.R;
        import com.example.eventsearch.SearchFragment;
        import com.example.eventsearch.VenueTabFragment;
        import com.google.android.material.tabs.TabLayout;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter2 extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_3, R.string.tab_text_4, R.string.tab_text_5};
    private final Context mContext;
    private Bundle mBundle;

    public SectionsPagerAdapter2(Context context, FragmentManager fm, Bundle bundle) {
        super(fm);
        mContext = context;
        mBundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
//        return PlaceholderFragment.newInstance(position + 1);

        Fragment fragment = null;
        switch(position) {
            case  0:
                fragment = new EventTabFragment();
                fragment.setArguments(mBundle);
                break;
            case  1: fragment = new ArtistTabFragment();
                fragment.setArguments(mBundle);
                break;
            case  2: fragment = new VenueTabFragment();
                fragment.setArguments(mBundle);
                break;
        }
        return  fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }



    @Override
    public int getCount() {
        return 3;
    }
}