package org.foomla.androidapp.async;

import org.foomla.androidapp.R;
import org.foomla.androidapp.utils.ProgressVisualizationUtil;

import org.foomla.api.client.FoomlaClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;

import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.AsyncTask;

import android.widget.Toast;

public abstract class DownloadTask<Result> extends AsyncTask<FoomlaClient, Void, Result> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadTask.class);

    public interface DownloadHandler<Result> {
        void handle(Result result);
    }

    private final Activity activity;

    private final DownloadHandler<Result> handler;

    public DownloadTask(final Activity activity, final DownloadHandler<Result> handler) {
        this.activity = activity;
        this.handler = handler;
    }

    /**
     * Check whether the task is canceled and catch exceptions that might occur during the download process.
     *
     * @param   params  An instance of {@link FoomlaClient} that is used to download data from foomla server.
     *
     * @return  the result of {@linkplain #download(org.foomla.api.client.FoomlaClient)} or null if an exceptions
     *          occurs.
     */
    @Override
    protected Result doInBackground(final FoomlaClient... params) {
        try {
            if (!isCancelled()) {
                return download(params[0]);
            }
        } catch (Exception e) {
            LOGGER.error("Unable to download data!", e);
            cancel(false);
        }

        return null;
    }

    /**
     * This method needs to be implemented by subclasses. It is only responsible to download data - not to check the
     * network state or to handle exceptions.
     *
     * @param   foomlaClient  The {@link FoomlaClient} that should be used to fetch the data.
     *
     * @return  the data.
     */
    protected abstract Result download(FoomlaClient foomlaClient) throws Exception;

    /**
     * Returns the {@link Context}.
     *
     * @return  the {@link Context}.
     */
    protected Activity getActivity() {
        return activity;
    }

    protected int getLoadingTextId() {
        return -1;
    }

    protected void hideProgressbar() {
        ProgressVisualizationUtil.hideProgressbar(activity);
    }

    @Override
    protected void onCancelled() {
        hideProgressbar();
        showUserHint(R.string.downloadtask_alert_no_data_desc);
        activity.finish();
    }

    /**
     * Delegates the result of the download to {@literal #handler} and hides the {@link ProgressDialog} if one is
     * existing.
     *
     * @param  result  the abstract result of the download which is delegated to {@literal #handler}.
     */
    @Override
    protected void onPostExecute(final Result result) {
        handler.handle(result);
        hideProgressbar();
    }

    /**
     * Shows a {@link ProgressDialog} and checks the network state.
     */
    @Override
    protected void onPreExecute() {
        showProgressbar();
        LOGGER.debug("Start download");

        if (!checkNetworkState()) {
            LOGGER.warn("Network is not available.");
            cancel(true);
        }
    }

    protected void showProgressbar() {
        int loadingTextId = getLoadingTextId();
        if (loadingTextId != -1) {
            ProgressVisualizationUtil.showProgressbar(activity, loadingTextId);
        } else {
            ProgressVisualizationUtil.showProgressbar(activity);
        }
    }

    protected void showUserHint(final int messageId) {
        Toast.makeText(activity, messageId, Toast.LENGTH_LONG).show();
    }

    /**
     * Determine if network access is available and connected.
     *
     * @return  true, if network is available and connected, otherwise false.
     */
    private boolean checkNetworkState() {
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Service.CONNECTIVITY_SERVICE);
        NetworkInfo[] allNetworkInfo = manager.getAllNetworkInfo();

        if (allNetworkInfo != null) {
            for (NetworkInfo networkInfo : allNetworkInfo) {
                if (networkInfo.isAvailable() && networkInfo.isConnected()) {
                    return true;
                }
            }
        }

        return false;
    }

}
