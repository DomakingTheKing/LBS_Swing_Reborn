import java.util.concurrent.atomic.AtomicInteger;

public class Heart {

    private final AtomicInteger health;

    public Heart(){
        health = new AtomicInteger(100);
    }

    public synchronized void decHealth(){
        health.decrementAndGet();
    }

    public int getHealth(){
        return health.get();
    }

}
