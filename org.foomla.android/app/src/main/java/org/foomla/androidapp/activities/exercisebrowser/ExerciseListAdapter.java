package org.foomla.androidapp.activities.exercisebrowser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.foomla.androidapp.R;
import org.foomla.androidapp.async.LoadExerciseImageTask;
import org.foomla.androidapp.domain.Exercise;
import org.foomla.androidapp.domain.TrainingFocus;
import org.foomla.androidapp.utils.EnumTextUtil;
import org.foomla.androidapp.utils.ImageUtil.ImageType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExerciseListAdapter extends BaseAdapter {

    public interface ItemListener {
        void onDisplayExerciseDetails(final Exercise exercise);

        void onSelectItem(final Exercise exercise);
    }

    private final Context context;
    private List<Exercise> exercises;
    private final SparseArray<Drawable> imageCache = new SparseArray<Drawable>();
    private final ItemListener itemListener;
    private boolean showSelectButton;

    public ExerciseListAdapter(Context context, ItemListener itemListener, boolean showSelectButton) {
        super();
        this.context = context;
        this.itemListener = itemListener;
        this.exercises = new ArrayList<Exercise>();
        this.showSelectButton = showSelectButton;
    }

    @Override
    public int getCount() {
        return exercises.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < getCount()) {
            return exercises.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listitem_exercise, parent, false);

        Exercise exercise = exercises.get(position);
        initializeTitleView(exercise, view);

        initializeFocusView(exercise, view);
        initializeDetailListener(exercise, view);
        initializeMarker(exercise, view);

        setImage(view, exercise);

        return view;
    }

    public boolean isShowSelectButton() {
        return showSelectButton;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    public void setShowSelectButton(boolean showSelectButton) {
        this.showSelectButton = showSelectButton;
        notifyDataSetChanged();
    }

    private void initializeDetailListener(final Exercise exercise, final View view) {
        View container = view.findViewById(R.id.exerciseContainer);

        if (container == null) {
            return;
        }

        if (!isShowSelectButton()) {
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onDisplayExerciseDetails(exercise);
                }
            });
        } else {
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onSelectItem(exercise);
                }
            });
        }
        container.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo contextMenuInfo) {
                menu.setHeaderTitle("Übung");
                if (isShowSelectButton()) {
                    menu.add(Menu.NONE, exercises.indexOf(exercise), Menu.NONE, R.string.choose);
                }
                menu.add(Menu.NONE, exercises.indexOf(exercise), Menu.NONE, R.string.open);
            }
        });
    }

    private void initializeFocusView(Exercise exercise, View view) {
        TextView tv = (TextView) view.findViewById(R.id.focus);
        TrainingFocus trainingFocus = exercise.getTrainingFocus();
        if (trainingFocus != null) {
            tv.setText(EnumTextUtil.getText(context, trainingFocus));
        } else {
            tv.setText("-");
        }
    }

    @SuppressLint("DefaultLocale")
    private void initializeMarker(Exercise exercise, View view) {
        if (isExerciseNew(exercise)) {
            TextView markerTextView = (TextView) view.findViewById(R.id.marker);
            markerTextView.setText(context.getString(R.string.exercisebrowser_newexercise).toUpperCase());
        } else {
            view.findViewById(R.id.marker_container).setVisibility(View.GONE);
        }
    }

    private void initializeTitleView(Exercise exercise, View view) {
        TextView tv = (TextView) view.findViewById(R.id.title);
        tv.setText(exercise.getTitle());
    }

    private boolean isExerciseNew(Exercise exercise) {
        Date creation = exercise.getCreatedAt();
        if (creation == null) {
            return false;
        }

        return System.currentTimeMillis() - creation.getTime() <= (14 * 24 * 60 * 60 * 1000);
    }

    private void setImage(View view, Exercise exercise) {
        final ImageView imageView = (ImageView) view.findViewById(R.id.image);

        final Integer exerciseId = exercise.getId();
        if (imageCache.get(exerciseId) == null) {
            new LoadExerciseImageTask(context, null, ImageType.X_THUMBNAIL) {
                @Override
                protected void onPostExecute(Drawable result) {
                    imageView.setImageDrawable(result);
                    imageCache.put(exerciseId, result);
                }
            }.execute(exercise);
        } else {
            imageView.setImageDrawable(imageCache.get(exerciseId));
        }
    }
}
