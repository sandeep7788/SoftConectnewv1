package com.exam.softconect.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exam.softconect.Helper.TestPanelHelper;
import com.exam.softconect.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeashBoardRankAdapter extends PagerAdapter {

    private ArrayList<TestPanelHelper> imageModelArrayList;
    private LayoutInflater inflater;
    private Context context;


    public DeashBoardRankAdapter(Context context, ArrayList<TestPanelHelper> imageModelArrayList) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.all_rank_single_item_deashboard, view, false);

        assert imageLayout != null;

        TextView Name_ = (TextView) imageLayout.findViewById(R.id.txt_name_rank_deashboard);
        TextView test_ = (TextView) imageLayout.findViewById(R.id.txt_test_name_rank_deashboard);
        TextView address_ = (TextView) imageLayout.findViewById(R.id.txt_address_rank_deashboard);
        TextView rank_ = (TextView) imageLayout.findViewById(R.id.txt_rank_deashboard);
        CircleImageView img_profile = imageLayout.findViewById(R.id.img_profile_rank_deashboard);

        Name_.setText(imageModelArrayList.get(position).getDeashboard_name());
        test_.setText(imageModelArrayList.get(position).getDeashboard_test());
        address_.setText(imageModelArrayList.get(position).getDeashboard_city()+","+imageModelArrayList.get(position).getDeashboard_district());
        rank_.setText("Rank - " + imageModelArrayList.get(position).getDeashboard_rank());

        String str_profile = imageModelArrayList.get(position).getDeashboard_image_profile();

        if (!str_profile.equalsIgnoreCase("")) {

            Picasso.with(context).load(str_profile).into(img_profile);
        }


        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
