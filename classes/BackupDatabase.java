/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class BackupDatabase {

    private static final File source = new File("Store");
    private static final File destination = new File("Backup/Store");

    public static void directoryExists() throws IOException {
        if (destination.exists() && destination.isDirectory()) {
            deleteDirectory(destination);
            copyFolder(source, destination);
        } else {
            createDiectory();
            copyFolder(source, destination);
        }
    }

    private static void createDiectory() {
        try {
            destination.mkdir();
        } catch (Exception e) {
            System.out.println("Directory not created: " + e.getMessage());
        }
    }

    private static void deleteDirectory(File destination) {
        if (destination.exists()) {
            File[] files = destination.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
    }

    private static void copyFolder(File source, File destination) {
        try {
            FileUtils.copyDirectory(source, destination);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
