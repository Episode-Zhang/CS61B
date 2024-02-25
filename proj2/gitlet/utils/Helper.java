package gitlet.utils;


import gitlet.entity.Blob;

import java.io.File;

/**
 * Helper class, including some public methods shared between multiple classes.
 *
 * @author jeffrey-zhang
 * @version 1.0
 */
public class Helper {

    /** Customized exception, will be used in Blob class for a blob's corresponding file doesn't exist.
     * @see #getFileByPath(String) */
    public static class FileDoesNotExistException extends Exception {
        public FileDoesNotExistException(String msg) {
            super(msg);
        }
    }


    /** Root directory of the working directory. */
    public static final String ROOT_DIR = System.getProperty("user.dir");

    /** Directory of the repository, using relative path. */
    public static final String REPO_DIR = ".gitlet";


    /**
     * Check if the repository has been initialized.
     *
     * @return true if the repository has been initialized, otherwise, false.
     */
    public static boolean repoInitialized() {
        return Utils.join(ROOT_DIR, REPO_DIR).exists();
    }

    /**
     * Get the File object corresponding to the given file's relative path.
     * If the file doesn't exist, print indicated message.
     *
     * @param relativePath the relative path of the file.
     * @return File object.
     * @throws FileDoesNotExistException if the file does not exist.
     */
    public static File getFileByPath(String relativePath) throws FileDoesNotExistException {
        final File file = Utils.join(Helper.ROOT_DIR, relativePath);
        final boolean fileExists = file.exists();
        if (!fileExists) {
            // This exception should be caught and handled by caller.
            // Finally, in the user level, any input from user should not cause any exception.
            throw new FileDoesNotExistException("file doesn't exist, please check the given path.");
        }
        return file;
    }

    /**
     * Print the content of the blob associated with the given path.
     *
     * @param path path of the blob file.
     */
    public static void printBlobContent(String path) {
        File blobFile = Utils.join(ROOT_DIR, path);
        if (!blobFile.exists()) {
            System.out.printf("blob does not exist! Please check the given path: %s.%n", path);
            return;
        }
        Blob blob = Utils.readObject(blobFile, Blob.class);
        System.out.println(blob);
    }

    /**
     * Print the content of the given blob.
     *
     * @param blob The blob object.
     */
    public static void printBlobContent(Blob blob) {
        System.out.println(blob);
    }
}
