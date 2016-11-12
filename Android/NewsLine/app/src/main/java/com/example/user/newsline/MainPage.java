package com.example.user.newsline;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user on 12-Nov-16.
 */
public class MainPage extends Fragment {

    TextView headlines_tv;
    TextView trending_tv;
    TextView caregories_tv;
    EditText search_et;
    Button go_btn;

    String data;
    Async async;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View theInflatedView = inflater.inflate(R.layout.activity_main_page, container, false);

        headlines_tv = (TextView) theInflatedView.findViewById(R.id.headlines_tv);
        trending_tv = (TextView) theInflatedView.findViewById(R.id.trending_tv);
        caregories_tv = (TextView) theInflatedView.findViewById(R.id.caregories_tv);
        search_et = (EditText) theInflatedView.findViewById(R.id.search_et);
        go_btn = (Button) theInflatedView.findViewById(R.id.go_btn);
        data = "Categories";
        async = new Async(data);
        async.execute();

        headlines_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = headlines_tv.getText().toString();
                async = new Async(data);
                async.execute();
            }
        });
        trending_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = trending_tv.getText().toString();
                async = new Async(data);
                async.execute();
            }
        });
        caregories_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = caregories_tv.getText().toString();
                async = new Async(data);
                async.execute();
            }
        });
        go_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_et.getText().toString().equals("")) {
                    search_et.setError("Enter");
                } else {
                    data = search_et.getText().toString();
                    async = new Async(data);
                    async.execute();
                }
            }
        });

        return theInflatedView;

    }


    final class Async extends AsyncTask<Void, Void, JSONArray> {

        String data;

        public Async(String data) {
            this.data = data;
        }

        @Override
        protected JSONArray doInBackground(Void... arg0) {
            try {
                OkHttpClient oClient = new OkHttpClient();
                if (!data.equals("")) {
                    Request request = new Request.Builder()
                            .url("http://159.203.139.57/api/" + data)
                            .build();
                    Response response = oClient.newCall(request).execute();
                    String jsonString = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    int type = jsonObject.getInt("type");
                    if (type == 1) {
                        String heading = jsonObject.getString("heading");
                        JSONArray jsonArray = jsonObject.getJSONArray("values");
                        Log.d("array", jsonArray.toString());
                        arrangeTags(jsonArray, heading);
                    }else if (type == 2){
                        JSONArray jsonArray = jsonObject.getJSONArray("values");
                        Log.d("array",jsonArray.toString());
                        arrangeTimeLine(jsonArray);
                    }

                }
            } catch (Exception e) {
                Log.e("e", "Exception : " + e.getMessage());
            }
            return null;
        }
    }


    public void arrangeTags(final JSONArray jsonArray, final String heading ) {

        final JSONArray array = jsonArray;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {
                        TagView tagView = new TagView();
                        tagView.jsonValue(array,heading);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.main_container, tagView)
                                .commit();

                } catch (Exception e) {
                    Log.d("e", e.toString());
                }
            }
        });
    }
    public void arrangeTimeLine(final JSONArray jsonArray ) {

        final JSONArray array = jsonArray;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {
                    TimeLine timeLine = new TimeLine();
                    timeLine.jsonValues(array);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_container, timeLine)
                            .commit();

                } catch (Exception e) {
                    Log.d("e", e.toString());
                }
            }
        });
    }

}
