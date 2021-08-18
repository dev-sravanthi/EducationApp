package util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatButton;


public class MyCustomButton extends AppCompatButton {

    public MyCustomButton(Context context, AttributeSet attr){
        super(context,attr);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "Montserrat-Regular.ttf"));
    }
}