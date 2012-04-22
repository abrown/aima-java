package aima.core.learning.data;

import java.io.*;

/**
 * Provides access to the learning/data/ directory for loading *.csv files using
 * class.getResourceAsStream(). Also implements caching for DataSet.loadFrom().
 *
 * @author Andrew Brown
 */
public class DataResource {

    /**
     * Return temporary file path for this filename
     *
     * @param filename
     * @return
     */
    public static File getFileFrom(String filename) {
        return new File(System.getProperty("java.io.tmpdir"), filename);
    }

    /**
     * Check whether the given temporary file exists
     *
     * @param filename
     * @return
     */
    public static boolean isCached(String filename) {
        return DataResource.getFileFrom(filename).exists();
    }

    /**
     * Return a stream for the given file name
     *
     * @param filename
     * @return
     */
    public static InputStream get(String filename) {
        File f = DataResource.getFileFrom(filename);
        try {
            return new FileInputStream(f);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find " + f);
        }
    }

    /**
     * Save file to the system's temporary directory
     *
     * @param filename
     * @param stream
     * @return number of lines saved
     */
    public static int put(String filename, InputStream stream) {
        try {
            File f = DataResource.getFileFrom(filename);
            if( !f.exists() ){
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
                lineCount++;
            }
            reader.close();
            writer.close();
            return lineCount;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
