package com.exam.softconect.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exam.softconect.Activity.ViewResultActivity;
import com.exam.softconect.Helper.TestPanelHelper;
import com.exam.softconect.R;

import java.util.List;

public class AllResultAdapter extends RecyclerView.Adapter<AllResultAdapter.ProductViewHolder> {

    Context mctx;
    private List<TestPanelHelper> allResultList;
    String[] testID;
    String str_testID;

    public AllResultAdapter(Context mctx, List<TestPanelHelper> allResultList) {
        this.mctx = mctx;
        this.allResultList = allResultList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mctx);
        View view = inflater.inflate(R.layout.all_result_single_item, null);
        return new ProductViewHolder(view, mctx, allResultList);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        TestPanelHelper testList_1 = allResultList.get(position);

        holder.txt_all_result_name_single.setText(testList_1.getTest_name_result());
        holder.txt_all_result_total_score.setText(testList_1.getTotal_score());
        holder.txt_all_result_total_test_marks.setText(testList_1.getTotal_test_marks());

        testID = new String[allResultList.size()];
        for (int i = 0; i < allResultList.size(); i++) {

            TestPanelHelper list = allResultList.get(i);
            testID[i] = String.valueOf(list.getTest_Id_result());
        }
    }

    @Override
    public int getItemCount() {
        return allResultList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_all_result_name_single, txt_all_result_total_score, txt_all_result_total_test_marks;
        List<TestPanelHelper> testList;
        Context mctx;

        public ProductViewHolder(View itemView, Context mctx, List<TestPanelHelper> testList) {
            super(itemView);
            this.mctx = mctx;
            this.testList = testList;
            itemView.setOnClickListener(this);
            txt_all_result_name_single = itemView.findViewById(R.id.txt_all_result_name_single);
            txt_all_result_total_score = itemView.findViewById(R.id.txt_all_result_total_score);
            txt_all_result_total_test_marks = itemView.findViewById(R.id.txt_all_result_total_test_marks);

        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            for (int j = 0; j < testID.length; j++) {
                if (j == position) {
                    str_testID = testID[j];
                }
            }

            Intent intent = new Intent(mctx, ViewResultActivity.class);
            intent.putExtra("testId", str_testID);
            mctx.startActivity(intent);
        }
    }
}

