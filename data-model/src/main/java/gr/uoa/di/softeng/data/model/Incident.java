package gr.uoa.di.softeng.data.model;

import java.util.Date;

/**
 *
 */
public class Incident {

    private String id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private Double x;
    private Double y;

    public Incident() {

        // Keep this for json encoding/decoding.
    }

    public Incident(String id, String title, String description, Double x, Double y, Date startDate, Date endDate) {

        setId(id);
        setTile(title);
        setDescription(description);
        setX(x);
        setY(y);
        setStartDate(startDate);
        setEndDate(endDate);
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getId() {

        return id;
    }

    public void setTile(String title) {

        this.title = title;
    }

    public String getTitle() {

        return title;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getDescription() {

        return description;
    }

    public void setX(Double x) {

        this.x = x;
    }

    public Double getX() {

        return x;
    }

    public void setY(Double y) {

        this.y = y;
    }

    public Double getY() {

        return y;
    }

    public void setStartDate(Date startDate) {

        this.startDate = startDate;
    }

    public Date getStartDate() {

        return startDate;
    }

    public void setEndDate(Date endDate) {

        this.endDate = endDate;
    }

    public Date getEndDate() {

        return endDate;
    }

}
