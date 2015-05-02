package org.foomla.androidapp.async;

import org.foomla.androidapp.R;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ProgressDialog;

import android.content.Context;

import android.os.AsyncTask;

public abstract class RepositoryLoadTask<Result> extends AsyncTask<Object, Void, Result> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryLoadTask.class);

    public interface LoadHandler<Result> {
        void handle(Result result);
    }

    private final Context context;
    private final LoadHandler<Result> handler;

    private ProgressDialog progressDialog;

    public RepositoryLoadTask(final Context context, final LoadHandler<Result> handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    protected Result doInBackground(final Object... params) {
        try {
            if (!isCancelled()) {
                if (params != null && params.length > 0) {
                    return load(params[0]);
                } else {
                    load(null);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Unable to load data!", e);
            cancel(true);
        }

        return null;
    }

    protected Context getContext() {
        return context;
    }

    protected String getProgessAnimationText(final Context ctx) {
        return ctx.getString(R.string.downloadtask_progress_title);
    }

    protected int getProgessAnimationTextId() {
        return R.string.downloadtask_progress_title;
    }

    protected void hideProgressAnimation() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    protected abstract Result load(Object params) throws Exception;

    @Override
    protected void onCancelled() {
        super.onCancelled();
        hideProgressAnimation();
    }

    @Override
    protected void onPostExecute(final Result result) {
        handler.handle(result);
        hideProgressAnimation();
    }

    @Override
    protected void onPreExecute() {
        Context ctx = getContext();
        showProgressAnimation(ctx);
        LOGGER.debug("Start loading data");
    }

    protected void showProgressAnimation(final Context ctx) {
        this.progressDialog = ProgressDialog.show(ctx, ctx.getString(R.string.downloadtask_progress_title),
                ctx.getString(R.string.downloadtask_progress_desc));
    }
}
