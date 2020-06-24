//AEM:2895
//Στυλιανός Μαντζουράνης
//smantzou@csd.auth.gr

import java.util.Scanner;

public class Main {
private static FileReader objFR ;
private static Scanner sc;



    /**
     * Κλήση συναρτήσεων και δημιουργία αντικειμένων για την υλοποίηση των λειτουργιών Α,Β και Γ κατά σειρά
     * @param args
     */
    public static void main(String[] args) {
        sc = new Scanner(System.in);
        System.out.println("Please insert filename:");
        objFR = new FileReader(sc.next());


         Function_A objA= new Function_A();
         objA.FindMST();
         Function_B objB = new Function_B();
         VandE carrier = objA.ReturnDistances();
         while(carrier!=null){
             objB.InitializeDistances(carrier);
             carrier = objA.ReturnDistances();

         }
         objB.FindMatches();
         Function_C objC = new Function_C();
         objC.Fill();

    }
}
