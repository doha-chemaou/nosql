package queries_results;
import indexation.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dictionnaire.*;

public class QueriesResults {
    public static List<String> branches = new ArrayList<>();
    static List<Long> keys = new ArrayList<>();
    public static List<Long> subjects = new ArrayList<>();
    public static List<Long> common_subjects = new ArrayList<>();
    public static String results = "";
    public static String result = "";

    // predicate , object , subject 
    static public void results (String query_){
       
            keys = new ArrayList<>();
            subjects = new ArrayList<>();
            common_subjects = new ArrayList<>();
            for (int i = 0 ; i < branches.size();i++){
                Long key = Dictionnaire.getResource_key().get(branches.get(i));
                keys.add(key);
            }
            int k;
            for (k = 0 ; k < keys.size(); k+=2){
                for(int i = 0 ; i < POS.getTriplets().size() ; i++){
                    Long predicate = Dictionnaire.getResource_key().get(POS.getTriplets().get(i).get(0));
                    Long object = Dictionnaire.getResource_key().get(POS.getTriplets().get(i).get(1));
                    Long subject = Dictionnaire.getResource_key().get(POS.getTriplets().get(i).get(2));

                    if(keys.get(k)==predicate && keys.get(k+1)==object){
                        if(k == 0){
                            if(!subjects.contains(subject))
                                subjects.add(subject);
                        }
                        else{
                            if(subjects.contains(subject) && !common_subjects.contains(subject)){
                                common_subjects.add(subject);
                            }
                        }
                    }
                }
                if(k!=0){
                    subjects = common_subjects;
                    common_subjects = new ArrayList<>();
                }
                
                if(subjects.isEmpty()) k = keys.size();
            }
 
            // if(subjects.size()!=0) 
            String a = "```PAS DE RESULTATS```";
            results +=  query_ +"\n----> Resultats : "+(subjects.size()==0?a:"")+"\n";//+"Resultats : \n";
            for(int i = 0 ; i < subjects.size();i++){
                result = Dictionnaire.key_resource.get(subjects.get(i));
                results+= result +"\n";
            }
            // if(subjects.size()==0) results+= "Pas de resultats";
            results+="\n----------------------------------------------\n";
   
}

    public static void writes_into_file(String contenu,String path){
        try {

            BufferedWriter fWriter = new BufferedWriter(new FileWriter(path));
            fWriter.write(contenu);
            fWriter.close();
    }

    // Catch block to handle if exception occurs
        catch (IOException e) {
        // Print the exception
            System.out.print(e.getMessage());
    }

    }

    public static void all(String query_){
        results(query_);
        writes_into_file(results,"C:\\mes_dossiers\\S3\\914 - noSQL\\CHEMAOU_doha_squelette_projet\\src\\main\\java\\queries_results\\results.md");

    }
}

