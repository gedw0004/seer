package common;

import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A file input/output class
 * @author   Gareth Edwards
 * @version  0.0.1
 */
public class FileIO
{
    private ArrayList<String> records;
    private String fileName;

    /**
     * Default FileIO constructor
     */
    public FileIO()
    {
        records = new ArrayList<String>();
        setFileName("");
    }

    /**
     * FileIO constructor
     * @param    pFileName   Name of the input file
     */
    public FileIO(String pFileName)
    {
        this();
        setFileName(pFileName);
    }

    /**
     * Read an input file
     * @return   long    The number of records read
     */
    public long readFile() throws IOException
    {
        FileReader fRead = new FileReader(fileName);
        Scanner fScan = new Scanner(fRead);
        long result = 0;

        while(fScan.hasNextLine())
        {
            records.add(fScan.nextLine());
            result++;
        }

        fRead.close();
        return result;
    }

    /**
     * Write a set of records to an output file
     * @param    records     An array of strings
     * @return   long        The number of records written
     */
    public long writeFile(String[] records) throws IOException
    {
        FileWriter fWrite = new FileWriter(fileName, true);
        long result = 0;

        for (int i = 0; i < records.length; i++)
        {
            fWrite.append(records[i] + "\n");
            result++;
        }

        fWrite.close();
        return result;
    }

    @Override
    public String toString()
    {
        return "\n<start object: FileIO>\n" +
                "\tfileName = " + fileName +
                "\n\trecords = " + records.toString() +
                "\n<end object: FileIO>";
    }

    //region Set and Get Methods
    /**
     * Get the file name
     * @return   String  The file name
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * Get the records object
     * @return   ArrayList<String>   the records object
     */
    public ArrayList<String> getRecords()
    {
        return records;
    }

    /**
     * Set the file name
     * @param    pFileName   The input file name
     */
    public void setFileName(String pFileName)
    {
        fileName = pFileName;
    }

    /**
     * Set the records array
     * @param records   Array of records
     */
    public void setRecords(ArrayList<String> records) {
        this.records = records;
    }
    //endregion
}
