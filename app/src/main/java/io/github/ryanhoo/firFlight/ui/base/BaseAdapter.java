package io.github.ryanhoo.firFlight.ui.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/19/16
 * Time: 8:47 PM
 * Desc: BaseAdapter
 */
public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected Context mContext;
    protected List<T> mData;
    private OnItemClickListener<T> mOnItemClickListener;

    public BaseAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data;
    }

    protected void onItemClick(T item, int position) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(item, position);
        }
    }

    protected LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mContext);
    }

    protected OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    public T getItem(int position) {
        if (mData == null || position >= mData.size()) return null;
        return mData.get(position);
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener<T> {

        void onItemClick(T item, int position);
    }

}
