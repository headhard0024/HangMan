package no3ratii.mohammad.dev.app.hangman;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class ActivityMain extends Activity {

    private int               resetGameConter;
    private char[]            letter;
    private char[]            dashes;
    private char[]            spaces;
    private View              view;
    private String            wordDashes   = "";
    private String            publicWord   = "";
    private int               fillCount    = 0;

    private int               winnerCouner = 0;
    private int               lossCouner   = 0;
    private ArrayList<String> wordList     = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final TextView txtShow = (TextView) findViewById(R.id.txtShow);
        final ImageView imgShow = (ImageView) findViewById(R.id.imgShow);
        /*
         * 
         */
        ModuleWebService module = new ModuleWebService();
        ModuleWebService.Listener listener = new ModuleWebService.Listener() {

            @Override
            public void onDataRecive(String value) {
                readData(value);
                for (String string: wordList) {
                    Log.i("LOG", "Data is:" + string);
                }
                final String word = selectWord();
                publicWord = word;
                wordDashes = getDashWord(word);
                txtShow.setText(wordDashes);
            }


            @Override
            public void onFail(int code) {
                Log.i("LOG", "Error :" + code);
            }
        };
        module.url("http://192.168.1.2/translate/translate1.php")
                .listener(listener)
                .cacheData(false)
                .cacheDiractory(null)
                .inputArguments(null)
                .cacheExpiryTime(60)
                .connectionTimeOut(2000)
                .socketTimeOut(5000)
                .read();

        Button btna = (Button) findViewById(R.id.btna);
        Button btnb = (Button) findViewById(R.id.btnb);
        Button btnc = (Button) findViewById(R.id.btnc);
        Button btnd = (Button) findViewById(R.id.btnd);
        Button btne = (Button) findViewById(R.id.btne);
        Button btnf = (Button) findViewById(R.id.btnf);
        Button btng = (Button) findViewById(R.id.btng);
        Button btnh = (Button) findViewById(R.id.btnh);
        Button btni = (Button) findViewById(R.id.btni);
        Button btnj = (Button) findViewById(R.id.btnj);
        Button btnk = (Button) findViewById(R.id.btnk);
        Button btnl = (Button) findViewById(R.id.btnl);
        Button btnm = (Button) findViewById(R.id.btnm);
        Button btnn = (Button) findViewById(R.id.btnn);
        Button btno = (Button) findViewById(R.id.btno);
        Button btnp = (Button) findViewById(R.id.btnp);
        Button btnq = (Button) findViewById(R.id.btnq);
        Button btnr = (Button) findViewById(R.id.btnr);
        Button btns = (Button) findViewById(R.id.btns);
        Button btnt = (Button) findViewById(R.id.btnt);
        Button btnu = (Button) findViewById(R.id.btnu);
        Button btnv = (Button) findViewById(R.id.btnv);
        Button btnw = (Button) findViewById(R.id.btnw);
        Button btnx = (Button) findViewById(R.id.btnx);
        Button btny = (Button) findViewById(R.id.btny);
        Button btnz = (Button) findViewById(R.id.btnz);

        View.OnClickListener letterListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                TextView textView = (TextView) view;
                Animation animation = AnimationUtils.loadAnimation(ActivityMain.this, R.anim.button_alpha);
                textView.setAnimation(animation);
                textView.setVisibility(view.INVISIBLE);

                //get to convertIdToString for take id by String format for use from letter
                String letter = convertIdToString(textView);

                letter = letter.replace("btn", "");
                char letterChar = letter.charAt(0);

                String wordLoweCased = publicWord.toLowerCase();

                if (wordLoweCased.contains(letter)) {
                    for (int i = 0; i < wordLoweCased.length(); i++) {
                        if (wordLoweCased.charAt(i) == letterChar) {
                            char[] wordDasheCharArray = wordDashes.toCharArray();
                            wordDasheCharArray[i] = publicWord.charAt(i);
                            wordDashes = new String(wordDasheCharArray);
                            txtShow.setText(wordDashes);
                            if ( !wordDashes.contains("-")) {
                                openFinishActivity("WIN");
                                G.winnerCouner++;
                            }
                        }
                    }
                } else {
                    fillCount++;
                    int imageId;

                    switch (fillCount) {
                        case 1:
                            imageId = (R.drawable.img_2);
                            break;
                        case 2:
                            imageId = (R.drawable.img_4);
                            break;
                        case 3:
                            imageId = (R.drawable.img_5);
                            break;
                        case 4:
                            imageId = (R.drawable.img_6);
                            break;
                        case 5:
                            imageId = (R.drawable.img_7);
                            break;
                        case 6:
                            imageId = (R.drawable.img_8);
                            break;
                        case 7:
                            imageId = (R.drawable.img_9);
                            break;
                        case 8:
                            imageId = (R.drawable.img_9);
                            break;
                        default:
                            imageId = (R.drawable.img_9);
                            break;
                    }
                    imgShow.setImageResource(imageId);
                    if (fillCount >= 8) {
                        openFinishActivity("LOSS");
                        G.lossConter++;
                    }
                }
            }
        };

        // **** listener for click letter **** 
        btna.setOnClickListener(letterListener);
        btnb.setOnClickListener(letterListener);
        btnc.setOnClickListener(letterListener);
        btnd.setOnClickListener(letterListener);
        btne.setOnClickListener(letterListener);
        btnf.setOnClickListener(letterListener);
        btng.setOnClickListener(letterListener);
        btnh.setOnClickListener(letterListener);
        btni.setOnClickListener(letterListener);
        btnj.setOnClickListener(letterListener);
        btnk.setOnClickListener(letterListener);
        btnl.setOnClickListener(letterListener);
        btnm.setOnClickListener(letterListener);
        btnn.setOnClickListener(letterListener);
        btno.setOnClickListener(letterListener);
        btnp.setOnClickListener(letterListener);
        btnq.setOnClickListener(letterListener);
        btnr.setOnClickListener(letterListener);
        btns.setOnClickListener(letterListener);
        btnt.setOnClickListener(letterListener);
        btnu.setOnClickListener(letterListener);
        btnv.setOnClickListener(letterListener);
        btnw.setOnClickListener(letterListener);
        btnx.setOnClickListener(letterListener);
        btny.setOnClickListener(letterListener);
        btnz.setOnClickListener(letterListener);
        //**** end listener
    }


    private String selectWord() {

        wordList.add("Memory");
        int randomValue = (int) (Math.random() * wordList.size());
        return wordList.get(randomValue);

    }


    private String getDashWord(String word) {
        String wordDashe = "";
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != ' ') {
                wordDashe += "-";
            } else {
                wordDashe += " ";
            }
        }
        return wordDashe;
    }


    private String convertIdToString(View view) {
        return view.getResources().getResourceEntryName(view.getId());
    }


    private void openFinishActivity(String result) {
        Intent intent = new Intent(ActivityMain.this, ActivityFinish.class);
        intent.putExtra("RESULT", result);
        intent.putExtra("WORD", publicWord);
        ActivityMain.this.startActivity(intent);
        finish();
    }


    private void readData(String value) {
        try {
            JSONArray jsonArray = new JSONArray(value);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                wordList.add(object.getString("word"));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
}