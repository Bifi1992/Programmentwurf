import java.sql.Time;

/**
 * Created by ludwig on 18.05.17.
 */
public class TestShip {
    float speed;
    float elapsedtime;
    float distance = 1000;
    public TestShip(float speed){
        this.speed = speed;
    }
    public float run(){
        elapsedtime = distance * speed;
        return elapsedtime;
    }
}
