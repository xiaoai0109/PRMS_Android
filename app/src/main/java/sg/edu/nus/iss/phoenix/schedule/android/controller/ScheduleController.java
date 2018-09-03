package sg.edu.nus.iss.phoenix.schedule.android.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import sg.edu.nus.iss.phoenix.core.android.controller.ControlFactory;
import sg.edu.nus.iss.phoenix.core.android.controller.MainController;
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
        Log.v(TAG, "Editing radio program: " + programSlot.getRadioProgramName() + " "
                + programSlot.getProgramSlotDate() + " " + programSlot.getProgramSlotSttime() + "...");

        Intent intent = new Intent(MainController.getApp(), MaintainScheduleScreen.class);
        Bundle b = new Bundle();
        b.putString("Name", programSlot.getRadioProgramName());
        b.putString("Date", programSlot.getProgramSlotDate());
        b.putString("Sttime", programSlot.getProgramSlotSttime());
        b.putString("Duration", programSlot.getProgramSlotDuration());
        b.putString("Presenter", programSlot.getProgramSlotPresenter());
        b.putString("Producer", programSlot.getProgramSlotProducer());
        intent.putExtras(b);

        MainController.displayScreen(intent);
    }

    public void onDisplaySchedule(MaintainScheduleScreen maintainScheduleScreen) {
        this.maintainScheduleScreen = maintainScheduleScreen;
//        if (ps2edit == null)
//            maintainScheduleScreen.createSchedule();
//        else
//            maintainScheduleScreen.editSchedule(ps2edit);
    }

    public void selectUpdateSchedule(ProgramSlot ps) {
//        new UpdateScheduleDelegate(this).execute(ps);
    }

    public void selectDeleteSchedule(ProgramSlot ps) {
//        new DeleteScheduleDelegate(this).execute(ps.getScheduleSlotName());
    }

    public void programDeleted(boolean success) {
        // Go back to ScheduleList screen with refreshed schedules.
        startUseCase();
    }

    public void programUpdated(boolean success) {
        // Go back to ScheduleList screen with refreshed schedules.
        startUseCase();
    }

    public void selectCreateSchedule(ProgramSlot ps) {
//        new CreateScheduleDelegate(this).execute(ps);
    }

    public void programCreated(boolean success) {
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
}
