package be.pxl.ievent.Models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by jessevandenberghe on 26/09/2017.
 */

public class Event extends RealmObject{
    private int id;
    private String name;
    private Long startDateTime;
    private Long endDateTime;
    private int location;
    private int organisator;
    private RealmList<RealmString> speakers;
    private int maxSubscriptions;
    private RealmList<RealmString> subscribers;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Long startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Long getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Long endDateTime) {
        this.endDateTime = endDateTime;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getOrganisator() {
        return organisator;
    }

    public void setOrganisator(int organisator) {
        this.organisator = organisator;
    }

    public RealmList<RealmString> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(RealmList<RealmString> speakers) {
        this.speakers = speakers;
    }

    public int getMaxSubscriptions() {
        return maxSubscriptions;
    }

    public void setMaxSubscriptions(int maxSubscriptions) {
        this.maxSubscriptions = maxSubscriptions;
    }

    public RealmList<RealmString> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(RealmList<RealmString> subscribers) {
        this.subscribers = subscribers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
