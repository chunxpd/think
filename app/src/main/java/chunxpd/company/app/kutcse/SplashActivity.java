package chunxpd.company.app.kutcse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SplashActivity extends Activity {

    //private final static int closeMill = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this, MainActivity.class).setAction(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

       // getKeyHash(this);
        finish();

    }

    public  static final String getKeyHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("TAG" + "KeyHash:%s", keyHash);
                return keyHash;
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            Log.d("TAG" + "getKeyHash Error:%s", e.getMessage());
        }
        catch (NoSuchAlgorithmException e) {
            Log.d("TAG"+"getKeyHash Error:%s", e.getMessage());
        }
        return "";
    }

}