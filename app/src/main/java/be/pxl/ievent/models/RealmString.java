package be.pxl.ievent.models;

import io.realm.RealmObject;

/**
 * Created by 11501253 on 21/08/2017.
 */

public class RealmString extends RealmObject {
    private String name;

    public RealmString() {
    }

    public RealmString(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}