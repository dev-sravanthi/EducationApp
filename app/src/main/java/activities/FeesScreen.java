package activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bluebase.educationapp.R;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;

import fragments.FeeInvoiceFrag;
import fragments.FeeReportFrag;
import fragments.PendingFeesFrag;
import util.AndroidUtil;
import util.ObjectSerializer;
import util.ViewPagerAdapter;

public class FeesScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Toolbar toolbar;

    private SharedPreferences.Editor editor;
    private ViewPager viewPager;

    private AndroidUtil util;
    private AlertDialog.Builder builder;
    private ViewPagerAdapter adapter;
    private PendingFeesFrag fragmentOne;
    private FeeReportFrag fragmentTwo;
    private FeeInvoiceFrag fragmentThree;
    private SharedPreferences prefs;
    public static FeesScreen instance;
    private TabLayout allTabs;
    private Spinner spin_siblings;
    ArrayList<String> list_spinner, list_id, list_name, list_section, list_code;
    String spin_id, spin_id_code, selected_id, s_id, sibling_id,token;
    int spin_id_pos = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fees_screen);

        builder = new AlertDialog.Builder(this);
        prefs = getSharedPreferences("sms", Context.MODE_PRIVATE);
        editor = prefs.edit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        util = new AndroidUtil(FeesScreen.this);
        if (!util.isOnline()) {
            builder.setMessage("No Internet Connection").setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
        }

        spin_siblings=findViewById(R.id.spin_siblings);
        spin_siblings.setOnItemSelectedListener(this);

        list_spinner = new ArrayList<>();

        list_id = new ArrayList<String>();
        list_name = new ArrayList<String>();
        list_section = new ArrayList<String>();
        list_code = new ArrayList<String>();
        list_spinner.add("-Siblings-");

        try {
            list_id = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString
                    ("student_id", ObjectSerializer.serialize(new ArrayList<String>())));
            list_name = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString
                    ("student_name", ObjectSerializer.serialize(new ArrayList<String>())));
            list_section = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString
                    ("student_sec", ObjectSerializer.serialize(new ArrayList<String>())));
            list_code = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString
                    ("student_code", ObjectSerializer.serialize(new ArrayList<String>())));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list_id.size(); i++) {
            String id = list_id.get(i);
            String name = list_name.get(i);
            String section = list_section.get(i);
            String code = list_code.get(i);

            list_spinner.add(code + "-" + name + "(" + section + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getSupportActionBar().getThemedContext(), android.R.layout.simple_spinner_item, list_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_siblings.setAdapter(adapter);

        instance=this;
        getAllWidgets();
        setupViewPager();
    }

    public static FeesScreen getInstance() {
        return instance;
    }

    private void getAllWidgets() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        allTabs = (TabLayout) findViewById(R.id.tabs);
    }

    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentOne = new PendingFeesFrag();
        fragmentTwo = new FeeReportFrag();
        fragmentThree = new FeeInvoiceFrag();
        adapter.addFragment(fragmentOne, "Pending Fees");
        adapter.addFragment(fragmentTwo, "Fee Reports");
        adapter.addFragment(fragmentThree, "Fee Invoice");
        setViewPageAdapter();
    }

    private void setViewPageAdapter() {
        viewPager.setAdapter(adapter);
        allTabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        Intent MainActivity = new Intent(getBaseContext(), Dashboard.class);
        editor.putString("checkstring", "fees").commit();
        MainActivity.addCategory(Intent.CATEGORY_HOME);
        MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainActivity);
        FeesScreen.this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_menu:
                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                startActivityForResult(intent, 0);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public FragmentRefreshListener_fees getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener_fees fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener_fees fragmentRefreshListener;

    public interface FragmentRefreshListener_fees{
        void onRefresh();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spin_siblings) {
            spin_id_pos = position;
            if (spin_id_pos > 0) {
                selected_id = list_spinner.get(spin_id_pos).toString().trim();
                s_id = list_spinner.get(spin_id_pos).toString().trim();

                if (list_spinner.contains(s_id)) {
                    int id_code = list_spinner.indexOf(s_id);
                    sibling_id=list_id.get(id_code-1);

                    editor.putString("sibling_id",sibling_id);
                    editor.apply();

                    if(getFragmentRefreshListener()!=null){
                        getFragmentRefreshListener().onRefresh();
                    }
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}