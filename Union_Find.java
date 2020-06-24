//AEM:2895
//Στυλιανός Μαντζουράνης
//smantzou@csd.auth.gr


import java.util.ArrayList;

/**
 * Κλάση η οποία δημιουργήθηκε για την οργάνωση των Group της λειτουργίας Α
 */
public class Union_Find {
    private ArrayList<Integer> Group;
    private double spanning ;
    public Union_Find(){}
    public Union_Find(int x, int y,double spanning){
        Group = new ArrayList<>();

        Group.add(x);
        Group.add(y);
        this.spanning=spanning;
    }


    /**
     * Έλεγχος για την ύπαρξη του στοιχείου
     * @param num
     * και επιστροφή
     * @return true or false
     * αντίστοιχα
     */
    public boolean CheckFor(int num){


            if(Group.contains(num)){


                return true;
            }


        return false;



    }

    /**
     * @return μεγέθους του Arraylist Group
     */
    public int Size(){
        return Group.size();
    }

    /**
     * Προσθήκη του
     * @param num id μυρμηγιού στο Group και
     * @param distance της αποόστασης του κόμβου στο συνολικό Spanning
     */
    public void AddtoGroup(int num,double distance){

        Group.add(num);
        spanning=spanning+distance;
    }

    /**
     * Συνάρτηση ελέγχου για την ύπαρξη κοινών id στα
     * @param e groups
     * @param a groups
     * και επιστροφής
     * @return true or false
     * αντίστοιχα
     */
    public boolean CheckForSimilar(Union_Find e,Union_Find a){
             for(int i=0;i<e.Group.size();i++){
                 for(int j=0;j<a.Group.size();j++){
                     if(e.Group.get(i).equals(a.Group.get(j))){

                         return true;
                     }
                 }
             }
             return false;

    }

    /**
     * Προσθήκη ενός id στο Group
     * @param num
     */
    private void AddtoGroup(int num){

        Group.add(num);

    }

    /**
     * @return επιστροφή του spanning του group
     */
    public double ReturnSpanning(){
        return spanning;
    }

    /**
     *
     * @return επιστροφή του μέλους στην
     * @param i  θέση
     */
    public int ReturnMember(int i){
        return Group.get(i);

    }


    /**
     * Συνάρτηση που δέχεται δύο
     * @param e αντικείμενα
     * @param a Union Find
     * και επιστρέφει
     * @return ένα αντικείμενο Union Find το οποίο είναι τα παραπάνω ενωμένα
     * με το μικρότερο να εισάγεται στο μεγαλύτερο
     */
    public Union_Find Unify(Union_Find e,Union_Find a){

        if(e.Size()>=a.Size()){


            for(int i=0;i<a.Group.size();i++){
                if(e.CheckFor(a.Group.get(i))){
                   continue;
                }
                else {


                    e.AddtoGroup(a.Group.get(i));


                }
            }
            e.spanning=e.spanning+a.spanning;
            return e;

        }
        else{
            for(int i=0;i<e.Group.size();i++){
                if(a.CheckFor(e.Group.get(i))){
                  continue;
                }
                else {


                    a.AddtoGroup(e.Group.get(i));

                }
            }
            a.spanning=a.spanning+e.spanning;

            return a;

        }



    }

}
