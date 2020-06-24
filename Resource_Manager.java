//AEM:2895
//Στυλιανός Μαντζουράνης
//smantzou@csd.auth.gr


import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

/**
 * Κλάση η οποία δημιουργήθηκε , για το "διάβασμα" του αρχειού data.txt
 * και την δημιουργία τον πίνακα μυρμηγκιών
 */
public class Resource_Manager {
    public ArrayList<Ant> AntArray;

    private Scanner scRead;
    private Ant ant;
    private File file;
    public Resource_Manager()

    {

        AntArray= new ArrayList<>(0);
        file = new File(FileReader.path);
        try {
            scRead = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Δημιουργία πίνακα αντικειμένου μυρμηγκιών κατά το διάβασμα του αρχείου
     * @throws IOException
     */
    public void InsertAnts() throws IOException {
        while(scRead.hasNext()){
               ant = new Ant(scRead.nextInt());

               ant.InsertCoords(scRead.nextDouble(),scRead.nextDouble());
               if ((ant.colour).equals("black")){
                   for(int i=0;i<5;i++){
                       ant.DefineSeed(scRead.nextInt());
                   }
               }
               else {
                   ant.DefineCapacity(scRead.nextInt());
               }
               AntArray.add(ant);
        }
        scRead.close();


    }

}
