package fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bluebase.educationapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import activities.FullScreenImage;
import activities.HomeWork;
import activities.HomeworkStudentReply;
import activities.LoginScreen;
import bean.TH_SubjectList;
import bean.TH_beandata;
import bean.TodayHomeWork_bean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Utility;

public class TodaysHomework extends Fragment{
    private String date,subject,description,icon,homework_id,id;
    ArrayList<String> list_date,list_subject,list_description,list_icon;
    private List<TH_Recycler_bean> list=new ArrayList<>();
    private RecyclerView recyclerView;
    private TodayHomeWorkAdapter dataAdapter;
    boolean networkAvailability=false;
    TextView text_date;
    View root;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    String sibling_id,token;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.todayshomework, container, false);

        prefs = getActivity().getSharedPreferences("sms", Context.MODE_PRIVATE);
        editor = prefs.edit();

        token= prefs.getString("token","");
        sibling_id=prefs.getString("login_student_id","");

        ((HomeWork)getActivity()).setFragmentRefreshListener(new HomeWork.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh Your Fragment
//                Toast.makeText(getActivity(), "refreshed", Toast.LENGTH_SHORT).show();
                sibling_id = prefs.getString("sibling_id","");
                initViews();
            }
        });

        list_date=new ArrayList<>();
        list_subject=new ArrayList<>();
        list_description=new ArrayList<>();
        list_icon=new ArrayList<>();

        text_date=root.findViewById(R.id.text_date);

        networkAvailability= Utility.isConnectingToInternet(getActivity());

        if(networkAvailability==true){
            initViews();
        }else{
            Utility.getAlertNetNotConneccted(getContext(), "Internet Connection");
        }

        return root;
    }

    private void initViews(){

        recyclerView = root.findViewById(R.id.recyclerView);
        list.clear();
        recyclerView.setHasFixedSize(true);
        dataAdapter = new TodayHomeWorkAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(dataAdapter);
        loadJSON();

    }

    private void loadJSON() {
        showBar();
        Call<TodayHomeWork_bean> call= RetrofitClient.getInstance().getApi().getTodaysHomework("Bearer "+token,sibling_id);
        call.enqueue(new Callback<TodayHomeWork_bean>() {
            @Override
            public void onResponse(Call<TodayHomeWork_bean> call, Response<TodayHomeWork_bean> response) {
                progressDialog.dismiss();

                if(response.isSuccessful()){
                    TodayHomeWork_bean todayHomeWork_bean=response.body();
                    List<TH_beandata> th_beandataList=todayHomeWork_bean.getTh_beandataList();
                    for (int i=0;i<th_beandataList.size();i++){
                        date=th_beandataList.get(i).getDate();
                        id=th_beandataList.get(i).getId();
                        text_date.setText(date);

                        List<TH_SubjectList> th_subjectLists=th_beandataList.get(i).getSubjectLists();
                        for (int j=0;j<th_subjectLists.size();j++){
                            description=th_subjectLists.get(j).getDescription();
                            icon=th_subjectLists.get(j).getIcon();
                            subject=th_subjectLists.get(j).getSubject_name();
                            homework_id=th_subjectLists.get(i).getHomework_id();

                            TH_Recycler_bean recycler_bean=new TH_Recycler_bean(icon,description,subject,homework_id);
                            list.add(recycler_bean);
                        }
                    }
                    dataAdapter.notifyDataSetChanged();

                }else{
                    progressDialog.dismiss();
                    Utility.showAlertDialog(getActivity(),"Error",response.errorBody().toString(),false);
                }

            }

            @Override
            public void onFailure(Call<TodayHomeWork_bean> call, Throwable t) {
                progressDialog.dismiss();
                Utility.showAlertDialog(getActivity(),"Error",t.getMessage(),false);
            }

        });

    }


    public class TodayHomeWorkAdapter extends RecyclerView.Adapter<TodayHomeWorkAdapter.ViewHolder> {
        private List<TH_Recycler_bean> recycler_beans;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_description;
            public ImageView img_icon,img_upload;
            public Button btn_subject;

            public ViewHolder(View view) {
                super(view);
                btn_subject = (Button) view.findViewById(R.id.btn_subject);
                text_description = (TextView) view.findViewById(R.id.text_description);
                img_icon=(ImageView)view.findViewById(R.id.img_icon);
                img_upload=view.findViewById(R.id.img_upload);

            }
        }

        public TodayHomeWorkAdapter(List<TH_Recycler_bean> mlist)
        {
            this.recycler_beans = mlist;
            this.context=context;
        }

        @Override
        public TodayHomeWorkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_todayshomework, parent, false);

            return new TodayHomeWorkAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TodayHomeWorkAdapter.ViewHolder holder, int position) {
            TH_Recycler_bean data = recycler_beans.get(position);
            holder.text_description.setText(data.getDescription());
            holder.btn_subject.setText(data.getSubject());
            RequestOptions myOptions = new RequestOptions()
                    .override(100, 100);

            Glide.with(getContext().getApplicationContext())
                    .asBitmap()
                    .apply(myOptions)
                    .load(data.getIcon())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap,
                                                    Transition<? super Bitmap> transition) {
                            int w = bitmap.getWidth();
                            int h = bitmap.getHeight();
                            holder.img_icon.setImageBitmap(bitmap);
                        }
                    });
//            Glide.with(holder.itemView.getContext())
//                    .load(data.getIcon())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(holder.img_icon);

            holder.img_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(getActivity(), FullScreenImage.class);
                    i.putExtra("imageurl",data.getIcon());
                    startActivity(i);

                }
            });

            holder.img_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), HomeworkStudentReply.class);
                    i.putExtra("id", id);
                    i.putExtra("subjectname", data.getSubject());
                    i.putExtra("desc", data.getDescription());
                    i.putExtra("homeworkid", data.getHomeworkid());

                    startActivity(i);
                }

            });
        }

        @Override
        public int getItemCount() {
            return recycler_beans.size();
        }
    }

    public class TH_Recycler_bean {
        private String icon;
        private String description;
        private String subject;
        private String homeworkid;

        public TH_Recycler_bean(String icon, String description, String subject,String homeworkid) {
            this.icon = icon;
            this.description = description;
            this.subject = subject;
            this.homeworkid=homeworkid;
        }

        public String getHomeworkid() {
            return homeworkid;
        }

        public void setHomeworkid(String homeworkid) {
            this.homeworkid = homeworkid;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
    }

    public void showBar(){
        builder = new AlertDialog.Builder(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Processing Data...");
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please Wait");
        progressDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}