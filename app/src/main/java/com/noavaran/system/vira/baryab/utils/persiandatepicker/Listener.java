package com.noavaran.system.vira.baryab.utils.persiandatepicker;

import com.noavaran.system.vira.baryab.utils.persiandatepicker.util.PersianCalendar;

public interface Listener {
    void onDateSelected(PersianCalendar persianCalendar);

    void onDisimised();
}
