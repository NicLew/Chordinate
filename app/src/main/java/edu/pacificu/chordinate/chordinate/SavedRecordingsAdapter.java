package edu.pacificu.chordinate.chordinate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SavedRecordingsAdapter extends ArrayAdapter<SavedRecording> {

    /**
     * The constructor.
     *
     * @param context           The context to create.
     * @param savedRecordings   The list of saved recordings.
     */
    public SavedRecordingsAdapter(Context context, ArrayList<SavedRecording> savedRecordings) {
        super(context, 0, savedRecordings);
    }

    /**
     * Gets an item from the list and sets the text views appropriately.
     *
     * @param position      The position of the selected item in the list.
     * @param convertView   The view to be used.
     * @param parent        The parent view group to be used.
     * @return  The modified view for the selected item.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        SavedRecording savedRecording = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_saved_recording, parent, false);
        }

        // Lookup view for data population
        TextView tvRecName = (TextView) convertView.findViewById(R.id.tvRecName);
        TextView tvRecDate = (TextView) convertView.findViewById(R.id.tvRecDate);
        TextView tvRecLength = (TextView) convertView.findViewById(R.id.tvRecLength);

        // Populate the data into the template view using the data object
        tvRecName.setText(savedRecording.getRecName());
        tvRecDate.setText(savedRecording.getDateStr());
        tvRecLength.setText(savedRecording.getLengthStr());

        // Return the completed view to render on screen
        return convertView;
    }
}
