package server.src; /**
 * Created by kopec on 2016-03-07.
 */
import java.util.*;

public class AnimalInfo {
    public int owner;
    public Vector features;
    public boolean feeded = false;
    public int foodNeeded = 1;
    public boolean alive = true;

    public AnimalInfo(int o){
        owner = o;
    }
}

