package com.exam.softconect.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exam.softconect.Helper.TestPanelHelper;
import com.exam.softconect.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllRankAdapter extends RecyclerView.Adapter<AllRankAdapter.ProductViewHolder> {

    Context mctx;
    private List<TestPanelHelper> allRankList;

    public AllRankAdapter(Context mctx, List<TestPanelHelper> allRankList) {
        this.mctx = mctx;
        this.allRankList = allRankList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mctx);
        View view = inflater.inflate(R.layout.all_rank_single_item, null);
        return new ProductViewHolder(view, mctx, allRankList);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        TestPanelHelper rankList_1 = allRankList.get(position);

        holder.txt_name_rank.setText(rankList_1.getRank_list_name()+"\n"+rankList_1.getRank_list_address());
        holder.txt_rank_test_name_single.setText(rankList_1.getRank_list_test_name());
        holder.txt_rank_total_score.setText(rankList_1.getRank_list_get_marks());
        holder.txt_rank_total_test_marks.setText(rankList_1.getRank_list_total_marks());
        holder.txt_rank.setText(rankList_1.getRank_list_rank());

        if (!rankList_1.getRank_list_image().equalsIgnoreCase("")) {
            Picasso.with(mctx).load(rankList_1.getRank_list_image()).fit().centerCrop().into(holder.img_profile_rank);

        }

    }

    @Override
    public int getItemCount() {
        return allRankList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_name_rank, txt_rank_test_name_single, txt_rank_total_score, txt_rank_total_test_marks,txt_rank;
        List<TestPanelHelper> testList;
        Context mctx;
        CircleImageView img_profile_rank;

        public ProductViewHolder(View itemView, Context mctx, List<TestPanelHelper> testList) {
            super(itemView);
            this.mctx = mctx;
            this.testList = testList;
            itemView.setOnClickListener(this);
            img_profile_rank = itemView.findViewById(R.id.img_profile_rank);
            txt_name_rank = itemView.findViewById(R.id.txt_name_rank);
            txt_rank_test_name_single = itemView.findViewById(R.id.txt_rank_test_name_single);
            txt_rank_total_score = itemView.findViewById(R.id.txt_rank_total_score);
            txt_rank_total_test_marks = itemView.findViewById(R.id.txt_rank_total_test_marks);
            txt_rank = itemView.findViewById(R.id.txt_rank);

        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();


        }
    }
}

