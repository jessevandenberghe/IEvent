package be.pxl.ievent.models;

import be.pxl.ievent.models.apiResponses.Location;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jessevandenberghe on 26/09/2017.
 */

public class Event extends RealmObject{
    @PrimaryKey
    private int id;
    private String name;
    private String category;
    private Long startDateTime;
    private Long endDateTime;
    private Location location;
    private String LocationName;
    private String organisator;
    private RealmList<RealmString> speakers;
    private int maxSubscriptions;
    private RealmList<RealmString> subscribers;
    private String description;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getOrganisator() {
        return organisator;
    }

    public void setOrganisator(String organisator) {
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

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }
}
