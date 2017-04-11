package br.com.valmirosjunior.caronafap.util;

import android.content.res.Resources;
import android.support.annotation.NonNull;

/**
 * Created by junior on 10/04/17.
 */

public class Util {
    @NonNull
    public static String getValueStringResource(int stringId){
        return Resources.getSystem().getString(stringId);
    }
}
