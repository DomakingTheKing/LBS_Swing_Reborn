import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Model {

    private final AtomicBoolean gameRunning = new AtomicBoolean(true);

    View view;
    Controller controller;
    Heart heart;

    public Model(){

    }

    public void start(){
        new Thread(() -> {
            while (gameRunning.get()) {
                try {
                    controller.aggiornaMovimento();
                    view.upHpCounter(heart.getHealth());

                    synchronized (this) {
                        wait(10);
                    }
                } catch (InterruptedException e) {
                    System.out.println("Errore non so quale");
                }
            }
            end();
        }).start();
    }

    public void end(){
        System.exit(0);
    }

    public void startAttack(int nAtt, JPanel jpAttTot) {
        Attack att = new Attack(nAtt, jpAttTot);
        att.setModel(this);

        Thread attT = new Thread(att);
        attT.start();

        try{
            attT.join();
        } catch(InterruptedException e){
            System.out.println("[!] Thread " + attT.getName() + " è stato interrotto");
        }

        System.out.println("Attack finished");
    }

    public void startAttack(int nAtt, JPanel jpAttTot, String face, int pos, String attHeight, int laserThickness){
        Attack att = new Attack(nAtt, jpAttTot, face, pos, attHeight, laserThickness);
        att.setModel(this);

        Thread attT = new Thread(att);
        attT.start();

        try{
            attT.join();
        } catch(InterruptedException e){
            System.out.println("[!] Thread " + attT.getName() + " è stato interrotto");
        }

        System.out.println("Attack finished");
    }

    public void setView(View view){
        this.view = view;
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    public View getView(){
        return view;
    }

    public void setHeart(Heart heart){
        this.heart = heart;
    }

    public Heart getHeart(){ return heart; }

    public boolean getGameRunningBool(){
        return gameRunning.get();
    }

    public AtomicBoolean getGameRunning(){
        return gameRunning;
    }

}
