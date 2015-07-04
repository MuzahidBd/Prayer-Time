package com.alimuzaffar.ramadanalarm.fragments;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alimuzaffar.ramadanalarm.AppSettings;
import com.alimuzaffar.ramadanalarm.Constants;
import com.alimuzaffar.ramadanalarm.R;
import com.alimuzaffar.ramadanalarm.SetAlarmActivity;
import com.alimuzaffar.ramadanalarm.utils.PrayTime;

import java.util.LinkedHashMap;
import java.util.TimeZone;

public class SalaatTimesFragment extends Fragment implements Constants {

  int mIndex = 0;
  Location mLastLocation;

  public static SalaatTimesFragment newInstance(int index, Location location) {
    SalaatTimesFragment fragment = new SalaatTimesFragment();
    Bundle args = new Bundle();
    args.putInt(EXTRA_ALARM_INDEX, index);
    args.putParcelable(EXTRA_LAST_LOCATION, location);
    fragment.setArguments(args);
    return fragment;
  }

  public SalaatTimesFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mIndex = getArguments().getInt(EXTRA_ALARM_INDEX);
      mLastLocation = (Location) getArguments().getParcelable(EXTRA_LAST_LOCATION);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_salaat_times, container, false);
    init(view);
    return view;
  }

  protected void init(View view) {
    // In future releases we will add more cards.
    // Then we'll need to do this for each card.
    // For now it's included in the layout which
    // makes it easier to work with the layout editor.
    // inflater.inflate(R.layout.view_prayer_times, timesContainer, true);

    //Toolbar will now take on default Action Bar characteristics
    LinkedHashMap<String, String> prayerTimes =
        PrayTime.getPrayerTimes(getActivity(), mIndex, mLastLocation.getLatitude(), mLastLocation.getLongitude());

    TextView title = (TextView) view.findViewById(R.id.card_title);
    title.setText(TimeZone.getDefault().getID());

    TextView fajr = (TextView) view.findViewById(R.id.fajr);
    TextView dhuhr = (TextView) view.findViewById(R.id.dhuhr);
    TextView asr = (TextView) view.findViewById(R.id.asr);
    TextView maghrib = (TextView) view.findViewById(R.id.maghrib);
    TextView isha = (TextView) view.findViewById(R.id.isha);
    TextView sunrise = (TextView) view.findViewById(R.id.sunrise);
    TextView sunset = (TextView) view.findViewById(R.id.sunset);
    TextView alarm = (TextView) view.findViewById(R.id.alarm);

    fajr.setText(prayerTimes.get(String.valueOf(fajr.getTag())));
    dhuhr.setText(prayerTimes.get(String.valueOf(dhuhr.getTag())));
    asr.setText(prayerTimes.get(String.valueOf(asr.getTag())));
    maghrib.setText(prayerTimes.get(String.valueOf(maghrib.getTag())));
    isha.setText(prayerTimes.get(String.valueOf(isha.getTag())));
    sunrise.setText(prayerTimes.get(String.valueOf(sunrise.getTag())));
    sunset.setText(prayerTimes.get(String.valueOf(sunset.getTag())));

    //set text for the first card.
    setAlarmButtonText(alarm, mIndex);
    setAlarmButtonClickListener(alarm, mIndex);
  }

  private void setAlarmButtonText(TextView button, int index) {
    boolean isAlarmSet = AppSettings.getInstance(getActivity()).isAlarmSetFor(index);
    int isAlarmSetInt = isAlarmSet ? 0 : 1;
    String buttonText = getResources().getQuantityString(R.plurals.button_alarm, isAlarmSetInt);
    button.setText(buttonText);
  }

  private void setAlarmButtonClickListener(TextView alarm, int index) {
    alarm.setOnClickListener(new View.OnClickListener() {
      int index = 0;

      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), SetAlarmActivity.class);
        intent.putExtra(EXTRA_ALARM_INDEX, index);
        startActivity(intent);
      }

      public View.OnClickListener init(int index) {
        this.index = index;
        return this;
      }

    }.init(index));
  }

  public void setLocation(Location location) {
    mLastLocation = location;
    if (isAdded()) {
      init(getView());
    }
  }

}