package com.company.opsc_south_side_application.profileFragments;

import static com.company.opsc_south_side_application.MainActivity.favouritePlacesModelsList;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.opsc_south_side_application.R;
import com.company.opsc_south_side_application.adapters.whereplaceAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView back;
    private RecyclerView recyclerViewFav;

    public FavouriteListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteListFragment newInstance(String param1, String param2) {
        FavouriteListFragment fragment = new FavouriteListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_favourite_list, container, false);

        back = view.findViewById(R.id.textViewBackToProfile);
        recyclerViewFav = view.findViewById(R.id.recyclerViewFavouritePlace2);

        recyclerViewFav.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        whereplaceAdapter adapterfav = new whereplaceAdapter(favouritePlacesModelsList);

        recyclerViewFav.setAdapter(adapterfav);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment1 = new profileFragment();
                //Developers, 2021)
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerViewWhere, fragment1).commit();
            }
        });
        return view;
    }
}