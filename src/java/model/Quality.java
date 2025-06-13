package model;

public class Quality {
    private int qualityId;
    private String qualityName;
    private String status;

    public Quality() {}

    public Quality(int qualityId, String qualityName, String status) {
        this.qualityId = qualityId;
        this.qualityName = qualityName;
        this.status = status;
    }

    public int getQualityId() {
        return qualityId;
    }

    public void setQualityId(int qualityId) {
        this.qualityId = qualityId;
    }

    public String getQualityName() {
        return qualityName;
    }

    public void setQualityName(String qualityName) {
        this.qualityName = qualityName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Quality{" +
               "qualityId=" + qualityId +
               ", qualityName='" + qualityName + '\'' +
               ", status='" + status + '\'' +
               '}';
    }
}
