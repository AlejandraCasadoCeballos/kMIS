package kmis.structure;
import kmis.Main;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Solution {

    private final Instance instance;
    private final BitSet sol;
    private final List<Integer> elementsSol;

    public Solution(Instance instance){
        this.instance=instance;
        this.elementsSol=new ArrayList<>(instance.getNumElementsSol());
        sol=new BitSet();


    }

    public void printSol(){
        System.out.print("Los elementos escogidos son: ");
        for (int e: elementsSol){
            System.out.print((e+1)+" ");
        }

        System.out.println();
        System.out.print("Tienen en común "+ sol.cardinality()+" elementos y son: ");
        for(int i=0;i<sol.length();i++){
            if(sol.get(i)){
                System.out.print(i+1+" ");
            }
        }
    }

    private void calculateAllJoins(){
        sol.clear();
        sol.or(instance.getConnections()[elementsSol.get(0)]);
        int size = elementsSol.size();
        for(int i=1; i<size;i++){
            sol.and(instance.getConnections()[elementsSol.get(i)]);
        }
    }

    public void add(int elem) {
        BitSet elemConn = instance.getConnections()[elem];
        if(elementsSol.isEmpty()){
            sol.or(elemConn);
        } else {
            sol.and(elemConn);
        }
        elementsSol.add(elem);
    }

}
