package org.foomla.androidapp.activities.mytrainings;

import java.util.ArrayList;
import java.util.List;

import org.foomla.androidapp.R;
import org.foomla.api.entities.twizard.Training;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public class MyTrainingsFragment extends ListFragment {

    public interface FragmentCallback {
        List<Training> getMyTrainings();

        void onDeleteTraining(Training training);

        void onTrainingSelected(Training training);
    }

    private static final int DELETE_ITEM_ID = 0;

    private FragmentCallback fragmentCallback;
    private final MyTrainingsAdapter myTrainingsAdapter = new MyTrainingsAdapter(new ArrayList<Training>(0));

    public void deleteTraining(final Training training) {
        if (fragmentCallback != null) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    switch (which) {

                        case DialogInterface.BUTTON_POSITIVE :
                            fragmentCallback.onDeleteTraining(training);
                            break;

                        default :
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setMessage(R.string.delete_question).setPositiveButton(R.string.delete, dialogClickListener)
                   .setNegativeButton(R.string.cancel, dialogClickListener).show();
        }
    }

    public void notifyDataChanged() {
        myTrainingsAdapter.setTrainings(fragmentCallback.getMyTrainings());
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceBundle) {
        super.onActivityCreated(savedInstanceBundle);
        myTrainingsAdapter.setTrainings(fragmentCallback.getMyTrainings());
        registerForContextMenu(getListView());
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        if (item.getItemId() == DELETE_ITEM_ID) {
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            Object selectedItem = getListView().getItemAtPosition(info.position);
            if (selectedItem instanceof Training) {
                deleteTraining((Training) selectedItem);
            }
        }

        return true;
    }

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        Activity activity = getActivity();
        if (activity instanceof FragmentCallback) {
            this.fragmentCallback = (FragmentCallback) activity;
        } else {
            throw new IllegalArgumentException("Parent Activity must implement FragmentCallback.");
        }

        setListAdapter(myTrainingsAdapter);
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View view, final ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        if (view.getId() == android.R.id.list) {
            menu.add(Menu.NONE, DELETE_ITEM_ID, 0, R.string.delete);
        }
    }

    @Override
    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        Object item = getListView().getItemAtPosition(position);
        if (item instanceof Training) {
            showTrainingDetail((Training) item);
        }
    }

    public void showTrainingDetail(final Training training) {
        if (fragmentCallback != null) {
            fragmentCallback.onTrainingSelected(training);
        }
    }
}
