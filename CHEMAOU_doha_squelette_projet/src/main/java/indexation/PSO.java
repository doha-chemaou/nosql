package indexation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CHEMAOU Doha 
 */

public class PSO extends Indexation{

    public void fillsTriplets(List<String> subjects,List<String> objects , List<String> predicates){
        triplets = new ArrayList<>();

        List triplet = new ArrayList<>();
        int size = subjects.size();
        for (int i = 0 ; i < size ; i++){
            triplet = new ArrayList<>();

            triplet.add(predicates.get(i));
            triplet.add(subjects.get(i));
            triplet.add(objects.get(i));

            triplets.add(triplet);
        }    
    }

    public void pso(Boolean b) throws IOException{
        indexation("PSO",b);
    } 
}
