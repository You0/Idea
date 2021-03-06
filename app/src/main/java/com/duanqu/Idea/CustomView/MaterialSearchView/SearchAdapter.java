/*
 * Copyright (c) 2016.  TedaLIEz <aliezted@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duanqu.Idea.CustomView.MaterialSearchView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.duanqu.Idea.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by JianGuo on 11/8/16.
 * Adapter holding search suggestions.
 */
// FIXME: 11/9/16 drop frames when initialize data.
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {
    private Context mContext;
    private List<SearchHistory> mSuggestions;
    private List<SearchHistory> mHistory;
    private HistoryDbHelper dbHelper;
    private OnItemClickListener mOnItemClickListener;
    private OnItemClearListener mOnItemClearListener;

    SearchAdapter(Context context) {
        mContext = context;
        dbHelper = HistoryDbHelper.getInstance(context);
        initData();
    }

    private void initData() {
        mHistory = dbHelper.getAllHistories();
        mSuggestions = new ArrayList<>(mHistory);
    }

    public SearchAdapter(Context context, List<SearchHistory> suggestions) {
        mContext = context;
        mSuggestions = suggestions;
        mHistory = suggestions;
    }


    void addItem(SearchHistory item) {
        dbHelper.addItem(item);
        if (mSuggestions.contains(item)) {
            mHistory.get(mHistory.indexOf(item)).incrementAndGet();
            mSuggestions.get(mSuggestions.indexOf(item)).incrementAndGet();
            return;
        }
        mHistory.add(item);
        mSuggestions.add(item);
        notifyItemInserted(mSuggestions.size());
    }


    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchAdapter.ViewHolder holder, int position) {
        holder.mSuggestion.setText(mSuggestions.get(position).keywords);
        holder.mSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, mSuggestions.get(holder.getAdapterPosition()).keywords);
                }
            }
        });
        holder.mClearImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClearListener != null) {
                    mOnItemClearListener.onClear(mSuggestions.get(holder.getAdapterPosition()).keywords);
                }
                removeItem(holder.getAdapterPosition());
            }
        });
    }

    private void removeItem(int position) {
        SearchHistory history = mSuggestions.get(position);
        dbHelper.delete(history);
        mSuggestions.remove(history);
        mHistory.remove(history);
        notifyItemRemoved(position);
    }



    @Override
    public int getItemCount() {
        return mSuggestions.size();
    }


    @Override
    public Filter getFilter() {
        return new ItemFilter();
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (!TextUtils.isEmpty(constraint) && mSuggestions != null && mHistory.size() != 0) {
                FilterResults results = new FilterResults();
                String filterString = constraint.toString().trim().toLowerCase(Locale.getDefault());
                List<SearchHistory> rst = new ArrayList<>();

                for (SearchHistory item : mHistory) {
                    if (item.keywords.toLowerCase().contains(filterString)) {
                        rst.add(item);
                    }
                }
                results.values = rst;
                results.count = rst.size();
                return results;
            }
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results == null) {
                clearAndAddAll(mHistory);
                return;
            }
            // Only List<SearchHistory> will be gotten here
            @SuppressWarnings("unchecked")
            List<SearchHistory> rst = (List<SearchHistory>) results.values;
            clearAndAddAll(rst);
        }
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    void setOnItemClearListener(OnItemClearListener onItemClearListener) {
        mOnItemClearListener = onItemClearListener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, String text);
    }

    public interface OnItemClearListener {
        void onClear(String text);
    }

    private void clearAndAddAll(List<SearchHistory> data) {
        mSuggestions = new ArrayList<>(data);
        notifyDataSetChanged();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mSuggestion;
        ImageView mClearImageView;
        ViewHolder(View itemView) {
            super(itemView);
            mSuggestion = (TextView) itemView.findViewById(R.id.textView_suggestion);
            mClearImageView = (ImageView) itemView.findViewById(R.id.imageView_clear);
        }

    }

    void clearSuggestions() {
        dbHelper.clearDatabase();
        mSuggestions.clear();
        mHistory.clear();
        notifyDataSetChanged();
    }
}
