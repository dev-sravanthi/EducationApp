package util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Date;

public class AndroidUtil {
    private Context con;

    public AndroidUtil(Context con) {
        this.con = con;
    }

    public AndroidUtil() {

    }

    public boolean isOnline() {
        ConnectivityManager connectivity = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static int getMonthNumber(String name) {
        int month_no = 0;

        System.out.println(name);
        switch (name) {
            case "January":
                month_no = 1;
                break;
            case "February":
                month_no = 2;
                break;
            case "March":
                month_no = 3;
                break;
            case "April":
                month_no = 4;
                break;
            case "May":
                month_no = 5;
                break;
            case "June":
                month_no = 6;
                break;
            case "July":
                month_no = 7;
                break;
            case "August":
                month_no = 8;
                break;
            case "September":
                month_no = 9;
                break;
            case "October":
                month_no = 10;
                break;
            case "November":
                month_no = 11;
                break;
            case "December":
                month_no = 12;
                break;
            default:
                month_no = 1;
        }
        return month_no;
    }

}
