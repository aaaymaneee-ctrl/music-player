package com.aymane;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class SongLibrary {
    
    public ObservableList<Song> getSongs(Stage stage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Music Folder");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File selectedDirectory = directoryChooser.showDialog(stage);
        
        ObservableList<Song> songs = FXCollections.observableArrayList();
        
        if (selectedDirectory != null) {
            System.out.println("Selected folder: " + selectedDirectory.getAbsolutePath());
            
            File[] files = selectedDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && isAudioFile(file.getName())) {
                        songs.add(new Song(file));
                    }
                }
            }
            System.out.println("Found " + songs.size() + " songs");
        } else {
            System.out.println("Selection canceled.");
        }
        
        return songs;
    }
    
    private boolean isAudioFile(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".mp3") || 
               lower.endsWith(".wav") || 
               lower.endsWith(".flac") || 
               lower.endsWith(".m4a") ||
               lower.endsWith(".ogg");
    }
}