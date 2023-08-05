package capers;

import java.io.File;
import java.io.IOException;
import static capers.Dog.DOG_FOLDER;

/** A repository for Capers 
 * @author Episode
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = Utils.join(CWD, ".capers");

    /** Story folder. */
    static final File STORY_FOLDER = Utils.join(CAPERS_FOLDER, "story");

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */
    public static void setupPersistence() {
        // Create directories based on the existence of these relative folders.
        // ! This method only responsible for directory creation,
        // ! which means that each single file's existence should be checked by writeStory or makeDog file.
        if (!CAPERS_FOLDER.exists()) {
            CAPERS_FOLDER.mkdir();
        }
        if (!DOG_FOLDER.exists()) {
            DOG_FOLDER.mkdir();
        }
        if (!STORY_FOLDER.exists()) {
            STORY_FOLDER.mkdir();
        }
    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) throws IOException {
        // check if story file exists
        final String storyFileName = "story.txt";
        File storyFile = Utils.join(STORY_FOLDER, storyFileName);
        if (!storyFile.exists()) {
            storyFile.createNewFile();
        }
        StringBuilder sb = new StringBuilder();
        // concatenate stories
        sb.append(Utils.readContentsAsString(storyFile))
          .append(text)
          .append('\n');
        String story = sb.toString();
        Utils.writeContents(storyFile, story);
        System.out.println(story);
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) throws IOException {
        Dog dog = new Dog(name, breed, age);
        dog.saveDog();
        System.out.println(dog);
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) throws IOException {
        Dog dog = Dog.fromFile(name);
        dog.haveBirthday();
        dog.saveDog();
    }
}
