package com.example.user.newsline;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by user on 12-Nov-16.
 */
public class TimeLine extends Fragment {

    View theInflatedView;
    LinearLayout timeline_ll;
    View timeline_item_view;
    TextView timeline_date_tv;
    LinearLayout timeline_news_ll;
    JSONArray jsonArray;
    String date;
    JSONArray news_jsonarray;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        theInflatedView = inflater.inflate(R.layout.activity_timeline, container, false);
        timeline_ll = (LinearLayout) theInflatedView.findViewById(R.id.timeline_ll);

        arrangeTimeLine(jsonArray);

        return theInflatedView;
    }



    public void arrangeTimeLine(final JSONArray jsonArray) {

        final JSONArray array = jsonArray;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        date = object.getString("date");
                        news_jsonarray = object.getJSONArray("news");
                        timeline_item_view = View.inflate(getActivity(), R.layout.timeline_item, null);

                        timeline_date_tv = (TextView) timeline_item_view.findViewById(R.id.timeline_date_tv);
                        timeline_news_ll = (LinearLayout) timeline_item_view.findViewById(R.id.timeline_news_ll);
                        timeline_date_tv.setText(date);
                        for (int j = 0; i < news_jsonarray.length(); i++){
                            JSONObject news_obj = news_jsonarray.getJSONObject(i);
                            TextView title = new TextView(getActivity());
                            title.setText(news_obj.getString("title"));
                            timeline_news_ll.addView(title);
                        }


                        timeline_ll.addView(timeline_item_view);
                    }
                } catch (Exception e) {
                    Log.d("e", e.toString());
                }
            }
        });
    }

    public void jsonValues(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }
}
