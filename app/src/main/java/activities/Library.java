package activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bluebase.educationapp.R;

public class Library extends AppCompatActivity {

    private TextView text_lib_borrowlist,text_lib_rr_list,text_lib_finelist,text_lib_fine_invoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Library");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        text_lib_borrowlist=findViewById(R.id.text_lib_borrowlist);
        text_lib_rr_list=findViewById(R.id.text_lib_rr_list);
        text_lib_finelist=findViewById(R.id.text_lib_finelist);
        text_lib_fine_invoice=findViewById(R.id.text_lib_fine_invoice);

        text_lib_borrowlist.setPaintFlags(text_lib_borrowlist.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        text_lib_borrowlist.setText("Borrow List");
        text_lib_borrowlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(getApplicationContext(),LibraryBorrowList.class);
//                startActivity(i);
//                finish();

                Toast.makeText(Library.this, "Borrow List Service Required", Toast.LENGTH_SHORT).show();

            }
        });

        text_lib_rr_list.setPaintFlags(text_lib_rr_list.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        text_lib_rr_list.setText("Return/Renew List");
        text_lib_rr_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(getApplicationContext(),ForgotPasswordScreen.class);
//                startActivity(i);
//                finish();
                Toast.makeText(Library.this, "Return/Renew List Service Required", Toast.LENGTH_SHORT).show();
            }
        });

        text_lib_finelist.setPaintFlags(text_lib_finelist.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        text_lib_finelist.setText("Fine List");
        text_lib_finelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),LibraryFineList.class);
                startActivity(i);
                finish();
            }
        });

        text_lib_fine_invoice.setPaintFlags(text_lib_fine_invoice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        text_lib_fine_invoice.setText("Fine Invoice");
        text_lib_fine_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(getApplicationContext(),ForgotPasswordScreen.class);
//                startActivity(i);
//                finish();

                Toast.makeText(Library.this, "Fine List Service Required", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent MainActivity = new Intent(getBaseContext(), Dashboard.class);
        MainActivity.addCategory(Intent.CATEGORY_HOME);
        MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainActivity);
        Library.this.finish();
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

}
