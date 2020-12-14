package com.hackatonapi.HackatonRest.helpers;

import com.hackatonapi.HackatonRest.entity.NamedEntity;

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
}