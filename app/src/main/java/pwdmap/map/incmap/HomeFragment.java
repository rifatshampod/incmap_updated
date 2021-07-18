package pwdmap.map.incmap;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class HomeFragment extends Fragment {

    TextView blogBtn, aboutBtn, mapBtn, googleMapBtn, bemyeyeBtn, thirdappBtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        blogBtn = v.findViewById(R.id.blog_btn);
        aboutBtn = v.findViewById(R.id.about_btn);
        mapBtn = v.findViewById(R.id.map_btn);
        googleMapBtn = v.findViewById(R.id.btn_google_maps);
        bemyeyeBtn = v.findViewById(R.id.btn_bemyeye);
        thirdappBtn = v.findViewById(R.id.btn_third_app);


        blogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Category.class);
                //intent.putExtra("name",1);
                startActivity(intent);
            }
        });

        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MiddleLayer.class);
                intent.putExtra("name",2);
                startActivity(intent);
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getActivity(), MiddleLayer.class);
                //intent.putExtra("name",3);
                //startActivity(intent);

                Intent intent = new Intent(getActivity(), NearByPlaces.class);              /*--------Floyd work-------*/
                //intent.putExtra("name",1);
                startActivity(intent);
            }
        });

        googleMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setClassName("com.google.android.apps.maps",
                        "");

                startActivity(i);  */

                Intent i = new Intent(Intent.ACTION_MAIN);
                PackageManager managerclock = v.getContext().getPackageManager();
                i = managerclock.getLaunchIntentForPackage("com.google.android.apps.maps");
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(i);

            }
        });

        bemyeyeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { /*
                Intent i = new Intent(Intent.ACTION_MAIN);
                PackageManager managerclock = v.getContext().getPackageManager();
                i = managerclock.getLaunchIntentForPackage("org.telegram.messenger");
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(i); */

                final String appPackageName = "com.bemyeyes.bemyeyes";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        thirdappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = "com.google.android.accessibility.soundamplifier&hl=en&gl=US";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });






        return v;
    }
}