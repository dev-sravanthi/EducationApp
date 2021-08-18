package fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bluebase.educationapp.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import activities.ClassTest;
import activities.HomeWork;
import bean.ClassTestTodayBean;
import bean.SMSSpecificBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Utility;

public class ClassTestToday extends Fragment {
    View v;
    String date,subname,description;
    private List<CTTodayRecyBean> list=new ArrayList<>();
    private RecyclerView recyclerView;
    private CTTodayAdapter ctTodayAdapter;
    boolean networkAvailability=false;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    String sibling_id,token;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.classtest_today, container, false);

        prefs = getActivity().getSharedPreferences("sms", Context.MODE_PRIVATE);
        editor = prefs.edit();

        token= prefs.getString("token","");
        sibling_id=prefs.getString("login_student_id","");

        ((ClassTest)getActivity()).setFragmentRefreshListener(new ClassTest.FragmentRefreshListener_classtest() {
            @Override
            public void onRefresh() {
                // Refresh Your Fragment
//                Toast.makeText(getActivity(), "refreshed", Toast.LENGTH_SHORT).show();
                sibling_id = prefs.getString("sibling_id","");
                findviewbyids();
            }
        });


        networkAvailability= Utility.isConnectingToInternet(getActivity());

        if(networkAvailability==true){
            findviewbyids();
        }else{
            Utility.getAlertNetNotConneccted(getContext(), "Internet Connection");
        }

        return v;
    }

    private void findviewbyids() {
        recyclerView = v.findViewById(R.id.recyclerView);
        list.clear();
        ctTodayAdapter = new CTTodayAdapter(list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ctTodayAdapter);
        loadJSON();
    }

    private void loadJSON() {
        showBar();
        Call<ClassTestTodayBean> call= RetrofitClient.getInstance().getApi().getClassTestToday("Bearer "+token,sibling_id);
        call.enqueue(new Callback<ClassTestTodayBean>() {

            @Override
            public void onResponse(Call<ClassTestTodayBean> call, Response<ClassTestTodayBean> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    ClassTestTodayBean classTestTodayBean=response.body();

                    List<ClassTestTodayBean.CTTodayBeanData> ctTodayBeanDataList=classTestTodayBean.getCtTodayBeanDataList();
                    for(int i=0;i<ctTodayBeanDataList.size();i++){
                        date=ctTodayBeanDataList.get(i).getDate();

                        List<ChildCTTodayRecyBean> childCTTodayRecyBean=new ArrayList<>();

                        List<ClassTestTodayBean.CTTodayBeanSubject> ctTodayBeanSubjectList=
                                ctTodayBeanDataList.get(i).getCtTodayBeanSubjectList();
                        for(int j=0;j<ctTodayBeanSubjectList.size();j++){

                            subname=ctTodayBeanSubjectList.get(j).getSubject_name();
                            description=ctTodayBeanSubjectList.get(j).getDescription();


                            childCTTodayRecyBean.add(new ChildCTTodayRecyBean(subname,description));
                        }

                        System.out.println("childCTTodayRecyBean==="+childCTTodayRecyBean.size());

                        CTTodayRecyBean ctTodayRecyBean=new CTTodayRecyBean(date,childCTTodayRecyBean);
                        list.add(ctTodayRecyBean);

                    }

                    ctTodayAdapter.notifyDataSetChanged();

                }else{
                    progressDialog.dismiss();
                    try {
                        System.out.println("todayHomeWork_bean====fail"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ClassTestTodayBean> call, Throwable t) {
                progressDialog.dismiss();
                System.out.println("todayHomeWork_bean===="+t.getMessage());
            }

        });

    }

    public class CTTodayAdapter extends RecyclerView.Adapter<CTTodayAdapter.ViewHolder> {
        private RecyclerView.RecycledViewPool viewPool= new RecyclerView.RecycledViewPool();
        private List<CTTodayRecyBean> ctTodayRecyBeanList;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_date;
            RecyclerView child_recyclerview;

            public ViewHolder(View view) {
                super(view);
                text_date = (TextView) view.findViewById(R.id.text_date);
                child_recyclerview=(RecyclerView) view.findViewById(R.id.child_recyclerview);
            }
        }

        public CTTodayAdapter(List<CTTodayRecyBean> mlist) {
            this.ctTodayRecyBeanList = mlist;
            this.context = context;
        }

        @Override
        public CTTodayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_classtest_today, parent, false);

            return new CTTodayAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CTTodayAdapter.ViewHolder holder, int position) {
            CTTodayRecyBean data = ctTodayRecyBeanList.get(position);
            holder.text_date.setText(data.getDate());

            LinearLayoutManager layoutManager = new LinearLayoutManager(holder.child_recyclerview.getContext(),
                    LinearLayoutManager.VERTICAL,false);
            layoutManager.setInitialPrefetchItemCount(data.getChildCTTodayRecyBeans().size());
            ChildCTTodayAdapter childItemAdapter= new ChildCTTodayAdapter(data.getChildCTTodayRecyBeans());
            holder.child_recyclerview.setLayoutManager(layoutManager);
            holder.child_recyclerview.setAdapter(childItemAdapter);
            holder.child_recyclerview.setRecycledViewPool(viewPool);
        }

        @Override
        public int getItemCount() {
            return ctTodayRecyBeanList.size();
        }
    }

    public class CTTodayRecyBean{
        private String date;
        private List<ChildCTTodayRecyBean> childCTTodayRecyBeans;

        public CTTodayRecyBean(String date,List<ChildCTTodayRecyBean> childCTTodayRecyBeans){
            this.date=date;
            this.childCTTodayRecyBeans=childCTTodayRecyBeans;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<ChildCTTodayRecyBean> getChildCTTodayRecyBeans() {
            return childCTTodayRecyBeans;
        }

        public void setChildCTTodayRecyBeans(List<ChildCTTodayRecyBean> childCTTodayRecyBeans) {
            this.childCTTodayRecyBeans = childCTTodayRecyBeans;
        }
    }

    public class ChildCTTodayAdapter extends RecyclerView.Adapter<ChildCTTodayAdapter.ChildViewHolder> {

        private List<ChildCTTodayRecyBean> ChildItemList;

        // Constuctor
        ChildCTTodayAdapter(List<ChildCTTodayRecyBean> childItemList){
            this.ChildItemList = childItemList;
        }

        @NonNull
        @Override
        public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,int i){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_recyview_classtest_today,
                            viewGroup, false);
            return new ChildViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChildViewHolder holder,int position){

            ChildCTTodayRecyBean childItem= ChildItemList.get(position);
            holder.text_subname.setText(childItem.getSubname());
            if(childItem.getDescription()==null){
                holder.text_description.setText("No Description");
            }else{
                holder.text_description.setText(childItem.getDescription());
            }

        }

        @Override
        public int getItemCount(){
            return ChildItemList.size();
        }

        class ChildViewHolder extends RecyclerView.ViewHolder {

            TextView text_subname,text_description;

            ChildViewHolder(View itemView){
                super(itemView);
                text_subname = itemView.findViewById(R.id.text_subname);
                text_description=itemView.findViewById(R.id.text_description);
            }
        }
    }

    public class ChildCTTodayRecyBean{
        private String subname,description;

        public ChildCTTodayRecyBean(String subname,String description){
            this.subname=subname;
            this.description=description;
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
