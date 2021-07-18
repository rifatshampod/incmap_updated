package pwdmap.map.incmap.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class SignUpModel {


        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("data")
        @Expose
        private Data data;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

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


        public class Data {

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("username")
            @Expose
            private String username;
            @SerializedName("fname")
            @Expose
            private String fname;
            @SerializedName("lname")
            @Expose
            private String lname;
            @SerializedName("user_type")
            @Expose
            private String userType;
            @SerializedName("social_type")
            @Expose
            private String socialType;
            @SerializedName("social_id")
            @Expose
            private Object socialId;
            @SerializedName("about")
            @Expose
            private String about;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("phone")
            @Expose
            private String phone;
            @SerializedName("birthday")
            @Expose
            private String birthday;
            @SerializedName("type_of_diabetes")
            @Expose
            private String typeOfDiabetes;
            @SerializedName("diagnosed_since")
            @Expose
            private String diagnosedSince;
            @SerializedName("cr_number")
            @Expose
            private String crNumber;
            @SerializedName("image")
            @Expose
            private String image;
            @SerializedName("address")
            @Expose
            private String address;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("mac_address")
            @Expose
            private String macAddress;
            @SerializedName("time_zone")
            @Expose
            private Object timeZone;
            @SerializedName("deleted_at")
            @Expose
            private Object deletedAt;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("updated_at")
            @Expose
            private String updatedAt;
            @SerializedName("access_token")
            @Expose
            private String accessToken;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getFname() {
                return fname;
            }

            public void setFname(String fname) {
                this.fname = fname;
            }

            public String getLname() {
                return lname;
            }

            public void setLname(String lname) {
                this.lname = lname;
            }

            public String getUserType() {
                return userType;
            }

            public void setUserType(String userType) {
                this.userType = userType;
            }

            public String getSocialType() {
                return socialType;
            }

            public void setSocialType(String socialType) {
                this.socialType = socialType;
            }

            public Object getSocialId() {
                return socialId;
            }

            public void setSocialId(Object socialId) {
                this.socialId = socialId;
            }

            public String getAbout() {
                return about;
            }

            public void setAbout(String about) {
                this.about = about;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getTypeOfDiabetes() {
                return typeOfDiabetes;
            }

            public void setTypeOfDiabetes(String typeOfDiabetes) {
                this.typeOfDiabetes = typeOfDiabetes;
            }

            public String getDiagnosedSince() {
                return diagnosedSince;
            }

            public void setDiagnosedSince(String diagnosedSince) {
                this.diagnosedSince = diagnosedSince;
            }

            public String getCrNumber() {
                return crNumber;
            }

            public void setCrNumber(String crNumber) {
                this.crNumber = crNumber;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getMacAddress() {
                return macAddress;
            }

            public void setMacAddress(String macAddress) {
                this.macAddress = macAddress;
            }

            public Object getTimeZone() {
                return timeZone;
            }

            public void setTimeZone(Object timeZone) {
                this.timeZone = timeZone;
            }

            public Object getDeletedAt() {
                return deletedAt;
            }

            public void setDeletedAt(Object deletedAt) {
                this.deletedAt = deletedAt;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public String getAccessToken() {
                return accessToken;
            }

            public void setAccessToken(String accessToken) {
                this.accessToken = accessToken;
            }

        }

    }
