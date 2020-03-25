package gr.uoa.di.softeng.data;

import java.util.Date;

/**
 *
 */
public class Incident {

    private Integer id;
    private String name;
    private String description;
    private Date creation_date;

    Incident() {

    }

    Incident(Integer id, String name, String description, Date creationDate) {

        setId(id);
        setName(name);
        setDescription(description);
        setCreationDate(creationDate);
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public Integer getId() {

        return id;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getDescription() {

        return description;
    }

    public void setCreationDate(Date creationDate) {

        this.creation_date = creationDate;
    }

    public Date getCreationDate() {

        return creation_date;
    }

}
