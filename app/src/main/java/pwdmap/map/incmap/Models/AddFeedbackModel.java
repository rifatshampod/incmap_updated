package pwdmap.map.incmap.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddFeedbackModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data {

        @SerializedName("location_id")
        @Expose
        private String locationId;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("descriptionss")
        @Expose
        private String descriptionss;
        @SerializedName("descriptionsss")
        @Expose
        private String descriptionsss;

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescriptionss() {
            return descriptionss;
        }

        public void setDescriptionss(String descriptionss) {
            this.descriptionss = descriptionss;
        }

        public String getDescriptionsss() {
            return descriptionsss;
        }

        public void setDescriptionsss(String descriptionsss) {
            this.descriptionsss = descriptionsss;
        }

    }
}