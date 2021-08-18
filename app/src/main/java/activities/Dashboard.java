package activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bluebase.educationapp.R;
import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bean.MenuModel;
import bean.StudentBean;
import data.repo.RetrofitClient;
import fragments.FeeInvoiceFrag;
import fragments.PendingFeesFrag;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;
import util.CustomAdapter;
import util.ObjectSerializer;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    GridView gridView;
    public static String[] prgmNameList;
    public static int[] prgmImages;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    private AppBarConfiguration mAppBarConfiguration;
    CircularImageView circularImageView;
    GridView gv;
    public Intent intent;
    ArrayList<String> list_stud_id,list_Stud_name,list_stud_section,list_stud_code;
    ExpandableListView expListView;
    List<MenuModel> listDataHeader;
    Map<MenuModel, List<String>> listDataChild;
    ExpandableListAdapter listAdapter;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getSharedPreferences("sms", Context.MODE_PRIVATE);
        editor = prefs.edit();

        token=prefs.getString("token","");

        expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        enableExpandableList();

        list_stud_id=new ArrayList<>();
        list_Stud_name=new ArrayList<>();
        list_stud_section=new ArrayList<>();
        list_stud_code=new ArrayList<>();

        loadJSON();

        circularImageView=findViewById(R.id.profile_image);
        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(circularImageView.getContext(), circularImageView);
                popup.getMenuInflater().inflate(R.menu.popupmenu_parent, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getTitle().equals("Parent")){
                            Intent i=new Intent(getApplicationContext(),ProfileDetails.class);
                            startActivity(i);
                            finish();

                        }else if(item.getTitle().equals("Student")){
                            Intent i=new Intent(getApplicationContext(),StudentDetails.class);
                            startActivity(i);
                            finish();
                        }

                        return true;
                    }
                });

                popup.show();//showing popup men

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        prgmNameList = new String[]{"HomeWork","Attendance","Fees","Transport","SMS","Voice","Library","Exam Result",
                "Circular Events","Class Test","Class Timetable","Study Lab"};
        prgmImages = new int[]{R.drawable.homework,R.drawable.attendance,R.drawable.fees,R.drawable.transport,
                R.drawable.sms,R.drawable.voice_message,R.drawable.library,R.drawable.exam_result,
                R.drawable.events_circular,R.drawable.class_test,R.drawable.class_timetable,
                R.drawable.studylab};

        gv = findViewById(R.id.gridView1);
        gv.setAdapter(new CustomAdapter(this, prgmNameList, prgmImages));

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String selected = ((TextView) view.findViewById(R.id.textView1))
                        .getText().toString();

                switch (selected) {

                    case "HomeWork":
                        editor.putString("checkstring", "homework").commit();
                        intent = new Intent(Dashboard.this, HomeWork.class);
                        startActivity(intent);
                        break;

                    case "Attendance":
                        editor.putString("checkstring", "attendance").commit();
                        intent = new Intent(Dashboard.this, Attendance.class);
                        startActivity(intent);
                        break;

                    case "Fees":
                        editor.putString("checkstring", "fees").commit();
                        intent = new Intent(Dashboard.this, FeesScreen.class);
                        startActivity(intent);
                        break;

                    case "Transport":
                        editor.putString("checkstring", "transport").commit();
                        intent = new Intent(Dashboard.this, TransportRouteStoppingDetails.class);
                        startActivity(intent);
                        break;

                    case "SMS":
                        editor.putString("checkstring", "sms").commit();
                        intent = new Intent(Dashboard.this, SMSScreen.class);
                        startActivity(intent);
                        break;

                    case "Voice":
                        editor.putString("checkstring", "voice").commit();
                        intent = new Intent(Dashboard.this, VoiceScreen.class);
                        startActivity(intent);
                        break;

                    case "Library":
                        editor.putString("checkstring", "library").commit();
                        intent = new Intent(Dashboard.this, Library.class);
                        startActivity(intent);
                        break;

                    case "Exam Result":
                        Toast.makeText(Dashboard.this, "Not Developed", Toast.LENGTH_SHORT).show();

//                        editor.putString("checkstring", "examresult").commit();
//                        intent = new Intent(Dashboard.this, Library.class);
//                        startActivity(intent);
                        break;

                    case "Circular Events":
                        editor.putString("checkstring", "circularevents").commit();
                        intent = new Intent(Dashboard.this, CircularEventsNews.class);
                        startActivity(intent);
                        break;

                    case "Class Test":
                        editor.putString("checkstring", "classtest").commit();
                        intent = new Intent(Dashboard.this, ClassTest.class);
                        startActivity(intent);
                        break;


                    case "Class Timetable":
                        Toast.makeText(Dashboard.this, "Not Developed", Toast.LENGTH_SHORT).show();

//                        editor.putString("checkstring", "classtimetable").commit();
//                        intent = new Intent(Dashboard.this, Library.class);
//                        startActivity(intent);
                        break;

                    case "Study Lab":
                        Toast.makeText(Dashboard.this, "Not Developed", Toast.LENGTH_SHORT).show();

//                        editor.putString("checkstring", "studylab").commit();
//                        intent = new Intent(Dashboard.this, Library.class);
//                        startActivity(intent);
                        break;
                }

            }
        });

    }


    private void loadJSON() {
        Call<StudentBean> call= RetrofitClient.getInstance().getApi().getStudentData("Bearer "+token);
        call.enqueue(new Callback<StudentBean>() {
            @Override
            public void onResponse(Call<StudentBean> call, Response<StudentBean> response) {

                if(response.isSuccessful()){
                    StudentBean studentBean=response.body();

                    List<StudentBean.StudentDataBean> list_studdatabean=studentBean.getStudentDataBeanList();
                    for (int i=0;i<list_studdatabean.size();i++){
                        String code=list_studdatabean.get(i).getCode();
                        String student_id=list_studdatabean.get(i).getStudent_id();
                        String student_name=list_studdatabean.get(i).getStudent_name();
                        String standard_section=list_studdatabean.get(i).getStandard_section();
                        String community=list_studdatabean.get(i).getCommunity();
                        String caste=list_studdatabean.get(i).getCaste();
                        String sub_caste=list_studdatabean.get(i).getSub_caste();
                        String photo=list_studdatabean.get(i).getPhoto();
                        String gender=list_studdatabean.get(i).getGender();
                        String dob=list_studdatabean.get(i).getDob();
                        String doj=list_studdatabean.get(i).getDoj();
                        String academic=list_studdatabean.get(i).getAcademic();

                        list_stud_id.add(student_id);
                        list_Stud_name.add(student_name);
                        list_stud_section.add(standard_section);
                        list_stud_code.add(code);

                        ST_bean st_bean= new ST_bean(code,student_id,student_name,standard_section,student_name,community,caste,sub_caste,
                                photo,gender,dob,doj,academic);
                    }

                    try {
                        editor.putString("student_id", ObjectSerializer.serialize(list_stud_id));
                        editor.putString("student_name", ObjectSerializer.serialize(list_Stud_name));
                        editor.putString("student_sec", ObjectSerializer.serialize(list_stud_section));
                        editor.putString("student_code", ObjectSerializer.serialize(list_stud_code));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    editor.apply();


                }else{
                    try {
                        System.out.println("todayHomeWork_bean====fail"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<StudentBean> call, Throwable t) {
                System.out.println("todayHomeWork_bean===="+t.getMessage());
            }

        });

    }

    public class ST_bean{

        private String stu_code,stu_id,stu_name,stu_class,stu_firstname,stu_community,stu_caste,stu_subcaste,
                stu_photo,stu_gender,stu_dob,stu_doj,stu_academic;

        public ST_bean(String stu_code,String stu_id, String stu_name, String stu_class,String stu_firstname,String stu_community,
                       String stu_caste,String stu_subcaste,String stu_photo,String stu_gender,String stu_dob,
                       String stu_doj,String stu_academic) {
            this.stu_code=stu_code;
            this.stu_id = stu_id;
            this.stu_name = stu_name;
            this.stu_class = stu_class;
            this.stu_firstname=stu_firstname;
            this.stu_community=stu_community;
            this.stu_caste=stu_caste;
            this.stu_subcaste=stu_subcaste;
            this.stu_photo=stu_photo;
            this.stu_gender=stu_gender;
            this.stu_dob=stu_dob;
            this.stu_doj=stu_doj;
            this.stu_academic=stu_academic;
        }

        public String getStu_code() {
            return stu_code;
        }

        public void setStu_code(String stu_code) {
            this.stu_code = stu_code;
        }

        public String getStu_photo() {
            return stu_photo;
        }

        public void setStu_photo(String stu_photo) {
            this.stu_photo = stu_photo;
        }

        public String getStu_gender() {
            return stu_gender;
        }

        public void setStu_gender(String stu_gender) {
            this.stu_gender = stu_gender;
        }

        public String getStu_dob() {
            return stu_dob;
        }

        public void setStu_dob(String stu_dob) {
            this.stu_dob = stu_dob;
        }

        public String getStu_doj() {
            return stu_doj;
        }

        public void setStu_doj(String stu_doj) {
            this.stu_doj = stu_doj;
        }

        public String getStu_academic() {
            return stu_academic;
        }

        public void setStu_academic(String stu_academic) {
            this.stu_academic = stu_academic;
        }

        public String getStu_id() {
            return stu_id;
        }

        public void setStu_id(String stu_id) {
            this.stu_id = stu_id;
        }

        public String getStu_name() {
            return stu_name;
        }

        public void setStu_name(String stu_name) {
            this.stu_name = stu_name;
        }

        public String getStu_class() {
            return stu_class;
        }

        public void setStu_class(String stu_class) {
            this.stu_class = stu_class;
        }

        public String getStu_firstname() {
            return stu_firstname;
        }

        public void setStu_firstname(String stu_firstname) {
            this.stu_firstname = stu_firstname;
        }

        public String getStu_community() {
            return stu_community;
        }

        public void setStu_community(String stu_community) {
            this.stu_community = stu_community;
        }

        public String getStu_caste() {
            return stu_caste;
        }

        public void setStu_caste(String stu_caste) {
            this.stu_caste = stu_caste;
        }

        public String getStu_subcaste() {
            return stu_subcaste;
        }

        public void setStu_subcaste(String stu_subcaste) {
            this.stu_subcaste = stu_subcaste;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.mybutton:
                displayToast();
        }

        return super.onOptionsItemSelected(item);


    }

    private void displayToast() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Dashboard.this);
        builder.setTitle("Confirmation");
        builder.setIcon(R.drawable.error);
        builder.setMessage("Are you sure to quit from this APP?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            intent = new Intent(Dashboard.this, HomeworkStudentReply.class);
            startActivity(intent);
        } else
        if (id == R.id.nav_gallery) {
            intent = new Intent(Dashboard.this, HomeworkStudentReply.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            intent = new Intent(Dashboard.this, HomeworkStudentReply.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent MainActivity = new Intent(getBaseContext(), LoginScreen.class);
        MainActivity.addCategory(Intent.CATEGORY_HOME);
        MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainActivity);
        Dashboard.this.finish();
    }

    private void enableExpandableList() {
        listDataHeader = new ArrayList<MenuModel>();
        listDataChild = new HashMap<MenuModel, List<String>>();

        prepareListData(listDataHeader, listDataChild);
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setGroupIndicator(null);
        expListView.setDivider(null);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

//                System.out.println("listDataHeader.get(groupPosition===="+);

                MenuModel menuModelList=listDataHeader.get(groupPosition);
                String name=menuModelList.getString();

                if(name=="Dashboard"){
                    Intent i=new Intent(getApplicationContext(),Dashboard.class);
                    startActivity(i);
                }else if(name.equalsIgnoreCase("School Calender")){
                    Toast.makeText(Dashboard.this, "Not Developed", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                // Temporary code:
                String childmenu=listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);

                if(childmenu.equalsIgnoreCase("Circular News Events")){
                    Intent i=new Intent(getApplicationContext(),CircularEventsNews.class);
                    startActivity(i);
                }else if(childmenu.equalsIgnoreCase("Attendance Details")){
                    Intent i=new Intent(getApplicationContext(),Attendance.class);
                    startActivity(i);
                }else if(childmenu.equalsIgnoreCase("Class Test")){
                    Intent i=new Intent(getApplicationContext(),ClassTest.class);
                    startActivity(i);
                }else if(childmenu.equalsIgnoreCase("Homework")){
                    Intent i=new Intent(getApplicationContext(),HomeWork.class);
                    startActivity(i);
                }else if(childmenu.equalsIgnoreCase("Class TimeTable")){
                    Toast.makeText(Dashboard.this, "Not Developed", Toast.LENGTH_SHORT).show();
                }else if(childmenu.equalsIgnoreCase("Staff Details")){
                    Toast.makeText(Dashboard.this, "Not Developed", Toast.LENGTH_SHORT).show();
                }else if(childmenu.equalsIgnoreCase("Vehicle Tracking")){
                    Intent i=new Intent(getApplicationContext(),TransportRouteStoppingDetails.class);
                    startActivity(i);
                }else if(childmenu.equalsIgnoreCase("Fee Payment")){
                    Intent i=new Intent(getApplicationContext(),FeesScreen.class);
                    startActivity(i);
                }else if(childmenu.equalsIgnoreCase("Fee Invoice")){
                    Intent i=new Intent(getApplicationContext(), FeeInvoiceFrag.class);
                    startActivity(i);
                }else if(childmenu.equalsIgnoreCase("Fee Pending")){
                    Intent i=new Intent(getApplicationContext(), PendingFeesFrag.class);
                    startActivity(i);
                }else if(childmenu.equalsIgnoreCase("Exam Result")){
                    Toast.makeText(Dashboard.this, "Not Developed", Toast.LENGTH_SHORT).show();
                }else if(childmenu.equalsIgnoreCase("Exam TimeTable")){
                    Toast.makeText(Dashboard.this, "Not Developed", Toast.LENGTH_SHORT).show();
                }else if(childmenu.equalsIgnoreCase("Live Classes")){
                    Toast.makeText(Dashboard.this, "Not Developed", Toast.LENGTH_SHORT).show();
                }else if(childmenu.equalsIgnoreCase("Offline Subject Video")){
                    Toast.makeText(Dashboard.this, "Not Developed", Toast.LENGTH_SHORT).show();
                }else if(childmenu.equalsIgnoreCase("Borrow List")){
                    Intent i=new Intent(getApplicationContext(),Library.class);
                    startActivity(i);
                }else if(childmenu.equalsIgnoreCase("Return/Renew List")){
                    Intent i=new Intent(getApplicationContext(),Library.class);
                    startActivity(i);
                }else if(childmenu.equalsIgnoreCase("Fine List")){
                    Intent i=new Intent(getApplicationContext(),LibraryFineList.class);
                    startActivity(i);
                }else if(childmenu.equalsIgnoreCase("Fine Invoice")){
                    Intent i=new Intent(getApplicationContext(),Library.class);
                    startActivity(i);
                }else if(childmenu.equalsIgnoreCase("Material Bill")){
                    Toast.makeText(Dashboard.this, "Not Developed", Toast.LENGTH_SHORT).show();
                }else if(childmenu.equalsIgnoreCase("Material bill Issued")){
                    Toast.makeText(Dashboard.this, "Not Developed", Toast.LENGTH_SHORT).show();
                }else if(childmenu.equalsIgnoreCase("Hostel Fee Invoice")){
                    Toast.makeText(Dashboard.this, "Not Developed", Toast.LENGTH_SHORT).show();
                }else if(childmenu.equalsIgnoreCase("Hostel Details")){
                    Toast.makeText(Dashboard.this, "Not Developed", Toast.LENGTH_SHORT).show();
                }else if(childmenu.equalsIgnoreCase("Extra Curricular")){
                    Toast.makeText(Dashboard.this, "Not Developed", Toast.LENGTH_SHORT).show();
                }else if(childmenu.equalsIgnoreCase("Refreshment")){
                    Toast.makeText(Dashboard.this, "Not Developed", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
    }

    private void prepareListData(List<MenuModel> listDataHeader, Map<MenuModel, List<String>> listDataChild) {

        // Adding parent data
        Bitmap bitmap_dashboard = BitmapFactory.decodeResource(getResources(), R.drawable.d_dashboard);
        listDataHeader.add(new MenuModel("Dashboard",bitmap_dashboard));
        Bitmap bitmap_daily_act = BitmapFactory.decodeResource(getResources(), R.drawable.d_dailyactivities);
        listDataHeader.add(new MenuModel("Daily Activities",bitmap_daily_act));
        Bitmap bitmap_payment_inv = BitmapFactory.decodeResource(getResources(), R.drawable.d_paymentinvoice);
        listDataHeader.add(new MenuModel("Payment/Invoice",bitmap_payment_inv));
        Bitmap bitmap_exammanage = BitmapFactory.decodeResource(getResources(), R.drawable.d_exammanage);
        listDataHeader.add(new MenuModel("Exam Manage",bitmap_exammanage));
        Bitmap bitmap_onlineclass = BitmapFactory.decodeResource(getResources(), R.drawable.d_onlineclass);
        listDataHeader.add(new MenuModel("Online Class",bitmap_onlineclass));
        Bitmap bitmap_library = BitmapFactory.decodeResource(getResources(), R.drawable.d_library);
        listDataHeader.add(new MenuModel("Library",bitmap_library));
        Bitmap bitmap_inventory = BitmapFactory.decodeResource(getResources(), R.drawable.d_inventory);
        listDataHeader.add(new MenuModel("Inventory",bitmap_inventory));
        Bitmap bitmap_hostel = BitmapFactory.decodeResource(getResources(), R.drawable.d_hostel);
        listDataHeader.add(new MenuModel("Hostel",bitmap_hostel));
        Bitmap bitmap_extraact = BitmapFactory.decodeResource(getResources(), R.drawable.d_extraactivites);
        listDataHeader.add(new MenuModel("Extra Activities",bitmap_extraact));
        Bitmap bitmap_schoolcal = BitmapFactory.decodeResource(getResources(), R.drawable.d_schoolcalender);
        listDataHeader.add(new MenuModel("School Calender",bitmap_schoolcal));

        // Adding child data
        List<String> list_daily_activities = new ArrayList<String>();
        list_daily_activities.add("Circular News Events");
        list_daily_activities.add("Attendance Details");
        list_daily_activities.add("Class Test");
        list_daily_activities.add("Homework");
        list_daily_activities.add("Class TimeTable");
        list_daily_activities.add("Staff Details");
        list_daily_activities.add("Vehicle Tracking");

        List<String> list_paymentinvoice = new ArrayList<String>();
        list_paymentinvoice.add("Fee Payment");
        list_paymentinvoice.add("Fee Invoice");
        list_paymentinvoice.add("Fee Pending");

        List<String> list_exammanage = new ArrayList<String>();
        list_exammanage.add("Exam Result");
        list_exammanage.add("Exam Time Table");

        List<String> list_onlineclasses = new ArrayList<String>();
        list_onlineclasses.add("Live Classes");
        list_onlineclasses.add("Offline Subject Video");

        List<String> list_library = new ArrayList<String>();
        list_library.add("Borrow List");
        list_library.add("Return/Renew List");
        list_library.add("Fine List");
        list_library.add("Fine Invoice");

        List<String> list_inventory = new ArrayList<String>();
        list_inventory.add("Material Bill");
        list_inventory.add("Material Bill Issued");

        List<String> list_hostel = new ArrayList<String>();
        list_hostel.add("Hostel Fee Invoice");
        list_hostel.add("Hostel Details");

        List<String> list_extraactivities = new ArrayList<String>();
        list_extraactivities.add("Extra Curricular");
        list_extraactivities.add("Refreshment");

        List<String> list_dashboard=new ArrayList<>();
        List<String> list_schoolcalender=new ArrayList<>();

        listDataChild.put(listDataHeader.get(0), list_dashboard);
        listDataChild.put(listDataHeader.get(1), list_daily_activities); // Header, Child data
        listDataChild.put(listDataHeader.get(2), list_paymentinvoice);
        listDataChild.put(listDataHeader.get(3), list_exammanage);
        listDataChild.put(listDataHeader.get(4), list_onlineclasses);
        listDataChild.put(listDataHeader.get(5), list_library);
        listDataChild.put(listDataHeader.get(6), list_inventory);
        listDataChild.put(listDataHeader.get(7), list_hostel);
        listDataChild.put(listDataHeader.get(8), list_extraactivities);
        listDataChild.put(listDataHeader.get(9), list_schoolcalender);
    }


}