package indexation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CHEMAOU Doha 
 */


public class SPO extends Indexation{
    
    

    public void fillsTriplets(List<String> subjects,List<String> objects , List<String> predicates){
        triplets = new ArrayList<>();

        List triplet = new ArrayList<>();
        int size = subjects.size();
        for (int i = 0 ; i < size ; i++){
            triplet = new ArrayList<>();

            triplet.add(subjects.get(i));
            triplet.add(predicates.get(i));
            triplet.add(objects.get(i));

            triplets.add(triplet);
        }   
    }

    // replacing each resource ith its value
    public void spo(Boolean b) throws IOException{
        indexation("SPO",b);
    }   
}
