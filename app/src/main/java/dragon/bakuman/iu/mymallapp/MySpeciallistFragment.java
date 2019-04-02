package dragon.bakuman.iu.mymallapp;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class MySpeciallistFragment extends Fragment {


    public MySpeciallistFragment() {
        // Required empty public constructor
    }

    private RecyclerView speciallistRecyclerView;

    private Dialog loadingDialog;

    public static SpeciallistAdapter speciallistAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_speciallist, container, false);

        ///// loading dialog

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);

        loadingDialog.setCancelable(false);

        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));

        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        loadingDialog.show();

        ///// loading dialog

        speciallistRecyclerView = view.findViewById(R.id.my_special_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        speciallistRecyclerView.setLayoutManager(linearLayoutManager);

        if (DBqueries.speciallistModelList.size() == 0){

            DBqueries.speciallist.clear();

            DBqueries.loadSpeciallist(getContext(), loadingDialog, true);
        } else {
            loadingDialog.dismiss();
        }

        speciallistAdapter = new SpeciallistAdapter(DBqueries.speciallistModelList, true);
        speciallistRecyclerView.setAdapter(speciallistAdapter);
        speciallistAdapter.notifyDataSetChanged();


        return view;
    }

}
