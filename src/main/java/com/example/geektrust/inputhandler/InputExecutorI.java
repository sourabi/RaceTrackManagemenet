package com.example.geektrust.inputhandler;

import com.example.geektrust.manager.TrackManager;
import com.example.geektrust.manager.TrackManagerI;

public interface InputExecutorI {
    void execute(InputRequest inputRequest);
    TrackManagerI trackManager = TrackManager.getInstance();
}