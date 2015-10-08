package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    List<ParseObject> helpBuyList ;
    ListView helpBuyListView;
    HelpBuyListAdapter HBLA;
    SwipeRefreshLayout swipeLayout;

    OnFragmentInteractionListener mListener;

    static int ON_PARSEALL_DONE = 0;

    String TAG = "ItemFragment";


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.fragment_layout,null);
        helpBuyListView = (ListView)contentView.findViewById(R.id.helpBuy_listView);
        swipeLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                                    android.R.color.holo_green_light,
                                    android.R.color.holo_orange_light,
                                    android.R.color.holo_red_light);
        HBLA = new HelpBuyListAdapter(getActivity());
        helpBuyListView.setAdapter(HBLA);
        parseSetting();


        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
             mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void parseSetting(){
        ParseAnalytics.trackAppOpenedInBackground(getActivity().getIntent());

   //     currentUser.logOut();
        (new Thread(new parseQueryAll())).start();

    }

    public class parseQueryAll implements   Runnable{

        @Override
        public void run() {
            ParseQuery<ParseObject> query =  ParseQuery.getQuery("HelpBuy");
            query.setLimit(100);
            try {

                helpBuyList = null;
                if (HBLA!=null){
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }

                helpBuyList = query.find();
                Log.e(TAG, "queryAllDone");

                Message msg = new Message();
                msg.what = ON_PARSEALL_DONE;
                handler.sendMessage(msg);

            }catch (ParseException pe){
                Log.e("error", pe.getMessage());
            }
        }
    }


    @Override public void onRefresh() {
        (new Thread(new parseQueryAll())).start();
    }

    public class HelpBuyListAdapter extends BaseAdapter

    {

        Context c;
        HelpBuyListAdapter(Context c){
            this.c = c;
        }

        @Override
        public int getCount() {
            if (helpBuyList==null)
                return 0;
            return helpBuyList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if(convertView == null){
                LayoutInflater li = LayoutInflater.from(c);
                convertView = li.inflate(R.layout.list_item_view, null);
                holder = new ViewHolder((Button)convertView.findViewById(R.id.textView_title),
                        (Button)convertView.findViewById(R.id.textView_cate));
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startContentFragment(position);
                }
            });

            if (helpBuyList!=null) {
                holder.text_category.setText(helpBuyList.get(position).getString("category"));
                holder.text_title.setText(helpBuyList.get(position).getString("title"));
            }

            return convertView;
        }

        class ViewHolder {
            Button text_title;
            Button text_category;

            public ViewHolder(Button text_title, Button text_category) {
                this.text_title = text_title;
                this.text_category = text_category;
            }
        }

    }

    public Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                //ON_PARSEALL_DONE
                case 0:{
                    if (swipeLayout!=null)
                        swipeLayout.setRefreshing(false);
                    HBLA.notifyDataSetChanged();
                }break;
                case 1:{
                    HBLA.notifyDataSetChanged();
                }break;
            }
        }
    };



    public void startContentFragment(int position){
        Log.d(TAG,"startContentFragment");
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putInt("what",0);
        mListener.onFragmentInteraction(bundle);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Bundle bundle);
    }

}
