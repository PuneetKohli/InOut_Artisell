package com.coep.puneet.artisell.UI.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.coep.puneet.artisell.R;

import java.util.HashMap;

public class AddProductActivity extends BaseActivity
{
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    private void say(final String s) {
        final HashMap<String, String> map = new HashMap<String, String>(1);
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, AddProductActivity.class.getName());
        tts.speak(s, TextToSpeech.QUEUE_FLUSH, map);
    }


    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_add_product;
    }

    @Override
    protected void setupToolbar()
    {
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);

    }

    @Override
    protected void setupLayout()
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            View customDialogView = inflater.inflate(R.layout.profile_popup_edit_details, null, false);
            final TextView popupEdittext = (TextView) customDialogView.findViewById(R.id.popup_editText);
            popupEdittext.setText(getString(R.string.alert_leave_add_product));
            builder.setTitle(getString(R.string.alert_title_confirm));
            builder.setPositiveButton(getString(R.string.alert_positive_button), new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    finish();
                }
            });
            builder.setView(customDialogView);
            builder.create();
            builder.show();
            //onBackPressed();
        }
        return true;
    }

    @Override
    protected void onDestroy() {


        //Close the Text to Speech Library
        if(tts != null) {

            tts.stop();
            tts.shutdown();
            Log.d("AddProduct", "TTS Destroyed");
        }
        super.onDestroy();
    }
}
