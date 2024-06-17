import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int anchoTablero = 500;
        int altoTablero = 500;

        JFrame frame = new JFrame("Dinosaurio Chrome");
        //frame.setVisible(true);
        frame.setSize(anchoTablero, altoTablero);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ChromeDinosaurio chromeDinosaurio = new ChromeDinosaurio();
        frame.add(chromeDinosaurio);
        frame.pack();
        chromeDinosaurio.requestFocus();
        frame.setVisible(true);
    }
}
