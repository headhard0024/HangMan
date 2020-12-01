package no3ratii.mohammad.dev.app.hangman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class ActivityFinish extends Activity {

    private String winnerCouner;
    private String lossCouner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        TextView txtShow = (TextView) findViewById(R.id.txtShow);
        Button btnExit = (Button) findViewById(R.id.btnExit);
        Button btnNew = (Button) findViewById(R.id.btnNew);
        //        txtWinnerCounter = (TextView) findViewById(R.id.txtWinnerCounter);
        //        txtLossCounter = (TextView) findViewById(R.id.txtLossCounter);

        btnExit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnNew.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityFinish.this, ActivityMain.class);
                ActivityFinish.this.startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        String finish = intent.getStringExtra("RESULT");
        String word = intent.getStringExtra("WORD");
        winnerCouner = intent.getStringExtra("WINNER_COUNTER");
        lossCouner = intent.getStringExtra("LOSS_COUNTER");
        Log.i("LOG", "winner|" + G.winnerCouner);
        Log.i("LOG", "loss|" + G.lossConter);
        if (finish.equals("WIN")) {
            txtShow.setText("تو یه قهرمانی");

        } else if (finish.equals("LOSS")) {
            txtShow.setText(" کلمه " + word + " بود ");
        }
    }
}
