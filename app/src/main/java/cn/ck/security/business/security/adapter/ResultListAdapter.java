package cn.ck.security.business.security.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.ck.security.R;
import cn.ck.security.business.security.model.Car;

/**
 * @author chengkun
 * @since 2019/1/16 00:06
 */
public class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.ViewHolder> {

    private List<Car> mCars;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private OnCallListener mOnCallListener;

    public ResultListAdapter(List<Car> cars, Context context) {
        mCars = cars;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_car_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.carNumTxt.setText(mCars.get(position).getCarNumber());
        holder.callImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnCallListener != null) {
                    mOnCallListener.onCallClick(position);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mCars == null ? 0 : mCars.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView callImage;
        TextView carNumTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            callImage = itemView.findViewById(R.id.image_call);
            carNumTxt = itemView.findViewById(R.id.txt_car_num);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnCallListener {
        void onCallClick(int position);
    }

    public void setOnCallListener(OnCallListener listener) {
        this.mOnCallListener = listener;
    }
}
