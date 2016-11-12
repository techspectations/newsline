package com.example.user.newsline;

import android.graphics.Typeface;
import android.os.AsyncTask;
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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    LinearLayout.LayoutParams params;

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

                    params = new LinearLayout.LayoutParams
                            (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10,10,10,20);

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
                            final String news_id = news_obj.getString("id");
                            TextView title_tv = new TextView(getActivity());
                            title_tv.setTypeface(null, Typeface.BOLD);
                            title_tv.setTextSize(18);
                            title_tv.setLayoutParams(params);
                            title_tv.setText(news_obj.getString("title"));
                            title_tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    AsyncNews asyncNews = new AsyncNews(news_id);
                                    asyncNews.execute();

                                }
                            });
                            timeline_news_ll.addView(title_tv);
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


    final class AsyncNews extends AsyncTask<Void, Void, String > {

        String news_id;

        public AsyncNews(String data) {
            this.news_id = data;
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try {
                OkHttpClient oClient = new OkHttpClient();
                if (!news_id.equals("")) {
                    Request request = new Request.Builder()
                            .url("http://159.203.139.57/api/news/" + news_id)
                            .build();
                    Response response = oClient.newCall(request).execute();
                    String jsonString = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    arrangeNews(jsonObject);

                }
            } catch (Exception e) {
                Log.e("e", "Exception : " + e.getMessage());
            }
            return null;
        }
    }

    public void arrangeNews(final JSONObject jsonObject){

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try{

                    JSONObject object = jsonObject;
                    NewsDetails newsDetails = new NewsDetails();
                    newsDetails.jsonValueNews(object);
                    getFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.main_container, newsDetails)
                            .commit();

                }catch (Exception e){

                }

            }
        });

    }

}
