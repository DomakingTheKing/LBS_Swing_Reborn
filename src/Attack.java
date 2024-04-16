import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Attack implements Runnable{

    private JPanel jpAttTot;
    private int nAtt, face, pos, attHeight, laserThickness;
    private JLabel jlLaser;
    private AtomicBoolean active;

    Model model;

    // costruttori
    public Attack(int nAtt, JPanel jpAttTot){
        this.nAtt = nAtt;
        this.jpAttTot = jpAttTot;
    }

    public Attack(int nAtt, JPanel jpAttTot, String face, int pos, String attHeight, int laserThickness){
        this(nAtt, jpAttTot);
        this.pos = pos;

        switch (attHeight.toUpperCase()) {
            case "XL":
                this.attHeight = 150;
                break;
            case "L":
                this.attHeight = 128;
                break;
            case "S":
                this.attHeight = 64;
                break;
            case "XS":
                this.attHeight = 32;
                break;
            default:
                this.attHeight = 96;
        }

        switch (face.toUpperCase()) {
            case "SX":
                this.face = 1;
                break;
            case "UP":
                this.face = 2;
                break;
            case "DX":
                this.face = 3;
                break;
            default:
                this.face = 0;
        }

        if(laserThickness >= 10 && laserThickness <= this.attHeight){
            this.laserThickness = laserThickness;
        } else {
            this.laserThickness = this.attHeight;
        }
    }



    @Override
    public void run() {
        startAtt();
    }



    private void startAtt(){
        switch(nAtt){
            case 1:
                attGaster(face, pos, laserThickness, attHeight);
                break;
        }
    }


    private void attGaster(int face, int pos, int laserThickness, int attHeight){

        JPanel jpGaster;
        JLayeredPane jlpAtt = (JLayeredPane) Finder.findChildComponentByName(jpAttTot, "jlpAtt");
        JLabel gasterBlaster = new JLabel();

        jlLaser = new JLabel();
        jlLaser.setName("jlLaser");
        jlLaser.setOpaque(true);
        jlLaser.setBackground(Color.white);

        switch(face){
            case 1:
                jpGaster = (JPanel) Finder.findChildComponentByName(jpAttTot, "jpGastSx");

                gasterBlaster.setIcon(new ImageIcon("Assets/Images/GasterBlasterClosedSx.png"));
                gasterBlaster.setBounds(jpGaster.getWidth() - 128, pos, 128, attHeight);

                jlLaser.setBounds(0, pos + ((gasterBlaster.getHeight() - laserThickness) / 2), 0, laserThickness); // algoritmo per capire altezza laser in base ad altezza blaster
                jlpAtt.add(jlLaser, JLayeredPane.POPUP_LAYER);

                active = new AtomicBoolean(true);

                Collision collision = new Collision(model.getHeart(), jpAttTot, active, model.getGameRunning());
                collision.setAttack(this);
                Thread collisionT = new Thread(collision);
                collisionT.start();

                jpGaster.add(gasterBlaster);
                jpGaster.repaint();
                jpGaster.revalidate();

                sleepS(1);

                gasterBlaster.setIcon(new ImageIcon("Assets/Images/GasterBlasterOpenedSx.png"));

                //gastBlasterSound();

                laserGO(gasterBlaster, jpGaster);
                break;

            case 2:
                jpGaster = (JPanel) Finder.findChildComponentByName(jpAttTot, "jpGastDx");

                gasterBlaster.setIcon(new ImageIcon("Assets/Images/GasterBlasterClosedDx.png"));
                gasterBlaster.setBounds(jpGaster.getWidth() - 128, pos, 128, attHeight);

                jlLaser.setBounds(0, pos + ((gasterBlaster.getHeight() - laserThickness) / 2), 0, laserThickness); // algoritmo per capire altezza laser in base ad altezza blaster
                jlpAtt.add(jlLaser, JLayeredPane.POPUP_LAYER);

                active = new AtomicBoolean(true);

                collision = new Collision(model.getHeart(), jpAttTot, active, model.getGameRunning());
                collision.setAttack(this);
                collisionT = new Thread(collision);
                collisionT.start();

                jpGaster.add(gasterBlaster);
                jpGaster.repaint();
                jpGaster.revalidate();

                sleepS(1);

                gasterBlaster.setIcon(new ImageIcon("Assets/Images/GasterBlasterOpenedDx.png"));

                //gastBlasterSound();

                laserGO(gasterBlaster, jpGaster);
        }
    }






    private void laserGO(JLabel gasterBlaster, JPanel jpGaster){

        new Thread(() -> {
            JLayeredPane jlpAtt = (JLayeredPane) Finder.findChildComponentByName(jpAttTot, "jlpAtt");

            while (jlLaser.getWidth() < jlpAtt.getWidth()) {
                jlLaser.setSize(jlLaser.getWidth() + 1, jlLaser.getHeight());
                jlLaser.repaint();
                jlLaser.revalidate();

                synchronized (this) {
                    sleepM(1);
                }
            }
            gasterBlaster.setIcon(new ImageIcon("Assets/Images/GasterBlasterClosedUp.png"));

            sleepM(500);

            jlpAtt.remove(jlLaser);
            jlpAtt.repaint();
            jlpAtt.revalidate();

            jpGaster.remove(gasterBlaster);
            jpGaster.repaint();
            jpGaster.revalidate();

            active.set(false);

        }).start();
    }





    private void sleepS(int sec){
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void sleepM(int mills){
        try {
            TimeUnit.MILLISECONDS.sleep(mills);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void gastBlasterSound(){
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File("Assets/Audio/Sounds/Blaster.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
            System.out.println("Errore nella riproduzione, controllare il formato audio o la presenza di esso");
        }
    }




    public void setModel(Model model){ this.model = model; }

}

