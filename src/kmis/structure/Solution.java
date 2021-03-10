package kmis.structure;
import kmis.Main;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Solution {

    private final Instance instance;
    private BitSet sol;
    private List<Integer> elementsSol;

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

    public void calculateAllJoins(){
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

    public void remove(int elem) {
        elementsSol.remove(Integer.valueOf(elem));
    }

    public void removeByPos(int pos) {
        elementsSol.remove(pos);
    }

    public Solution clone(){
        Solution solution=new Solution(instance);
        solution.sol=this.sol;
        solution.elementsSol=this.elementsSol;
        return solution;
    }

    public List<Integer> getElementsSol() {
        return elementsSol;
    }

    public int getObjectiveFunction(){
        return sol.cardinality();
    }

    public BitSet getSol() {
        return sol;
    }

    public void setSol(BitSet sol) {
        this.sol = sol;
    }

    public void copySol(BitSet sol) {
        this.sol.clear();
        this.sol.or(sol);
    }


}
