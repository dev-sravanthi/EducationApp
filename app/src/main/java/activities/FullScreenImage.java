package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bluebase.educationapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class FullScreenImage extends AppCompatActivity {
    ImageView img_fullscreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_image);

        img_fullscreen=findViewById(R.id.img_fullscreen);

        Intent i=getIntent();
        String url=i.getStringExtra("imageurl");

        Glide.with(FullScreenImage.this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_fullscreen);

        img_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(getApplicationContext(),CircularEventsNews.class);
//                startActivity(i);
                finish();
            }
        });
    }
}
