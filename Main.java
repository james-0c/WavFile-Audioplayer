import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        
        File SelectedFile = FolderPicker.pickFile("E:\\Users\\Programming Projects\\JavaExperiment\\Music");

        if (SelectedFile == null) {
            System.out.println("No selected file.");
            return;
        }

        System.out.println("Selected " + SelectedFile.getName());
        AudioPlayer.play(SelectedFile);

    }
    
}
