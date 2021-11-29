package indexation;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.github.jsonldjava.shaded.com.google.common.collect.BiMap;

import dictionnaire.Dictionnaire;

/**
 * @author CHEMAOU Doha 
 */

public abstract class Indexation {
    String devider = "____________\n";
    List<String> subjects = Dictionnaire.getSubjects();
    List<String> objects = Dictionnaire.getObjects();
    List<String> predicates = Dictionnaire.getPredicates();
    static List<List<String>> triplets = new ArrayList<>();
    public static HashMap<Long, HashMap<Long, List<Long>>> indexations;
    int m = 0;


    abstract public void fillsTriplets(List<String> subjects,List<String> objects , List<String> predicates);
    
    //should be called after fillsTriplets
    public void fills_indexation(Boolean b){
        m++;
        List l = new ArrayList<>();
        HashMap hash = new HashMap<>();
        indexations = new HashMap<>();
        for (int i = 0 ; i < triplets.size(); i++){
            long two_ = Dictionnaire.getKey(triplets.get(i).get(2));
            long one_ = Dictionnaire.getKey(triplets.get(i).get(1));
            long zero_ = Dictionnaire.getKey(triplets.get(i).get(0));
            if(m==2 && b){
                System.out.println(triplets.get(i));
                System.out.println(two_);
                System.out.println(one_);
                System.out.println(zero_);
            }
            
            if(indexations.get(zero_)==null){
                l = new ArrayList<>();
                l.add(two_);
                hash = new HashMap<>();
                hash.put(one_,l);
                indexations.put(zero_,hash);
            }
            else {
                if(indexations.get(zero_).get(one_)==null){
                    hash = indexations.get(zero_);
                    l = new ArrayList<>();
                    l.add(two_);
                    hash.put(one_,l);
                    indexations.put(zero_,hash);
                }
                else{
                    l = indexations.get(zero_).get(one_);
                    l.add(two_);
                    hash = indexations.get(zero_);
                    hash.put(one_,l);
                    indexations.put(zero_,hash);
                }
            }
        }
        
    }

    public void fills(Boolean b){
        fills_indexation(b);
        String first="",second="",third="";
        for (long i : indexations.keySet()) {
            
              first = Dictionnaire.key_resource.get(i) ;
              if(first.contains("//"))
                first = first.split("/")[first.split("/").length-1] ;
              
              for(long j : indexations.get(i).keySet()){
                  second = Dictionnaire.key_resource.get(j);
                  if(second.contains("//"))
                    second = second.split("/")[second.split("/").length-1] ;
                  
                  
                  for(long k : indexations.get(i).get(j)){
                      third = Dictionnaire.key_resource.get(k);
                      if(third.contains("//"))
                        third = third.split("/")[third.split("/").length-1] ;
                      if(b)
                        System.out.println(first+"_"+second+" : "+third);
                  }
              }
          }
    }

    public static List<List<String>> getTriplets(){
        return triplets;
    }

    public void indexation_entete(String indexation){
        System.out.println();
        int number_of_dashes = 2 + "indexation".length()+ 3 + 1 ;
        System.out.print("\t     ");
        for(int i = 0 ; i < number_of_dashes ; i++) 
            System.out.print("-");
        
        System.out.println();
        System.out.println("\t    | indexation "+indexation+" |");

        System.out.print("\t     ");
        for(int i = 0 ; i < number_of_dashes ; i++) 
            System.out.print("-");

        System.out.println();
    }

    public void indexation(String indexation,Boolean b) throws IOException{
        indexation_entete(indexation);

        fillsTriplets(subjects,objects,predicates);

        fills(b);

        if(b)
            System.out.println(devider);

        corps_indexation(b);

        writeDot(indexation);
        if(!b)
            System.out.println("done indexing");

    }

    public void corps_indexation (Boolean b){
        BiMap<String,Long> resource_key = (BiMap<String, Long>) Dictionnaire.getResource_key();
        int size = subjects.size();
        if(b){
            for (int i = 0 ; i < size ; i++){
                int size_ = 3;
                for (int j = 0 ; j < size_ ; j++){
                    System.out.print(resource_key.get(triplets.get(i).get(j))+"\t");
                }
                System.out.println();
            }
            System.out.println("\n");
        }
    }

    void writeDot(String indexation) throws IOException{
        try(BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("dots/"+indexation+".dot")))){
          out.write("digraph {"); 
          out.newLine();
          String first="",second="",third="";
          for (long i : indexations.keySet()) {
            //   graph = "";
              first = Dictionnaire.key_resource.get(i) ;
              if(first.contains("//"))
                first = first.split("/")[first.split("/").length-1] ;
              first = "\""+first+"\"";
              for(long j : indexations.get(i).keySet()){
                  second = Dictionnaire.key_resource.get(j);
                  if(second.contains("//"))
                    second = second.split("/")[second.split("/").length-1] ;
                  second = "\""+second+"\"";
                  out.write(first+" -> "+second);
                  out.newLine();
                  for(long k : indexations.get(i).get(j)){
                      third = Dictionnaire.key_resource.get(k);
                      if(third.contains("//"))
                        third = third.split("/")[third.split("/").length-1] ;
                      third = "\""+third+"\"";
                      out.write(second+" -> "+third);
                      out.newLine();
                  }
              }
          }
          out.write("}");
        }
      }
    
}