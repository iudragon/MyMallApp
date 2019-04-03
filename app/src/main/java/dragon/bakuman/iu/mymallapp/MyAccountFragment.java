package dragon.bakuman.iu.mymallapp;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {


    public MyAccountFragment() {
        // Required empty public constructor
    }

    private Button viewAllAddressButton;

    private Dialog contributionDialog;
    private ImageView contribFlaticonCircle;

    public static final int MANAGE_ADDRESS = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_my_account, container, false);

        contribFlaticonCircle = view.findViewById(R.id.contribFlaticonCircle);
        contribFlaticonCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contributionDialog = new Dialog(view.getContext());
                contributionDialog.setContentView(R.layout.contribution_dialog);
                contributionDialog.setCancelable(true);

                contributionDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                contributionDialog.show();



//                Button contributionBtn = contributionDialog.findViewById(R.id.contrib);
//
//                contributionBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(view.getContext(), "Cliked the contrib btn", Toast.LENGTH_SHORT).show();
//
//
//                        contributionDialog.dismiss();
//                    }
//                });

            }
        });





//        viewAllAddressButton = view.findViewById(R.id.view_all_addresses_btn);
//        viewAllAddressButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent myAddressIntent = new Intent(getContext(), MyAddressesActivity.class);
//                myAddressIntent.putExtra("MODE", MANAGE_ADDRESS);
//                startActivity(myAddressIntent);
//            }
//        });

        return view;
    }

}
