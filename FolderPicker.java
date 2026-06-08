import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.concurrent.CompletableFuture;

public class FolderPicker extends JPanel {

    private final String[] files;
    private final String folderPath;
    private final CompletableFuture<File> result;  // holds the picked file
    private int selected = 0;

    static final Color BG     = Color.BLACK;
    static final Color FG     = new Color(220, 220, 220);
    static final Color SEL_BG = new Color(220, 220, 220);
    static final Color SEL_FG = Color.BLACK;
    static final Font  FONT   = new Font("Monospaced", Font.PLAIN, 16);
    static final int   ROW_H  = 24;
    static final int   PAD_X  = 12;

    public FolderPicker(String[] files, String folderPath, CompletableFuture<File> result) {
        this.files      = files;
        this.folderPath = folderPath;
        this.result     = result;

        setBackground(BG);
        setPreferredSize(new Dimension(500, Math.min(files.length, 20) * ROW_H + 40));
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP    -> { if (selected > 0)              selected--; repaint(); }
                    case KeyEvent.VK_DOWN  -> { if (selected < files.length-1) selected++; repaint(); }
                    case KeyEvent.VK_ENTER -> confirm();
                    case KeyEvent.VK_ESCAPE -> cancel();
                }
            }
        });
    }

    void confirm() {
        File chosen = new File(folderPath, files[selected]);
        result.complete(chosen);   
        SwingUtilities.getWindowAncestor(this).dispose();
    }

    void cancel() {
        result.complete(null);     // returns null on Escape
        SwingUtilities.getWindowAncestor(this).dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(FONT);

        int y = 20;
        for (int i = 0; i < files.length; i++) {
            String label = (i == selected ? " > " : "   ") + files[i];
            if (i == selected) {
                g2.setColor(SEL_BG);
                g2.fillRect(0, y - ROW_H + 5, getWidth(), ROW_H);
                g2.setColor(SEL_FG);
            } else {
                g2.setColor(FG);
            }
            g2.drawString(label, PAD_X, y);
            y += ROW_H;
        }
    }

    // Static helper — call this from anywhere, it blocks until user picks
    public static File pickFile(String folderPath) throws Exception {
        File folder = new File(folderPath);

        String[] files = folder.list();
        if (files == null || files.length == 0) return null;
        java.util.Arrays.sort(files);

        CompletableFuture<File> result = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Folder Picker — " + folderPath);
            FolderPicker picker = new FolderPicker(files, folderPath, result);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setBackground(Color.BLACK);
            frame.add(picker);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            picker.requestFocusInWindow();
        });

        return result.get();  // blocks here until confirm() or cancel() fires
    }
}