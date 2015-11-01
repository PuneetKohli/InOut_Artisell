package com.coep.puneet.artisell.UI.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coep.puneet.artisell.ParseObjects.Category;
import com.coep.puneet.artisell.ParseObjects.Product;
import com.coep.puneet.artisell.R;
import com.coep.puneet.artisell.UI.Activity.AddProductActivity;
import com.coep.puneet.artisell.UI.Fragment.steps.AddProductStep1_category;
import com.coep.puneet.artisell.UI.Fragment.steps.AddProductStep2_name;
import com.coep.puneet.artisell.UI.Fragment.steps.AddProductStep3_image;
import com.coep.puneet.artisell.UI.Fragment.steps.AddProductStep4_price;
import com.coep.puneet.artisell.UI.Fragment.steps.AddProductStep_summary;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.codepond.wizardroid.WizardFlow;
import org.codepond.wizardroid.WizardFragment;
import org.codepond.wizardroid.persistence.ContextManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A sample to demonstrate a form in multiple steps.
 */
public class AddProductWizardFragment extends WizardFragment
{
    @Bind(R.id.image_prev_button) ImageView prevImage;
    @Bind(R.id.image_next_button) ImageView nextImage;
    @Bind(R.id.wizard_next_button) TextView nextButton;
    @Bind(R.id.wizard_previous_button) TextView previousButton;
    @Bind(R.id.step_container) ViewPager mViewPager;

    private String mNextButtonText;
    private String mFinishButtonText;
    private String mBackButtonText;

    public AddProductWizardFragment()
    {
        super();
    }

    public AddProductWizardFragment(ContextManager contextManager)
    {
        super(contextManager);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.custom_wizard_layout, container, false);
        ButterKnife.bind(this, rootView);
        mViewPager.setOnTouchListener(new View.OnTouchListener()
        {

            public boolean onTouch(View arg0, MotionEvent arg1)
            {
                return true;
            }
        });
        this.nextButton.setText(this.getNextButtonLabel());
        this.previousButton.setText(this.getBackButtonLabel());
        return rootView;
    }

    public void onResume()
    {
        super.onResume();
        this.updateWizardControls();
    }

    public void onStepChanged()
    {
        String s = ((AppCompatActivity) getActivity()).getSupportActionBar().getTitle().toString();
        switch (this.wizard.getCurrentStepPosition())
        {
            case 0:
                s = getString(R.string.title_add_product_1);
                break;
            case 1:
                s = getString(R.string.title_add_product_2);
                break;

            case 2:
                s = getString(R.string.title_add_product_3);
                break;
            case 3:
                s = getString(R.string.title_add_product_4);
                break;
            case 4:
                s = getString(R.string.title_add_product_5);
            default:
                break;
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(s);
        super.onStepChanged();
        this.updateWizardControls();
    }

    private void updateWizardControls()
    {
        this.previousButton.setEnabled(!this.wizard.isFirstStep());
        this.previousButton.setText(this.getBackButtonLabel());
        this.nextButton.setEnabled(this.wizard.canGoNext());
        this.nextButton.setText(this.wizard.isLastStep() ? this.getFinishButtonText() : this.getNextButtonLabel());
        if (this.wizard.isFirstStep())
        {
            this.prevImage.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        }
        else
        {
            this.prevImage.clearColorFilter();
        }
        if (!this.wizard.canGoNext())
        {
            this.nextImage.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        }
        else
        {
            this.nextImage.clearColorFilter();
        }
        if (this.wizard.isLastStep())
        {
            this.nextImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_circle_black_24dp));
        }
        else
        {
            this.nextImage.setImageDrawable(getResources().getDrawable(R.drawable.finger_point_front));
        }
    }

    @OnClick(R.id.layout_next_button)
    void goNext()
    {

        int step = this.wizard.getCurrentStepPosition();
        switch (step)
        {
            case 0:
                Category category = ((AddProductActivity) getActivity()).manager.currentProduct.getCategory();
                if (category.getCategory_name() == null)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    View customDialogView = inflater.inflate(R.layout.profile_popup_edit_details, null, false);
                    final TextView popupEdittext = (TextView) customDialogView.findViewById(R.id.popup_editText);
                    popupEdittext.setText("Please Select 1 Category");
                    builder.setTitle("Error");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                        }
                    });
                    builder.setView(customDialogView);
                    builder.create();
                    builder.show();
                }
                else
                {
                    this.wizard.goNext();
                }
                break;
            case 1:
                String name = ((AddProductActivity) getActivity()).manager.currentProduct.getProduct_name();
                if (name.equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    View customDialogView = inflater.inflate(R.layout.profile_popup_edit_details, null, false);
                    builder.setTitle("Error");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                        }
                    });
                    builder.setView(customDialogView);
                    builder.create();
                    builder.show();
                }
                else
                {
                    this.wizard.goNext();
                }
                break;
            case 2:
                ParseFile file = ((AddProductActivity) getActivity()).manager.currentProduct.getProductImage();
                ArrayList tags = ((AddProductActivity) getActivity()).manager.currentProduct.getProductTags();
                if (file.getName().equals("null"))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    View customDialogView = inflater.inflate(R.layout.profile_popup_edit_details, null, false);
                    final TextView popupEdittext = (TextView) customDialogView.findViewById(R.id.popup_editText);
                    popupEdittext.setText("Please Upload Image");
                    builder.setTitle("Error");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                        }
                    });
                    builder.setView(customDialogView);
                    builder.create();
                    builder.show();
                }
                else
                {
                    this.wizard.goNext();
                }
                break;
            case 3:
                int price = ((AddProductActivity) getActivity()).manager.currentProduct.getProductPrice();
                int quantity = ((AddProductActivity) getActivity()).manager.currentProduct.getProductQuantity();
                if (price == 0 || quantity == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    View customDialogView = inflater.inflate(R.layout.profile_popup_edit_details, null, false);
                    final TextView popupEdittext = (TextView) customDialogView.findViewById(R.id.popup_editText);
                    if (price == 0 && quantity == 0)
                    {
                        popupEdittext.setText("Please Enter Price and Quantity");
                    }
                    else if (price == 0)
                    {
                        popupEdittext.setText("Please Enter Price");
                    }
                    else
                    {
                        popupEdittext.setText("Please Enter Quantity");
                    }

                    builder.setTitle("Error");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                        }
                    });
                    builder.setView(customDialogView);
                    builder.create();
                    builder.show();
                }
                else
                {
                    this.wizard.goNext();
                }
                break;
            case 4:
                this.wizard.goNext();
                break;

        }

    }

    @OnClick(R.id.layout_prev_button)
    void goBack()
    {
        this.wizard.goBack();
    }

    @Override
    public WizardFlow onSetup()
    {
        return new WizardFlow.Builder()
                .addStep(AddProductStep1_category.class)
                .addStep(AddProductStep2_name.class)
                .addStep(AddProductStep3_image.class)
                .addStep(AddProductStep4_price.class)
                .addStep(AddProductStep_summary.class)
                .create();
   }

    /*
        You'd normally override onWizardComplete to access the wizard context and/or close the wizard
     */
    @Override
    public void onWizardComplete()
    {
        super.onWizardComplete();   //Make sure to first call the super method before anything else
        final Product product = ((AddProductActivity) getActivity()).manager.currentProduct;

        ((AddProductActivity) getActivity()).manager.currentArtisanProducts.add(product);

        product.getProductImage().saveInBackground(new SaveCallback()
        {
            @Override
            public void done(ParseException e)
            {
                if (e == null)
                {
                    product.saveEventually(new SaveCallback()
                    {
                        @Override
                        public void done(ParseException e)
                        {
                            ParseUser.getCurrentUser().addUnique("user_products", product);
                            ParseUser.getCurrentUser().saveEventually();
                        }
                    });
                }
                else
                {
                    //Toast.makeText(getActivity(), "Unable to save product to server in offline mode, you can view it offline for the time being", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Toast.makeText(getActivity(), "Your Product has been saved", Toast.LENGTH_SHORT).show();

        getActivity().finish();
    }

    public String getNextButtonLabel()
    {
        return TextUtils.isEmpty(this.mNextButtonText) ? this.getResources().getString(R.string.action_go_ahead) : this.mNextButtonText;
    }

    public void setNextButtonText(String nextButtonText)
    {
        this.mNextButtonText = nextButtonText;
    }

    public String getFinishButtonText()
    {
        return TextUtils.isEmpty(this.mFinishButtonText) ? this.getResources().getString(R.string.action_done) : this.mFinishButtonText;
    }

    public void setFinishButtonText(String finishButtonText)
    {
        this.mFinishButtonText = finishButtonText;
    }

    public String getBackButtonLabel()
    {
        return TextUtils.isEmpty(this.mBackButtonText) ? this.getResources().getString(R.string.action_go_back) : this.mBackButtonText;
    }

    public void setBackButtonText(String backButtonText)
    {
        this.mBackButtonText = backButtonText;
    }


}
