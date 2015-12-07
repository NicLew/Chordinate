package edu.pacificu.chordinate.chordinate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CompositionsAdapter extends ArrayAdapter<SavedComposition> {

    /*
     * The constructor.
     *
     * @param context             The context to create.
     * @param savedCompositions   The list of saved compositions.
     */
    public CompositionsAdapter(Context context, ArrayList<SavedComposition> savedCompositions) {
        super(context, 0, savedCompositions);
    }

     /*
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
        SavedComposition savedComposition = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_saved_comp, parent, false);
        }

        // Lookup view for data population
        TextView compName = (TextView) convertView.findViewById(R.id.compName);
        TextView compDate = (TextView) convertView.findViewById(R.id.compDate);

        // Populate the data into the template view using the data object
        //compName.setText(savedComposition.getCompName());
        compName.setText(savedComposition.getName());
        compDate.setText(savedComposition.getDateStr());

        // Return the completed view to render on screen
        return convertView;
    }
}
