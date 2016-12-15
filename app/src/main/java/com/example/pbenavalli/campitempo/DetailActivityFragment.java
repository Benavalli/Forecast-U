package com.example.pbenavalli.campitempo;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;

import com.example.pbenavalli.campitempo.data.WeatherContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ShareActionProvider mShareActionProvider;

    private static final int DETAIL_LOADER = 0;

    private static final String FORECAST_HASHTAG = " #TempoNaCity";

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private String forecastStr;

    private Uri mUri;

    static final String DETAIL_URI = "URI";

    // Specify the columns we need.
    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

    // these constants correspond to the projection defined above, and must change if the
    // projection changes
    private static final int COL_WEATHER_ID = 0;
    private static final int COL_WEATHER_DATE = 1;
    private static final int COL_WEATHER_DESC = 2;
    private static final int COL_WEATHER_MAX_TEMP = 3;
    private static final int COL_WEATHER_MIN_TEMP = 4;
    private static final int COL_WEATHER_HUMIDITY = 5;
    private static final int COL_WEATHER_PRESSURE = 6;
    private static final int COL_WEATHER_DEGREES = 7;
    private static final int COL_WEATHER_WIND_SPEED = 8;
    private static final int COL_WEATHER_ICO_ID= 9;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailActivityFragment.DETAIL_URI);
        }


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ViewHolder viewHolder = new ViewHolder(rootView);
        rootView.setTag(viewHolder);
        //check intent

        /*if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            forecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
            ((TextView) rootView.findViewById(R.id.detail_text)).setText(forecastStr);


        if (intent != null) {
            forecastStr = intent.getDataString();
            ((TextView) rootView.findViewById(R.id.detail_text)).setText(forecastStr);
        }
        }*/
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detail, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

       // Get the provider and hold onto it to set/change the share intent.
       mShareActionProvider =  (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (forecastStr != null )
          mShareActionProvider.setShareIntent(createForecastShareIntent());

    }

     private Intent createForecastShareIntent() {
       Intent shareIntent = new Intent(Intent.ACTION_SEND);
       shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
       shareIntent.setType("text/plain");
       shareIntent.putExtra(Intent.EXTRA_TEXT, forecastStr + FORECAST_HASHTAG);
       return shareIntent;
    }

    void onLocationChanged( String newLocation ) {
        // replace the uri, since the location has changed
        Uri uri = mUri;
        if (null != uri) {
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
            Uri updatedUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation, date);
            mUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String locationSetting = Utility.getPreferredLocation(getActivity());
        /*Intent intent = getActivity().getIntent();
        if (intent == null || intent.getData() == null) {
            return null;
        }

        return new CursorLoader(getActivity(),
                intent.getData(),
                FORECAST_COLUMNS,
                null,
                null,
                null);*/

        if (null != mUri){
            return  new CursorLoader(getActivity(),
                    mUri,
                    FORECAST_COLUMNS,
                    null,
                    null,
                    null);
        }
        return null;
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descriptionView;
        public final TextView highTempView;
        public final TextView lowTempView;
        public final TextView pressureView;
        public final TextView humidityView;
        public final TextView windView;
        public final TextView dayNameView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.detail_imageView);
            dateView = (TextView) view.findViewById(R.id.detail_date);
            descriptionView = (TextView) view.findViewById(R.id.detail_description);
            highTempView = (TextView) view.findViewById(R.id.detail_max);
            lowTempView = (TextView) view.findViewById(R.id.detail_min);
            pressureView = (TextView) view.findViewById(R.id.detail_pressure);
            humidityView = (TextView) view.findViewById(R.id.detail_humidity);
            windView = (TextView) view.findViewById(R.id.detail_wind);
            dayNameView = (TextView) view.findViewById(R.id.detail_day_name);
        }
    }


    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        /*Log.v(LOG_TAG, "In onLoadFinished");
        if (!cursor.moveToFirst()) { return; }

        String dateString = Utility.formatDate(
                cursor.getLong(COL_WEATHER_DATE));

        String weatherDescription =
                cursor.getString(COL_WEATHER_DESC);

        boolean isMetric = Utility.isMetric(getActivity());

        String high = Utility.formatTemperature(
                getContext(), cursor.getDouble(COL_WEATHER_MAX_TEMP), isMetric);

        String low = Utility.formatTemperature(
                getContext(), cursor.getDouble(COL_WEATHER_MIN_TEMP), isMetric);

        forecastStr = String.format("%s - %s - %s/%s", dateString, weatherDescription, high, low);

        TextView detailTextView = (TextView)getView().findViewById(R.id.detail_text);
        detailTextView.setText(forecastStr);
        */

        if (cursor != null && cursor.moveToFirst()) {
            // Read weather condition ID from cursor
            ViewHolder viewHolder = (ViewHolder) getView().getTag();
            // Use placeholder Image
            int weatherId = cursor.getInt(COL_WEATHER_ICO_ID);
            weatherId = Utility.getArtResourceForWeatherCondition(weatherId);
            viewHolder.iconView.setImageResource(weatherId);

            // Read date from cursor and update views for day of week and date
            long date = cursor.getLong(COL_WEATHER_DATE);
            String friendlyDateText = Utility.getDayName(getActivity(), date);
            String dateText = Utility.getFormattedMonthDay(getActivity(), date);
            viewHolder.dayNameView.setText(friendlyDateText);
            viewHolder.dateView.setText(dateText);

            // Read description from cursor and update view
            String description = cursor.getString(COL_WEATHER_DESC);
            viewHolder.descriptionView.setText(description);

            // Read high temperature from cursor and update view
            boolean isMetric = Utility.isMetric(getActivity());

            double high = cursor.getDouble(COL_WEATHER_MAX_TEMP);
            String highString = Utility.formatTemperature(getActivity(), high, isMetric);
            viewHolder.highTempView.setText(highString);

            // Read low temperature from cursor and update view
            double low = cursor.getDouble(COL_WEATHER_MIN_TEMP);
            String lowString = Utility.formatTemperature(getActivity(), low, isMetric);
            viewHolder.lowTempView.setText(lowString);

            // Read humidity from cursor and update view
            float humidity = cursor.getFloat(COL_WEATHER_HUMIDITY);
            viewHolder.humidityView.setText(getActivity().getString(R.string.format_humidity, humidity));

            // Read wind speed and direction from cursor and update view
            float windSpeedStr = cursor.getFloat(COL_WEATHER_WIND_SPEED);
            float windDirStr = cursor.getFloat(COL_WEATHER_DEGREES);
            viewHolder.windView.setText(Utility.getFormattedWind(getActivity(), windSpeedStr, windDirStr));

            // Read pressure from cursor and update view
            float pressure = cursor.getFloat(COL_WEATHER_PRESSURE);
            viewHolder.pressureView.setText(getActivity().getString(R.string.format_pressure, pressure));

            // We still need this for the share intent
            forecastStr = String.format("%s - %s - %s/%s", dateText, description, high, low);

            // If onCreateOptionsMenu has already happened, we need to update the share intent now.
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createForecastShareIntent());
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

}
