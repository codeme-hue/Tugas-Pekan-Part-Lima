package id.KardiHaekal.note.Account;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import id.KardiHaekal.note.MainActivity;
import id.KardiHaekal.note.R;
import id.KardiHaekal.note.UserSign.LoginActivity;
import id.KardiHaekal.note.UserSign.RegisterActivity;


public class StartActivity extends AppCompatActivity {

  private Button btnReg, btnLog;

  private FirebaseAuth fAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start);

    btnReg = (Button) findViewById(R.id.start_reg_btn);
    btnLog = (Button) findViewById(R.id.start_log_btn);

    fAuth = FirebaseAuth.getInstance();

    updateUI();

   /* btnLog.setOnClickListener(new View.OnClickListener() {
     // @Override
      //public void onClick(View view) {
        //login();
      //}
    //});

  //  btnReg.setOnClickListener(new View.OnClickListener() {
    //  @Override
      //public void onClick(View view) {
        //register();
      //}
    //});

  }

 // private void register(){
    //Intent regIntent = new Intent(StartActivity.this, RegisterActivity.class);
    //startActivity(regIntent);
  //}

 // private void login(){
  //  Intent logIntent = new Intent(StartActivity.this, LoginActivity.class);
   // startActivity(logIntent);
  */}

  private void updateUI(){
    if (fAuth.getCurrentUser() != null){
      Log.i("StartActivity", "fAuth != null");
      Intent startIntent = new Intent(StartActivity.this, MainActivity.class);
      startActivity(startIntent);
      finish();
    } else {

      Log.i("StartActivity", "fAuth == null");
    }
  }

  public void move_log(View view) {
    Intent logIntent = new Intent(StartActivity.this, LoginActivity.class);
    startActivity(logIntent);
  }

  public void move_reg(View view) {
    Intent regIntent = new Intent(StartActivity.this, RegisterActivity.class);
    startActivity(regIntent);
  }
}