import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import java.io.FileNotFoundException;
import java.util.Scanner;



public class AudioPlayer {
    
    public static void main(String[] args) throws Exception {
        String FilePath = "E:\\Users\\Programming Projects\\JavaExperiment\\Music\\MGMT - Little Dark Age.wav"; // Replace with your audio file path by copying path to wav file
        play(new File(FilePath));
    }

    private static void applyVolume(FloatControl control, int level){

        float min = control.getMinimum();
        float max = control.getMaximum();
        float dB = min + (max - min) * (level / 10.0f);
        control.setValue(dB);

    }

    private static void printUI(int VolumeLevel){

        String bar = "[" + "█".repeat(VolumeLevel) + " ".repeat(10 - VolumeLevel) + "]";
        System.out.println("Volume: " + bar + " " + VolumeLevel + "/10");
        System.out.println("P = Play, S = Stop, R = Reset, + = Volume Up, - = Volume Down, B = Return to song selection, Q = Quit");
        System.out.print("Enter your choice: ");

    }



    public static void play(File file) throws Exception {

        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Scanner scanner = new Scanner(System.in)) {
            
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // volume control
            FloatControl Volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            int VolumeLevel = 5; // Intialise its default value to 5/10
            applyVolume(Volume, VolumeLevel);
            


            String response = "";
            while (!response.equalsIgnoreCase("Q")) {
                printUI(VolumeLevel);
                response = scanner.nextLine();

                switch (response.toUpperCase()) {
                    case "P":
                        clip.start();
                        break;
                    case "S":
                        clip.stop();
                        break;
                    case "R":
                        clip.setFramePosition(0);
                        break;
                    case "Q":
                        clip.close();
                        break;
                    case "B":
                        FolderPicker.pickFile("E:\\Users\\Programming Projects\\JavaExperiment\\Music");
                        break;
                    case "+":
                        if (VolumeLevel < 10) {
                            VolumeLevel++;
                            applyVolume(Volume, VolumeLevel);
                        }
                        break;
                    case "-":
                        if (VolumeLevel > 0) {
                            VolumeLevel--;
                            applyVolume(Volume, VolumeLevel); 
                        }
                        break;
                    default:
                        System.out.println("Invalid command. Please try again.");
                }
            }

        }

        catch(FileNotFoundException e) {
            System.out.println("Couldn't locate file.");
        }

        catch (UnsupportedAudioFileException e) {
            System.out.println("The specified audio file is not supported.");
        }
        
        catch (IOException e) {
            System.out.println("Something went wrong.");
        }
        
        catch (LineUnavailableException e) {
            System.out.println("Audio line for playing back is unavailable.");
        }
        
        finally{
            System.out.println("Goodbye");
        }

    }

}
