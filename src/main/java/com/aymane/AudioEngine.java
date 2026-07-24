package com.aymane;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class AudioEngine {
    private MediaPlayer mediaPlayer;
    private Song currentSong;
    private boolean isPlaying = false;
    
    // Default values
    private static final double DEFAULT_SPEED = 1.0;
    private static final double MIN_SPEED = 0.5;
    private static final double MAX_SPEED = 2.0;
    
    public AudioEngine() {
    }
    
    public void play(Song song) {
        if (song == null) {
            System.out.println("Cannot play null song");
            return;
        }
        
        try {
            stop();
            
            File file = song.getFile();
            String uri = file.toURI().toString();
            Media media = new Media(uri);
            
            mediaPlayer = new MediaPlayer(media);
            currentSong = song;
            
            mediaPlayer.setOnReady(() -> {
                System.out.println("Ready to play: " + song.getTitle());
                mediaPlayer.play();
                isPlaying = true;
            });
            
            mediaPlayer.setOnEndOfMedia(() -> {
                System.out.println("Finished playing: " + song.getTitle());
                isPlaying = false;
            });
            
            mediaPlayer.setOnError(() -> {
                System.out.println("Error playing: " + mediaPlayer.getError());
                isPlaying = false;
            });
            
            mediaPlayer.setRate(DEFAULT_SPEED);
            
        } catch (Exception e) {
            System.err.println("Error loading audio: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void pause() {
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
            System.out.println("Paused: " + (currentSong != null ? currentSong.getTitle() : "unknown"));
        } else if (mediaPlayer != null && !isPlaying) {
            mediaPlayer.play();
            isPlaying = true;
            System.out.println("Resumed: " + (currentSong != null ? currentSong.getTitle() : "unknown"));
        } else {
            System.out.println("No media to pause");
        }
    }
    
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
            isPlaying = false;
            currentSong = null;
            System.out.println("Stopped playback");
        }
    }
    
    public void setSpeed(double speed) {
        if (mediaPlayer != null) {
            double clampedSpeed = Math.max(MIN_SPEED, Math.min(MAX_SPEED, speed));
            mediaPlayer.setRate(clampedSpeed);
            System.out.println("Speed set to: " + clampedSpeed);
        }
    }
    
    public boolean isPlaying() {
        return isPlaying;
    }
    
    public Song getCurrentSong() {
        return currentSong;
    }
    
    public double getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentTime().toSeconds();
        }
        return 0;
    }
    
    public double getTotalDuration() {
        if (mediaPlayer != null && mediaPlayer.getMedia() != null) {
            return mediaPlayer.getTotalDuration().toSeconds();
        }
        return 0;
    }
    
    public void seek(double seconds) {
        if (mediaPlayer != null) {
            double duration = getTotalDuration();
            if (duration > 0) {
                double seekPos = Math.max(0, Math.min(duration, seconds));
                mediaPlayer.seek(Duration.seconds(seekPos));
                System.out.println("Seeked to: " + String.format("%.1f", seekPos) + " seconds");
            }
        }
    }
}