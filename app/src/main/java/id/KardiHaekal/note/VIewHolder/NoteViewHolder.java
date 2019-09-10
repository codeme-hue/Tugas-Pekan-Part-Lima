package id.KardiHaekal.note.VIewHolder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import id.KardiHaekal.note.R;

public class NoteViewHolder extends RecyclerView.ViewHolder {

  public View noteCard;
  View mView;

  TextView textTitle, textTime;

  public NoteViewHolder(View itemView) {
    super(itemView);

    mView = itemView;

    textTitle = mView.findViewById(R.id.note_title);
    textTime = mView.findViewById(R.id.note_time);
    noteCard = mView.findViewById(R.id.note_card);
  }

  public void setNoteTitle(String title) {
    textTitle.setText(title);
  }

  public void setNoteTime(String time) {
    textTime.setText(time);
  }
}
