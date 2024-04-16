import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Collision implements Runnable {
    private JPanel jpAttTot;
    private Heart heart;
    private AtomicBoolean active;
    private AtomicBoolean gameRunning;

    private JLabel jlHeart;
    private JLabel jlLaser;

    private Clip clip;
    private AudioInputStream audio;

    Attack attack;

    public Collision(Heart heart, JPanel jpAttTot, AtomicBoolean active, AtomicBoolean gameRunning) {
        this.heart = heart;
        this.jpAttTot = jpAttTot;
        this.active = active;
        this.gameRunning = gameRunning;

        jlHeart = (JLabel) Finder.findChildComponentByName(jpAttTot, "jlHeart");
        jlLaser = (JLabel) Finder.findChildComponentByName(jpAttTot, "jlLaser");
    }

    @Override
    public void run() {
        while (active.get() & gameRunning.get()) {
            synchronized (this) {
                try {

                    wait(10);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread() + " -> Sleep Interrupted");
                }
            }


            if (jlHeart.getBounds().intersects(jlLaser.getBounds())) {
                heart.decHealth();

                if (heart.getHealth() <= 0) {
                    gameRunning.set(false);
                }

                damageClip();
            }
        }
        Thread.currentThread().interrupt();
    }

    private void damageClip() {
        if (clip == null) {
            try {
                audio = AudioSystem.getAudioInputStream(new File("Assets/Audio/Sounds/SoulDamage.wav").getAbsoluteFile());
                clip = AudioSystem.getClip();
                clip.open(audio);
                clip.start();

                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
                System.out.println("Errore nella riproduzione, controllare il formato audio o la presenza di esso");
            }

            if (!clip.isActive()) {
                clip.start();
            }
        }
    }




    public void setAttack(Attack attack){
        this.attack = attack;
    }
}
