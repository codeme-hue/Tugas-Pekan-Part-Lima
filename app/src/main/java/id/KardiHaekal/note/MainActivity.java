package id.KardiHaekal.note;

import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import id.KardiHaekal.note.Account.StartActivity;
import id.KardiHaekal.note.CreateNote.NewNoteActivity;
import id.KardiHaekal.note.GetTimeAgo.GetTimeAgo;
import id.KardiHaekal.note.ItemDecoration.GridSpacingItemDecoration;
import id.KardiHaekal.note.Model.NoteModel;
import id.KardiHaekal.note.VIewHolder.NoteViewHolder;


public class MainActivity extends AppCompatActivity {

  private FirebaseAuth fAuth;
  private RecyclerView mNotesList;
  private GridLayoutManager gridLayoutManager;

  private DatabaseReference fNotesDatabase;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mNotesList = (RecyclerView) findViewById(R.id.main_notes_list);

    gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

    mNotesList.setHasFixedSize(true);
    mNotesList.setLayoutManager(gridLayoutManager);
    mNotesList.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));

    fAuth = FirebaseAuth.getInstance();
    if (fAuth.getCurrentUser() != null) {
      fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes")
          .child(fAuth.getCurrentUser().getUid());
    }

    updateUI();

    loadData();
  }

  @Override
  public void onStart() {
    super.onStart();

  }

  private void loadData() {
    Query query = fNotesDatabase.orderByValue();
    FirebaseRecyclerAdapter<NoteModel, NoteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NoteModel, NoteViewHolder>(
        NoteModel.class,
        R.layout.single_note_layout,
        NoteViewHolder.class,
        query

    ) {
      @Override
      protected void populateViewHolder(final NoteViewHolder viewHolder, NoteModel model,
          int position) {
        final String noteId = getRef(position).getKey();

        fNotesDatabase.child(noteId).addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChild("title") && dataSnapshot.hasChild("timestamp")) {
              String title = dataSnapshot.child("title").getValue().toString();
              String timestamp = dataSnapshot.child("timestamp").getValue().toString();

              viewHolder.setNoteTitle(title);
              viewHolder.setNoteTime(timestamp);

              GetTimeAgo getTimeAgo = new GetTimeAgo();
              viewHolder.setNoteTime(
                  getTimeAgo.getTimeAgo(Long.parseLong(timestamp), getApplicationContext()));

              viewHolder.noteCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                  intent.putExtra("noteId", noteId);
                  startActivity(intent);
                }
              });
            }

          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
        });

      }
    };
    mNotesList.setAdapter(firebaseRecyclerAdapter);
  }

  private void updateUI() {

    if (fAuth.getCurrentUser() != null) {
      Log.i("MainActivity", "fAuth != null");
    } else {
      Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
      startActivity(startIntent);
      finish();
      Log.i("MainActivity", "fAuth == null");
    }

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);

    getMenuInflater().inflate(R.menu.main_menu, menu);

    return true;
  }


  public void new_notes(MenuItem item) {
    Intent newIntent = new Intent(MainActivity.this, NewNoteActivity.class);
    startActivity(newIntent);
  }

  /**
   * konversi dp ke pixel
   */
  private int dpToPx(int dp) {
    Resources r = getResources();
    return Math
        .round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
  }

}
