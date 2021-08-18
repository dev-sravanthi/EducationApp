package fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import activities.HomeWork;
import activities.VoiceScreen;
import bean.VoiceOverallBean;
import bean.VoiceSpecificBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Utility;

public class VoiceSpecific extends Fragment {

    View v;
    private String d_id,d_date,d_time_stamp,d_title,d_voice_msg_file,code,status,msg;
    private List<VoiceSpecificAdapterBean> list=new ArrayList<>();
    private RecyclerView recyclerView;
    private VoiceSpecificAdapter voiceSpecificAdapter;
    boolean networkAvailability=false;
    MediaPlayer mediaPlayer;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    String sibling_id,token;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    LinearLayout ll_nodatafound;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.voice_specific, container, false);

        prefs = getActivity().getSharedPreferences("sms", Context.MODE_PRIVATE);
        editor = prefs.edit();

        token= prefs.getString("token","");
        sibling_id=prefs.getString("login_student_id","");

        ((VoiceScreen)getActivity()).setFragmentRefreshListener(new VoiceScreen.FragmentRefreshListener_voice() {
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
        recyclerView.setHasFixedSize(true);
        voiceSpecificAdapter = new VoiceSpecificAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(voiceSpecificAdapter);
        loadJSON();
    }

    private void loadJSON() {
        showBar();
        Call<VoiceSpecificBean> call= RetrofitClient.getInstance().getApi().getVoiceSpecific("Bearer "+token,sibling_id);
        call.enqueue(new Callback<VoiceSpecificBean>() {

            @Override
            public void onResponse(Call<VoiceSpecificBean> call, Response<VoiceSpecificBean> response) {

                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    VoiceSpecificBean voiceSpecificBean=response.body();
                    code=voiceSpecificBean.getCode();
                    status=voiceSpecificBean.getStatus();
                    msg=voiceSpecificBean.getMsg();

                    List<VoiceSpecificBean.VoiceSpecificDataBean> voiceSpecificDataBeanList=voiceSpecificBean.getSpecificDataBeanList();
                    if(voiceSpecificDataBeanList.size()>0){
                        recyclerView.setVisibility(View.VISIBLE);
                        for (int i=0;i<voiceSpecificDataBeanList.size();i++){
                            d_id=voiceSpecificDataBeanList.get(i).getId();
                            d_date=voiceSpecificDataBeanList.get(i).getDate();
                            d_time_stamp=voiceSpecificDataBeanList.get(i).getTime_stamp();
                            d_title=voiceSpecificDataBeanList.get(i).getTitle();
                            d_voice_msg_file=voiceSpecificDataBeanList.get(i).getVoice_msg_file();

                            VoiceSpecificAdapterBean smsOverallAdapterBean=new VoiceSpecificAdapterBean(d_id,d_date,
                                    d_time_stamp,d_title,d_voice_msg_file);
                            list.add(smsOverallAdapterBean);

                        }
                        voiceSpecificAdapter.notifyDataSetChanged();
                    }else{
                        ll_nodatafound=v.findViewById(R.id.ll_nodatafound);
                        ll_nodatafound.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }


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
            public void onFailure(Call<VoiceSpecificBean> call, Throwable t) {
                progressDialog.dismiss();
                System.out.println("todayHomeWork_bean===="+t.getMessage());
            }

        });

    }

    private void playAudio(String url) {

        String audioUrl = url;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "Audio started playing..", Toast.LENGTH_SHORT).show();
    }

    private void pauseAudio() {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            Toast.makeText(getActivity(), "Audio has been paused", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Audio has not played", Toast.LENGTH_SHORT).show();
        }
    }

    public class VoiceSpecificAdapter extends RecyclerView.Adapter<VoiceSpecificAdapter.ViewHolder> {
        private List<VoiceSpecificAdapterBean> voiceOverallAdapterBeanList;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public CircularImageView btn_playbutton,btn_pausebutton;
            public TextView text_date_time;

            public ViewHolder(View view) {
                super(view);
                btn_playbutton=(CircularImageView)view.findViewById(R.id.btn_playbutton);
                btn_pausebutton=(CircularImageView)view.findViewById(R.id.btn_pausebutton);
                text_date_time=(TextView)view.findViewById(R.id.text_date_time);
            }
        }

        public VoiceSpecificAdapter(List<VoiceSpecificAdapterBean> mlist) {
            this.voiceOverallAdapterBeanList = mlist;
            this.context = context;
        }

        @Override
        public VoiceSpecificAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_voice_specific, parent, false);

            return new VoiceSpecificAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(VoiceSpecificAdapter.ViewHolder holder, int position) {
            VoiceSpecificAdapterBean data = voiceOverallAdapterBeanList.get(position);
            holder.text_date_time.setText(data.getDate());

            holder.btn_playbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playAudio(data.getVoice_msg_file());
                    holder.btn_playbutton.setVisibility(View.GONE);
                    holder.btn_pausebutton.setVisibility(View.VISIBLE);

                }
            });

            holder.btn_pausebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.btn_playbutton.setVisibility(View.VISIBLE);
                    holder.btn_pausebutton.setVisibility(View.GONE);
                    pauseAudio();
                }
            });
        }

        @Override
        public int getItemCount() {
            return voiceOverallAdapterBeanList.size();
        }
    }

    public class VoiceSpecificAdapterBean{
        private String id,date,time_stamp,title,voice_msg_file;

        public VoiceSpecificAdapterBean(String id,String date,String time_stamp,String title,String voice_msg_file){
            this.id=id;
            this.date=date;
            this.time_stamp=time_stamp;
            this.title=title;
            this.voice_msg_file=voice_msg_file;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime_stamp() {
            return time_stamp;
        }

        public void setTime_stamp(String time_stamp) {
            this.time_stamp = time_stamp;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVoice_msg_file() {
            return voice_msg_file;
        }

        public void setVoice_msg_file(String voice_msg_file) {
            this.voice_msg_file = voice_msg_file;
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
