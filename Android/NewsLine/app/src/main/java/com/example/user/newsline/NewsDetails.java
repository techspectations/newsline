package com.example.user.newsline;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

/**
 * Created by user on 12-Nov-16.
 */
public class NewsDetails extends Fragment {

    View theInflatedView;
    JSONObject object;
    TextView news_title_tv;
    TextView news_content_tv;
    ImageView news_img;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        theInflatedView = inflater.inflate(R.layout.activity_news, container, false);

        news_content_tv = (TextView) theInflatedView.findViewById(R.id.news_content_tv);
        news_title_tv = (TextView) theInflatedView.findViewById(R.id.news_title_tv);
        news_img = (ImageView) theInflatedView.findViewById(R.id.news_img);

        try {
            news_content_tv.setText(object.getString("content"));
            news_title_tv.setText(object.getString("title"));
            Picasso.with(getActivity())
                    .load(object.getString("imgMob"))
                    .into(news_img);


        } catch (Exception e) {
            Log.d("log",e.toString());
        }


        return theInflatedView;
    }

    public void jsonValueNews(JSONObject jsonObject) {
        object = jsonObject;
    }
}