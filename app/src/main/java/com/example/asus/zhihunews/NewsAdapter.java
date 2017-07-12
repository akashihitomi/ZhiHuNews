package com.example.asus.zhihunews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static android.R.id.list;
import static android.media.CamcorderProfile.get;


public class NewsAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;
        private List<NEWS> NewsData;
        private Context context;

        public NewsAdapter(Context context, List<NEWS> listNewsBean) {
            this.mLayoutInflater = LayoutInflater.from(context);
            this.NewsData = listNewsBean;
            this.context = context;
        }

        @Override
        public int getCount() {
            return NewsData.size();
        }

        @Override
        public Object getItem(int position) {
            return NewsData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.home, null);
                viewHolder = new ViewHolder();
                viewHolder.img_icon = (ImageView) convertView.findViewById(R.id.icon);
                viewHolder.news_title = (TextView) convertView.findViewById(R.id.Title);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String image_url = NewsData.get(position).getIcon_URL();
            Picasso.with(context).
                    load(image_url).
                    into(viewHolder.img_icon);
            NEWS news = NewsData.get(position);
            viewHolder.news_title.setText(news.getTitle());
            return convertView;
        }
}

class ViewHolder{
    ImageView img_icon;
    TextView news_title;
}

