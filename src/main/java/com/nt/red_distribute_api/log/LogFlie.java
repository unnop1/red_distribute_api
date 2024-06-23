package com.nt.red_distribute_api.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogFlie {

	public static void logMessage(String className, String path, String subFolder, String methodName, String messageLog) {
        Logger logger = Logger.getLogger(className);

        try {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("MMyyyy");
            String pathLog = "/data/" + path + "/" + subFolder + "/" + df.format(date);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String fileName = dateFormat.format(date) + ".txt";
            File file = new File(pathLog + "/" + fileName);

            // Ensure directory exists, create if it doesn't
            File dir = new File(pathLog);
            if (!dir.exists()) {
                // Check if we can write to the parent directory
                File parentDir = dir.getParentFile();
                if (parentDir != null && parentDir.canWrite()) {
                    boolean created = dir.mkdirs();
                    if (!created) {
                        throw new IOException("Failed to create directory: " + pathLog);
                    }
                } else if (parentDir != null && !parentDir.canWrite()) {
                    // Attempt to set write permissions for the parent directory
                    if (!parentDir.setWritable(true)) {
                        throw new IOException("Cannot set write permissions for parent directory: " + parentDir);
                    }
                    boolean created = dir.mkdirs();
                    if (!created) {
                        throw new IOException("Failed to create directory: " + pathLog);
                    }
                } else {
                    throw new IOException("Cannot access parent directory: " + parentDir);
                }
            } else if (!dir.canWrite()) {
                // Attempt to set write permissions for the directory
                if (!dir.setWritable(true)) {
                    throw new IOException("Cannot set write permissions for directory: " + dir);
                }
            }

            // Create the log file if it doesn't exist
            if (!file.exists()) {
                boolean fileCreated = file.createNewFile();
                if (!fileCreated) {
                    throw new IOException("Failed to create log file: " + file.getAbsolutePath());
                }
            } else if (!file.canWrite()) {
                // Attempt to set write permissions for the log file
                if (!file.setWritable(true)) {
                    throw new IOException("Cannot set write permissions for log file: " + file);
                }
            }

            // Configure FileHandler
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathLog + "/" +fileName, true))){
			writer.write(messageLog);
			writer.newLine();
			}
			catch(IOException ex){
			ex.printStackTrace();
			}

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error creating directory or file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("General error: " + e.getMessage());
        }
    }

}