package com.micahThor.taskmaster;


import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.micahThor.taskmaster.TaskFragment.OnListFragmentInteractionListener;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Task} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTaskRecyclerViewAdapter extends RecyclerView.Adapter<MyTaskRecyclerViewAdapter.ViewHolder> {

    private final List<Task> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final taskOnClickListener clickListener;

    public MyTaskRecyclerViewAdapter(List<Task> items, OnListFragmentInteractionListener listener, taskOnClickListener clickListener) {
        mValues = items;
        mListener = listener;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mDescriptionView.setText(mValues.get(position).getBody());
        holder.mStateView.setText(mValues.get(position).getState());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                String title = holder.mTitleView.getText().toString();
                String description = holder.mDescriptionView.getText().toString();
                String state = holder.mStateView.getText().toString();
                Task t = new Task(title, description, state);

                clickListener.onClick(t);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mDescriptionView;
        public final TextView mStateView;
        public Task mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.taskTitle);
            mDescriptionView = (TextView) view.findViewById(R.id.taskDescription);
            mStateView = (TextView) view.findViewById(R.id.taskState);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }

    public static interface taskOnClickListener {
        public void onClick(Task t);
    }

}
