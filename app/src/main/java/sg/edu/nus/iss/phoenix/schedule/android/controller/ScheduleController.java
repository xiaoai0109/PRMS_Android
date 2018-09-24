package sg.edu.nus.iss.phoenix.schedule.android.controller;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import sg.edu.nus.iss.phoenix.core.android.controller.ControlFactory;
import sg.edu.nus.iss.phoenix.core.android.controller.MainController;
import sg.edu.nus.iss.phoenix.radioprogram.entity.RadioProgram;
import sg.edu.nus.iss.phoenix.schedule.android.delegate.CreateScheduleDelegate;
import sg.edu.nus.iss.phoenix.schedule.android.delegate.DeleteScheduleDelegate;
import sg.edu.nus.iss.phoenix.schedule.android.delegate.RetrieveSchedulesDelegate;
import sg.edu.nus.iss.phoenix.schedule.android.delegate.UpdateScheduleDelegate;
import sg.edu.nus.iss.phoenix.schedule.android.ui.MaintainScheduleScreen;
import sg.edu.nus.iss.phoenix.schedule.android.ui.ScheduleListScreen;
import sg.edu.nus.iss.phoenix.schedule.android.ui.MaintainScheduleScreen;
import sg.edu.nus.iss.phoenix.schedule.android.ui.ScheduleListScreen;
import sg.edu.nus.iss.phoenix.schedule.entity.ProgramSlot;

/**
 * Created by mia on 2/9/18.
 */

public class ScheduleController {
    // Tag for logging.
    private static final String TAG = ScheduleController.class.getName();

    private ScheduleListScreen scheduleListScreen;
    private MaintainScheduleScreen maintainScheduleScreen;
    private ProgramSlot ps2edit = null;

    public void startUseCase() {
        ps2edit = null;
        Intent intent = new Intent(MainController.getApp(), ScheduleListScreen.class);
        MainController.displayScreen(intent);
    }

    public void onDisplayScheduleList(ScheduleListScreen scheduleListScreen) {
        Log.e("ScheduleController", "onDisplayScheduleList");
        this.scheduleListScreen = scheduleListScreen;
        new RetrieveSchedulesDelegate(this).execute("all");
    }

    public void schedulesRetrieved(List<ProgramSlot> programSlots) {
        scheduleListScreen.showSchedules(programSlots);
    }

    public void selectCreateSchedule() {
        ps2edit = null;
        Intent intent = new Intent(MainController.getApp(), MaintainScheduleScreen.class);
        MainController.displayScreen(intent);
    }

    public void selectEditSchedule(ProgramSlot programSlot) {
        ps2edit = programSlot;
        Log.d(TAG, "Editing program slot: " + programSlot.getRadioProgramName() + " "
                + programSlot.getProgramSlotDate() + " " + programSlot.getProgramSlotSttime() + "...");

        Intent intent = new Intent(MainController.getApp(), MaintainScheduleScreen.class);
        Bundle b = new Bundle();
//        b.putString("Id", programSlot.getId());
//        b.putString("Rpname", programSlot.getRadioProgramName());
//        b.putString("Date", programSlot.getProgramSlotDate());
//        b.putString("Sttime", programSlot.getProgramSlotSttime());
//        b.putString("Duration", programSlot.getProgramSlotDuration());
//        b.putString("Presenter", programSlot.getProgramSlotPresenter());
//        b.putString("Producer", programSlot.getProgramSlotProducer());
//        intent.putExtras(b);

        MainController.displayScreen(intent);
    }

    public void selectCopySchedule(ProgramSlot programSlot) {
        ps2edit = programSlot;
        ps2edit.setId(null);
        ps2edit.setProgramSlotDate(null);
        ps2edit.setProgramSlotSttime(null);
        Log.d(TAG, "Copying program slot 2: " + "id: " + programSlot.getId() + " "
                + programSlot.getRadioProgramName() + " "
                + programSlot.getProgramSlotDate() + " "
                + programSlot.getProgramSlotSttime() + " "
                + programSlot.getProgramSlotPresenter() + "...");

        Intent intent = new Intent(MainController.getApp(), MaintainScheduleScreen.class);
//        Bundle b = new Bundle();
//        b.putString("Rpname", programSlot.getRadioProgramName());
//        b.putString("Duration", programSlot.getProgramSlotDuration());
//        b.putString("Presenter", programSlot.getProgramSlotPresenter());
//        b.putString("Producer", programSlot.getProgramSlotProducer());
//        intent.putExtras(b);

        MainController.displayScreen(intent);
    }

    public void onDisplaySchedule(MaintainScheduleScreen maintainScheduleScreen) {
        this.maintainScheduleScreen = maintainScheduleScreen;
        if (ps2edit == null)
            maintainScheduleScreen.createSchedule();
        else if (ps2edit.getId() == null) {
            Log.d(TAG, "I am displaying copy" + "id:" + ps2edit.getId());
            maintainScheduleScreen.copySchedule(ps2edit);
        }
        else {
            Log.d(TAG, "I am displaying edit" + "id:" + ps2edit.getId());
            maintainScheduleScreen.editSchedule(ps2edit);
        }
    }

    public void selectUpdateSchedule(ProgramSlot ps) {
        Log.d(TAG, "selectUpdateSchedule new program slot at " + ps.getId() + " " +
                ps.getRadioProgramName() + " " +
                ps.getProgramSlotDate() + " " +
                ps.getProgramSlotSttime() + " " +
                ps.getProgramSlotDuration() + " " +
                ps.getProgramSlotPresenter() + "...");
//        Log.d(TAG, "selectUpdateSchedule old program slot at " + oldPs.getRadioProgramName() + " " +
//                oldPs.getProgramSlotDate() + " " +
//                oldPs.getProgramSlotSttime() + " " +
//                oldPs.getProgramSlotDuration() + " " +
//                oldPs.getProgramSlotPresenter() + "...");
        new UpdateScheduleDelegate(this).execute(ps);
    }

    public void selectDeleteSchedule(ProgramSlot ps) {
        new DeleteScheduleDelegate(this).execute(ps.getId());
    }

    public void scheduleDeleted(boolean success) {
        // Go back to ScheduleList screen with refreshed schedules.
        Log.v(TAG, "scheduleDeleted success: " + success);
        if (success){
            startUseCase();
        }
        else {
            Log.v(TAG, "scheduleDeleted false");
            maintainScheduleScreen.deletionWarning();
        }
    }

    public void scheduleUpdated(boolean success) {
        // Go back to ScheduleList screen with refreshed schedules.
        startUseCase();
    }

    public void selectCreateSchedule(ProgramSlot ps) {
        new CreateScheduleDelegate(this).execute(ps);
    }

    public void scheduleCreated(boolean success) {
        // Go back to ScheduleList screen with refreshed schedules.
        startUseCase();
    }

    public void selectCancelCreateEditSchedule() {
        // Go back to ScheduleList screen with refreshed programs.
        startUseCase();
    }

    public void maintainedSchedule() {
        ControlFactory.getMainController().maintainedSchedule();
    }

//    public void selectedProgram(RadioProgram rpSelected, ProgramSlot ps) {
//        ps.setRadioProgramName(rpSelected.getRadioProgramName());
//        selectEditSchedule(ps);
//    }

    public void selectedProgram(RadioProgram rpSelected) {
        ps2edit.setRadioProgramName(rpSelected.getRadioProgramName());
//        ps2edit.setProgramSlotDuration(rpSelected.getRadioProgramDuration());
        Log.v(TAG, "ps2edit: " + ps2edit.getId() + ps2edit.getRadioProgramName());

        Intent intent = new Intent(MainController.getApp(), MaintainScheduleScreen.class);
        MainController.displayScreen(intent);
    }

    public void setTempProgramSlot(ProgramSlot tmpPs) {
        ps2edit = tmpPs;
        Log.d(TAG, "tmp ps2edit" + ps2edit.getProgramSlotDate() + ps2edit.getRadioProgramName() + ps2edit.getProgramSlotPresenter());
    }
}
