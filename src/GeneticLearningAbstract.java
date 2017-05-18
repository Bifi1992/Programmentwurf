import java.util.ArrayList;
import java.util.List;

/**
 * Created by ludwig on 17.05.17.
 */
public class GeneticLearningAbstract {
    Double mutation = 0.01;
    Integer steps = 100;
    Integer goal = 100;
    List<Double> population = new ArrayList<Double>();
    List<Double> parents = new ArrayList<Double>();


    public void createPopulationRandom() {
        for (Integer i = 0; i < 4; i++) {
            this.population.add((double) (Math.random() * ((100) + 1)));
        }

        System.out.println("Population Set");
        for (Integer j = 0; j < this.population.size(); j++) {
            System.out.println(population.get(j));
        }
    }

    public void getFitness(){
        List<Double> sub = new ArrayList<Double>();
        Double min = 0.0;
        Double secndmin = 0.0;
        Double item = 0.0;
        Double itemorig = 0.0;

        for (Integer j = 0; j < this.population.size(); j++) {
            item = (double)Math.abs(goal - population.get(j));
            itemorig =  (double)population.get(j);
            if(j == 0){
                min = itemorig;
                secndmin = itemorig;
            }
            else if(Math.abs(goal-min) > item){
                secndmin = min;
                min = itemorig;

            }else if(Math.abs(goal-secndmin) > item){
                secndmin = itemorig;
            }
            //System.out.println("Sub:  " + item);
            //sub.add(item);
        }
        System.out.println("Best Values ||  Min 1:  " + min + "  Min: 2  " + secndmin);
        parents.clear();
        parents.add(min);
        parents.add(secndmin);
    }
    public void manipulateParents(){
        Double optimumvalue = 0.0;
        for(Integer i = 0; i < parents.size(); i++){
            System.out.println("Parent " + i + " = " + parents.get(i));
            optimumvalue += parents.get(i);
        }
        optimumvalue = optimumvalue/2;
        System.out.println("optimized step 1 " + optimumvalue);
        calculateNewPopulation(optimumvalue);
    }
    public void variatePopulation(){

    }
    public void calculateNewPopulation(Double optimumvalue){
        population.clear();
        Integer sign = 1;
        for(Integer i = 0; i < 4; i++){
            this.population.add((double) (optimumvalue - sign*(Math.random() * ((2) + 1))));
            System.out.println("Optimized Value: " + ((optimumvalue - sign*(Math.random() * ((2) + 1)))));
            sign = sign * (-1);
        }
        /*for(Integer i = 0; i < 2; i++){
            this.population.add((double) (optimumvalue - sign*(Math.random() * ((2) + 1))));
            System.out.println("Optimized Value: " + ((optimumvalue - sign*(Math.random() * ((2) + 1)))));
            sign = sign * (-1);
        }*/
    }

}
