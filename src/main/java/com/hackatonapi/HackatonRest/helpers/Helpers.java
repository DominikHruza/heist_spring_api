package com.hackatonapi.HackatonRest.helpers;

import com.hackatonapi.HackatonRest.entity.NamedEntity;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Helpers {

    private Helpers() {
    }

    public static boolean checkForDuplicates(List list){
        final Set set = new HashSet();

        for (var listItem : list) {
            if(!set.add(listItem)){
                return true;
            }
        }

        return false;
    }

    public static boolean checkIfExistsInList(List list, String searchTerm){
        return list.stream().anyMatch(o -> {
            if (!(o instanceof NamedEntity)) {
                return false;
            }

            return ((NamedEntity) o).getName().equals(searchTerm);
        });
    }

    public static boolean isWithinRange(
            ZonedDateTime dateTime,
            ZonedDateTime start,
            ZonedDateTime end){
        boolean b = !(dateTime.isBefore(start) || dateTime.isAfter(end));
        return b;
    }

    public static DateTime zonedDateTimeToDateTime(final ZonedDateTime zdt) {
        return new DateTime(zdt.toInstant().toEpochMilli(), DateTimeZone.forID(zdt.getOffset().getId()));
    }
}