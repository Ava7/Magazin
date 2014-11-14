/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;

public class BackupDatabase {

    private static final LocalDate date = LocalDate.now();
    private static final File source = new File("Store");
    private static final File destination = new File("Backup/" + date);

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
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
    }

    private static void copyFolder(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }
            String files[] = source.list();
            for (String file : files) {
                File srcFile = new File(source, file);
                File destFile = new File(destination, file);

                copyFolder(srcFile, destFile);
            }
        } else {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new FileInputStream(source);
                out = new FileOutputStream(destination);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                in.close();
                out.close();
            }
        }
    }

}
