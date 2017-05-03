package com.theunheard.saigono;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class AboutFragment extends Fragment implements FragmentInterface {


    private Button sendFeedbackButton;
    private Button rateButton;
    private EditText feedbackEditText;
    private AdView adView;
    private TextView aboutTextView;


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public void fragmentBecameVisible() {
        return;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_app, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        sendFeedbackButton = (Button) getView().findViewById(R.id.sendFeedbackButton);
        feedbackEditText = (EditText) getView().findViewById(R.id.feedbackEditText);
        rateButton = (Button) getView().findViewById(R.id.rateButton);
        aboutTextView = (TextView) getView().findViewById(R.id.aboutTextView);


        adView = (AdView) getView().findViewById(R.id.adViewFeedbackSection);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);



        setupAboutTextView();
        setupSendFeedbackButton();
        setupRateButton();

    }

    private void setupAboutTextView() {
        aboutTextView.setText(Html.fromHtml(getString(R.string.about_text, Html.FROM_HTML_MODE_COMPACT)));
        aboutTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    private boolean startRateActivity(Intent aIntent) {
        try
        {
            startActivity(aIntent);
            return true;
        }
        catch (ActivityNotFoundException e)
        {
            return false;
        }
    }

    // http://stackoverflow.com/questions/10816757/rate-this-app-link-in-google-play-store-app-on-the-phone
    private void setupRateButton() {

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                //Try Google play
                intent.setData(Uri.parse("market://details?id=" + getContext().getPackageName()));
                if (!startRateActivity(intent)) {
                    //Market (Google play) app seems not installed, let's try to open a webbrowser
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?" + getContext().getPackageName()));
                    if (!startRateActivity(intent)) {
                        //Well if this also fails, we have run out of options, inform the user.
                        Toast.makeText(getActivity(), "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void setupSendFeedbackButton() {
        sendFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("message/rfc822");
                Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "theunheardbiz@gmail.com" });
                Email.putExtra(Intent.EXTRA_SUBJECT, "SaigoNo Feedback");
                Email.putExtra(Intent.EXTRA_TEXT, feedbackEditText.getText());
                startActivity(Intent.createChooser(Email, "Send Feedback:"));
            }
        });
    }
}
