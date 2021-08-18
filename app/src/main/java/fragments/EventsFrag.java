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
import android.widget.ImageView;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import activities.CircularEventsNews;
import activities.FullScreenImage;
import activities.HomeWork;
import bean.CircularFragBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Utility;

public class EventsFrag extends Fragment implements View.OnClickListener{

    private String date,description,title,attach_url;
    private List<EventsBean> list=new ArrayList<>();
    private RecyclerView recyclerView;
    private EventsAdapter eventsAdapter;
    boolean networkAvailability=false;
    View root;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    String sibling_id,token;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.circular_frag, container, false);

        prefs = getActivity().getSharedPreferences("sms", Context.MODE_PRIVATE);
        editor = prefs.edit();

        token= prefs.getString("token","");
        sibling_id=prefs.getString("login_student_id","");

        ((CircularEventsNews)getActivity()).setFragmentRefreshListener(new CircularEventsNews.FragmentRefreshListener_cir() {
            @Override
            public void onRefresh() {
                // Refresh Your Fragment
//                Toast.makeText(getActivity(), "refreshed", Toast.LENGTH_SHORT).show();
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

        return root;
    }

    private void initViews(){
        recyclerView = root.findViewById(R.id.recyclerView);
        list.clear();
        recyclerView.setHasFixedSize(true);
        eventsAdapter = new EventsAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(eventsAdapter);
        loadJSON();

    }

    private void loadJSON() {
        showBar();
        Call<CircularFragBean> call= RetrofitClient.getInstance().getApi().getEventsData("Bearer "+token,sibling_id);
        call.enqueue(new Callback<CircularFragBean>() {

            @Override
            public void onResponse(Call<CircularFragBean> call, Response<CircularFragBean> response) {
                progressDialog.dismiss();

                if(response.isSuccessful()){
                    CircularFragBean circularFragBean=response.body();

                    List<CircularFragBean.CircularDataBean> circularDataBeanslist=
                            circularFragBean.getCircularDataBeanList();
                    for (int i=0;i<circularDataBeanslist.size();i++){
                        date=circularDataBeanslist.get(i).getDate();
                        title=circularDataBeanslist.get(i).getTitle();
                        description=circularDataBeanslist.get(i).getDescription();
                        attach_url=circularDataBeanslist.get(i).getAttach_url();

                        EventsBean circularBean=new EventsBean(date,title,description,attach_url);
                        list.add(circularBean);
                    }

                    eventsAdapter.notifyDataSetChanged();

                }else{
                    try {
                        progressDialog.dismiss();
                        System.out.println("todayHomeWork_bean====fail"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<CircularFragBean> call, Throwable t) {
                System.out.println("todayHomeWork_bean===="+t.getMessage());
                progressDialog.dismiss();
            }

        });

    }

    public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
        private List<EventsBean> eventsBeans;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_date,text_title,text_description;
            public ImageView imageView;

            public ViewHolder(View view) {
                super(view);
                imageView=(ImageView)view.findViewById(R.id.imageView);
                text_date = (TextView) view.findViewById(R.id.text_date);
                text_title = (TextView) view.findViewById(R.id.text_title);
                text_description = (TextView) view.findViewById(R.id.text_description);

            }
        }

        public EventsAdapter(List<EventsBean> mlist)
        {
            this.eventsBeans = mlist;
            this.context=context;
        }

        @Override
        public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_circular_frag, parent, false);

            return new EventsAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(EventsAdapter.ViewHolder holder, int position) {
            EventsBean data = eventsBeans.get(position);

            holder.text_date.setText(data.getDate());
            holder.text_title.setText(data.getTitle());
            holder.text_description.setText(data.getDescription());
            if(data.getAttach_url()!=null){
//                Glide.with(getActivity())
//                        .load(data.getAttach_url())
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(holder.imageView);
                RequestOptions myOptions = new RequestOptions()
                        .override(100, 100);

                Glide.with(getContext().getApplicationContext())
                        .asBitmap()
                        .apply(myOptions)
                        .load(data.getAttach_url())
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap,
                                                        Transition<? super Bitmap> transition) {
                                int w = bitmap.getWidth();
                                int h = bitmap.getHeight();
                                holder.imageView.setImageBitmap(bitmap);
                            }
                        });
               holder.imageView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       holder.imageView.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               Intent i=new Intent(getActivity(), FullScreenImage.class);
                               i.putExtra("imageurl",data.getAttach_url());
                               startActivity(i);

                           }
                       });
                   }
               });
            }else{
                holder.imageView.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return eventsBeans.size();
        }
    }

    public class EventsBean {
        private String date,title,description,attach_url;

        public EventsBean(String date,String title,String description,String attach_url){
            this.date=date;
            this.title=title;
            this.description=description;
            this.attach_url=attach_url;
        }

        public String getAttach_url() {
            return attach_url;
        }

        public void setAttach_url(String attach_url) {
            this.attach_url = attach_url;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "Image zoomed", Toast.LENGTH_SHORT).show();
       // new PhotoFullPopupWindow(getActivity(), R.layout.popup_photo_full, v, URL, null);
    }


}