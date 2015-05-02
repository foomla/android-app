package org.foomla.androidapp.data;

import java.util.List;

import android.database.Cursor;

public interface EntityBuilder<T> {

    void addRow(Cursor cursor);

    List<T> build();
}
