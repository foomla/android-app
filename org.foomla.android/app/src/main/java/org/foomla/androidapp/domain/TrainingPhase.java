package org.foomla.androidapp.domain;

public enum TrainingPhase {

    ARRIVAL,
    WARM_UP,
    MAIN,
    SCRIMMAGE;

    public static TrainingPhase getById(int id) {
        switch (id) {
            case 0: return ARRIVAL;
            case 1: return WARM_UP;
            case 2: return MAIN;
            case 3: return MAIN;
            case 4: return SCRIMMAGE;
            default: throw new IllegalArgumentException("Unknown training phase id:" + id);
        }
    }
}

