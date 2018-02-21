import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        model game = new model();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                startview view = new startview(game);
            }
        });
    }
}
