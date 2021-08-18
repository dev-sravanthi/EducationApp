package activities;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bluebase.educationapp.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import bean.LibraryBorrowBook;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Utility;

public class LibraryBorrowList extends AppCompatActivity {
    private String code,status,msg;
    boolean networkAvailability=false;
    private AlertDialog.Builder builder;
    RecyclerView recyclerView;
    private List<LibBorrowListBean> list=new ArrayList<>();
    private LibBorrowListAdapter libBorrowListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_borrow_list);

        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        networkAvailability= Utility.isConnectingToInternet(LibraryBorrowList.this);

        if(networkAvailability==true){
            findviewids();
        }else{
            Utility.getAlertNetNotConneccted(getApplicationContext(), "Internet Connection");
        }
    }

    private void findviewids() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        libBorrowListAdapter = new LibBorrowListAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(libBorrowListAdapter);
        loadJSON();
    }

    private void loadJSON() {
        Call<LibraryBorrowBook> call= RetrofitClient.getInstance().getApi().getLibraryBorrowBook();
        call.enqueue(new Callback<LibraryBorrowBook>() {

            @Override
            public void onResponse(Call<LibraryBorrowBook> call, Response<LibraryBorrowBook> response) {

                if(response.isSuccessful()){
                    LibraryBorrowBook libraryBorrowBook=response.body();
                    status=libraryBorrowBook.getStatus();
                    code=libraryBorrowBook.getCode();
                    msg=libraryBorrowBook.getCode();

                    List<LibraryBorrowBook.LibBorrowBookData> libBorrowBookData=libraryBorrowBook.
                            getLibBorrowBookDataList();
                    for (int i=0;i<libBorrowBookData.size();i++){
                        String id=libBorrowBookData.get(i).getId();
                        String code=libBorrowBookData.get(i).getCode();
                        String date=libBorrowBookData.get(i).getDate();
                        String accession_no=libBorrowBookData.get(i).getAccession_no();
                        String book_name=libBorrowBookData.get(i).getBook_name();
                        String get_date=libBorrowBookData.get(i).getGet_date();
                        String due_date=libBorrowBookData.get(i).getDue_date();
                        String status=libBorrowBookData.get(i).getD_status();
                        String fine_status=libBorrowBookData.get(i).getFine_status();
                        String academic=libBorrowBookData.get(i).getAcademic();

                        LibBorrowListBean listBean= new LibBorrowListBean(id,code,date,accession_no,book_name,get_date,
                                due_date,status,fine_status,academic);
                        list.add(listBean);
                    }
                    libBorrowListAdapter.notifyDataSetChanged();

                }else{
                    try {
                        System.out.println("attendanceOverallBean====fail"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<LibraryBorrowBook> call, Throwable t) {
                System.out.println("attendanceOverallBean====url"+t.getMessage());
            }

        });
    }

    public class LibBorrowListAdapter extends RecyclerView.Adapter<LibBorrowListAdapter.ViewHolder> {
        private List<LibBorrowListBean> libBorrowListBeans;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_code,text_date,text_fstatus,text_accession_no,text_name,text_getdate,text_duedate,
                    text_status;
            public CircularImageView cir_imagebtn;
            public LinearLayout ll_childdata;

            public ViewHolder(View view) {
                super(view);
                text_code = (TextView) view.findViewById(R.id.text_code);
                text_date = (TextView) view.findViewById(R.id.text_date);
                text_fstatus= (TextView) view.findViewById(R.id.text_fstatus);
                cir_imagebtn=(CircularImageView)view.findViewById(R.id.cir_imagebtn);
                ll_childdata=(LinearLayout)view.findViewById(R.id.ll_childdata);
                text_accession_no= (TextView) view.findViewById(R.id.text_accession_no);
                text_name= (TextView) view.findViewById(R.id.text_name);
                text_getdate= (TextView) view.findViewById(R.id.text_getdate);
                text_duedate= (TextView) view.findViewById(R.id.text_duedate);
                text_status= (TextView) view.findViewById(R.id.text_status);

            }
        }

        public LibBorrowListAdapter(List<LibBorrowListBean> mlist) {
            this.libBorrowListBeans = mlist;
            this.context = context;
        }

        @Override
        public LibBorrowListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_library_borrow_list, parent, false);

            return new LibBorrowListAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(LibBorrowListAdapter.ViewHolder holder, int position) {
            LibBorrowListBean data = libBorrowListBeans.get(position);
            holder.text_code.setText(data.getCode());
            holder.text_date.setText(data.getDate());
            holder.text_fstatus.setText(data.getFine_status());

            holder.cir_imagebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(holder.ll_childdata.getVisibility() == View.VISIBLE){
                        holder.ll_childdata.setVisibility(View.GONE);
                        holder.cir_imagebtn.setBackgroundResource(R.drawable.addicon);
                    }else{
                        holder.ll_childdata.setVisibility(View.VISIBLE);
                        holder.cir_imagebtn.setBackgroundResource(R.drawable.minusicon);
                    }

                    holder.text_accession_no.setText(data.getAccession_no());
                    holder.text_name.setText(data.getBook_name());
                    holder.text_getdate.setText(data.getGet_date());
                    holder.text_duedate.setText(data.getDue_date());
                    holder.text_status.setText(data.getStatus());

                }
            });
        }

        @Override
        public int getItemCount() {
            return libBorrowListBeans.size();
        }
    }

    public class LibBorrowListBean{
        private String id,code,date,accession_no,book_name,get_date,due_date,status,fine_status,academic;

        public LibBorrowListBean(String id,String code,String date,String accession_no,String book_name,
                                 String get_date,String due_date,String status,String fine_status,String academic){
            this.id=id;
            this.code=code;
            this.date=date;
            this.accession_no=accession_no;
            this.book_name=book_name;
            this.get_date=get_date;
            this.due_date=due_date;
            this.status=status;
            this.fine_status=fine_status;
            this.academic=academic;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getAccession_no() {
            return accession_no;
        }

        public void setAccession_no(String accession_no) {
            this.accession_no = accession_no;
        }

        public String getBook_name() {
            return book_name;
        }

        public void setBook_name(String book_name) {
            this.book_name = book_name;
        }

        public String getGet_date() {
            return get_date;
        }

        public void setGet_date(String get_date) {
            this.get_date = get_date;
        }

        public String getDue_date() {
            return due_date;
        }

        public void setDue_date(String due_date) {
            this.due_date = due_date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getFine_status() {
            return fine_status;
        }

        public void setFine_status(String fine_status) {
            this.fine_status = fine_status;
        }

        public String getAcademic() {
            return academic;
        }

        public void setAcademic(String academic) {
            this.academic = academic;
        }
    }

}