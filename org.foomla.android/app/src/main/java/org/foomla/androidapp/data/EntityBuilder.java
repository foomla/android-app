package org.foomla.androidapp.data;

import android.database.Cursor;

import java.util.List;

public interface EntityBuilder<T> {

    void addRow(Cursor cursor);

    List<T> build();
}
