package com.lenovo.tvflowrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * @author songwenju
 */
public class CommonRecyclerViewHolder extends RecyclerView.ViewHolder {
    private CommonViewHolder mHolder;
    
    private CommonRecyclerViewHolder(CommonViewHolder mHolder) {
        super(mHolder.getConvertView());
        this.mHolder = mHolder;
    }
    
    public static CommonRecyclerViewHolder get(CommonViewHolder holder) {
        return new CommonRecyclerViewHolder(holder);
    }
    
    public static CommonRecyclerViewHolder get(Context context, ViewGroup parent, int layoutId){
        return new CommonRecyclerViewHolder(CommonViewHolder.get(context, parent, layoutId));
    }
    
    public CommonViewHolder getHolder(){
        return mHolder;
    }
}
