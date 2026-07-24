package com.aymane;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.stage.Stage;


public class PlayerController implements Initializable {
    
    @FXML
    private ListView<Song> songListView;
    
    @FXML
    private Slider speedSlider;
    
    @FXML
    private Slider pitchSlider;
    
    @FXML
    private Button playButton;
    
    @FXML
    private Button pauseButton;
    
    @FXML
    private Button stopButton;
    
    private AudioEngine audioEngine = new AudioEngine();
    private SongLibrary songLibrary = new SongLibrary();
    private Stage stage;
    private boolean isSeeking = false;
    private AnimationTimer timer;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        audioEngine = new AudioEngine();
        songLibrary = new SongLibrary();
                
        speedSlider.setMin(0.5);
        speedSlider.setMax(2.0);
        speedSlider.setValue(1.0);
        speedSlider.setMajorTickUnit(0.5);
        speedSlider.setMinorTickCount(4);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setBlockIncrement(0.1);
        
        pitchSlider.setMin(0);
        pitchSlider.setMax(100);
        pitchSlider.setValue(0);
        pitchSlider.setShowTickLabels(false);
        pitchSlider.setShowTickMarks(false);
        
        
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (audioEngine != null) {
                audioEngine.setSpeed(newValue.doubleValue());
                System.out.println("Speed changed to: " + String.format("%.2f", newValue.doubleValue()));
            }
        });
        pitchSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (isSeeking && audioEngine != null) {
                double duration = audioEngine.getTotalDuration();
                if (duration > 0) {
                    double seekPosition = (newValue.doubleValue() / 100.0) * duration;
                    audioEngine.seek(seekPosition);
                   
                }
            }
        });
        
        pitchSlider.setOnMousePressed(event -> {
            isSeeking = true;
        });
        pitchSlider.setOnMouseReleased(event -> {
            isSeeking = false;
            if (audioEngine != null) {
                double duration = audioEngine.getTotalDuration();
                if (duration > 0) {
                    double position = (pitchSlider.getValue() / 100.0) * duration;
                    audioEngine.seek(position);
                }
            }
        });
        
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateTimeSlider();
            }
        };
        timer.start();
    }
     private void updateTimeSlider() {
        if (audioEngine != null && !isSeeking) {
            double currentPosition = audioEngine.getCurrentPosition();
            double totalDuration = audioEngine.getTotalDuration();
            
            if (totalDuration > 0) {
                double progress = (currentPosition / totalDuration) * 100.0;
                pitchSlider.setValue(progress);
            }
        }
    }
    
    
    @FXML
    public void choose(){
        ObservableList<Song> songs = songLibrary.getSongs(stage);
        songListView.setItems(songs);
    }

    @FXML
    public void playSong(ActionEvent event) {
        Song selectedSong = songListView.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            audioEngine.play(selectedSong);
            System.out.println("Playing: " + selectedSong);
        } else {
            System.out.println("No song selected!");
        }
    }
    
    @FXML
    public void pauseSong(ActionEvent event) {
        audioEngine.pause();
        System.out.println("Paused");
    }
    
    @FXML
    public void stopSong(ActionEvent event) {
        audioEngine.stop();
        System.out.println("Stopped");
    }


}