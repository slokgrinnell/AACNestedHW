import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KVPair;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;
import java.io.PrintWriter;

public class AACMappings {
    /**
     * AACMappings
     * This class handles the mapping between image locations and AACCategories.
     * It is responsible for storing and retrieving images and categories used in an AAC device.
     *
     * @author Slok Rajbhandari
     * @author Catie Baker
     * @author Samuel A. Rebelsky
     */
  
    /**
     * Fields
     */
    AssociativeArray<String, AACCategory> categoryMappings;  // Maps category images to AACCategory objects
    AACCategory homepage;   // Represents the homepage category
    AACCategory currentCategory;  // Tracks the currently selected category
    File dataFile;   // File containing the mappings

    /**
     * Constructor
     * Initializes the AACMappings by reading from a file and creating mappings for categories and images.
     * @param filename contains the categories and their associated image file paths.
     */
    public AACMappings(String filename) {
        this.categoryMappings = new AssociativeArray<>();  // Initializing category mappings
        this.homepage = new AACCategory("");  // Initializing homepage category
        this.currentCategory = this.homepage;  // Setting current category to homepage
        this.dataFile = new File(filename);  // Initializing data file

        try {
            Scanner reader = new Scanner(new FileReader(this.dataFile));
            String categoryImage = null;
            String categoryText;
            String imagePath;
            String text;
            String currentLine;
            String[] tokens;

            // Reading the file line by line
            while (reader.hasNextLine()) {
                currentLine = reader.nextLine();
                tokens = currentLine.split(" ", 2);  // Splitting line into tokens

                if (currentLine.startsWith(">")) {
                    // Process image inside a category
                    imagePath = tokens[0].substring(1);  // Removing ">"
                    text = tokens[1];
                    try {
                        this.categoryMappings.get(categoryImage).addItem(imagePath, text);
                    } catch (KeyNotFoundException knfe) {
                        knfe.printStackTrace();
                    }
                } else {
                    // Process a new category
                    imagePath = tokens[0];  // First token is image path
                    categoryText = tokens[1];  // Second token is category text
                    try {
                        this.categoryMappings.set(imagePath, new AACCategory(imagePath)); // Setting category
                    } catch (NullKeyException nke) {
                        nke.printStackTrace();
                    }
                    this.homepage.addItem(imagePath, categoryText); // Adding category to homepage
                    categoryImage = imagePath;
                }  // end of if-else
            }  // end of while loop

            reader.close();  // Closing reader
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        }  // end of try-catch
    }  // end of constructor

    /**
     * Adds a new image and its corresponding text to the current category or to the homepage if no category is selected.
     * @param imageLoc the location of the image
     * @param text the text associated with the image
     */
    public void add(String imageLoc, String text) {
        if (this.currentCategory.equals(this.homepage)) {
            AACCategory newCategory = new AACCategory(text);  // Create new category
            try {
                this.categoryMappings.set(imageLoc, newCategory);  // Add category to mappings
            } catch (NullKeyException ne) {}
            this.homepage.addItem(imageLoc, text);  // Add category to homepage
            this.currentCategory = newCategory;  // Set current category to new category
        } else {
            this.currentCategory.addItem(imageLoc, text);  // Add item to current category
        }  // end of if-else
    }  // end of add method

    /**
     * Returns the current category's name.
     * @return the name of the current category
     */
    public String getCurrentCategory() {
        return this.currentCategory.getCategory();  // Return current category's name
    }  // end of getCurrentCategory method

    /**
     * Provides an array of all image locations in the current category.
     * @return an array of image locations
     */
    public String[] getImageLocs() {
        return this.currentCategory.getImages();  // Return all image locations in the current category
    }  // end of getImageLocs method

    /**
     * Retrieves the text associated with a selected image.
     * If the image corresponds to a category, the category is selected, and no text is spoken.
     * @param imageLoc the location of the image
     * @return the text associated with the image or category
     * @throws Exception if the image is not found
     */
    public String getText(String imageLoc) throws Exception {
        String result = this.currentCategory.getText(imageLoc);  // Get text for image location
        if (result.equals("Not Found")) {
            throw new Exception();  // Throw error if image is not found
        }
        if (this.currentCategory.equals(this.homepage)) {
            try {
                this.currentCategory = this.categoryMappings.get(imageLoc);  // Switch to category
            } catch (KeyNotFoundException kne) {
                throw new Exception();  // Throw error if category is not found
            }
        }
        return result;  // Return the associated text
    }  // end of getText method

    /**
     * Resets the current category to the homepage.
     */
    public void reset() {
        this.currentCategory = this.homepage;  // Reset to homepage
    }  // end of reset method

    /**
     * Writes the current AACMappings to a file, including all categories and their associated images and text.
     * @param filename the file to write the imageToTextMap to
     */
    public void writeToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            AACCategory currentCategory;
            // Write each category and its images to the file
            for (KVPair<String, AACCategory> categoryEntry : this.categoryMappings) {
                writer.write(categoryEntry.getKey() + " " + this.homepage.getText(categoryEntry.getKey()) + "\n");  // Write category
                currentCategory = categoryEntry.getValue();  // Get current category
                for (KVPair<String, String> imagePair : currentCategory.imageToTextMap) {
                    writer.write(">" + imagePair.getKey() + " " + imagePair.getValue() + "\n");  // Write image and text
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to file");
        }  // end of try-catch
    }  // end of writeToFile method
}  // end of AACMappings class
