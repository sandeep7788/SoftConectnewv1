package com.exam.softconect.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exam.softconect.Activity.ResumeTestActivity;
import com.exam.softconect.Activity.ViewResultActivity;
import com.exam.softconect.Helper.CommonUtils;
import com.exam.softconect.Helper.TestPanelHelper;
import com.exam.softconect.R;

import java.util.List;

public class TestPanelAdapter extends RecyclerView.Adapter<TestPanelAdapter.ProductViewHolder> {

    Context mctx;
    private List<TestPanelHelper> testList;
    String[] testID, btnstatus, testName, total_time, package_status;
    String str_testID, str_btnstatus, str_test_name, str_total_time, str_package_status;
    Dialog dialog_instruction;
    String str_status;
    Activity activity;

    public TestPanelAdapter(Context mctx, List<TestPanelHelper> testList, Activity activity) {
        this.mctx = mctx;
        this.testList = testList;
        this.activity = activity;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mctx);
        View view = inflater.inflate(R.layout.test_list_single_item, null);
        return new ProductViewHolder(view, mctx, testList);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        TestPanelHelper testList_1 = testList.get(position);

        holder.txt_test_panel_test_name_single.setText(testList_1.getTest_name());
        holder.txt_test_panel_total_question_single.setText(testList_1.getTotal_question());
        holder.txt_test_panel_total_marks_single.setText(testList_1.getTotal_marks());

        //set buttons
        str_btnstatus = testList_1.getBtn_status();
        if (str_btnstatus.equalsIgnoreCase("0")) {
            holder.lay_test_start_single.setVisibility(View.VISIBLE);
        } else if (str_btnstatus.equalsIgnoreCase("1")) {
            holder.lay_test_resumer_single.setVisibility(View.VISIBLE);
        } else if (str_btnstatus.equalsIgnoreCase("2")) {
            holder.lay_result_view.setVisibility(View.VISIBLE);
        }

        //get single value after click
        testID = new String[testList.size()];
        testName = new String[testList.size()];
        total_time = new String[testList.size()];
        btnstatus = new String[testList.size()];
        package_status = new String[testList.size()];
        for (int i = 0; i < testList.size(); i++) {

            TestPanelHelper list = testList.get(i);
            testID[i] = list.getTestID();
            testName[i] = list.getTest_name();
            total_time[i] = list.getTotal_time();
            btnstatus[i] = list.getBtn_status();
            package_status[i] = list.getPackage_status();
        }
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_test_panel_test_name_single, txt_test_panel_total_question_single, txt_test_panel_total_marks_single;
        LinearLayout lay_test_start_single, lay_test_resumer_single, lay_result_view;
        List<TestPanelHelper> testList;
        Context mctx;

        public ProductViewHolder(View itemView, Context mctx, List<TestPanelHelper> testList) {
            super(itemView);
            this.mctx = mctx;
            this.testList = testList;
            itemView.setOnClickListener(this);
            txt_test_panel_test_name_single = itemView.findViewById(R.id.txt_test_panel_test_name_single);
            txt_test_panel_total_question_single = itemView.findViewById(R.id.txt_test_panel_total_question_single);
            txt_test_panel_total_marks_single = itemView.findViewById(R.id.txt_test_panel_total_marks_single);
            lay_test_start_single = itemView.findViewById(R.id.lay_test_start_single);
            lay_test_resumer_single = itemView.findViewById(R.id.lay_test_resumer_single);
            lay_result_view = itemView.findViewById(R.id.lay_result_view);

        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            for (int j = 0; j < testID.length; j++) {
                if (j == position) {
                    str_testID = testID[j];
                    str_test_name = testName[j];
                    str_total_time = total_time[j];
                    str_btnstatus = btnstatus[j];
                    str_package_status = package_status[j];
                }
            }
            Log.e("@@str_pacakege_status",str_package_status.toString()+" ");
            Log.e("@@str_btnstatus",str_btnstatus.toString()+" ");
            if (str_package_status.equalsIgnoreCase("1")) {


                if (str_btnstatus.equalsIgnoreCase("0")) {

                    //call method
                    CommonUtils.getInstruction(mctx, str_testID, str_total_time);

                } else if (str_btnstatus.equalsIgnoreCase("1")) {

                    Intent intent = new Intent(mctx, ResumeTestActivity.class);
                    intent.putExtra("title", str_test_name);
                    intent.putExtra("testId", str_testID);
                    mctx.startActivity(intent);

                } else if (str_btnstatus.equalsIgnoreCase("2")) {
                    Intent intent = new Intent(mctx, ViewResultActivity.class);
                    intent.putExtra("testId", str_testID);
                    mctx.startActivity(intent);
                }
            } else {
                CommonUtils.AwesomeWarningPackageDialog(activity, "Sorry ! You are not authorized to access this test, Please purchase package.");
            }

        }
    }


}

