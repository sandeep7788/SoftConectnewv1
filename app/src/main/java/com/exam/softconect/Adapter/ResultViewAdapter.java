package com.exam.softconect.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.exam.softconect.Helper.TestPanelHelper;
import com.exam.softconect.R;

import java.util.List;

public class ResultViewAdapter extends RecyclerView.Adapter<ResultViewAdapter.ProductViewHolder> {

    Context mctx;
    private List<TestPanelHelper> resultList;
    String str_reult_status;


    public ResultViewAdapter(Context mctx, List<TestPanelHelper> resultList) {
        this.mctx = mctx;
        this.resultList = resultList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mctx);
        View view = inflater.inflate(R.layout.result_single_item, null);
        return new ProductViewHolder(view, mctx, resultList);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        TestPanelHelper testList_1 = resultList.get(position);

        holder.txt_question_number_result.setText(testList_1.getQuestion_number());
        holder.txt_your_answer_result.setText(testList_1.getYour_answer());
        holder.txt_correct_answer_result.setText(testList_1.getCorrect_answer());
        holder.txt_your_scored_result.setText(testList_1.getYour_scored());

        str_reult_status = testList_1.getQuestion_status();

        if (str_reult_status.equalsIgnoreCase("0")) {

            holder.img_status_result_cross.setVisibility(View.VISIBLE);
            holder.img_status_result_right.setVisibility(View.GONE);

        } else if (str_reult_status.equalsIgnoreCase("1")) {

            holder.img_status_result_cross.setVisibility(View.GONE);
            holder.img_status_result_right.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_question_number_result, txt_your_answer_result, txt_correct_answer_result, txt_your_scored_result;
        ImageView img_status_result_cross, img_status_result_right;
        List<TestPanelHelper> testList;
        Context mctx;

        public ProductViewHolder(View itemView, Context mctx, List<TestPanelHelper> testList) {
            super(itemView);
            this.mctx = mctx;
            this.testList = testList;
            itemView.setOnClickListener(this);
            txt_question_number_result = itemView.findViewById(R.id.txt_question_number_result);
            txt_your_answer_result = itemView.findViewById(R.id.txt_your_answer_result);
            txt_correct_answer_result = itemView.findViewById(R.id.txt_correct_answer_result);
            txt_your_scored_result = itemView.findViewById(R.id.txt_your_scored_result);
            img_status_result_cross = itemView.findViewById(R.id.img_status_result_cross);
            img_status_result_right = itemView.findViewById(R.id.img_status_result_right);

        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();


        }
    }


}

