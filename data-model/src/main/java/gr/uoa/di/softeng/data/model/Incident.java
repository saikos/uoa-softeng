package gr.uoa.di.softeng.data.model;

import java.util.Date;
import java.util.List;

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
    private List<Report> reports;

    public Incident() {

        // Keep this for json encoding/decoding.
    }

    public Incident(String id, String title, String description, Date startDate, Date endDate, Double x, Double y,
                    List<Report> reports) {

        setId(id);
        setTile(title);
        setDescription(description);
        setStartDate(startDate);
        setEndDate(endDate);
        setX(x);
        setY(y);
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

    public void setReports(List<Report> reports) {

        this.reports = reports;
    }

    public List<Report> getReports() {

        return reports;
    }

}
