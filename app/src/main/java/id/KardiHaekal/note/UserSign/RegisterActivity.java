package id.KardiHaekal.note.UserSign;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import id.KardiHaekal.note.MainActivity;
import id.KardiHaekal.note.R;


public class RegisterActivity extends AppCompatActivity {

  private Button btnReg;
  private TextInputEditText inName;
  private TextInputEditText inEmail;
  private TextInputEditText inPass;

  private FirebaseAuth fAuth;
  private DatabaseReference fUsersDatabase;

  private ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    btnReg = (Button) findViewById(R.id.btn_reg);

    inName = (TextInputEditText) findViewById(R.id.input_reg_name);
    inEmail = (TextInputEditText) findViewById(R.id.input_reg_email);
    inPass = (TextInputEditText) findViewById(R.id.input_reg_password);

    fAuth = FirebaseAuth.getInstance();
    fUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

    btnReg.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        String uname = inName.getText().toString().trim();
        String uemail = inEmail.getText().toString().trim();
        String upass = inPass.getText().toString().trim();

        registerUser(uname, uemail, upass);

      }
    });
  }

  private void registerUser( final String name, String email, String password) {

    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Permintaan anda sedang di proses, mohon ditunggu...");

    progressDialog.show();

    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
        new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(Task<AuthResult> task) {

            if (task.isSuccessful()) {

              fUsersDatabase.child(fAuth.getCurrentUser().getUid())
                  .child("basic").child("name").setValue(name).addOnCompleteListener(
                  new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                      if (task.isSuccessful()) {
                        progressDialog.dismiss();

                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                        Toast.makeText(RegisterActivity.this, "User berhasil dibuat",
                            Toast.LENGTH_SHORT).show();
                      } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,
                            "ERROR" + task.getException().getMessage()
                            , Toast.LENGTH_SHORT).show();
                      }
                    }
                  });

            } else {
              progressDialog.dismiss();
              Toast.makeText(RegisterActivity.this, "ERROR" + task.getException().getMessage(),
                  Toast.LENGTH_SHORT).show();
            }

          }
        });

  }
}
