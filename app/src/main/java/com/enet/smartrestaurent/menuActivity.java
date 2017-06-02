package com.enet.smartrestaurent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class menuActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Order order;

    private int[] tabIcons = {
            R.mipmap.ic_appetizer,
            R.mipmap.ic_salad,
            R.mipmap.ic_soup,
            R.mipmap.ic_sandwich
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        getActionBar().setTitle("Silver Ring Village Hotel");
        getSupportActionBar().setTitle(Html.fromHtml("<font color=#8B0000>Silver Ring Village Hotel - MENU</font>"));
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        order = new Order();
        order.setTableId(getIntent().getStringExtra("TABLE_ID"));
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        try {


            Iterator<Category> categoryIterator = Category.findAll(Category.class);
            Category category;
//        Log.d("add menu item",String.valueOf(MenuItem.findById(MenuItem.class,(long)0).price));
            while (categoryIterator.hasNext()) {
                category = categoryIterator.next();
                try {

                    Log.d("menuActivity", "get category: "+category.categoryId+":"+category.name);

                    SaladFragment fragment = new SaladFragment();
                    Bundle args = new Bundle();
                    args.putInt("index", category.categoryId);
                    fragment.setArguments(args);
                    adapter.addFrag(fragment, category.name);



                } catch (NullPointerException ex) {
                    Log.d("add menu item error", ex.toString());
                }
            }


        }
        catch (android.database.sqlite.SQLiteException ex){
            Toast.makeText(this,"No Data",Toast.LENGTH_SHORT).show();
        }
//        adapter.addFrag(new OneFragment(), "APPETIZERS");
//        adapter.addFrag(new SaladFragment(), "SALAD");
//        adapter.addFrag(new SoupFragment(), "SOUP");
//        adapter.addFrag(new SandwichFragment(), "SANDWICH");
//        adapter.addFrag(new TwoFragment(), "TWO");
//        adapter.addFrag(new ThreeFragment(), "THREE");
//        adapter.addFrag(new FourFragment(), "FOUR");
//        adapter.addFrag(new FiveFragment(), "FIVE");
//        adapter.addFrag(new SixFragment(), "SIX");
//        adapter.addFrag(new SevenFragment(), "SEVEN");
//        adapter.addFrag(new EightFragment(), "EIGHT");
//        adapter.addFrag(new NineFragment(), "NINE");
//        adapter.addFrag(new TenFragment(), "TEN");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }
    public void orderButtonClickHandler(View v){


//        String message = editText.getText().toString();
        if(order.getOrder().size()>0) {
            Intent intent = new Intent(this, ConfirmOrderActivity.class);
            intent.putExtra("ORDER", order.getOrder());
            intent.putExtra("TABLE_ID", order.getTableId());
            startActivity(intent);
        }
        else{
            Toast toast = Toast.makeText(this,"Nothing to order",Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void addButtonClickHandler(View v){
        String item =((TextView)((LinearLayout)((ViewGroup)v.getParent().getParent().getParent()).getChildAt(1)).getChildAt(0)).getText().toString();
        Log.d("menu",item);


        ((TextView) ((ViewGroup) ((ViewGroup) v.getParent().getParent().getParent()).getChildAt(2)).getChildAt(0)).setText(String.valueOf(order.addItem(item)));

    }
    public void removeButtonClickHandler(View v){
        String item =((TextView)((LinearLayout)((ViewGroup)v.getParent().getParent().getParent()).getChildAt(1)).getChildAt(0)).getText().toString();
        Log.d("menu",item);
        int newCount = order.removeItem(item);
        if(newCount>0) {
            ((TextView) ((ViewGroup) ((ViewGroup) v.getParent().getParent().getParent()).getChildAt(2)).getChildAt(0)).setText(String.valueOf(newCount));
        }
        else
            ((TextView)((ViewGroup)((ViewGroup)v.getParent().getParent().getParent()).getChildAt(2)).getChildAt(0)).setText("");
    }
}
