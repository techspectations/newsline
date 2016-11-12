package com.example.user.newsline;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import me.gujun.android.taggroup.TagGroup;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user on 12-Nov-16.
 */
public class TagView extends Fragment {
    View theInflatedView;
    String data;
    JSONArray array;
    String heading;
    String[] tags;
    TextView tag_heading_tv;
    TagGroup mTagGroup;
    Async async;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        theInflatedView = inflater.inflate(R.layout.activity_tag_view, container, false);

        tag_heading_tv = (TextView) theInflatedView.findViewById(R.id.tag_heading_tv);

        tag_heading_tv.setText(heading);
        tags = new String[array.length()];
        for (int i = 0; i < array.length(); i++){
            try {
                JSONObject object = array.getJSONObject(i);
                tags[i] = object.getString("text");
            }catch (Exception e){

            }

        }

        mTagGroup = (TagGroup) theInflatedView.findViewById(R.id.tag_group);
        mTagGroup.setTags(tags);

        mTagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                data = tag;
                async = new Async(data);
                async.execute();
            }
        });
        return theInflatedView;
    }
    public void jsonValue(JSONArray jsonArray,String heading){
        array = jsonArray;
        this.heading = heading;
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
                    tagView.jsonValue(array, heading);
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
