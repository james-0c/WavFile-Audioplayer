import java.util.Scanner;

public class DrawBox {

    public static void drawBox(int width, int length){

        // top border of the box
        System.out.println("+" + "-".repeat(length) + "+");

        // middle rows of the box
        for (int i = 0; i < width; i++) {
            System.out.println("|" + " ".repeat(length) + "|");
        }

        // bottom rows of the box
        System.out.println("+" + "-".repeat(length) + "+");

    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Length: ");
        int length = scanner.nextInt();

        System.out.print("Enter width: ");
        int width = scanner.nextInt();
        
        scanner.close();

        if (length < 1 || width < 2) {
            System.out.println("Length must be >= 1 and width must be >= 2.");
            return;
        }

        drawBox(length, width);
    }
    
}
