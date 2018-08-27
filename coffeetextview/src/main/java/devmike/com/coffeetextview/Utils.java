package devmike.com.coffeetextview;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.RestrictTo;
import android.util.Log;


public class Utils {

    public static float exFloat(Object obj, Context c){
        if (obj instanceof Float){
            return Float.valueOf(obj.toString());
        }else if (obj instanceof DimenRes){
            return c.getResources().getDimension((int)obj);
        }else{
            Log.w(CoffeeTextView.class.getSimpleName(), "###### Dimension must be a float ######");
            return 12;
        }
    }
}
