package com.noavaran.system.vira.baryab.utils.uncaughtexception;

public class ExceptionInfo {
    private String exceptionDate;
    private String exceptionMessage;
    private String versionName;
    private String packageName;
    private String phoneModel;
    private String androidVersion;
    private String board;
    private String brand;
    private String device;
    private String display;
    private String fingerPrint;
    private String host;
    private String id;
    private String model;
    private String product;
    private String tags;
    private String time;
    private String type;
    private String user;
    private String totalInternalMemorySize;
    private String availableInternalMemorySize;
    private String stacktrace;
    private String cause;

    public ExceptionInfo() {}

    public ExceptionInfo(String exceptionDate, String exceptionMessage, String versionName, String packageName, String phoneModel, String androidVersion, String board, String brand, String device, String display, String fingerPrint, String host, String id, String model, String product, String tags, String time, String type, String user, String totalInternalMemorySize, String availableInternalMemorySize, String stacktrace, String cause) {
        this.exceptionDate = exceptionDate;
        this.exceptionMessage = exceptionMessage;
        this.versionName = versionName;
        this.packageName = packageName;
        this.phoneModel = phoneModel;
        this.androidVersion = androidVersion;
        this.board = board;
        this.brand = brand;
        this.device = device;
        this.display = display;
        this.fingerPrint = fingerPrint;
        this.host = host;
        this.id = id;
        this.model = model;
        this.product = product;
        this.tags = tags;
        this.time = time;
        this.type = type;
        this.user = user;
        this.totalInternalMemorySize = totalInternalMemorySize;
        this.availableInternalMemorySize = availableInternalMemorySize;
        this.stacktrace = stacktrace;
        this.cause = cause;
    }

    public String getExceptionDate() {
        return exceptionDate;
    }

    public void setExceptionDate(String exceptionDate) {
        this.exceptionDate = exceptionDate;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getFingerPrint() {
        return fingerPrint;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTotalInternalMemorySize() {
        return totalInternalMemorySize;
    }

    public void setTotalInternalMemorySize(String totalInternalMemorySize) {
        this.totalInternalMemorySize = totalInternalMemorySize;
    }

    public String getAvailableInternalMemorySize() {
        return availableInternalMemorySize;
    }

    public void setAvailableInternalMemorySize(String availableInternalMemorySize) {
        this.availableInternalMemorySize = availableInternalMemorySize;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String toString() {
        return "{" +
                    "\"exceptionDate\":" + "\"" + getExceptionDate() + "\"" + "," +
                    "\"exceptionMessage\":" + "\"" + getExceptionMessage() + "\"" + "," +
                    "\"versionName\":" + "\"" + getVersionName() + "\"" + "," +
                    "\"packageName\":" + "\"" + getPackageName() + "\"" + "," +
                    "\"phoneModel\":" + "\"" + getPhoneModel() + "\"" + "," +
                    "\"androidVersion\":" + "\"" + getAndroidVersion() + "\"" + "," +
                    "\"board\":" + "\"" + getBoard() + "\"" + "," +
                    "\"brand\":" + "\"" + getBrand() + "\"" + "," +
                    "\"device\":" + "\"" + getDevice() + "\"" + "," +
                    "\"display\":" + "\"" + getDisplay() + "\"" + "," +
                    "\"fingerPrint\":" + "\"" + getFingerPrint() + "\"" + "," +
                    "\"host\":" + "\"" + getHost() + "\"" + "," +
                    "\"id\":" + "\"" + getId() + "\"" + "," +
                    "\"model\":" + "\"" + getModel() + "\"" + "," +
                    "\"product\":" + "\"" + getProduct() + "\"" + "," +
                    "\"tags\":" + "\"" + getTags() + "\"" + "," +
                    "\"time\":" + "\"" + getTime() + "\"" + "," +
                    "\"type\":" + "\"" + getType() + "\"" + "," +
                    "\"user\":" + "\"" + getUser() + "\"" + "," +
                    "\"totalInternalMemorySize\":" + "\"" + getTotalInternalMemorySize() + "\"" + "," +
                    "\"availableInternalMemorySize\":" + "\"" + getAvailableInternalMemorySize() + "\"" + "," +
                    "\"stackTrace\":" + "\"" + getStacktrace() + "\"" + "," +
                    "\"cause\":" + "\"" + getCause() + "\"" +
               "}";
    }
}