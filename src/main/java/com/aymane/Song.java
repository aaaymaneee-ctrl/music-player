package com.aymane;

import java.io.File;

public class Song {
    private String title;
    private String filePath;
    private File file;
    private long fileSize;
    
    public Song(File file) {
        this.file = file;
        this.filePath = file.getAbsolutePath();
        this.fileSize = file.length();
        
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            this.title = fileName.substring(0, dotIndex);
        } else {
            this.title = fileName;
        }
    }
    
    public String getTitle() { return title; }
    public String getFilePath() { return filePath; }
    public File getFile() { return file; }
    public long getFileSize() { return fileSize; }
    
    public String getFormattedSize() {
        if (fileSize < 1024) return fileSize + " B";
        if (fileSize < 1024 * 1024) return String.format("%.1f KB", fileSize / 1024.0);
        return String.format("%.1f MB", fileSize / (1024.0 * 1024.0));
    }
    
    @Override
    public String toString() {
        return title;
    }
}