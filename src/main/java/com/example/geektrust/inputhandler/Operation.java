package com.example.geektrust.inputhandler;

public enum Operation implements InputExecutorI{
    BOOK {
        @Override
        public void execute(final InputRequest inputRequest){
            System.out.println(trackManager.bookTrackIfAvailable(
                    inputRequest.getVehicle(),
                    inputRequest.getInputTime().getRequestedTime()));
        }
    },
    ADDITIONAL {
        @Override
        public void execute(final InputRequest inputRequest){
            System.out.println(trackManager.addExtraTimeIfAvailable(
                    inputRequest.getVehicle().getVehicleNo(),
                    inputRequest.getInputTime().getRequestedTime()));
        }
    },
    REVENUE {
        @Override
        public void execute(final InputRequest inputRequest){
            System.out.println(trackManager.calculateRevenue());
        }
    };
}
