package cn.ck.security.base.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.List;

/**
 * Adapter基类
 *
 * @author chengkun
 * @since 2019/1/3 21:57
 */
public abstract class BaseAdapter<Data> extends RecyclerView.Adapter {

    protected Context mContext;
    private int mItemLayoutId;
    private LayoutInflater mInflater;
    protected List<Data> mDataList;

    public BaseAdapter(Context context, List<Data> dataList, @LayoutRes int itemLayoutId) {
        mDataList = dataList;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mItemLayoutId, parent, false);
        return new BaseViewHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindView((BaseViewHolder) holder, mDataList.get(position));
    }

    protected abstract void bindView(BaseViewHolder viewHolder, Data data);


    /**
     * 暴露方法，绑定数据
     */
    public List<Data> getDataList() {
        return mDataList;
    }


    /**
     * 数据操作
     */
    public void setDataList(Collection<Data> list) {
        this.mDataList.clear();
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(Collection<Data> list) {
        int lastIndex = this.mDataList.size();
        if (this.mDataList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    /**
     * 添加数据
     *
     * @param data
     */
    public void addItem(Data data) {
        mDataList.add(data);
        notifyDataSetChanged();
    }


    /**
     * @param collection
     */
    public void addItems(Collection<Data> collection) {
        mDataList.addAll(collection);
        notifyDataSetChanged();
    }


    /**
     * 移除数据
     *
     * @param data 移除的数据
     */
    public void removeItem(Data data) {
        this.mDataList.remove(data);
        notifyDataSetChanged();
    }


    /**
     * 移除数据
     */
    public void removeItem(int position) {

        mDataList.remove(position);
        //该方法不会使position及其之后位置的itemView重新onBindViewHolder
        notifyItemRemoved(position);
        //所以需要从position到列表末尾进行数据刷新
        if (position != mDataList.size()) {
            notifyItemRangeChanged(position, mDataList.size() - position);
        }
    }


    /**
     * 清除全部数据
     */
    public void removeAllItem() {
        mDataList.clear();
        notifyDataSetChanged();
    }
}