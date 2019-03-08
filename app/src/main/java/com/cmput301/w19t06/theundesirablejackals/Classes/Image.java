package com.cmput301.w19t06.theundesirablejackals.Classes;

public class Image {
    private String fileLocation;

    public Image() {

    }

    public Image(String fileLocation) {
        // add check valid format here in the constructor
        this.fileLocation = fileLocation;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public static boolean isValidFormat(String fileLocation) {
        return true;
    }
}
