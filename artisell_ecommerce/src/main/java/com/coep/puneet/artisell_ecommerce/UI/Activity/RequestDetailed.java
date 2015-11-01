package com.coep.puneet.artisell_ecommerce.UI.Activity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coep.puneet.artisell_ecommerce.Custom.ExpandableHeightGridView;
import com.coep.puneet.artisell_ecommerce.Global.AppConstants;
import com.coep.puneet.artisell_ecommerce.ParseObjects.Request;
import com.coep.puneet.artisell_ecommerce.R;
import com.coep.puneet.artisell_ecommerce.UI.Adapter.CategoryGridTickAdapter;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

public class RequestDetailed extends BaseActivity
{

    @Bind(R.id.category_grid_view) ExpandableHeightGridView navGrid;
    @Bind(R.id.requestImage) ImageView requestImage;
    @Bind(R.id.seekBarBudget) SeekBar seekBar;
    @Bind(R.id.editText_requestdescription) EditText requestDescription;
    @Bind(R.id.editText_deliveredby) EditText deliveredBy;
    @Bind(R.id.textview_budget) TextView budget;

    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    Date date;
    int budgetProgress = 0;

    @OnClick(R.id.editText_deliveredby)
    void openDateDialog()
    {
        fromDatePickerDialog.show();
    }

    @OnClick(R.id.upload_button)
    void takePhoto()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, AppConstants.REQUEST_CAMERA);
    }

    @OnClick(R.id.button_save)
    void save()
    {
        request.setCustomer(ParseUser.getCurrentUser());
        request.setRequestStatus(0);
        request.setRequestBudget(budgetProgress);

        if (currentCategoryPos != -1)
        {
            request.setRequestCategory(manager.productCategories.get(currentCategoryPos));
        }
        else
        {
            Toast.makeText(this, "Please Select a Category", Toast.LENGTH_SHORT).show();
        }

        if (!requestDescription.getText().toString().equals(""))
        {
            request.setRequestDescription(requestDescription.getText().toString());
        }
        else
        {
            Toast.makeText(this, "Please Enter Description", Toast.LENGTH_SHORT).show();

        }

        if (parseFile != null)
        {
            request.getRequestPhoto().saveInBackground(new SaveCallback()
            {
                @Override
                public void done(ParseException e)
                {
                    request.saveEventually();
                    Toast.makeText(RequestDetailed.this, "Your Request has been accepted", Toast.LENGTH_SHORT).show();
                    manager.pendingList.add(request);
                    finish();
                    finish();
                }
            });
        }
        else
        {
            request.saveEventually();
            Toast.makeText(RequestDetailed.this, "Your Request has been accepted", Toast.LENGTH_SHORT).show();
            manager.pendingList.add(request);
            finish();
            finish();
        }

        //request.setRequestDeliverBy(new Date());
       /* if(parseFile != null)
        {
            request.getRequestPhoto().saveInBackground(new SaveCallback()
            {
                @Override
                public void done(ParseException e)
                {
                    request.saveEventually();
                }
            });
        }
        else {
            request.saveEventually();

        }*/
    }

    private Bitmap bm;
    private int currentCategoryPos = -1;
    ParseFile parseFile;
    final Request request = new Request();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_request_detailed;
    }

    @Override
    protected void setupToolbar()
    {
        final Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setTitle("");
        actionBar.canShowOverflowMenu();
    }

    @Override
    protected void setupLayout()
    {
        final CategoryGridTickAdapter mAdapter = new CategoryGridTickAdapter(this, manager.productCategories);

        navGrid.setAdapter(mAdapter);
        navGrid.setExpanded(true);
        navGrid.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                currentCategoryPos = position;
                mAdapter.updateView(position);
            }
        });
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        setDateTimeField();
        budget.setText("5000");
        seekBar.incrementProgressBy(50);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                budgetProgress = progress;
                budgetProgress = progress / 50;
                budgetProgress = progress * 50;
                budget.setText("" + budgetProgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK)
        {
            // The user picked an image. Send it to Clarifai for recognition.
            //Log.d("", "User picked image: " + intent.getData());
            if (requestCode == AppConstants.REQUEST_CAMERA)
            {
                bm = (Bitmap) intent.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                assert bm != null;
                bm.compress(Bitmap.CompressFormat.JPEG, 70, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try
                {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            if (bm != null)
            {
                requestImage.setImageBitmap(bm);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] byteArray = stream.toByteArray();
                parseFile = new ParseFile(byteArray);
                request.setRequestPhoto(parseFile);

            }
            else
            {
                Toast.makeText(this, "Unable to load Image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setDateTimeField()
    {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar date = Calendar.getInstance();
                date.set(year, monthOfYear, dayOfMonth);
                deliveredBy.setText(dateFormatter.format(date.getTime()));
                request.setRequestDeliverBy(date.getTime());
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

}
