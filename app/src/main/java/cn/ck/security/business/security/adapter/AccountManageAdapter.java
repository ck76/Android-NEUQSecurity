package cn.ck.security.business.security.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author chengkun
 * @since 2019/3/18 19:44
 */
public class AccountManageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 定义各种Type
     */
    private static final int TYPE_ONE = 0;
    private static final int TYPE_TWO = 1;

    private Context mContext;
    private List<String> userList;

    public AccountManageAdapter(Context context, List<String> userList) {
        mContext = context;
        this.userList = userList;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {

        public ViewHolder1(View itemView) {
            super(itemView);
        }
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder {

        public ViewHolder2(View itemView) {
            super(itemView);
        }
    }
}
