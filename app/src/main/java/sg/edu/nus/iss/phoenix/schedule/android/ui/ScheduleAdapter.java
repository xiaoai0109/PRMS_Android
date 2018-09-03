package sg.edu.nus.iss.phoenix.schedule.android.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.nus.iss.phoenix.R;
import sg.edu.nus.iss.phoenix.radioprogram.entity.RadioProgram;
import sg.edu.nus.iss.phoenix.schedule.entity.ProgramSlot;

import static android.content.ContentValues.TAG;

/**
 * Created by mia on 2/9/18.
 */

public class ScheduleAdapter extends ArrayAdapter<ProgramSlot> {

    public ScheduleAdapter(@NonNull Context context, ArrayList<ProgramSlot> programSlots) {
        super(context, 0, programSlots);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
//            listItemView = LayoutInflater.from(getContext()).inflate(
//                    R.layout.activity_radio_program, parent, false);
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.row_schedule, parent, false);
        }
        //    Word currentWord = getItem(position);
        ProgramSlot currentPS = getItem(position);

        TextView scheduleProgramName = (TextView)listItemView.findViewById(R.id.maintain_schedule_program_name_text_view);
        scheduleProgramName.setText(currentPS.getRadioProgramName(), TextView.BufferType.NORMAL);
        scheduleProgramName.setKeyListener(null); // This disables editing.

        TextView scheduleDate = (TextView)listItemView.findViewById(R.id.maintain_schedule_date_text_view);
        scheduleDate.setText(currentPS.getProgramSlotDate(), TextView.BufferType.NORMAL);
        scheduleDate.setKeyListener(null);

        TextView scheduleSttime = (TextView)listItemView.findViewById(R.id.maintain_schedule_sttime_text_view);
        scheduleSttime.setText(currentPS.getProgramSlotSttime(), TextView.BufferType.NORMAL);
        scheduleSttime.setKeyListener(null);

        TextView scheduleDuration = (TextView)listItemView.findViewById(R.id.maintain_schedule_duration_text_view);
        scheduleDuration.setText(currentPS.getProgramSlotDuration(), TextView.BufferType.NORMAL);
        scheduleDuration.setKeyListener(null);

        TextView schedulePresenter = (TextView)listItemView.findViewById(R.id.maintain_schedule_presenter_text_view);
        schedulePresenter.setText(currentPS.getProgramSlotPresenter(), TextView.BufferType.NORMAL);
        schedulePresenter.setKeyListener(null);

        TextView scheduleProducer = (TextView)listItemView.findViewById(R.id.maintain_schedule_producer_text_view);
        scheduleProducer.setText(currentPS.getProgramSlotProducer(), TextView.BufferType.NORMAL);
        scheduleProducer.setKeyListener(null);

        Log.v(TAG, "Listing program slot: " + currentPS.getRadioProgramName() + " " +
                currentPS.getProgramSlotSttime() + " " +
                currentPS.getProgramSlotSttime() + "...");

        return listItemView;
    }
}
