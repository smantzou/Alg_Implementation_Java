
//AEM:2895
//Στυλιανός Μαντζουράνης
//smantzou@csd.auth.gr

import java.io.*;
import java.lang.Math;
import java.util.ArrayList;


/**
 * Η κλάση Function_A υλοποιεί των κώδικα για την εύρεση του Minimum Spanning Tree
 * και των ακμών του.
 */
public class Function_A {
    private Resource_Manager objRM;
    private Object[][] Distances;//Πίνακας αποστάσεων
    private VandE[] Sort;
    private ArrayList<Union_Find> Groups;
    private Union_Find objUF;
    private int[][] Output ;
    private PrintWriter Writer;
    public Function_A(){
        objRM=new Resource_Manager();
        try {
            objRM.InsertAnts();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Distances = new Object[objRM.AntArray.size()][objRM.AntArray.size()];
        Sort = new VandE[((int) Math.pow(objRM.AntArray.size(),2)-objRM.AntArray.size())/2];   //Αρχικοποίηση του πίνακα έτσι ώστε να έχει το μέγεθος του πίνακα των
                                                                                                // αποστάσεων εκτός της κύριας διαγώνιου και των στοιχείων άνω αυτής

        Groups = new ArrayList<>();
        objUF = new Union_Find();
        Output = new int[objRM.AntArray.size()][objRM.AntArray.size()];
        try {
            Writer= new PrintWriter("output.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Συνάρτηση στην οποία γίνεται η εύρεση του Minimum Spanning Tree
     * μέσω των υπόλοιπων συναρτήσεων της κλάσης
     */
    public void FindMST(){
        CreateDistanceTable();
        CreateVandEs();
        InsertionSort();
        CreateUnionFinds();
        OutputResults();
        CreateDistanceTable();   //Επανάκληση των τριών αυτών συναρτήσεων μιας και ο πίνακας Sort
        CreateVandEs();          //κατεστράφει,αφού τοποθέτησαμε null στις μη χρησιμοποιημένες θέσης
        InsertionSort();        // Η επανάκληση γίνεται για να επιστραφούν οι τιμές του Sort στην Function_B



    }


    /**
     * Συνάρτηση που επιστρέψει ένα αντικείμενο κλάσης VandE το οποίο δεν ειναι null
     * @return load
     * μέσω της main στην Function_B
     * έπειτα τοποθετεί null στην θέση στην οποία βρισκόταν
     */
    public VandE ReturnDistances(){
        for(int i = 0; i< Sort.length; i++){
            if(Sort[i]!=null){
                VandE load = Sort[i];

                Sort[i]=null;
                return load;

            }


        }
        return null;




    }

    /**
     * Συνάρτηση που δέχεται δύο ακέραιους αριθμούς
     * @param x
     * @param y
     * επιστρέφει
     * @return true ή false
     * αν υπάρχουν και οι δύο ακέραιοι στον Sort πίνακα αντικειμένων
     */
    private boolean CheckSorted(int x,int y){

        for(int i = 0; i< Sort.length; i++) {
            if (Sort[i] != null) {
                if (Sort[i].from==x|| Sort[i].to==x){
                    if(Sort[i].from==y|| Sort[i].to==y){



                        return true;
                    }
                }

            }


        }
        return false;
    }

    /**
     * Συνάρτηση η οποία μορφοποιεί τα δεδομένα με κατάλληλο τρόπο έτσι ώστε να γίνει η έξοδος τους στο αρχείο
     */
    private void OutputResults(){

        int k=0;
         for(int i=0;i<objRM.AntArray.size();i++) {


             for (int j = i+1 ; j < objRM.AntArray.size(); j++) {

                 if (CheckSorted(Groups.get(0).ReturnMember(i), (Groups.get(0).ReturnMember(j)))) {


                     Output[k][0] = Groups.get(0).ReturnMember(i);
                     Output[k][ 1] = Groups.get(0).ReturnMember(j);

                     k++;




                 }


             }


         }






        for(int i=0;i<objRM.AntArray.size();i++) {
            if (Output[i][0]>Output[i][1]) {
                int x = Output[i][0];
                Output[i][0]=Output[i][1];
                Output[i][1]=x;;


            }


        }

        BubbleSort();
        BubbleSortY();



        Writer.println(Groups.get(0).ReturnSpanning());
        for (int i=0;i<objRM.AntArray.size()-1;i++){
            String from = String.valueOf(Output[i][0]+1);
            String to = String.valueOf(Output[i][1]+1);
            Writer.print(from);
            Writer.print("          ");
            Writer.println(to);
        }
        Writer.close();






    }


    /**
     * Ταξινόμηση φυσαλίδας ως προς το πρώτο id
     */
    private void BubbleSort() {
        int n = objRM.AntArray.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 2; j++) {
                if(Output[j][0]>Output[j+1][0]){
                    int tempx=Output[j][0];
                    int tempy=Output[j][1];
                    Output[j][0]=Output[j+1][0];
                    Output[j][1]=Output[j+1][1];
                    Output[j+1][0]=tempx;
                    Output[j+1][1]=tempy;


                }

            }


        }
    }

    /**
     * Ταξινόμηση φυσαλίδας ως προς το δεύτερο id
     */
    private void BubbleSortY(){
        int n = objRM.AntArray.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 2; j++) {
                if(Output[j][0]==Output[j+1][0]&&Output[j][1]>Output[j+1][1]) {
                    int tempx=Output[j][0];
                    int tempy=Output[j][1];
                    Output[j][0]=Output[j+1][0];
                    Output[j][1]=Output[j+1][1];
                    Output[j+1][0]=tempx;
                    Output[j+1][1]=tempy;


                }

            }


        }


    }

    /**
     * Συνάρτηση η οποία δημιουργεί αντικείμενα της Union Find με βάση τον Sort πίνακα αντικειμένων.
     *
     */
    private void CreateUnionFinds() {
        Groups.add(new Union_Find(Sort[0].from, Sort[0].to, Sort[0].distance));
        for(int i = 1; i< Sort.length; i++){                         //Επανάληψη που ξεκινά απο το δεύτερο αντικείμενο μιας και οι ακμές
             int from = Sort[i].from;                                //του πρώτου έχουν γίνει αυτομάτως ένα group
             int to = Sort[i].to;
             double distance = Sort[i].distance;
            boolean flag = false;                                    //Αρχικοποίηση μιας τοπικής flag η οποία ελέγχει αν εισάχθηκε κάπου ο κόμβος
            for(int l=0;l<Groups.size();l++){                        //Εισαγωγή null σε όλους τους κόμβους που δεν χρησιμοποιήθηκαν για την δημιουργία του MST
                if(Groups.get(l).Size()==objRM.AntArray.size()){     //Εφόσον το MST είναι με των αριθμό των μυρμηγκιών
                    Sort[i]=null;
                }
            }
            if(Groups.size()>1){                                   //Έλεγχος για ένωση
                CheckForUnification();
            }


             for(int j=0;j<Groups.size();j++){                     //Επανάληψη η οποία διατρέχει την Arraylist Groups

                 if((Groups.get(j).CheckFor(from))&&(Groups.get(j).CheckFor(to))){      //Εφόσον τα id που εξετάζουμε
                                                                                        //υπάρχουν και τα δύο
                                                                                         //δεν εισάγονται για την αποφυγή κύκλου
                     flag = true;
                     Sort[i]=null;

                     break;

                 }
                 if(Groups.get(j).CheckFor(from)){                      //Αν υπάρχει το ένα κόμματι του κόμβου σε ένα
                     Groups.get(j).AddtoGroup(to,distance);             //εισάγουμε το άλλο
                     flag =true;
                     break;
                 }
                 if(Groups.get(j).CheckFor(to)){
                     Groups.get(j).AddtoGroup(from,distance);
                     flag =true;
                     break;
                 }

             }
            if(!flag){                                                  //Αν η τιμή της flag μείνει ίδια
                assert Sort[i] != null;                                 //Δημιουργούμε ένα καινούργιο group με τον κόμβο
                Groups.add(new Union_Find(Sort[i].from, Sort[i].to, Sort[i].distance));
            }




        }




    }

    /**
     * Συνάρτηση η οποία καλεί την ένωση(Unify της Union Find) αν βρεί σύνδεση μεταξύ δυο group
     * έπειτα αφερούνται τα group που ενώθηκαν απο την Arraylist Groups
     */
    private void CheckForUnification(){
        for(int i=0;i<Groups.size();i++){
            for(int j=i+1;j<Groups.size();j++){
                  if(objUF.CheckForSimilar(Groups.get(i),Groups.get(j))){
                      Groups.add(objUF.Unify(Groups.get(i),Groups.get(j)));
                      Groups.remove(Groups.get(j));
                      Groups.remove(Groups.get(i));

                  }


            }

        }


    }

    /**
     * Δημιουργία αντικειμένων VandE και εισαγωγή τους στον πίνακα Sort
     */
    private void CreateVandEs(){
        int k=0;
        for(int i=1;i<objRM.AntArray.size();i++){
            for(int j=0;j<i;j++){
                VandE ve =  new VandE(i,j, (Double) Distances[i][j]);
                Sort[k]= ve;

                k++;
            }

        }

    }

    /**
     * Εύρεση των αποστάσεων μεταξύ όλων των μυρμηγκιών με βάση τον τύπο της απόστασης στο επίπεδο (d= √(x2-x1)2+(y2-y1)2)
     */
    private void CreateDistanceTable(){
        for(int i=0;i<objRM.AntArray.size();i++){

            for(int j=0;j<objRM.AntArray.size();j++){
                if(i==j){
                    Distances[i][j]= null;
                }
                else {
                    Distances[i][j] = Math.sqrt(Math.pow((objRM.AntArray.get(j).coord_x - objRM.AntArray.get(i).coord_x), 2) + Math.pow((objRM.AntArray.get(j).coord_y - objRM.AntArray.get(i).coord_y), 2));
                }


            }


        }
    }

    /**
     * Ταξινόμηση του πίνακα αντικειμένων Sort με InsertionSort
     */
    private void InsertionSort(){
        for(int i = 1; i< Sort.length; i++){
            VandE Okey = Sort[i];
            int j=i-1;
            while(j>=0 && Sort[j].distance > Okey.distance){
                Sort[j+1] = Sort[j];
                j--;
            }
            Sort[j+1]= Okey;

        }

    }



}
