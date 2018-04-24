package com.example.ubuntu.seefood;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by ubuntu on 7/2/18.
 */

public class ObjectAdapter extends ArrayAdapter<String> {

    public ObjectAdapter(Activity context, ArrayList<String> e){
        super(context,0,e);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView==null) {
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        String listItem = getItem(position);
        String[] class_count_confidence = listItem.split(",");

        TextView confidence_text = (TextView) listItemView.findViewById(R.id.confidence_text_view);
        confidence_text.setText(class_count_confidence[2]);
        // Setting the proper background color on the confidence circle.
        // Fetches the background from the TextView, which is a GradientDrawable.
        GradientDrawable confidenceCircle = (GradientDrawable)confidence_text.getBackground();
        // Gets the appropriate background color based on the current earthquake confidence
        int confidenceColor = getConfidenceColor(Double.parseDouble(class_count_confidence[2])/10);
        // Set the color on the confidence circle
        confidenceCircle.setColor(confidenceColor);

        TextView objectText = (TextView) listItemView.findViewById(R.id.object_text_view);
        objectText.setText(class_count_confidence[0]);

        TextView frameText = (TextView) listItemView.findViewById(R.id.frames_text_view);
        if(Objects.equals(class_count_confidence[1], "1")) {
            frameText.setText("Found in 1 frame");
        }else {
            frameText.setText("Found in " + class_count_confidence[1] + " frames");
        }

        return listItemView;
    }

    private int getConfidenceColor(double confidence) {
        int confidenceColorResourceId;
        int confidenceFloor = (int) Math.floor(confidence);
        switch (confidenceFloor) {
            case 0:
            case 1:
                confidenceColorResourceId = R.color.confidence1;
                break;
            case 2:
                confidenceColorResourceId = R.color.confidence2;
                break;
            case 3:
                confidenceColorResourceId = R.color.confidence3;
                break;
            case 4:
                confidenceColorResourceId = R.color.confidence4;
                break;
            case 5:
                confidenceColorResourceId = R.color.confidence5;
                break;
            case 6:
                confidenceColorResourceId = R.color.confidence6;
                break;
            case 7:
                confidenceColorResourceId = R.color.confidence7;
                break;
            case 8:
                confidenceColorResourceId = R.color.confidence8;
                break;
            case 9:
                confidenceColorResourceId = R.color.confidence9;
                break;
            default:
                confidenceColorResourceId = R.color.confidence10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), confidenceColorResourceId);
    }
}
