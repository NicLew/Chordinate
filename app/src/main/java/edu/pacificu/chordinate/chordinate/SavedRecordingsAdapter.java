package edu.pacificu.chordinate.chordinate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lewe4441 on 11/9/2015.
 */
public class SavedRecordingsAdapter extends ArrayAdapter<SavedRecording> {
    public SavedRecordingsAdapter(Context context, ArrayList<SavedRecording> users) {
        super(context, 0, users);
    }

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
