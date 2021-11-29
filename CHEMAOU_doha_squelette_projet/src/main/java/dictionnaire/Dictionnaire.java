package dictionnaire;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.io.IOException;

import com.github.jsonldjava.shaded.com.google.common.collect.BiMap;
import com.github.jsonldjava.shaded.com.google.common.collect.HashBiMap;


/**
 * @author CHEMAOU Doha 
 */

public class Dictionnaire {   

    // attributes 

    static BiMap<String,Long> resource_key = HashBiMap.create();
    public static BiMap<Long, String> key_resource;


    static List<String> subjects = new ArrayList<>();
    static List<String> objects = new ArrayList<>(); 
    static List<String> predicates = new ArrayList<>();

    // for more organised printings, aesthetics
    static int max_index = 0 ;
    static int max_resource = 0;
    static int index;


    // Methods 

    public static BiMap<String,Long> getResource_key(){
        return resource_key;
    }

    public static void addSubject(String subject){
        subjects.add(subject);
    }

    public static void addObject(String object){
        objects.add(object);
    }
    
    public static void addPredicate(String predicate){
        predicates.add(predicate);
    }

    public static List<String> getSubjects(){
        return subjects;
    }

    public static List<String> getObjects(){
        return objects;
    }

    public static List<String> getPredicates(){
        return predicates;
    }

    public static long getKey(String resource){
        return resource_key.get(resource);
    }

    public static void fill_resource_key(String object,String subject, String predicate){
        if(resource_key.get(object)==null){
            index = object.hashCode();
            long index_ = Integer.toUnsignedLong(index);
            max_index = max_index<index? index:max_index;
            max_resource = max_resource < object.length()? object.length():max_resource;
            resource_key.put(object,index_);
        }
        if(resource_key.get(subject)==null){
            index = subject.hashCode();
            long index_ = Integer.toUnsignedLong(index);
            max_index = max_index<index? index:max_index;
            max_resource = max_resource < subject.length()? subject.length():max_resource;
            resource_key.put(subject,index_);
        }
        if(resource_key.get(predicate)==null){
            index = predicate.hashCode();
            long index_ = Integer.toUnsignedLong(index);
            max_index = max_index<index? index:max_index;
            max_resource = max_resource < predicate.length()? predicate.length():max_resource;
            resource_key.put(predicate,index_);
        }
    }

    public static void displaying_dictionary(String dataFile)throws IOException{
        int size = subjects.size();

        for(int i = 0 ; i < size;i++){

            String object = objects.get(i);
            String subject = subjects.get(i);
            String predicate = predicates.get(i);

            fill_resource_key(object,subject,predicate);
            
        }
          
        // displaying dictionary
        entete_dictionnaire();
        key_resource = resource_key.inverse();

        if(!dataFile.contains("100K.nt")){
            affichage_entete(dataFile);
            affiche_dictionnaire();
        }
        else{
            System.out.println("la taille du dictionnaire est : " + resource_key.size());
        }  
    }

    public static void entete_dictionnaire(){
        System.out.println();
        int number_of_dashes = 2 + "Dictionnaire".length() ;
        System.out.print("\t     ");
        for(int i = 0 ; i < number_of_dashes ; i++) 
            System.out.print("-");
        
        System.out.println();
        System.out.println("\t    | Dictionnaire "+"|");

        System.out.print("\t     ");
        for(int i = 0 ; i < number_of_dashes ; i++) 
            System.out.print("-");

        System.out.println("\n");

    }

    public static void affichage_entete (String dataFile){

        int  number_of_underscores = 0;
        if(dataFile.contains("sample_data"))
            number_of_underscores = Integer.toString(max_index).length() + 8 + 8 + max_resource;
        else {
            number_of_underscores = Integer.toString(max_index).length() + 8 + 8 + 60;

        }
        
        int spaces_btween_key_resource = Integer.toString(max_index).length() + 3 + 8; // head of the printing
        String white_spaces = "";
        for (int i = 0; i < spaces_btween_key_resource ; i ++)
            white_spaces+=" ";
        
        System.out.println("key"+white_spaces+"resource");
        for (int i = 0 ; i < number_of_underscores ;i++){
            System.out.print("-");
        }
        System.out.println();
    }

    public static void affiche_dictionnaire(){
        // using treeMap to have sorted keys, from the lowest to the highest, just for the display
        // key_resource also used in Indexation
        Map<Long, String> map = new TreeMap<>(key_resource);

        for (Map.Entry<Long, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "\t\t" + entry.getValue());
        }

    }
    
}

