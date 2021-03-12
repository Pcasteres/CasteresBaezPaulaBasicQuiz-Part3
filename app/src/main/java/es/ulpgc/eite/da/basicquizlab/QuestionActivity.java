package es.ulpgc.eite.da.basicquizlab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class QuestionActivity extends AppCompatActivity {

  public static final String TAG = "Quiz.QuestionActivity";

  public static final String KEY_NEXT_BUTTON = "NEXT_BUTTON";
  public static final String KEY_INDEX_VALUE = "INDEX_VALUE ";
  public static final String KEY_USER_BUTTON = "USER_BUTTON";

  public static final int CHEAT_REQUEST = 1;

  private Button falseButton, trueButton,cheatButton, nextButton;
  private TextView questionText, replyText;

  private String[] questionArray;
  private int questionIndex=0;
  private int[] replyArray;
  private boolean nextButtonEnabled;
  private boolean trueButtonClicked;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_question);

    getSupportActionBar().setTitle(R.string.question_title);

    if(savedInstanceState != null) { // recreando activity

      // fijando estado
      nextButtonEnabled=savedInstanceState.getBoolean(KEY_NEXT_BUTTON);
      trueButtonClicked=savedInstanceState.getBoolean(KEY_USER_BUTTON);
      questionIndex=savedInstanceState.getInt(KEY_INDEX_VALUE);

      // aplicar estado
    }

    // 1
    initLayoutData();
    // 2
    linkLayoutComponents();
    // 3
    updateLayoutContent();
    enableLayoutButtons();
  }



  private void updateLayoutContent() {
    questionText.setText(questionArray[questionIndex]);

    /*
    if(!nextButtonEnabled) {
      replyText.setText(R.string.empty_text);
    }
    */

    if(trueButtonClicked) {

      if(replyArray[questionIndex] == 1) {
        replyText.setText(R.string.correct_text);
      } else {
        replyText.setText(R.string.incorrect_text);
      }

    } else { // haz hecho clic en false o no haz hecho clic

      if(!nextButtonEnabled) { //  no haz hecho clic

        replyText.setText(R.string.empty_text);

      } else { // haz hecho clic en false

        if(replyArray[questionIndex] == 0) {
          replyText.setText(R.string.correct_text);
        } else {
          replyText.setText(R.string.incorrect_text);
        }
      }
    }

    nextButton.setEnabled(nextButtonEnabled);
    cheatButton.setEnabled(!nextButtonEnabled);
    falseButton.setEnabled(!nextButtonEnabled);
    trueButton.setEnabled(!nextButtonEnabled);
  }

  private void enableLayoutButtons() {

    trueButton.setOnClickListener(v -> onTrueButtonClicked());
    falseButton.setOnClickListener(v -> onFalseButtonClicked());
    nextButton.setOnClickListener(v -> onNextButtonClicked());
    cheatButton.setOnClickListener(v -> onCheatButtonClicked());
  }

  private void initLayoutData() {
    questionArray=getResources().getStringArray(R.array.question_array);
    replyArray=getResources().getIntArray(R.array.reply_array);
  }


  private void linkLayoutComponents() {
    falseButton = findViewById(R.id.falseButton);
    trueButton = findViewById(R.id.trueButton);
    cheatButton = findViewById(R.id.cheatButton);
    nextButton = findViewById(R.id.nextButton);

    questionText = findViewById(R.id.questionText);
    replyText = findViewById(R.id.replyText);
  }


  private void onTrueButtonClicked() {

    trueButtonClicked = true;

    /*
    if(nextButtonEnabled) {
      return;
    }
    */

    if(replyArray[questionIndex] == 1) {
      replyText.setText(R.string.correct_text);
    } else {
      replyText.setText(R.string.incorrect_text);
    }

    nextButtonEnabled = true;
    updateLayoutContent();
  }

  private void onFalseButtonClicked() {

    trueButtonClicked=false;

    /*
    if(nextButtonEnabled) {
      return;
    }
    */

    if(replyArray[questionIndex] == 0) {
      replyText.setText(R.string.correct_text);
    } else {
      replyText.setText(R.string.incorrect_text);
    }

    nextButtonEnabled = true;
    updateLayoutContent();

  }

  private void onCheatButtonClicked() {

    /*
    if(nextButtonEnabled) {
      return;
    }
    */

    Intent intent = new Intent(this, CheatActivity.class);
    intent.putExtra(CheatActivity.EXTRA_ANSWER, replyArray[questionIndex]);
    startActivityForResult(intent, CHEAT_REQUEST);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    Log.d(TAG, "onActivityResult()");

    if (requestCode == CHEAT_REQUEST) {
      if (resultCode == RESULT_OK) {

        boolean answerCheated =
            data.getBooleanExtra(CheatActivity.EXTRA_CHEATED, false);

        Log.d(TAG, "answerCheated: " + answerCheated);

        if(answerCheated) {
          nextButtonEnabled = true;
          onNextButtonClicked();
        }
      }
    }
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);

    outState.putBoolean(KEY_NEXT_BUTTON, nextButtonEnabled);
    outState.putInt(KEY_INDEX_VALUE, questionIndex);
    outState.putBoolean(KEY_USER_BUTTON, trueButtonClicked);

  }

  private void onNextButtonClicked() {
    Log.d(TAG, "onNextButtonClicked()");

    /*
    if(!nextButtonEnabled) {
      return;
    }
    */

    nextButtonEnabled = false;
    questionIndex++;

    // si queremos que el quiz acabe al llegar
    // a la ultima pregunta debemos comentar esta linea
    checkIndexData();

    if(questionIndex < questionArray.length) {
      trueButtonClicked = false;
      updateLayoutContent();
    }

  }

  private void checkIndexData() {

    // hacemos que si llegamos al final del quiz
    // volvamos a empezarlo nuevamente
    if(questionIndex == questionArray.length) {
      questionIndex=0;
    }

  }

}
