//AEM:2895
//Στυλιανός Μαντζουράνης
//smantzou@csd.auth.gr
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Η κλάση Function_C υλοποιεί τον κώδικα για τον έλεγχο πλήρους γεμίσματος των κάδων των κόκκινων μυρμηγκιών
 * με την χρήση Δυναμικού Προγρμματισμού
 */
public class Function_C {
    private Resource_Manager ResMa;
    private PrintWriter writer;
    private int[][] Seeds;
    private int[] Result; //Πίνακας αποτελεσμάτων
    private ArrayList<int[]> ResultList; //Arraylist απο πίνακες αποτελεσμάτων
    private ArrayList<Pair> Pairs; //Arraylist από αντικείμενα Pair
    private  static final int Const = 2000000000;  //Ακέραια σταθερά
    private int i;
    public Function_C(){
        ResMa = new Resource_Manager();
        try {
            ResMa.InsertAnts();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer= new PrintWriter("filling.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Pairs = new ArrayList<>();

        ResultList = new ArrayList<>();


    }

    /**
     * Συναρτήση που καλεί τις συναρτήσεις για το γέμισμα των κάδων και της εξόδου στο αρχείο
     */
    public void Fill(){
         MakePairs();
         FillPairs();
         OutputResults();


    }

    /**
     * Σύναρτηση που οργανώνει τα αποτελέσματα για την έξοδο του προγράμματος στο αρχείο
     */
    private void OutputResults() {
        for(int i=0;i<Pairs.size();i++){

            if(ResultList.get(i)!=null){
                writer.print(Pairs.get(i).a.id);
                writer.print(" ");
                writer.print(Pairs.get(i).b.id);
                writer.print(" ");
                for(int j=0;j<Result.length;j++){
                    writer.print(ResultList.get(i)[j]);
                    writer.print(" ");
                }

            }
            if(ResultList.get(i)!=null) {
                writer.println();
            }
        }
        writer.close();

    }

    /**
     * Συνάρτηση που δημιουργεί τον πίνακα αποτελεσμάτων για τους σπόρους
     */
    private void FillPairs() {
        while(i<Pairs.size()){                              //Προσπέλαση του πίνακα ζευγαριών
            Seeds= new int[6][Pairs.get(i).a.capacity+1];
            for(int j=0;j<6;j++){                           //Μηδενισμός πρώτης στήλης
                Seeds[j][0]=0;
            }
            for(int j=1;j<Pairs.get(i).a.capacity+1;j++){   //Εισαγωγή της σταθεράς στην πρώτη γραμμή
                Seeds[0][j]= Const;
            }
            for(int k=1;k<6;k++){
                for(int j=1;j<Pairs.get(i).a.capacity+1;j++){
                    Compute(k,j, Pairs.get(i).b.seedweight.get(k-1));//Υπολογισμός στοιχείων πινάκων

                }


            }

            if(Seeds[5][Pairs.get(i).a.capacity]!=Const){    //Έλεγχος για το αν είναι δυνατό το γέμισμα του κάδου
                Seedify();
            }
            else{
                ResultList.add(null);

            }

            System.out.println(" " +
                    "                 ");




            i++;
        }

    }

    /**
     * Συνάρτηση "σποροποίησης" δηλαδή της εύρεσης της ποσότητας σπόρων με βάση τον πίνακα Seeds
     */
    private void Seedify() {
        Result = new int[5];            //Αρχικοποίηση πίνακα αποτελεσμάτων
        int c=Pairs.get(i).a.capacity;
        int l= 5;
        while(Seeds[l][c]!=0){
            if(c>=Pairs.get(i).b.seedweight.get(l-1)){       //Υλοποίηση αλγόριθμου εντοπισμού ποσότητας σπόρων
                if(trackmin(Seeds[l-1][c],1+Seeds[l][c-Pairs.get(i).b.seedweight.get(l-1)])=="UP"){

                    l--;
                }
                else {
                    c=c-Pairs.get(i).b.seedweight.get(l-1);
                    Result[l-1]++;
                }

            }
            else{
                l--;

            }
            if(Seeds[l][c]==0){
                break;
            }



        }
        ResultList.add(Result);



    }


    /**
     * Συνάρτηση που υπολογίζει την τιμή μιας θέσης του πίνακα
     * @param k δέχεται την γραμμή
     * @param j ,την στήλης
     * @param Vi και το βάρος του σπόρου
     */
    private void Compute(int k,int j,int Vi){
        if(j<Vi){
            Seeds[k][j]= Seeds[k-1][j];

        }
        else{
            Seeds[k][j]= min(1+Seeds[k][j-Vi],Seeds[k-1][j]);

        }

    }

    /**
     * Συνάρτηση που δέχεται την
     * @param up την τιμή του στοιχείου Seeds[i-1][j](του από πανω από αυτού που εξετάζουμε)
     * @param left την τιμή του στοιχείου 1+Seeds[i][j-Vi ]
     * @return και επιστρέφει την κίνηση που πρέπει να διαγράψουμε στον πίνακα
     */
    private String trackmin(int up,int left){
        if(up<left){
            return  "UP";
        }
        else{
            return "LEFT";
        }

    }

    /**
     * Συνάρτηση που δέχεται δύο
     * @param x ακέραιες
     * @param y μεταβλητές
     * @return και επιστρέψει το ελάχιστον των δύο
     */
    private int min(int x,int y){
        if(x>y){
            return y;
        }
        else {
            return x;
        }

    }

    /**
     * Συνάρτηση που δημιουργεί των πίνακα ζευγαριών
     */
    private void MakePairs() {
        for(int i=0;i<ResMa.AntArray.size()-1;i=i+2){
            Pairs.add(new Pair(ResMa.AntArray.get(i),ResMa.AntArray.get(i+1)));

        }

    }
}
