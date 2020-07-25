package com.exam.softconect.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exam.softconect.Activity.QuizActivity;
import com.exam.softconect.Helper.TestPanelHelper;
import com.exam.softconect.R;

import java.util.List;

public class ReviewQuizAdapter extends RecyclerView.Adapter<ReviewQuizAdapter.ProductViewHolder> {

    Context mctx;
    private List<TestPanelHelper> reviewList;
    String[] ques_id, student_ans,ques_order;
    String str_ques_id, str_button_status, str_student_ans,str_ques_order;
    Dialog dialog_review;
    QuizActivity quizActivity;

    public ReviewQuizAdapter(Context mctx, List<TestPanelHelper> reviewList, Dialog dialog_review, QuizActivity quizActivity) {
        this.mctx = mctx;
        this.reviewList = reviewList;
        this.dialog_review = dialog_review;
        this.quizActivity = quizActivity;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mctx);
        View view = inflater.inflate(R.layout.all_review_single_item, null);
        return new ProductViewHolder(view, mctx, reviewList);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {


        TestPanelHelper reviewList_1 = reviewList.get(position);

        holder.txt_review.setText(reviewList_1.getReview_list_count());


        //get button status and set color
        str_button_status = reviewList_1.getReview_list_btn_status();

        if (str_button_status.equalsIgnoreCase("0")) {

            GradientDrawable relativelayout1 = (GradientDrawable) holder.relativelayout1.getBackground().getCurrent();
            relativelayout1.setColor(Color.parseColor("#ffffff"));

        } else if (str_button_status.equalsIgnoreCase("1")) {

            GradientDrawable relativelayout1 = (GradientDrawable) holder.relativelayout1.getBackground().getCurrent();
            relativelayout1.setColor(Color.parseColor("#EF473A"));

        } else if (str_button_status.equalsIgnoreCase("2")) {

            GradientDrawable relativelayout1 = (GradientDrawable) holder.relativelayout1.getBackground().getCurrent();
            relativelayout1.setColor(Color.parseColor("#4AA641"));

        }

        ques_id = new String[reviewList.size()];
        student_ans = new String[reviewList.size()];
        ques_order = new String[reviewList.size()];
        for (int i = 0; i < reviewList.size(); i++) {

            TestPanelHelper list = reviewList.get(i);
            ques_id[i] = String.valueOf(list.getReview_list_ques_id());
            student_ans[i] = String.valueOf(list.getReview_list_student_ans());
            ques_order[i] = String.valueOf(list.getReview_list_ques_ordert());
        }
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_review;
        List<TestPanelHelper> reviewList_1;
        Context mctx;
        RelativeLayout relativelayout1;

        public ProductViewHolder(View itemView, Context mctx, List<TestPanelHelper> reviewList_1) {
            super(itemView);
            this.mctx = mctx;
            this.reviewList_1 = reviewList_1;
            itemView.setOnClickListener(this);
            txt_review = itemView.findViewById(R.id.txt_review);
            relativelayout1 = itemView.findViewById(R.id.relativelayout1);

        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            for (int j = 0; j < ques_id.length; j++) {
                if (j == position) {
                    str_ques_id = ques_id[j];
                    str_student_ans = student_ans[j];
                    str_ques_order = ques_order[j];
                }
            }

            quizActivity.setReview_Questions( str_student_ans, Integer.parseInt(str_ques_order));


            dialog_review.dismiss();


        }
    }
}

