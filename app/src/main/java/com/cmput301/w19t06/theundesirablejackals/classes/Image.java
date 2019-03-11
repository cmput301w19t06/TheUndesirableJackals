package com.cmput301.w19t06.theundesirablejackals.classes;

/**
 * This class manages an image's file location  and the validity of the file format
 * @author Art Limbaga
 */
public class Image {
    private String fileLocation;

    /**
     * For testing purposes only
     */
    public Image() {

    }

    /**
     * Creates a new Image object of which has the photo saved at filelocation
     * @param fileLocation the location of the photo for this ImageObject
     */
    public Image(String fileLocation) {
        // add check valid format here in the constructor
        this.fileLocation = fileLocation;
    }

    /**
     * @return The file location of the image
     */
    public String getFileLocation() {
        return fileLocation;
    }


    /**
     * @param fileLocation new file location of where the image can be found
     */
    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    /**
     * @param fileLocation
     * @return false if the file format is not valid, true otherwise
     */
    public static boolean isValidFormat(String fileLocation) {
        return true;
    }
}
