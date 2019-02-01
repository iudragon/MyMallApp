package dragon.bakuman.iu.mymallapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private FrameLayout mFrameLayout;

    public static boolean onResetPasswordFragment = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFrameLayout = findViewById(R.id.register_frameLayout);

        // this will set our Fragment as soon as Register Activity is created.

        setDefaultFragment(new SignInFragment());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (onResetPasswordFragment) {


                Log.d(TAG, "onKeyDown: HAHA " + onResetPasswordFragment);

                onResetPasswordFragment = false;

                Log.d(TAG, "onKeyDown: HAHA AFTER " + onResetPasswordFragment);

                setFragment(new SignInFragment());

                return false;
            }


        }

        return super.onKeyDown(keyCode, event);
    }

    private void setDefaultFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(mFrameLayout.getId(), fragment);
        fragmentTransaction.commit();


    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_out_from_right);
        fragmentTransaction.replace(mFrameLayout.getId(), fragment);
        fragmentTransaction.commit();


    }
}
