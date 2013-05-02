package com.abstractthis.consoul;

//The MIT License (MIT)
//
//Copyright (c) 2013 David Smith <www.abstractthis.com>
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in
//all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//THE SOFTWARE.

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

abstract class ConsoleUtil {
	private static final int COPY_BUFFER_SIZE = 1024 * 8;
	
	public static void copyDirectory(File fromDir, File toDir) {
		boolean success = doDirCopy(fromDir, toDir);
		if( !success ) {
			throw new RuntimeException("Failed to explode application.");
		}
	}
	
	public static void copyDirectory(String fromDirPath, String toDirPath) {
		copyDirectory(new File(fromDirPath), new File(toDirPath));
	}
	
	public static void copyDirectory(URL fromDirURL, URL toDirURL) {
		URI fromDirURI = null;
		URI toDirURI = null;
		try {
			fromDirURI = fromDirURL.toURI();
			toDirURI = toDirURL.toURI();
		}
		catch(URISyntaxException e) {
			throw new RuntimeException("Failed to copy directory.");
		}
		copyDirectory(new File(fromDirURI), new File(toDirURI));
	}
	
	private static boolean doDirCopy(File from, File to) {
		if( !from.exists() || !from.isDirectory() ) return false;
		if( to.exists() ) return false;
		boolean mkToDir = to.mkdir();
		if( !mkToDir ) return false;
		byte[] fileCopyBuffer = new byte[COPY_BUFFER_SIZE];
		// Get files contained in the directory we're copying
		String[] filesInDir = from.list();
		if( filesInDir != null ) {
			for(String fileName : filesInDir) {
				File toFile = new File(to, fileName);
				File fromFile = new File(from, fileName);
				boolean fileCopySuccess = copyFile(fromFile, toFile, fileCopyBuffer);
				if( !fileCopySuccess ) return false;
			}
		}
		return true;
	}
	
	private static boolean copyFile(File from, File to, byte[] buffer) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			FileInputStream fis = new FileInputStream(from);
			FileOutputStream fos = new FileOutputStream(to);
			bis = new BufferedInputStream(fis);
			bos = new BufferedOutputStream(fos);
			for(int read = bis.read(buffer); read != -1; read = bis.read(buffer)) {
				bos.write(buffer);
			}
			bos.flush();
			fos.getFD().sync();
		}
		catch(IOException ioe) {
			return false;
		}
		finally {
			if( bis != null ) {
				try { bis.close(); }
				catch(IOException ignored) {}
			}
			if( bos != null ) {
				try{ bos.close(); }
				catch(IOException ignored) {}
			}
		}
		return true;
	}

}
