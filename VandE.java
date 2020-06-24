
//AEM:2895
//Στυλιανός Μαντζουράνης
//smantzou@csd.auth.gr


/**
 * H Κλάση VandE δημιουργήθηκε για την οργάνωση αποστάσεων από
 * (from) και προς (to) δύο μυρμιγκιών.
 */
public class VandE {
    public int from;
    public int to;
    public double distance; //Απόσταση μεταξύ των δύο σημείων

    public VandE(int from,int to,double distance){
        this.from=from;
        this.to=to;
        this.distance=distance;

    }
}
