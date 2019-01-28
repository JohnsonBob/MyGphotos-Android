package io.gphotos.gin.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import io.gphotos.gin.ui.GalleryFragment;
import io.gphotos.gin.ui.StatusFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    public int getCount() {
        return 2;
    }

    public MainViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new StatusFragment();
            case 1:
                return new GalleryFragment();
            default:
                return null;
        }
    }
}
