package dragon.bakuman.iu.mymallapp;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {


    public SignUpFragment() {
        // Required empty public constructor
    }

    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;

    private EditText email;
    private EditText fullname;
    private EditText password;
    private EditText confirmpassword;

    private ImageButton closeBtn;
    private Button signUpBtn;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firebaseFirestore;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public static boolean disableCloseBtn = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        parentFrameLayout = getActivity().findViewById(R.id.register_frameLayout);

        alreadyHaveAnAccount = view.findViewById(R.id.tv_already_hav_an_account);

        email = view.findViewById(R.id.sign_up_email);
        fullname = view.findViewById(R.id.sign_up_name);
        password = view.findViewById(R.id.sign_up_password);
        confirmpassword = view.findViewById(R.id.sign_up_confirm_password);

        closeBtn = view.findViewById(R.id.sign_up_close_btn);
//        signUpBtn = view.findViewById(R.id.sign_up_btn);
        progressBar = view.findViewById(R.id.sign_up_progress_bar);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();

        if (disableCloseBtn) {

            closeBtn.setVisibility(View.GONE);
        } else {

            closeBtn.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());

            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainIntent();

            }
        });


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


//        signUpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkEmailAndPassword();
//            }
//        });
    }

    private void checkEmailAndPassword() {


        Drawable customErrorIcon = getResources().getDrawable(R.drawable.ic_fitness);
        customErrorIcon.setBounds(0, 0, customErrorIcon.getIntrinsicWidth(), customErrorIcon.getIntrinsicHeight());


        //regular expression!!?
        if (email.getText().toString().matches(emailPattern)) {

            if (password.getText().toString().equals(confirmpassword.getText().toString())) {

                progressBar.setVisibility(View.VISIBLE);
//                signUpBtn.setEnabled(false);
//                signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Map<String, Object> userData = new HashMap<>();
                            userData.put("fullname", fullname.getText().toString());

                            firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                    .set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        CollectionReference userDataReference = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");

                                        ///// Mapss

                                        Map<String, Object> wishlistMap = new HashMap<>();
                                        wishlistMap.put("list_size", (long) 0);

                                        Map<String, Object> ratingsMap = new HashMap<>();
                                        ratingsMap.put("list_size", (long) 0);

                                        Map<String, Object> cartMap = new HashMap<>();
                                        cartMap.put("list_size", (long) 0);

                                        Map<String, Object> myAddressesMap = new HashMap<>();
                                        myAddressesMap.put("list_size", (long) 0);

                                        ///// Mapss


                                        final List<String> documentNames = new ArrayList<>();
                                        documentNames.add("MY_WISHLIST");
                                        documentNames.add("MY_RATINGS");
                                        documentNames.add("MY_CART");
                                        documentNames.add("MY_ADDRESSES");

                                        List<Map<String, Object>> documentFields = new ArrayList<>();
                                        documentFields.add(wishlistMap);
                                        documentFields.add(ratingsMap);
                                        documentFields.add(cartMap);
                                        documentFields.add(myAddressesMap);


                                        for (int x = 0; x < documentNames.size(); x++) {

                                            final int finalX = x;
                                            userDataReference.document(documentNames.get(x))
                                                    .set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        if (finalX == documentNames.size() - 1) {

                                                            mainIntent();
                                                        }

                                                    } else {

                                                        progressBar.setVisibility(View.INVISIBLE);
//                                                        signUpBtn.setEnabled(true);
//                                                        signUpBtn.setTextColor(getResources().getColor(R.color.colorWhite));
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });
                                        }


                                    } else {

                                        String error = task.getException().getMessage();
                                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });


                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
//                            signUpBtn.setEnabled(true);
//                            signUpBtn.setTextColor(getResources().getColor(R.color.colorWhite));
                            String error = task.getException().getMessage();
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            } else {

                confirmpassword.setError("Password does not match", customErrorIcon);


            }

        } else {

            email.setError("Invalid Email", customErrorIcon);


        }

    }
//
//    private void checkInputs() {
//
//        if (!TextUtils.isEmpty(email.getText())) {
//
//
//            if (!TextUtils.isEmpty(fullname.getText())) {
//
//                if (!TextUtils.isEmpty(password.getText()) && password.length() >= 8) {
//
//                    if (!TextUtils.isEmpty(confirmpassword.getText())) {
//
////                        signUpBtn.setEnabled(true);
////                        signUpBtn.setTextColor(getResources().getColor(R.color.colorWhite));
//
//                    } else {
//                        signUpBtn.setEnabled(false);
//                        signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
//                    }
//
//
//                } else {
//                    signUpBtn.setEnabled(false);
//                    signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
//                }
//
//            } else {
//                signUpBtn.setEnabled(false);
//                signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
//            }
//
//
//        } else {
//
//            signUpBtn.setEnabled(false);
//            signUpBtn.setTextColor(Color.argb(50, 255, 255, 255));
//
//        }
//
//    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_out_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();


    }

    private void mainIntent() {

        if (disableCloseBtn) {
            disableCloseBtn = false;


        } else {

            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(mainIntent);

        }

        getActivity().finish();

    }

}
