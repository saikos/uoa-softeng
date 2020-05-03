package gr.uoa.di.softeng.data.model;

/**
 *
 */
public class Report {

    private String id;
    private String name;
    private byte[] data;
    private String checksum;
    private String mimeType;

    public Report() {

        // Keep this for json encoding/decoding.
    }

    public Report(String id, String name, byte[] data, String checksum, String mimeType) {

    }

    public void setId(String id) {

        this.id = id;
    }

    public String getId() {

        return id;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void setChecksum(String checksum) {

        this.checksum = checksum;
    }

    public String getChecksum() {

        return checksum;
    }

    public void setMimeType(String mimeType) {

        this.mimeType = mimeType;
    }

    public String getMimeType() {

        return mimeType;
    }

}
