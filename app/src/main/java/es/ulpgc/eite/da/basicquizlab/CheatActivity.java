package es.ulpgc.eite.da.basicquizlab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CheatActivity extends AppCompatActivity {

  public static final String TAG = "Quiz.CheatActivity";

  public final static String EXTRA_ANSWER = "EXTRA_ANSWER";
  public final static String EXTRA_CHEATED = "EXTRA_CHEATED";

  public static final String KEY_USER_BUTTON = "USER_BUTTON";



  private Button noButton, yesButton;
  private TextView answerText;

  private int currentAnswer;
  private boolean answerCheated;
  private boolean yesButtonClicked;
  private boolean yesButtonEnabled;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cheat);

    getSupportActionBar().setTitle(R.string.cheat_title);

    if(savedInstanceState != null) { // recreando activity

      // fijando estado

      answerCheated = savedInstanceState.getBoolean(EXTRA_CHEATED);
      yesButtonClicked=savedInstanceState.getBoolean(KEY_USER_BUTTON);

      // aplicar estado en 3
    }

    //1
    initLayoutData(); //inicializa la aplicación
    //2
    linkLayoutComponents();

    enableLayoutButtons();
  }

  private void initLayoutData() {
    currentAnswer = getIntent().getExtras().getInt(EXTRA_ANSWER);

  }

  private void linkLayoutComponents() {
    noButton = findViewById(R.id.noButton);
    yesButton = findViewById(R.id.yesButton);

    answerText = findViewById(R.id.answerText);
  }

  private void enableLayoutButtons() { //permite el usar y pulsar

    //noButton.setOnClickListener(v -> onNoButtonClicked());
    //yesButton.setOnClickListener(v -> onYesButtonClicked());

    if (yesButtonClicked) {
      yesButton.setEnabled(false); //Tras pulsar 'Yes' deshabilito los botones para no poder pulsarlos
      noButton.setEnabled(false);
      if (currentAnswer == 0) {
        answerText.setText(R.string.false_text);
      } else {
        answerText.setText(R.string.true_text);
      }

    } else { // haz hecho click en no o no haz hecho clic

      noButton.setOnClickListener(v -> onNoButtonClicked());
      yesButton.setOnClickListener(v -> onYesButtonClicked());
    }
  }

  private void returnCheatedStatus() {
    Log.d(TAG, "returnCheatedStatus()");
    Log.d(TAG, "answerCheated: " + answerCheated);

    Intent intent = new Intent();
    intent.putExtra(EXTRA_CHEATED, answerCheated);
    setResult(RESULT_OK, intent);

    finish();
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);

    outState.putBoolean("EXTRA_CHEATED",answerCheated);
    //outState.putInt("EXTRA_ANSWER", currentAnswer);
    outState.putBoolean("USER_BUTTON",yesButtonClicked);

  }

  @Override
  public void onBackPressed() {
    Log.d(TAG, "onBackPressed()");

    returnCheatedStatus();
  }


  private void onYesButtonClicked() {
    yesButton.setEnabled(false);
    noButton.setEnabled(false);
    answerCheated = true;

    yesButtonClicked =true;

    if (currentAnswer == 0) {
      answerText.setText(R.string.false_text);
    } else {
      answerText.setText(R.string.true_text);

    }
  }


  private void onNoButtonClicked() {
    yesButton.setEnabled(false);
    noButton.setEnabled(false);

    yesButtonClicked = false;

    returnCheatedStatus();
  }

}
