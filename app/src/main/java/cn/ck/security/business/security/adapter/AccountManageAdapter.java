package cn.ck.security.business.security.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.List;

import cn.ck.security.R;

/**
 * 修改密码界面
 * 其实也不是非要多type，可以根据位置动态设置holder里的背景颜色,就是再练练手
 *
 * @author chengkun
 * @since 2019/3/18 19:44
 */
public class AccountManageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String FILED_ID = "id";

    /**
     * 定义各种Type
     */
    private static final int TYPE_ONE = 0;
    private static final int TYPE_TWO = 1;

    private Context mContext;
    private List<JsonObject> userList;
    private OnItemClickListener mOnItemClickListener;

    public AccountManageAdapter(Context context, List<JsonObject> userList) {
        mContext = context;
        this.userList = userList;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? TYPE_ONE : TYPE_TWO;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        switch (viewType) {
            case TYPE_ONE:
                view = inflater.inflate(R.layout.item_account1, parent, false);
                return new TypeOneHolder(view);
            case TYPE_TWO:
                view = inflater.inflate(R.layout.item_account2, parent, false);
                return new TypeTwoHolder(view);
            default:
                throw new IllegalStateException("ViewType 异常");
                //throw new IllegalStateException("ViewType 异常");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TypeOneHolder) {
            ((TypeOneHolder) holder).txtAccount.setText(userList.get(position).get(FILED_ID).getAsString());
            ((TypeOneHolder) holder).btnChangePwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position);
                    }
                }
            });
        } else if (holder instanceof TypeTwoHolder) {
            ((TypeTwoHolder) holder).txtAccount.setText(userList.get(position).get(FILED_ID).getAsString());
            ((TypeTwoHolder) holder).btnChangePwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position);
                    }
                }
            });
        } else {
            throw new IllegalStateException("viewHolder 异常");
        }
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    public static class TypeOneHolder extends RecyclerView.ViewHolder {
        TextView txtAccount;
        TextView btnChangePwd;

        public TypeOneHolder(View itemView) {
            super(itemView);
            txtAccount = (TextView) itemView.findViewById(R.id.txt_account);
            btnChangePwd = (TextView) itemView.findViewById(R.id.btn_change_pwd);
        }
    }

    public static class TypeTwoHolder extends RecyclerView.ViewHolder {
        TextView txtAccount;
        TextView btnChangePwd;

        public TypeTwoHolder(View itemView) {
            super(itemView);
            txtAccount = (TextView) itemView.findViewById(R.id.txt_account);
            btnChangePwd = (TextView) itemView.findViewById(R.id.btn_change_pwd);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void notifyDataChanged(List<JsonObject> userList) {
        if (userList != null) {
            this.userList = userList;
            notifyDataSetChanged();
        }
    }
}
