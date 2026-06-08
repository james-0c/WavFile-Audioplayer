import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;


public class Testbed extends JPanel {
    
    private final String[] files;
    private int selected = 0;

    static final Color BG       = Color.BLACK;
    static final Color FG       = new Color(220, 220, 220);
    static final Color SEL_BG   = new Color(220, 220, 220);
    static final Color SEL_FG   = Color.BLACK;
    static final Font  FONT     = new Font("Monospaced", Font.PLAIN, 16);
    static final int   ROW_H    = 24;
    static final int   PAD_X    = 12;

    public Testbed(String[] files) {
        this.files = files;
        setBackground(BG);
        setPreferredSize(new Dimension(500, Math.min(files.length, 20) * ROW_H + 40));
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP   -> { if (selected > 0)              selected--; repaint(); }
                    case KeyEvent.VK_DOWN -> { if (selected < files.length-1) selected++; repaint(); }
                    case KeyEvent.VK_ENTER -> confirm();
                    case KeyEvent.VK_ESCAPE -> System.exit(0);
                }
            }
        });
    }

    void confirm() {
        System.out.println("Selected: " + files[selected]);
        System.exit(0);
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

    public static void main(String[] args) {
        String folderPath = args.length > 0 ? args[0] : "E:\\Users\\Programming Projects\\JavaExperiment\\Music";
        File folder = new File(folderPath);

        if (!folder.isDirectory()) {
            System.out.println("Not a valid directory: " + folderPath);
            return;
        }

        String[] files = folder.list();
        if (files == null || files.length == 0) {
            System.out.println("Folder is empty.");
            return;
        }

        java.util.Arrays.sort(files);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Folder Picker — " + folderPath);
            Testbed picker = new Testbed(files);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBackground(Color.BLACK);
            frame.add(picker);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            picker.requestFocusInWindow();
        });
    }

}
