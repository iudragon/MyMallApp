 package dragon.bakuman.iu.mymallapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

 public class RegisterActivity extends AppCompatActivity {


    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFrameLayout = findViewById(R.id.register_frameLayout);

        // this will set our Fragment as soon as Register Activity is created.

        setFragment(new SignInFragment());

    }

     private void setFragment(Fragment fragment) {

         FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
         fragmentTransaction.replace(mFrameLayout.getId(), fragment);
         fragmentTransaction.commit();


     }
 }
