package com.cmput301.w19t06.theundesirablejackals;

public class Image {
    private String fileLocation;

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
}
