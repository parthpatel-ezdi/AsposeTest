/**
 * 
 */
package com.ezdi.aspose;

import java.io.File;

/**
 * @author parth.m
 *
 */
public class Util {
	
	public static String getDataDir() {
        File dir = new File("/home/local/EZDI/parth.m/Desktop/AsposeTest/");
            if (dir.isDirectory() == false)
                dir.mkdir();
        System.out.println("Using data directory: " + dir.toString());
        return dir.toString() + File.separator;
    }

}
