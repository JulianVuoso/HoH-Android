package ar.edu.itba.hci.hoh.repositories;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Response;

import ar.edu.itba.hci.hoh.api.Api;
import ar.edu.itba.hci.hoh.elements.Result;

public abstract class Repository {
    protected final Api api;
    protected final Application application;

    protected Repository(Application application) {
        this.api = Api.getInstance(application);
        this.application = application;
    }

    protected static <T> Response.Listener<T> getListener(final MutableLiveData<Result<T>> result) {
        return (response) -> result.setValue(new Result<>(response));
    }

    protected static <T> Response.ErrorListener getErrorListener(final Api api, final MutableLiveData<Result<T>> result) {
        return (error) -> result.setValue(new Result<>(null, api.handleError(error)));
    }
}
