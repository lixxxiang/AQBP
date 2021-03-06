package com.cgwx.yyfwptz.lixiang.AQBP.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cgwx.yyfwptz.lixiang.AQBP.R;
import com.cgwx.yyfwptz.lixiang.AQBP.di.components.DaggerFMineComponent;
import com.cgwx.yyfwptz.lixiang.AQBP.di.modules.FMineModule;
import com.cgwx.yyfwptz.lixiang.AQBP.presenter.FMainPresenter;
import com.cgwx.yyfwptz.lixiang.AQBP.presenter.FMineContract;
import com.cgwx.yyfwptz.lixiang.AQBP.presenter.FMinePresenter;
import com.cgwx.yyfwptz.lixiang.AQBP.view.activity.LoginActivity;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import static android.content.Context.MODE_PRIVATE;
import static com.cgwx.yyfwptz.lixiang.AQBP.view.activity.MainActivity.infos;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment extends Fragment implements FMineContract.View{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.poTel) TextView potel;
    @BindView(R.id.pname) TextView pname;
    @BindView(R.id.poNo) TextView pNO;
    @BindView(R.id.quitt) Button quitt;

    protected View mRootView;

    private OkHttpClient changeStateClient;
    int serversLoadTimes = 0;
    int maxLoadTimes = 19;
    private OkHttpClient refuseAlarmClient;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Gson refuseAlarmgson;
    public static boolean quitting;

    private OnFragmentInteractionListener mListener;


    @Inject
    FMinePresenter presenter;

    public MineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
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
        mRootView = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DaggerFMineComponent.builder().fMineModule(new FMineModule(this))
                .build()
                .inject(this);


        quitt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitting = true;
                SharedPreferences sp = getActivity().getSharedPreferences("Puser", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
//        for (int i = 0; i < infos.length; i++){
//            Log.e("ddfsfsddd", infos[i]);
//        }

        pNO.setText(infos[0]);
        pname.setText(infos[1]);

        String pre = infos[2].substring(0, 3);
        String post = infos[2].substring(7, 11);
        potel.setText(pre + "****" + post);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
