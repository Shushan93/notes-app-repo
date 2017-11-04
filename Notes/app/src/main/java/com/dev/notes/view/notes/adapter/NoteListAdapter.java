package com.dev.notes.view.notes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.notes.R;
import com.dev.notes.model.NoteModel;
import com.dev.notes.utils.Utils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> implements RealmChangeListener {

    private RealmList<NoteModel> notes;
    private OnNoteItemClickListener onNoteItemClickListener;

    @Override
    public void onChange(Object o) {
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_subject_txt)
        TextView titleTxt;
        @BindView(R.id.item_context_txt)
        TextView contextTxt;
        @BindView(R.id.item_date_txt)
        TextView dateTxt;
        @BindView(R.id.item_notif_img)
        ImageView notificationImg;
        @BindView(R.id.item_color_view)
        View noteColorView;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public void setNotes(RealmList<NoteModel> notesList) {
        notes = notesList;
        notes.addChangeListener(this);
        notifyDataSetChanged();
    }

    public void setOnNoteItemClickListener(OnNoteItemClickListener listener) {
        onNoteItemClickListener = listener;
    }

    @Override
    public NoteListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NoteModel noteModel = notes.get(position);
        if (noteModel != null) {
            holder.titleTxt.setText(noteModel.getTitle());
            holder.contextTxt.setText(noteModel.getNoteContext());
            Calendar date = noteModel.getCalendar();
            if (date != null && noteModel.isDateSet()) {
                String dateStr = Utils.getDateInFormat(date);
                holder.dateTxt.setText(dateStr);
                holder.dateTxt.setVisibility(View.VISIBLE);
            } else {
                holder.dateTxt.setVisibility(View.GONE);
            }
            if (noteModel.isHasNotification()) {
                holder.notificationImg.setImageResource(R.drawable.notif_on);
            } else {
                holder.notificationImg.setImageResource(R.drawable.notif_off);
            }
            holder.noteColorView.setBackgroundColor(noteModel.getColor());
            holder.itemView.setOnClickListener(view -> {
                if (onNoteItemClickListener != null) {
                    onNoteItemClickListener.onNoteClick(noteModel.getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public interface OnNoteItemClickListener {
        void onNoteClick(long id);
    }

}
