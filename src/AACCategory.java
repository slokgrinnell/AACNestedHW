import java.util.NoSuchElementException;

import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;
import edu.grinnell.csc207.util.KVPair;

/**
 * Represents the mappings for a single category of items that should
 * be displayed
 * 
 * @author Catie Baker & Slok Rajbhandari
 *
 */
public class AACCategory implements AACPage {
    /**
     * Fields
     */
    String categoryName;   // The name of the category
    AssociativeArray<String, String> imageToTextMap;   // Maps image locations to associated text

    /**
     * Constructor
     * Creates a new empty category with the given name.
     * @param name the name of the category
     */
    public AACCategory(String name) {
        this.categoryName = name;  // Set the category name
        this.imageToTextMap = new AssociativeArray<>();  // Initialize the image to text map
    }  // end of constructor

    /**
     * Adds the mapping of the image location to the text of the category.
     * @param imageLoc the image location
     * @param text the text associated with the image
     */
    public void addItem(String imageLoc, String text) {
        try {
            this.imageToTextMap.set(imageLoc, text);  // Add the image location and text to the map
        } catch (NullKeyException nke) {
            System.err.println("Please enter a valid image location.");  // Handle invalid image location
        }
    }  // end of addItem method

    /**
     * Returns the name of the category.
     * @return the name of the category
     */
    public String getCategory() {
        return this.categoryName;  // Return the category name
    }  // end of getCategory method

    /**
     * Returns an array of all the images in the category.
     * @return the image locations as an array
     */
    public String[] getImages() {
        String images = "";  // Initialize an empty string to store image locations
        for (KVPair<String, String> pair : this.imageToTextMap) {
            images += pair.getKey() + "\n";  // Add image locations to the string
        }
        return images.split("\n");  // Return image locations as an array
    }  // end of getImages method

    /**
     * Returns an array of all the images in the category.
     * @return the image locations as an array
     */
    @Override
    public String[] getImageLocs() {
        String[] imageLocs = new String[this.imageToTextMap.size()];  // Initialize array to hold image locations
        int index = 0;  // Index for the array
        for (KVPair<String, String> pair : this.imageToTextMap) {
            imageLocs[index] = pair.getKey();  // Add each image location (key) to the array
            index++;  // Increment the index
        }
        return imageLocs;  // Return the array of image locations
    }  // end of getImageLocs method

    /**
     * Returns the text associated with a given image location.
     * @param imageLoc the image location
     * @return the text associated with the image
     */
    public String getText(String imageLoc) {
        try {
            return this.imageToTextMap.get(imageLoc);  // Retrieve the text for the image location
        } catch (KeyNotFoundException kne) {
            return "Not Found.";  // Handle case where image is not found
        }  // end of try-catch
    }  // end of getText method

    /**
     * Checks if the category contains the specified image.
     * @param imageLoc the image location
     * @return true if the image exists in the category, false otherwise
     */
    public boolean hasImage(String imageLoc) {
        return this.imageToTextMap.hasKey(imageLoc);  // Check if the image exists in the category
    }  // end of hasImage method

    /**
     * Implements the select method for AACPage interface.
     * Returns the text associated with a given image location.
     * If the image is not found, throws NoSuchElementException.
     * @param imageLoc the image location
     * @return the text associated with the image
     * @throws NoSuchElementException if the image is not found
     */
    @Override
    public String select(String imageLoc) {
        String text = this.getText(imageLoc);  // Get the text associated with the image location
        if (text.equals("Not Found.")) {
            throw new NoSuchElementException("Image not found in the category.");  // Handle image not found
        }
        return text;  // Return the text associated with the image
    }  // end of select method
}  // end of AACCategory class
