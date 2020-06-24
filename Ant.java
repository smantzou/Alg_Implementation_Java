//AEM:2895
//Στυλιανός Μαντζουράνης
//smantzou@csd.auth.gr
import java.util.ArrayList;

/**
 * Η Κλάση Ant δημιουργήθηκε για την οργάνωση των δεδομένων των μυρμηγκιών
 */
public class Ant {
    public int id;
    public String colour;
    public double coord_x;
    public double coord_y;
    public int capacity;
    public ArrayList<Integer> seedweight;

    /**
     * Διάκριση των αντικείμενων ,μεταξύ σε μαύρα και κόκκινα, με βάση
     * @param id (το id μυρμηγκιού)
     *  και αρχικοποίηση των κατάλληλων μεταβλητών
     *  πχ την int capacity για την χωριτικότητα των κόκκινων μυρμηγκιών
     */
    public Ant(int id)
    {
        this.id=id;
        if(id%2==0){
           colour= "black";
           seedweight = new ArrayList<>(0);
        }
        else{
            colour="red";

        }
    }


    public void InsertCoords(double coord_x,double coord_y){
        this.coord_x=coord_x;
        this.coord_y=coord_y;
    }
    public void DefineCapacity(int capacity){
        this.capacity=capacity;


    }
    public void DefineSeed(int weight){
        if(seedweight.size()<5) {
            seedweight.add(weight);
        }


    }

}
