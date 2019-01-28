package io.gphotos.gin.model;

import com.google.gson.annotations.SerializedName;

public class HeartStats {
    @SerializedName("Account")
    public String account;
    @SerializedName("Battery")
    public int battery;
    @SerializedName("CameraConnected")
    public boolean cameraConnected;
    @SerializedName("CameraModel")
    public String cameraModel;
    @SerializedName("LibVersion")
    public String libVersion;
    @SerializedName("MachineID")
    public String machineId;
    @SerializedName("MemoryFree")
    public long memoryFree;
    @SerializedName("MemoryTotal")
    public long memoryTotal;
    @SerializedName("NetworkOK")
    public boolean networkOk;
    @SerializedName("StartedAt")
    public long startedAt;
    @SerializedName("StorageFree")
    public String storageFree;
    @SerializedName("StoragePercentage")
    public String storagePercentage;
    @SerializedName("StorageTotal")
    public String storageTotal;
    @SerializedName("SyncProgress")
    public int syncProgress;
    @SerializedName("SyncStatus")
    public int syncStatus;
    @SerializedName("UploadFail")
    public int uploadFail;
    @SerializedName("UploadOK")
    public int uploadOk;
    @SerializedName("UploadPending")
    public int uploadPending;
    @SerializedName("UploadQueued")
    public int uploadQueued;
    @SerializedName("UserConfig")
    public UserConfig userConfig;
    @SerializedName("Version")
    public String version;

    public static class Builder {
        private String account;
        private int battery;
        private boolean cameraConnected;
        private String cameraModel;
        private String libVersion;
        private String machineId;
        private long memoryFree;
        private long memoryTotal;
        private boolean networkOk = true;
        private long startedAt;
        private String storageFree;
        private String storagePercentage;
        private String storageTotal;
        private int syncProgress;
        private int syncStatus;
        private int uploadFail;
        private int uploadOk;
        private int uploadPending;
        private int uploadQueued;
        private UserConfig userConfig;
        private String version;

        public HeartStats build() {
            long j = this.startedAt;
            String str = this.account;
            int i = this.uploadOk;
            int i2 = this.uploadFail;
            int i3 = this.uploadPending;
            int i4 = this.uploadQueued;
            int i5 = this.battery;
            boolean z = this.cameraConnected;
            String str2 = this.cameraModel;
            String str3 = this.machineId;
            String str4 = this.version;
            String str5 = this.libVersion;
            boolean z2 = this.networkOk;
            String str6 = this.storageFree;
            boolean z3 = z2;
            String str7 = this.storageTotal;
            String str8 = this.storagePercentage;
            UserConfig userConfig = this.userConfig;
            int i6 = this.syncStatus;
            String str9 = str5;
            int i7 = this.syncProgress;
            return new HeartStats(j, str, i, i2, i3, i4, i5, z, str2, str3, str4, str9, z3, str6, str7, str8, userConfig, i6, i7, this.memoryTotal, this.memoryFree);
        }

        public Builder memoryFree(long j) {
            this.memoryFree = j;
            return this;
        }

        public Builder memoryTotal(long j) {
            this.memoryTotal = j;
            return this;
        }

        public Builder syncProgress(int i) {
            this.syncProgress = i;
            return this;
        }

        public Builder syncStatus(int i) {
            this.syncStatus = i;
            return this;
        }

        public Builder userConfig(UserConfig userConfig) {
            this.userConfig = userConfig;
            return this;
        }

        public Builder storagePercentage(String str) {
            this.storagePercentage = str;
            return this;
        }

        public Builder storageTotal(String str) {
            this.storageTotal = str;
            return this;
        }

        public Builder storageFree(String str) {
            this.storageFree = str;
            return this;
        }

        public Builder networkOk(boolean z) {
            this.networkOk = z;
            return this;
        }

        public Builder libVersion(String str) {
            this.libVersion = str;
            return this;
        }

        public Builder version(String str) {
            this.version = str;
            return this;
        }

        public Builder machineId(String str) {
            this.machineId = str;
            return this;
        }

        public Builder cameraModel(String str) {
            this.cameraModel = str;
            return this;
        }

        public Builder startedAt(long j) {
            this.startedAt = j;
            return this;
        }

        public Builder account(String str) {
            this.account = str;
            return this;
        }

        public Builder uploadOk(int i) {
            this.uploadOk = i;
            return this;
        }

        public Builder uploadFail(int i) {
            this.uploadFail = i;
            return this;
        }

        public Builder uploadPending(int i) {
            this.uploadPending = i;
            return this;
        }

        public Builder uploadQueued(int i) {
            this.uploadQueued = i;
            return this;
        }

        public Builder battery(int i) {
            this.battery = i;
            return this;
        }

        public Builder cameraConnected(boolean z) {
            this.cameraConnected = z;
            return this;
        }
    }

    public HeartStats(long j, String str, int i, int i2, int i3, int i4, int i5, boolean z, String str2, String str3, String str4, String str5, boolean z2, String str6, String str7, String str8, UserConfig userConfig, int i6, int i7, long j2, long j3) {
        this.startedAt = j;
        this.account = str;
        this.uploadFail = i2;
        this.uploadOk = i;
        this.uploadPending = i3;
        this.uploadQueued = i4;
        this.battery = i5;
        this.cameraConnected = z;
        this.cameraModel = str2;
        this.machineId = str3;
        this.version = str4;
        this.libVersion = str5;
        this.networkOk = z2;
        this.storageFree = str6;
        this.storagePercentage = str8;
        this.storageTotal = str7;
        this.userConfig = userConfig;
        this.syncProgress = i7;
        this.syncStatus = i6;
        this.memoryFree = j3;
        this.memoryTotal = j2;
    }
}
