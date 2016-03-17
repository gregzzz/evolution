package server.src;
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

