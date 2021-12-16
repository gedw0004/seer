package common;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerObject {
    private enum MsgType {
        ERROR,
        WARNING,
        INFORMATION
    }

    private FileIO fileIO;
    private AppProperties appProperties;

    public LoggerObject() {
        fileIO = new FileIO();
        appProperties = new AppProperties("app.properties");
        fileIO.setFileName(appProperties.getProperty("logFile"));
    }

    public boolean logErr(String text) {
        return logMessage(MsgType.ERROR, text);
    }

    public boolean logInfo(String text) {
        return logMessage(MsgType.INFORMATION, text);
    }

    public boolean logWarn(String text) {
        return logMessage(MsgType.WARNING, text);
    }

    private boolean logMessage(MsgType type, String text) {
        boolean result = true;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTime = LocalDateTime.now().format(formatter);

        try {
            fileIO.writeFile(new String[]{dateTime + "\t" + type.name() + "\t\t" + text});
        }
        catch (IOException e) {
            result = false;
        }

        return result;
    }

    //region Set and Get Methods
    public FileIO getFileIO() {
        return fileIO;
    }

    public void setFileIO(FileIO fileIO) {
        this.fileIO = fileIO;
    }

    public AppProperties getAppProperties() {
        return appProperties;
    }

    public void setAppProperties(AppProperties appProperties) {
        this.appProperties = appProperties;
    }
    //endregion
}
