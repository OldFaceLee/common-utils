package com.ai.commonUtils.unzipUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author: lixuejun
 * @date: Create in 2019/11/28 下午3:01
 * @description:
 */
public class UnzipUtils {

    public static void unZip(String srcZipFilePath, String destDir) {

        try {
            ZipInputStream Zin = new ZipInputStream(new FileInputStream(
                    srcZipFilePath));
            BufferedInputStream bInput = new BufferedInputStream(Zin);
            File file = null;
            ZipEntry entry;
            try {
                while ((entry = Zin.getNextEntry()) != null
                        && !entry.isDirectory()) {
                    file = new File(destDir, entry.getName());
                    if (!file.exists()) {
                        (new File(file.getParent())).mkdirs();
                    }
                    FileOutputStream out = new FileOutputStream(file);
                    BufferedOutputStream bOutput = new BufferedOutputStream(out);
                    int b;
                    while ((b = bInput.read()) != -1) {
                        bOutput.write(b);
                    }
                    bOutput.close();
                    out.close();

                }
                bInput.close();
                Zin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
