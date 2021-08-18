package fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import activities.FullScreenImage;
import activities.HomeWork;
import activities.LoginScreen;
import bean.PastHomeWorkBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.ObjectSerializer;
import util.Utility;

public class PastHomeworks extends Fragment{

    View v;
    RecyclerView recyclerView;
    boolean networkAvailability=false;
    PastHW_Adapter pastHW_adapter;
    private List<PH_recyclerbean> list=new ArrayList<>();
    private ArrayList<String> list_subject_name,list_descriptions;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    String subject_name,descrip,icon;
    String sibling_id,token;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.past_home_work, container, false);

        prefs = getActivity().getSharedPreferences("sms", Context.MODE_PRIVATE);
        editor = prefs.edit();

        token= prefs.getString("token","");
        sibling_id=prefs.getString("login_student_id","");


        ((HomeWork)getActivity()).setFragmentRefreshListener(new HomeWork.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh Your Fragment
                sibling_id = prefs.getString("sibling_id","");
                initViews();
            }
        });

        networkAvailability= Utility.isConnectingToInternet(getActivity());

        if(networkAvailability==true){
            initViews();
        }else{
            Utility.getAlertNetNotConneccted(getContext(), "Internet Connection");
        }

        return v;
    }

    private void initViews(){

        list_subject_name=new ArrayList<>();
        list_descriptions=new ArrayList<>();

        recyclerView = v.findViewById(R.id.recyclerView_pasthomework);
        list.clear();
        pastHW_adapter = new PastHW_Adapter(list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pastHW_adapter);
        loadJSON();
    }

    private void loadJSON() {
        showBar();
        Call<PastHomeWorkBean> call= RetrofitClient.getInstance().getApi().getPastHomeWorkData("Bearer "+token,sibling_id);
        call.enqueue(new Callback<PastHomeWorkBean>() {
            @Override
            public void onResponse(Call<PastHomeWorkBean> call, Response<PastHomeWorkBean> response) {

                progressDialog.dismiss();
                if(response.isSuccessful()){
                    PastHomeWorkBean phw_databean=response.body();
                    List<PastHomeWorkBean.PHW_Databean> list_phw_databean=phw_databean.getPhw_databeans();
                    for(int i=0;i<list_phw_databean.size();i++){
                        String date=list_phw_databean.get(i).getDate();
                     //   String description=list_phw_databean.get(i).getDescription();

                        List<ChildPastHomeworkRecyBean> childPastHomeworkRecyBeanList=new ArrayList<>();

                        List<PastHomeWorkBean.PHW_Subjects> list_phwsubjects=
                                list_phw_databean.get(i).getPhw_subjects();

                        for (int j=0;j<list_phwsubjects.size();j++){
                            subject_name=list_phwsubjects.get(j).getSubject_name();
                            descrip=list_phwsubjects.get(j).getDescription();
                            icon=list_phwsubjects.get(j).getIcon();

                            childPastHomeworkRecyBeanList.add(new
                                    ChildPastHomeworkRecyBean(subject_name, descrip,icon));
                        }

                        PH_recyclerbean ph_recyclerbean=new PH_recyclerbean(date,childPastHomeworkRecyBeanList);
                        list.add(ph_recyclerbean);

                    }

                    pastHW_adapter.notifyDataSetChanged();

                }else{
                    progressDialog.dismiss();
                    Utility.showAlertDialog(getActivity(),"Error",response.errorBody().toString(),false);
                }

            }

            @Override
            public void onFailure(Call<PastHomeWorkBean> call, Throwable t) {
                progressDialog.dismiss();
                Utility.showAlertDialog(getActivity(),"Error",t.getMessage(),false);
            }

        });

    }

    public class PastHW_Adapter extends RecyclerView.Adapter<PastHW_Adapter.ViewHolder> {
        private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        private List<PH_recyclerbean> ph_recyclerbeanList;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_pwh_date;
            public RecyclerView rv_sub_view;
            public ImageView iv_arrow;
            public LinearLayout ll_data;

            public ViewHolder(View view) {
                super(view);

                text_pwh_date = (TextView) view.findViewById(R.id.text_pwh_date);
                rv_sub_view = (RecyclerView) view.findViewById(R.id.rv_sub_view);
                iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
                ll_data = (LinearLayout) view.findViewById(R.id.ll_data);
            }

        }

        public PastHW_Adapter(List<PH_recyclerbean> mlist) {
            this.ph_recyclerbeanList = mlist;
            this.context = context;
        }

        @Override
        public PastHW_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_past_home_work, parent, false);

            return new PastHW_Adapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(PastHW_Adapter.ViewHolder holder, int position) {
            PH_recyclerbean data = ph_recyclerbeanList.get(position);
            holder.text_pwh_date.setText(data.getDate());

            holder.iv_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.ll_data.setVisibility(holder.ll_data.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
            });

            LinearLayoutManager layoutManager = new LinearLayoutManager(holder.rv_sub_view.getContext(),
                    LinearLayoutManager.VERTICAL, false);
            layoutManager.setInitialPrefetchItemCount(data.getChildPastHomeworkRecyBeanList().size());
            ChildPastHomeworkAdapter childItemAdapter = new
                    ChildPastHomeworkAdapter(data.getChildPastHomeworkRecyBeanList());
            holder.rv_sub_view.setLayoutManager(layoutManager);
            holder.rv_sub_view.setAdapter(childItemAdapter);
            holder.rv_sub_view.setRecycledViewPool(viewPool);
        }

        @Override
        public int getItemCount() {
            return ph_recyclerbeanList.size();
        }
    }

    public static class PH_recyclerbean {
        private String date,description;
        private List<ChildPastHomeworkRecyBean> childPastHomeworkRecyBeanList;

        public PH_recyclerbean(String date, List<ChildPastHomeworkRecyBean> childPastHomeworkRecyBeanList) {
            this.date = date;
//            this.description = description;
            this.childPastHomeworkRecyBeanList = childPastHomeworkRecyBeanList;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<ChildPastHomeworkRecyBean> getChildPastHomeworkRecyBeanList() {
            return childPastHomeworkRecyBeanList;
        }

        public void setChildPastHomeworkRecyBeanList(List<ChildPastHomeworkRecyBean> childPastHomeworkRecyBeanList) {
            this.childPastHomeworkRecyBeanList = childPastHomeworkRecyBeanList;
        }
    }

    public class ChildPastHomeworkAdapter extends RecyclerView.Adapter<ChildPastHomeworkAdapter.ChildViewHolder> {

        private List<ChildPastHomeworkRecyBean> ChildItemList;

        // Constuctor
        ChildPastHomeworkAdapter(List<ChildPastHomeworkRecyBean> childItemList) {
            this.ChildItemList = childItemList;
        }

        @NonNull
        @Override
        public ChildPastHomeworkAdapter.ChildViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_recyview_pasthomework,
                    viewGroup, false);
            return new ChildPastHomeworkAdapter.ChildViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChildPastHomeworkAdapter.ChildViewHolder holder, int position) {

            ChildPastHomeworkRecyBean childItem = ChildItemList.get(position);
            holder.text_child_subname.setText(childItem.getSubname());
            if (childItem.getDescription() == null) {
                holder.text_child_description.setText("No Description");
            } else {
                holder.text_child_description.setText(childItem.getDescription());
            }
//            Glide.with(holder.itemView.getContext())
//                    .load(childItem.getIcon())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(holder.img_subicon);
//            RequestOptions myOptions = new RequestOptions()
//                    .override(100, 100);
//
//            Glide.with(getContext().getApplicationContext())
//                    .asBitmap()
//                    .apply(myOptions)
//                    .load(childItem.getIcon())
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap bitmap,
//                                                    Transition<? super Bitmap> transition) {
//                            int w = bitmap.getWidth();
//                            int h = bitmap.getHeight();
//                            holder.img_subicon.setImageBitmap(bitmap);
//                        }
//                    });
//
//            holder.img_subicon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i=new Intent(getActivity(), FullScreenImage.class);
//                    i.putExtra("imageurl",childItem.getIcon());
//                    startActivity(i);
//
//                }
//            });

        }

        @Override
        public int getItemCount() {
            return ChildItemList.size();
        }

        class ChildViewHolder extends RecyclerView.ViewHolder {

            TextView text_child_subname, text_child_description;
            CircularImageView img_subicon;

            ChildViewHolder(View itemView) {
                super(itemView);
                text_child_subname = itemView.findViewById(R.id.text_child_subname);
                text_child_description = itemView.findViewById(R.id.text_child_description);
//                img_subicon=itemView.findViewById(R.id.img_subicon);

            }
        }
    }

    public class ChildPastHomeworkRecyBean {
        private String subname, description,icon;

        public ChildPastHomeworkRecyBean(String subname, String description,String icon) {
            this.subname = subname;
            this.description = description;
            this.icon=icon;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getSubname() {
            return subname;
        }

        public void setSubname(String subname) {
            this.subname = subname;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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