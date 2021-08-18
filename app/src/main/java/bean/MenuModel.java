package bean;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class MenuModel {
    public String string;
    public Bitmap bitmap;
    public final List<String> children = new ArrayList<String>();

    public MenuModel(String string,Bitmap bitmap){
        this.string = string;
        this.bitmap = bitmap;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public List<String> getChildren() {
        return children;
    }
}
