package com.exam.softconect.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exam.softconect.Activity.PackageActivity;
import com.exam.softconect.Helper.CommonUtils;
import com.exam.softconect.Helper.TestPanelHelper;
import com.exam.softconect.R;

import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ProductViewHolder> {

    Context mctx;
    private List<TestPanelHelper> testList;
    String[] packageID, amount, Package_name, discount_amount;
    String str_packageID, str_amount, str_Package_name, str_discount_amount;
    PackageActivity activity;

    public PackageAdapter(Context mctx, List<TestPanelHelper> testList, PackageActivity activity) {
        this.mctx = mctx;
        this.testList = testList;
        this.activity = activity;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mctx);
        View view = inflater.inflate(R.layout.package_single_item, null);
        return new ProductViewHolder(view, mctx, testList);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        TestPanelHelper testList_1 = testList.get(position);
        str_discount_amount = testList_1.getDiscount_amount();

        holder.txt_package_name_single.setText(testList_1.getPackage_name());
        holder.txt_package_time.setText(testList_1.getTotal_time());

        if (str_discount_amount.equalsIgnoreCase("0")) {

            holder.txt_package_amount_single.setText("INR " + testList_1.getTotal_amount() + " /-");
            holder.lay_discount_amount.setVisibility(View.GONE);
        } else {
            holder.lay_discount_amount.setVisibility(View.VISIBLE);

            holder.txt_package_amount_single.setText("INR " + testList_1.getTotal_amount() + " /-");
            holder.txt_package_amount_single.setPaintFlags(holder.txt_package_amount_single.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txt_package_discount_amount.setText("INR " + str_discount_amount + " /-");

        }

        packageID = new String[testList.size()];
        amount = new String[testList.size()];
        discount_amount = new String[testList.size()];
        Package_name = new String[testList.size()];
        for (int i = 0; i < testList.size(); i++) {

            TestPanelHelper list = testList.get(i);
            packageID[i] = String.valueOf(list.getPackageID());
            amount[i] = String.valueOf(list.getTotal_amount());
            discount_amount[i] = String.valueOf(list.getDiscount_amount());
            Package_name[i] = String.valueOf(list.getPackage_name());
        }
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_package_name_single, txt_package_amount_single, txt_package_time, txt_package_discount_amount;
        List<TestPanelHelper> testList;
        Context mctx;
        LinearLayout lay_discount_amount;

        public ProductViewHolder(View itemView, Context mctx, List<TestPanelHelper> testList) {
            super(itemView);
            this.mctx = mctx;
            this.testList = testList;
            itemView.setOnClickListener(this);
            txt_package_name_single = itemView.findViewById(R.id.txt_package_name_single);
            txt_package_amount_single = itemView.findViewById(R.id.txt_package_amount_single);
            txt_package_time = itemView.findViewById(R.id.txt_package_time);
            lay_discount_amount = itemView.findViewById(R.id.lay_discount_amount);
            txt_package_discount_amount = itemView.findViewById(R.id.txt_package_discount_amount);

        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            for (int j = 0; j < packageID.length; j++) {
                if (j == position) {
                    str_packageID = packageID[j];
                    str_Package_name = Package_name[j];

                    if (str_discount_amount.equalsIgnoreCase("0")) {
                        str_amount = amount[j];
                    } else {
                        str_amount = discount_amount[j];
                    }
                }
            }
            CommonUtils.packageID = str_packageID;
            str_Package_name = "purchase " + str_Package_name;
            activity.callInstamojoPay(CommonUtils.email_common, CommonUtils.mobile_common, str_amount, str_Package_name, CommonUtils.name_common);
        }
    }
}

