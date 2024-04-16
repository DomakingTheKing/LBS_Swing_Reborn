import javax.imageio.ImageIO;
import javax.swing.*;
import javax.sound.sampled.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class View extends JFrame{

    // Costanti
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 960;
    private static final String IMAGE_PATH = "Assets/Images/";
    private static final String MUSIC_PATH = "Assets/Audio/Music/";
    private static final String SOUND_PATH = "Assets/Audio/Sounds/";
    private static final String FONT_PATH = "Assets/Font/";

    // Variabili
    private JLayeredPane jlpAtt;
    private BackJPanel jpMain, jpSans;
    private JPanel jpGastSx, jpGastUp, jpGastDx, jpAttTot;
    private JLabel jlSans, jlHeart, jlHp;

    Font undertaleFont;
    Model model;

    public View(){}

    public void start(){
        initialize(); // 1
        // 1.5 -> undertaleFont
        preliminaries(); // 2
        createLayout(); // 3
        addComponents(); // 3.5
        frameCompletion(); // 4
        // 5 -> ostStart
    }

    public void startGameView() {
        model.startAttack(1, jpAttTot, "sx", 40, "M", 60);
        model.startAttack(1, jpAttTot, "dx", 65, "L", 100); // int nAtt, JPanel jpAttTot, String face, int pos, String attHeight, int laserThickness
    }


    // --- 1 ---
    private void initialize(){

        setSize(WIDTH, HEIGHT);

        // Main Panels
        jpMain = new BackJPanel(setBackground("Black.png")); // MAIN (Frame)
        jpSans = new BackJPanel(setBackground("transparentBack.png")); // Sans

        jlHp = new JLabel("HP: " + model.heart.getHealth()); // Indicatore HP

        jlSans = new JLabel(new ImageIcon(IMAGE_PATH + "Sans.png")); // Sans
        jlHeart = new JLabel(new ImageIcon(IMAGE_PATH + "Heart.png")); // Heart

        jlpAtt = new JLayeredPane();

        jpGastSx = new JPanel(); // Finestra Gaster Blasters
        jpGastUp = new JPanel(); // Finestra Gaster Blasters
        jpGastDx = new JPanel(); // Finestra Gaster Blasters
        jpAttTot = new JPanel();

        undertaleFont(); // 1.5
    }


    // --- 1.5 ---
    private void undertaleFont(){
        try {
            File fontFile = new File(FONT_PATH + "Undertale.ttf");
            undertaleFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            undertaleFont = undertaleFont.deriveFont(Font.PLAIN, 32);
        } catch (IOException e) {
            System.out.println("Errore durante il caricamento del font: " + e.getMessage());
        } catch (FontFormatException e) {
            System.out.println("Formato del font non valido: " + e.getMessage());
        }
    }


    // --- 2 ---
    private void preliminaries(){

        // Nome a ogni oggetto per essere identificato in futuro
        jlSans.setName("jlSans"); // Sans
        jlHeart.setName("jlHeart"); // Heart
        jlHp.setName("jlHp"); // HP
        jpMain.setName("jpMain"); // MAIN
        jpSans.setName("jpSans"); // Sans
        jlpAtt.setName("jlpAtt");
        jpGastSx.setName("jpGastSx"); // Finestra Gaster Blasters
        jpGastUp.setName("jpGastUp"); // Finestra Gaster Blasters
        jpGastDx.setName("jpGastDx"); // Finestra Gaster Blasters
        jpAttTot.setName("jpAttTot");

        // De-Opacizzazione dei Panels
        jpSans.setOpaque(false); // Sans
        jlpAtt.setOpaque(false);
        jpAttTot.setOpaque(false);
        jpGastSx.setOpaque(false); // Finestra Gaster Blasters
        jpGastDx.setOpaque(false); // Finestra Gaster Blasters
        jpGastUp.setOpaque(false); // Finestra Gaster Blasters
    }


    // --- 3 ---
    private void createLayout(){

        jpMain.setLayout(new BoxLayout(jpMain, BoxLayout.Y_AXIS));
        jpSans.setLayout(new BoxLayout(jpSans, BoxLayout.Y_AXIS));
        jlpAtt.setLayout(null);
        jpAttTot.setLayout(new GridBagLayout());
        jpGastDx.setLayout(null);
        jpGastSx.setLayout(null);
        jpGastUp.setLayout(null);

        jpAttTot.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlpAtt.setAlignmentX(Component.CENTER_ALIGNMENT);
        jpSans.setAlignmentX(Component.CENTER_ALIGNMENT);

        jlpAtt.setBorder(BorderFactory.createLineBorder(Color.white, 10));

        jpSans.setMaximumSize(new Dimension(200, 250));
        jlpAtt.setMinimumSize(new Dimension(600, 300));
        jpAttTot.setMaximumSize(new Dimension(900, 500));

        jlHeart.setBounds(300, 150, 32, 32);

        jlHp.setFont(undertaleFont);
        jlHp.setForeground(Color.white);
    }


    private void jpAttTotLayout(){

        GridBagConstraints gbc = new GridBagConstraints();

        // Impostazione dei vincoli per far riempire i pannelli in altezza
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;

        // Aggiunta di jpGastSx nella prima colonna
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.35; // Larghezza di 100 pixel
        jpAttTot.add(jpGastSx, gbc);

        // Aggiunta di jlpAtt nella seconda colonna
        gbc.gridx = 1;
        gbc.weightx = 0.6; // Larghezza di 600 pixel
        jpAttTot.add(jlpAtt, gbc);

        // Aggiunta di jpGastDx nella terza colonna
        gbc.gridx = 2;
        gbc.weightx = 0.35; // Larghezza di 100 pixel
        jpAttTot.add(jpGastDx, gbc);

        // Aggiunta di jpGastUp in cima alle tre colonne
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.35;
        jpAttTot.add(jpGastUp, gbc);
    }


    // --- 3.5 ---
    private void addComponents(){
        jpSans.add(jlSans);

        jpAttTotLayout();

        jlpAtt.add(jlHeart, JLayeredPane.DEFAULT_LAYER);
        jpMain.add(jlHp);
        jpMain.add(jpSans);
        jpMain.add(jpAttTot);

        add(jpMain);
    }


    // --- 4 ---
    private void frameCompletion(){
        setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // ostStart(); // 5
    }


    // --- 5 ---
    private void ostStart(){
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(MUSIC_PATH + "sans.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
            System.out.println("Errore nella riproduzione, controllare il formato audio o la presenza di esso");
        }
    }







    private Image setBackground(String file) {
        Image backImg = null;
        try {
            File imageFile = new File(IMAGE_PATH + file);
            backImg = ImageIO.read(imageFile);
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid image format or file: " + e.getMessage());
        }

        return backImg;
    }


    public void setModel(Model model){
        this.model = model;
    }
    public JPanel getJpAttTot(){
        return jpAttTot;
    }
    public void upHpCounter(int hp){
        jlHp.setText("HP: " + hp);
    }
    public void setKeyListener(Controller controller){
        addKeyListener(controller);
    }

}
