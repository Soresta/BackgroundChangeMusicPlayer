package sorest.bgchanger;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class scene2Controller implements Initializable {
    //-------------------------------------------- FXML DEĞİŞKENLERİ --------------------------------------------//
    private Stage stage;
    @FXML
    private VBox soundPane;
    @FXML
    private VBox picturePane;
    @FXML
    private ImageView myImageView;
    @FXML
    private ComboBox<String> speedBox;
    @FXML
    private ImageView playButton;
    @FXML
    private ImageView muteButton;
    @FXML
    private ImageView closeButton;
    @FXML
    private ImageView minimizeButton;
    @FXML
    private Slider soundSlider;
    @FXML
    private Slider musicSlider;
    @FXML
    private Button backgroundChoser;
    @FXML
    private Button soundChoser;
    @FXML
    private Text movingText;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private ImageView imageNo2;
    @FXML
    private ImageView imageNo3;
    @FXML
    private ImageView imageNo4;
    @FXML
    private ImageView fullScreen;
    @FXML
    private  ImageView addIcon;
    @FXML
    private Button addPlaylist;
    @FXML
    private Button pauseBtn;
    @FXML
    private ImageView imgControl;
    @FXML
    private VBox MusicsList;

    //-------------------------------------------- Diğer Değişkenler --------------------------------------------//
    private String currentSpeed;
    private boolean isfullScreen = false;
    private String speedBoxSpeeds[] = {"5 S                     ", "10 S", "15 S", "30 S", "1 M", "2 M", "5 M", "10 M", "30 M", "1 H"};
    private String PROJECT_ROOT_PATH;
    private MediaPlayer mediaPlayer;
    private Media sound;
    private Image myImage;
    private int sonSecilen;
    private int imageTimerDelay = 100;
    private int songNumber;
    private ArrayList<File> picturesInfolder = new ArrayList<File>();
    private ArrayList<File> musicsInFolder = new ArrayList<File>();
    private FileChooser fileChooser;
    private DirectoryChooser directoryChooser;
    private DirectoryChooser playListDirectoryChooser;
    private ArrayList<File> playlistSongs;
    private Timer timer;
    private Timer timerForMusic;
    private TimerTask musicTask;
    private Timer songTime;
    private TimerTask songTimeTask;
    private File selectedFileImage;
    private File selectedFileDirectory;
    private File selectedFileMusic;
    private File selectedPlayListDirectroy;
    private boolean isPause = true;
    private boolean isMute = true;
    private boolean running;
    private boolean isPicutresPlaying = true;

    //fotoğraf seçme işlemleri
    public void PictureFileSelector() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Resim Seçiniz");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Resim Dosyaları", "*.jpg", "*.png", "*.jpeg", "*.apng", "*.gif", "*.jpeg xr", "*.tiff", "*.raw", "*.psd");
        fileChooser.getExtensionFilters().add(imageFilter);
        selectedFileImage = fileChooser.showOpenDialog(stage);
        if (selectedFileImage != null) {
            myImage = new Image(selectedFileImage.toURI().toString());
            myImageView.setImage(myImage);
            sonSecilen = 1;
        } else System.out.println("Resim seçmediniz!");
    }

    //fullscreenMode


    //slayt seçme işlemleri
    public void DirectoryFileSelector() {
        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Resim Klasörü Seçiniz");
        selectedFileDirectory = directoryChooser.showDialog(stage);
        if (selectedFileDirectory != null) {
            picturesInfolder.clear();
            for (File file : selectedFileDirectory.listFiles()) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".apng")
                            || fileName.endsWith(".tiff") || fileName.endsWith(".gif") || fileName.endsWith(".jpeg xr") || fileName.endsWith(".raw") || fileName.endsWith(".psd")) {
                        picturesInfolder.add(file);
                        sonSecilen = 0;
                    }
                }
            }
            myImage = new Image(picturesInfolder.get(0).toURI().toString());
            myImageView.setImage(myImage);

            imageNo2.setImage(new Image(picturesInfolder.get(1).toURI().toString()));
            imageNo3.setImage(new Image(picturesInfolder.get(2).toURI().toString()));
            imageNo4.setImage(new Image(picturesInfolder.get(3).toURI().toString()));
        } else System.out.println("Klasör seçmediniz");
        if (picturesInfolder.isEmpty()) System.out.println("Seçtiğiniz klasörde resim bulunmamaktadır");
    }

    //fotoğraf slaytı geçiş hızı işlemleri
    public void getSpeed(ActionEvent event) {
        currentSpeed = speedBox.getValue();
        switch (currentSpeed) {
            case "5 S                     ":
                imageTimerDelay = 1000 * 5;
                break;
            case "10 S":
                imageTimerDelay = 1000 * 10;
                break;
            case "15 S":
                imageTimerDelay = 1000 * 15;
                break;
            case "30 S":
                imageTimerDelay = 1000 * 30;
                break;
            case "1 M":
                imageTimerDelay = 1000 * 60;
                break;
            case "2 M":
                imageTimerDelay = 1000 * 60 * 2;
                break;
            case "5 M":
                imageTimerDelay = 1000 * 60 * 5;
                break;
            case "30 M":
                imageTimerDelay = 1000 * 60 * 30;
                break;
            case "1 H":
                imageTimerDelay = 1000 * 60 * 60;
                break;
        }
    }

    //müzik seçme işlemleri
    public void MusicFileSelector(ActionEvent event) {
        playlistSongs = null;
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ses Dosyası Seçiniz");
        FileChooser.ExtensionFilter soundFilter = new FileChooser.ExtensionFilter("Sound Files", "*.mp3", "*.m4a", "*.mp4",
                "*.wav", "*.wma", "*.au", "*.aiff", "*.mid");
        fileChooser.getExtensionFilters().add(soundFilter);
        selectedFileMusic = fileChooser.showOpenDialog(stage);
        if (selectedFileMusic != null) {
            String musicPath = selectedFileMusic.toURI().toString();
            sound = new Media(musicPath);
            mediaPlayer = new MediaPlayer(sound);
            movingText.setText(selectedFileMusic.getName() + selectedFileMusic.toURI().getFragment());
        }
    }

    public void beginTimer() {
        timerForMusic = new Timer();
        musicTask = new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    running = true;
                    double current = mediaPlayer.getCurrentTime().toSeconds();
                    double end = sound.getDuration().toSeconds();
                    musicSlider.setValue(current / end * 100);
                    if (current / end >= 1) {
                        cancelTimer();
                        nextMedia();
                    }
                } else musicSlider.setValue(0);
            }
        };
        timerForMusic.scheduleAtFixedRate(musicTask, 0, 1000);
    }

    public void cancelTimer() {
        running = false;
        timerForMusic.cancel();
    }

    //duraklatma ve devam ettirme işlemleri
    public void PlayOrPause() {
        if (mediaPlayer != null) {
            if (isPause) {
                beginTimer();
                mediaPlayer.play();
                playButton.setImage(new Image(BgChanger.class.getResourceAsStream("pause.png")));
                isPause = false;
            } else {
                cancelTimer();
                isPause = true;
                mediaPlayer.pause();
                playButton.setImage(new Image(BgChanger.class.getResourceAsStream("playGreen.png")));
            }
        }
    }

    //başa sarma işlemi
    public void reset() {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(0));
        }
    }

    //durdurma işlemi
    public void Stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            movingText.setText(null);
            mediaPlayer = null;
            playButton.setImage(new Image(BgChanger.class.getResourceAsStream("playGreen.png")));
        }
    }

    //sessize alma işlemi
    public void Mute() {
        if (mediaPlayer != null) {
            if (isMute) {
                muteButton.setImage(new Image(BgChanger.class.getResourceAsStream("mute.png")));
                mediaPlayer.setMute(true);
                isMute = false;
            } else {
                muteButton.setImage(new Image(BgChanger.class.getResourceAsStream("unMute.png")));
                mediaPlayer.setMute(false);
                isMute = true;
            }
        }
    }


    public void pausePictures() {
        if (selectedFileDirectory != null) {
            if (isPicutresPlaying) {
                isPicutresPlaying = false;
            } else {
                isPicutresPlaying = true;
            }
        }
    }

    public void nextImage() {
        if (selectedFileDirectory != null) {
            if (picturesInfolder != null) {
                index++;
                File newFile = picturesInfolder.get(Math.abs((index) % picturesInfolder.size()));
                myImage = new Image(newFile.toURI().toString());
                myImageView.setImage(myImage);
                imageNo2.setImage(new Image(picturesInfolder.get(Math.abs((index +1) % picturesInfolder.size())).toURI().toString()));
                imageNo3.setImage(new Image(picturesInfolder.get(Math.abs((index + 2) % picturesInfolder.size())).toURI().toString()));
                imageNo4.setImage(new Image(picturesInfolder.get(Math.abs((index + 3) % picturesInfolder.size())).toURI().toString()));
                WallpaperChanger.changeWallpaper(newFile.getAbsolutePath());
            }
        }
    }

    public void backImage() {
        if (selectedFileDirectory != null) {
            if (picturesInfolder != null) {
                index--;
                File newFile = picturesInfolder.get(Math.abs((index) % picturesInfolder.size()));
                myImage = new Image(newFile.toURI().toString());
                myImageView.setImage(myImage);
                imageNo2.setImage(new Image(picturesInfolder.get(Math.abs((index +1) % picturesInfolder.size())).toURI().toString()));
                imageNo3.setImage(new Image(picturesInfolder.get(Math.abs((index + 2) % picturesInfolder.size())).toURI().toString()));
                imageNo4.setImage(new Image(picturesInfolder.get(Math.abs((index + 3) % picturesInfolder.size())).toURI().toString()));
                WallpaperChanger.changeWallpaper(newFile.getAbsolutePath());
            }
        }
    }

    int index = 1;

    //onayla butonu işlemleri
    public void Apply() {
        if (timer != null) {
            timer.cancel();
        }

        if (sonSecilen == 0) {
            if (selectedFileDirectory != null) {
                if (picturesInfolder != null) {
                    timer = new Timer();
                    TimerTask timeTaskForPictures = new TimerTask() {
                        @Override
                        public void run() {
                            if (isPicutresPlaying) {
                                if (sonSecilen == 0) {
                                    File newFile = picturesInfolder.get(index % picturesInfolder.size());
                                    myImage = new Image(newFile.toURI().toString());
                                    myImageView.setImage(myImage);
                                    imageNo2.setImage(new Image(picturesInfolder.get((index + 1) % picturesInfolder.size()).toURI().toString()));
                                    imageNo3.setImage(new Image(picturesInfolder.get((index + 2) % picturesInfolder.size()).toURI().toString()));
                                    imageNo4.setImage(new Image(picturesInfolder.get((index + 3) % picturesInfolder.size()).toURI().toString()));
                                    WallpaperChanger.changeWallpaper(newFile.getAbsolutePath());
                                    index++;
                                }
                            }
                        }
                    };
                    timer.scheduleAtFixedRate(timeTaskForPictures, 0, imageTimerDelay);
                }
            }
        }

        if (mediaPlayer != null) {
            songTime = new Timer();
            songTimeTask = new TimerTask() {
                int sure = 0;

                @Override
                public void run() {
                    sure++;
                }
            };
            songTime.scheduleAtFixedRate(songTimeTask, 0, 1000);
            beginTimer();
            mediaPlayer.play();
            playButton.setImage(new Image(BgChanger.class.getResourceAsStream("pause.png")));
        }

        if (sonSecilen == 1) {
            if (selectedFileImage != null) {
                WallpaperChanger.changeWallpaper(selectedFileImage.getAbsolutePath());
            }
        }
    }


    //------------------------------------------------- PlayerList Ayarları -------------------------------------------------//
    public void playlistPlayer() {
        playListDirectoryChooser = new DirectoryChooser();
        playListDirectoryChooser.setTitle("Müzik Klasörü Seçiniz");
        selectedPlayListDirectroy = playListDirectoryChooser.showDialog(stage);

        if (selectedPlayListDirectroy != null) {
            musicsInFolder.clear();
            for (File file : selectedPlayListDirectroy.listFiles()) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".mp3") || fileName.endsWith(".m4a") || fileName.endsWith(".mp4") || fileName.endsWith(".apng")
                            || fileName.endsWith(".wav") || fileName.endsWith(".wma") || fileName.endsWith(".mid") || fileName.endsWith(".au")) {
                        musicsInFolder.add(file);
                    }
                }
            }
        } else System.out.println("playerlist seçmediniz");

        if (musicsInFolder.isEmpty()) System.out.println("Seçtiğiniz klasörde resim bulunmamaktadır");
        if (musicsInFolder != null) {
            playlistSongs = new ArrayList<>();
            for (File file : musicsInFolder) {
                playlistSongs.add(file);
                sound = new Media(playlistSongs.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(sound);
                movingText.setText(playlistSongs.get(songNumber).getName());
            }
        }
    }

    //ayarlama sekmeleri açma işlemleri
    public void apear(ActionEvent event) {
        if (event.getSource() == backgroundChoser) {
            picturePane.setVisible(true);
            soundPane.setVisible(false);
        } else if (event.getSource() == soundChoser) {
            soundPane.setVisible(true);
            picturePane.setVisible(false);
        }
    }

    //açılan sekmelerin kapatma işlemleri
    public void disapear() {
        soundPane.setVisible(false);
        picturePane.setVisible(false);
    }

    //uygulamayı kapatma işlemi
    public void close() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    //uygulamayı küçültme işlemi
    public void minimize() {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PROJECT_ROOT_PATH = System.getProperty("user.dir");
        if (mediaPlayer != null) {
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.setAutoPlay(true);
            });
        }

        fullScreen.setOnMouseClicked(MouseEvent->{
            if (stage == null) {
                stage = (Stage) ((Node) MouseEvent.getSource()).getScene().getWindow();
            }

            if (isfullScreen) {
                isfullScreen = false;
                stage.setFullScreen(isfullScreen);
            } else {
                isfullScreen = true;
                stage.setFullScreen(isfullScreen);
            }
        });

        speedBox.getItems().addAll(speedBoxSpeeds);
        speedBox.setOnAction(this::getSpeed);

        musicSlider.setOnMouseClicked(event -> {
            if (mediaPlayer != null && sound != null) {
                double mouseX = event.getX();
                double sliderWidth = musicSlider.getWidth();
                double percent = mouseX / sliderWidth;
                double totalDuration = sound.getDuration().toSeconds();
                double newValue = percent * totalDuration;
                mediaPlayer.seek(Duration.seconds(newValue));
            }
        });

        progression();
        closeButton.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                close();
            }
        });

        minimizeButton.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                minimize();
            }
        });

        addPlaylist.setOnMouseEntered(event -> {
            addIcon.setImage(new Image(BgChanger.class.getResourceAsStream("addColor2.png")));
        });
        soundLevelSettings();
    }

    public void forward10sn(){
        double current = mediaPlayer.getCurrentTime().toSeconds();
        mediaPlayer.seek(Duration.seconds(current+10));
    }

    public void backword10sn(){
        double current = mediaPlayer.getCurrentTime().toSeconds();
        mediaPlayer.seek(Duration.seconds(current-10));
    }

    private void soundLevelSettings() {
        soundSlider.valueProperty().addListener((observableValue, number, t1) -> {
            if (mediaPlayer != null)
                mediaPlayer.setVolume(soundSlider.getValue() * 0.01);
        });
    }

    public void nextMedia() {
        if (mediaPlayer != null) {
            if (playlistSongs != null) {
                if (songNumber < playlistSongs.size() - 1) {
                    songNumber++;
                    mediaPlayer.stop();

                    sound = new Media(playlistSongs.get(songNumber).toURI().toString());
                    mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.setVolume(soundSlider.getValue() * 0.01);
                    movingText.setText(playlistSongs.get(songNumber).getName());
                    if (isMute == false) {
                        muteButton.setImage(new Image(BgChanger.class.getResourceAsStream("mute.png")));
                        mediaPlayer.setMute(true);
                    }
                    soundLevelSettings();
                    mediaPlayer.play();
                    if (isPause) PlayOrPause();
                } else {
                    songNumber = 0;
                    mediaPlayer.stop();

                    sound = new Media(playlistSongs.get(songNumber).toURI().toString());
                    mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.setVolume(soundSlider.getValue() * 0.01);
                    movingText.setText(playlistSongs.get(songNumber).getName());

                    if (isMute == false) {
                        muteButton.setImage(new Image(BgChanger.class.getResourceAsStream("mute.png")));
                        mediaPlayer.setMute(true);
                    }

                    soundLevelSettings();
                    mediaPlayer.play();
                    if (isPause) PlayOrPause();
                }
            }
        }
    }

    //önceki şarkıya
    public void previousMedia() {
        if (mediaPlayer != null) {
            if (playlistSongs != null) {
                if (songNumber > 0) {
                    songNumber--;
                    mediaPlayer.stop();

                    sound = new Media(playlistSongs.get(songNumber).toURI().toString());
                    mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.setVolume(soundSlider.getValue() * 0.01);
                    movingText.setText(playlistSongs.get(songNumber).getName());

                    if (isMute == false) {
                        muteButton.setImage(new Image(BgChanger.class.getResourceAsStream("mute.png")));
                        mediaPlayer.setMute(true);
                        System.out.println(1);
                    }

                    soundLevelSettings();
                    mediaPlayer.play();
                    if (isPause) PlayOrPause();
                } else {
                    songNumber = playlistSongs.size() - 1;
                    mediaPlayer.stop();

                    sound = new Media(playlistSongs.get(songNumber).toURI().toString());
                    mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.setVolume(soundSlider.getValue() * 0.01);
                    movingText.setText(playlistSongs.get(songNumber).getName());

                    if (isMute == false) {
                        muteButton.setImage(new Image(BgChanger.class.getResourceAsStream("mute.png")));
                        mediaPlayer.setMute(true);
                        System.out.println(1);
                    }

                    soundLevelSettings();
                    mediaPlayer.play();
                    if (isPause) PlayOrPause();
                }
            }
        }
    }

    //İlerleme çubugu ayarları
    private void progression() {
        musicSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            progressBar.setProgress(newValue.doubleValue() / musicSlider.getMax());
        });
    }
}