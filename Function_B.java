//AEM:2895
//Στυλιανός Μαντζουράνης
//smantzou@csd.auth.gr



import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;

/**
 * Η κλάση Function_B υλοποιεί τον κώδικα για την εύρεση των ευασταθών ταιριασμάτων με βάση τον αλγόριθμο
 * Gale-Shapley
 */
public class Function_B {
    private VandE[] MSTDistances;
    private int counter;
    private Resource_Manager RM ;
    private HashMap<Integer,Boolean> RedAnts;       //HashMap Δέσμευσης η ελευθερίας για κόκκινα μυρμήγκια
    private HashMap<Integer,Boolean> BlackAnts;     ////HashMap Δέσμευσης η ελευθερίας για μαύρα μυρμήγκια
    private ArrayList<ArrayList<Integer>> RedLists; //Arraylists προτιμήσεων για κάθε ένα από τα κόκκινα μυρμήγκια
    private ArrayList<ArrayList<Integer>> BlackLists;//Arraylists προτιμήσεων για κάθε ένα από τα μαύρα μυρμήγκια
    private HashMap<Integer,Integer> Contracts;     //HashMap "Συμβολαίων δέσμευσης" μεταξύ των ζευγαριών
    private VandE[] SortedMatches;
    private PrintWriter Writer;
    public Function_B(){
        counter=0;
        RM = new Resource_Manager();
        try {
            RM.InsertAnts();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MSTDistances = new VandE[((int) Math.pow(RM.AntArray.size(),2)-RM.AntArray.size())/2];
        RedAnts = new HashMap<>();
        BlackAnts = new HashMap<>();
        Contracts = new HashMap<>();
        RedLists = new ArrayList<>();
        BlackLists = new ArrayList<>();
        SortedMatches = new VandE[RM.AntArray.size()/2];
        try {
            Writer = new PrintWriter("couples.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Συνάρτηση που καλεί της για την δημιουργία και έξοδο των αποτελεσμάτων
     */
    public void FindMatches(){

        InitializeHashMaps();
        InitiliazeLists();
        MatchMake();
        OutputResults();


    }

    /**
     * Σύναρτηση που οργανώνει τα αποτελέσματα για την έξοδο του προγράμματος στο αρχείο
     */
    private void OutputResults(){
        for(int i=1;i<RM.AntArray.size();i=i+2){
            SortedMatches[i/2]= new VandE(Contracts.get(i),i,0);
        }
        InsertionSort();
        for(int i=0;i<SortedMatches.length;i++){
            Writer.print(String.valueOf(SortedMatches[i].from+1));
            Writer.print("  ");
            Writer.println(String.valueOf(SortedMatches[i].to+1));

        }
        Writer.close();
    }

    /**
     * Ταξινόμηση των ζευγαριών με InsertionSort
     */
    private void InsertionSort(){
        for(int i=1;i<SortedMatches.length;i++){
            VandE Okey = SortedMatches[i];
            int j=i-1;
            while(j>=0 && SortedMatches[j].from > Okey.from){
                SortedMatches[j+1] = SortedMatches[j];
                j--;
            }
            SortedMatches[j+1]= Okey;

        }
    }

    /**
     * Συνάρτηση δημιουργίας ζευγαριών
     */
    private void MatchMake() {
        boolean flag=true;                              //Αρχικοποίηση μιας flag σε αληθής
        while(flag){
            for(int i=0;i<RM.AntArray.size();i=i+2){   //Προσπέλασης πίνακα μυρμηγκίων μόνο για τα κόκκινα μέλη
                if(hasProposed(i)){                      //Έλεγχος για ταίριασμα κόκκινων μυρμήγκιων εφόσον είναι ελύθερα
                  if(EndPairingSequence()){
                      flag=false;
                  }
                  else {
                      flag=true;
                  }
                }
                else {
                    for(int j=0;j<RM.AntArray.size()/2;j++){        //Προσπέλαση πίνακα προτιμήσεων του κόκκινου μυρμηγκιού
                        if(hasProposed(i)){break;}
                        if(isEngaged(RedLists.get(i/2).get(j))){      //Έλεγχος για το αν το προτιμόμενο μαύρο μυρμήγκι είναι ελεύθερο
                            if(isBetter(RedLists.get(i/2).get(j),i)){  //Έλεγχος για το αν το κόκκινο είναι καλυτέρο απο το υπάρχον ταίρι
                                BreakUp(RedLists.get(i/2).get(j),i);   //Εφόσον το κόκκινο είναι δεσμευμένο
                                flag=true;
                                break;


                            }


                        }
                        else{
                            Accept(RedLists.get(i/2).get(j),i);
                            flag=true;
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Έλεγχος για τον τερματισμό του αλγορίθμου και επιστροφή
     * @return true or false
     * αντίστοιχα
     */
    private boolean EndPairingSequence() {
        for(int i=0;i<RM.AntArray.size();i++){
           if(i%2==0) {
               if (!RedAnts.get(i)) {
                   return false;
               }
           }
           else {
               if (!BlackAnts.get(i)) {
                   return false;
               }
           }
        }
        return true;
    }

    /**
     * Συνάρτηση διαγραφής ενός ζευγαριού και δημιουργία καινούργιο
     * @param x με αυτά
     * @param y τα id
     */
    private void BreakUp(int x,int y){
        RedAnts.remove(Contracts.get(x));
        RedAnts.put(Contracts.get(x),false);
        Contracts.remove(x);
        Contracts.put(x,y);
        RedAnts.remove(y);
        RedAnts.put(y,true);




    }

    /**
     * Συνάρτηση που διατρέχει τον πίνακα προτιμήσεων
     * @param x του μαύρου μυρμηγκιού
     * για να προσδιορίσει αν το
     * @param y κόκκινο μυρμήγκι είναι καλύτερο ταίρι
     * και επιστρέφει
     * @return true or false
     * αντίστοιχα
     */
    private boolean isBetter(int x,int y ){
        int partner = Contracts.get(x);
        for(int i=0;i<RM.AntArray.size()/2;i++){
           if( BlackLists.get(x/2).get(i)==partner){
               return false;
           }
           if(y==BlackLists.get(x/2).get(i)){
               return true;
           }
        }
          return false;
    }

    /**
     * Συνάρτηση που ελέγχει αν
     * @param x το κόκκινο μυρμήγκι είναι ελεύθερο
     * και επιστρέφει
     * @return true or false
     * αντίστοιχα
     */
    private boolean hasProposed(int x){
        return RedAnts.get(x);
    }
    /**
     * Συνάρτηση που ελέγχει αν
     * @param x το μαύρο μυρμήγκι είναι ελεύθερο
     * και επιστρέφει
     * @return true or false
     * αντίστοιχα
     */
    private boolean isEngaged(int x){
        return BlackAnts.get(x);
    }

    /**
     * Συνάρτηση που δημιουργεί το ζευγάρι
     * @param x
     * @param y
     *
     */
    private void Accept(int x,int y){
        RedAnts.remove(y);
        BlackAnts.remove(x);
        RedAnts.put(y,true);
        BlackAnts.put(x,true);
        Contracts.put(x,y);
    }

    /**
     * Συνάρτηση που δέχεται
     * @param obj ένα VandE
     *  αντικείμενο και το εισάγει στον πίνακα MSTDistances
     */
    public void InitializeDistances(VandE obj){
        MSTDistances[counter]=obj;
        counter++;
    }


    /**
     * Συναρτηση που δημιουργεί του πίνακα προτιμήσεων των μυρμηγκιών
     */
    private void InitiliazeLists() {
        for(int i=0;i<RM.AntArray.size();i++) {
            if (i % 2 == 0) {
                RedLists.add(new ArrayList<>());
                while (RedLists.get(i/2).size() < RM.AntArray.size()/2) {
                    for (int j = 0; j < MSTDistances.length; j++) {
                        if (MSTDistances[j].from == i && MSTDistances[j].to % 2 == 1) {
                            RedLists.get(i/2).add(MSTDistances[j].to);
                            if(RedLists.get(i/2).size()==RM.AntArray.size()/2){
                                break;
                            }
                        }
                        if (MSTDistances[j].to == i&& MSTDistances[j].from % 2 == 1) {
                            RedLists.get(i/2).add(MSTDistances[j].from);
                            if(RedLists.get(i/2).size()==RM.AntArray.size()/2){
                                break;
                            }
                        }
                    }
                }
            }
            else{
                BlackLists.add(new ArrayList<>());
                while (BlackLists.get(i/2).size() < RM.AntArray.size()/2) {
                    for (int j = 0; j < MSTDistances.length; j++) {
                        if (MSTDistances[j].from == i && MSTDistances[j].to % 2 == 0) {
                            BlackLists.get(i/2).add(MSTDistances[j].to);
                            if(BlackLists.get(i/2).size()==RM.AntArray.size()/2){
                                break;
                            }
                        }
                        if (MSTDistances[j].to == i && MSTDistances[j].from % 2 == 0) {
                            BlackLists.get(i/2).add(MSTDistances[j].from);
                            if(BlackLists.get(i/2).size()==RM.AntArray.size()/2){
                                break;
                            }
                        }
                    }

                }
            }
        }

    }

    /**
     * Συνάρτηση που δημιουργεί τα HashMap δέσμευσης για όλα τα μυρμήγκια
     */
    private void InitializeHashMaps() {
        for (int i = 0; i < RM.AntArray.size();i++) {
            if (i % 2 == 0) {
                RedAnts.put(i, false);
            } else {
                BlackAnts.put(i, false);
            }

        }
    }

    
}
