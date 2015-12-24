package com.byteshaft.jsonparsing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.byteshaft.jsonparsing.models.MovieModel;

import java.util.List;

public class MovieAdaptor extends ArrayAdapter {

    private List<MovieModel> movieModelList;
    private int resource;
    LayoutInflater inflater;

    public MovieAdaptor(Context context, int resource, List<MovieModel> objects) {
        super(context, resource, objects);

        movieModelList = objects;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
        }
        ImageView icoView;
        TextView tvMovieName;
        TextView tvYear;
        TextView tagLine;
        TextView tvDuration;
        RatingBar ratingBar;
        TextView tvCast;
        TextView tvDirector;
        TextView tvStory;

        icoView = (ImageView) convertView.findViewById(R.id.iv_icon);
        tvMovieName = (TextView) convertView.findViewById(R.id.tv_movie_name);
        tvYear = (TextView) convertView.findViewById(R.id.tv_year);
        tagLine = (TextView) convertView.findViewWithTag(R.id.tv_tagline);
        tvDuration = (TextView) convertView.findViewById(R.id.tv_duration);
        ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);
        tvDirector = (TextView) convertView.findViewById(R.id.tv_director);
        tvStory = (TextView) convertView.findViewById(R.id.tv_story);
        tvCast = (TextView) convertView.findViewById(R.id.tv_cast);

        tvMovieName.setText(movieModelList.get(position).getMovie());
//        tvYear.setText(movieModelList.get(position).getYear());
//        System.out.println("Tag line");
//        System.out.println(tagLine == null);
//
//        tagLine.setText(movieModelList.get(position).getTagline());

        tvDuration.setText(movieModelList.get(position).getDuration());

        // rating bar
        ratingBar.setRating(movieModelList.get(position).getRating() / 2);

        StringBuffer stringBuffer = new StringBuffer();
        for (MovieModel.Cast cast : movieModelList.get(position).getCastList()) {
            stringBuffer.append(cast.getName() + ", ");
        }
        tvCast.setText(stringBuffer);
        tvDirector.setText(movieModelList.get(position).getDirector());
        tvStory.setText(movieModelList.get(position).getStory());
        return convertView;
    }
}
