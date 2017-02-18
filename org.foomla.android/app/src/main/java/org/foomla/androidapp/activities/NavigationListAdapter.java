package org.foomla.androidapp.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Lists;

import org.foomla.androidapp.R;

import java.util.List;

public class NavigationListAdapter extends BaseAdapter {

    public enum NavigationItem {

        DIVIDER_MISC(R.string.menu_category_misc, true), DIVIDER_TRAINING(R.string.menu_category_training, true), EXERCISE_CATALOG(
                R.string.menu_exercise_catalog, R.drawable.catalog), HOME(R.string.menu_home, R.drawable.catalog), INFO(
                R.string.menu_info, R.drawable.info), NEW_TRAINING(R.string.menu_new_training, R.drawable.training), TRAININGS(R.string.menu_trainings, R.drawable.favorites);

        public boolean divider;
        public int icon;
        public int title;

        NavigationItem(int title, boolean divider) {
            this.icon = 0;
            this.title = title;
            this.divider = divider;
        }

        NavigationItem(int title, int icon) {
            this.icon = icon;
            this.title = title;
            this.divider = false;
        }

    }

    private static final List<NavigationItem> NAVIGATION_ITEMS = Lists.newArrayList(NavigationItem.HOME,
            NavigationItem.DIVIDER_TRAINING, NavigationItem.NEW_TRAINING, NavigationItem.TRAININGS,
            NavigationItem.EXERCISE_CATALOG, NavigationItem.DIVIDER_MISC, NavigationItem.INFO);

    public static NavigationItem getItemByIndex(int index) {
        if (index >= 0 && index < NAVIGATION_ITEMS.size()) {
            return NAVIGATION_ITEMS.get(index);
        } else {
            return null;
        }
    }

    protected final Context context;

    public NavigationListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return NAVIGATION_ITEMS.size();
    }

    @Override
    public NavigationItem getItem(int index) {
        return getItemByIndex(index);
    }

    @Override
    public long getItemId(int index) {
        return -1;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {

        View view = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        NavigationItem navigationItem = getItem(index);

        if (!navigationItem.divider) {
            view = inflater.inflate(R.layout.navigation_list_item, parent, false);
        } else {
            view = inflater.inflate(R.layout.navigation_list_item_divider, parent, false);
        }

        TextView textView = (TextView) view.findViewById(R.id.navigationItemTitle);
        textView.setText(navigationItem.title);

        if (navigationItem.icon != 0) {
            Drawable drawable = context.getResources().getDrawable(navigationItem.icon);

            ImageView imageView = (ImageView) view.findViewById(R.id.navigationItemIcon);
            imageView.setImageDrawable(drawable);
        }

        return view;
    }

}
