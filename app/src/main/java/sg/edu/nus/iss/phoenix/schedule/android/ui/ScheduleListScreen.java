package sg.edu.nus.iss.phoenix.schedule.android.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.phoenix.R;
import sg.edu.nus.iss.phoenix.core.android.controller.ControlFactory;
import sg.edu.nus.iss.phoenix.schedule.android.ui.ScheduleListScreen;
import sg.edu.nus.iss.phoenix.schedule.android.ui.ScheduleAdapter;
import sg.edu.nus.iss.phoenix.schedule.entity.ProgramSlot;

/**
 * Created by mia on 2/9/18.
 */

public class ScheduleListScreen extends AppCompatActivity {
    // Tag for logging
    private static final String TAG = ScheduleListScreen.class.getName();

    private ListView mListView;
    private ScheduleAdapter mPSAdapter;
    private ProgramSlot selectedPS = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        // mRPNameEditText = (EditText) findViewById(R.id.maintain_program_name_text_view);
        // mRPDescEditText = (EditText) findViewById(R.id.maintain_program_desc_text_view);
        // mDurationEditText = (EditText) findViewById(R.id.maintain_program_duration_text_view);

        ArrayList<ProgramSlot> programSlots = new ArrayList<ProgramSlot>();
        mPSAdapter = new ScheduleAdapter(this, programSlots);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControlFactory.getScheduleController().selectCreateSchedule();
            }
        });

        mListView = (ListView) findViewById(R.id.schedule_list);
        mListView.setAdapter(mPSAdapter);

        // Setup the item selection listener
        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // Log.v(TAG, "Radio program at position " + position + " selected.");
                ProgramSlot ps = (ProgramSlot) adapterView.getItemAtPosition(position);
                // Log.v(TAG, "Radio program name is " + rp.getRadioProgramName());
                selectedPS = ps;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // your stuff
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setSelection(0);
        Log.e("ScheduleListScreen", "onDisplayScheduleList");
        ControlFactory.getScheduleController().onDisplayScheduleList(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "View" menu option
            case R.id.action_view:
                if (selectedPS == null) {
                    // Prompt for the selection of a radio program.
                    Toast.makeText(this, "Select a program slot first! Use arrow keys on emulator", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "There is no selected program slot.");
                }
                else {
                    Log.d(TAG, "Viewing program slot: " + selectedPS.getId() + " " +
                            selectedPS.getRadioProgramName() + " " +
                            selectedPS.getProgramSlotSttime() + " " +
                            selectedPS.getProgramSlotDuration() + "...");
                    ControlFactory.getScheduleController().selectEditSchedule(selectedPS);
                }
                return true;
            case R.id.action_copy:
                if (selectedPS == null) {
                    // Prompt for the selection of a radio program.
                    Toast.makeText(this, "Select a program slot first! Use arrow keys on emulator", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "There is no selected program slot.");
                }
                else {
                    selectedPS.setId(null);
                    selectedPS.setProgramSlotDate(null);
                    selectedPS.setProgramSlotSttime(null);
                    Log.d(TAG, "Copying program slot: " + "id: " + selectedPS.getId() + " " +
                            selectedPS.getRadioProgramName() + " " +
                            selectedPS.getProgramSlotDate() + " " +
                            selectedPS.getProgramSlotSttime() + " " +
                            selectedPS.getProgramSlotPresenter() + "...");
                    ControlFactory.getScheduleController().selectCopySchedule(selectedPS);
                }
                return true;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        ControlFactory.getProgramController().maintainedProgram();
    }

    public void showSchedules(List<ProgramSlot> programSlots) {
        mPSAdapter.clear();
        for (int i = 0; i < programSlots.size(); i++) {
            mPSAdapter.add(programSlots.get(i));
        }
    }
}
