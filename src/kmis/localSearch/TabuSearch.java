package kmis.localSearch;

import kmis.structure.Instance;
import kmis.structure.RandomManager;
import kmis.structure.Solution;

import java.util.*;

public class TabuSearch implements ILocalSearch{


    private final Deque<Integer> tabu=new LinkedList<>();
    private final Set<Integer> tabuSet=new HashSet<>();
    private Solution bestSol;
    private final float percentageMaxSolSave =0.4f;
    private int maxSolSave;
    private int maxIterWithoutImprove=10;

    public Solution execute(Solution sol, Instance instance){

        maxSolSave=(int)Math.ceil(sol.getElementsSol().size()* percentageMaxSolSave);

        List<Integer> unSelectedCopy=createUnselectedList(instance,sol);
        List<Integer> selectedCopy = copySelectedPosList(instance);

        int numUnselected=unSelectedCopy.size();
        int numSelected=sol.getElementsSol().size();
        boolean improvement=true;

        BitSet testBitSet = new BitSet(instance.getNumElementsR());
        BitSet originalBitSet = new BitSet(instance.getNumElementsR());
        bestSol = new Solution(sol);

        int countWithoutImprove=0;
        while(improvement){
            improvement = false;
            Collections.shuffle(unSelectedCopy, RandomManager.getRandom());
            Collections.shuffle(selectedCopy, RandomManager.getRandom());
            BitSet bestBitSet = null;
            int bestRemoveNodePos=-1;
            int bestAddNode=-1;
            for (int i = 0; i < numSelected; i++) {
                int posSelectedNode = selectedCopy.get(i);
                int selectedNode = sol.getElementsSol().get(posSelectedNode);
                clearBitSet(testBitSet, instance, sol, selectedNode);
                originalBitSet.clear();
                originalBitSet.or(testBitSet);
                for(int j = 0; j < numUnselected; j++){
                    int unselectedNode = unSelectedCopy.get(j);
                    testBitSet.and(instance.getConnections()[unselectedNode]);

                    int solCard = bestSol.getSol().cardinality();
                    int testBitCard = testBitSet.cardinality();

                    if(solCard<testBitCard) {
                        countWithoutImprove=0;
                        improvement=true;
                        unSelectedCopy.remove(j);
                        unSelectedCopy.add(selectedNode);
                        sol.removeByPos(posSelectedNode);
                        sol.add(unselectedNode);
                        sol.copySol(testBitSet);

                        bestSol.copy(sol);
                        break;
                    }else {
                        countWithoutImprove++;
                        if(bestBitSet == null || bestBitSet.cardinality()<testBitCard){
                            if (bestBitSet == null) bestBitSet = new BitSet(instance.getNumElementsR());
                            bestBitSet.clear();
                            bestBitSet.or(testBitSet);
                            bestRemoveNodePos=posSelectedNode;
                            bestAddNode=unselectedNode;
                        }
                    }

                    if(countWithoutImprove>maxIterWithoutImprove){
                        break;
                    }

                    testBitSet.clear();
                    testBitSet.or(originalBitSet);
                }
                if(countWithoutImprove>maxIterWithoutImprove){
                    break;
                }
                if(improvement) {
                    break;
                }else{
                    tabuSearch(bestRemoveNodePos,bestAddNode,sol);
                }
            }
            if(countWithoutImprove>maxIterWithoutImprove){
                break;
            }
        }
        sol.copy(bestSol);

        return sol;
    }

    private void tabuSearch(int bestRemoveNodePos, int bestAddNode,Solution sol){
        if(tabu.size()==maxSolSave){
            tabuSet.remove(tabu.removeFirst());
        }
        tabu.add(bestAddNode);
        tabuSet.add(bestAddNode);
        sol.add(bestAddNode);
        sol.removeByPos(bestRemoveNodePos);
    }

    private void clearBitSet(BitSet testBitSet, Instance instance, Solution sol, int remove) {
        testBitSet.clear();
        boolean first = true;
        for (int s : sol.getElementsSol()) {
            if (s != remove) {
                if (first) {
                    testBitSet.or(instance.getConnections()[s]);
                    first = false;
                } else {
                    testBitSet.and(instance.getConnections()[s]);
                }
            }
        }
    }

    private List<Integer> createUnselectedList(Instance instance, Solution sol){
        List<Integer> unSelected=new ArrayList<>(instance.getNumElementsL());
        for (int i = 0; i < instance.getNumElementsL(); i++) {
            if(!sol.getElementsSol().contains(i)){
                unSelected.add(i);
            }
        }
        return unSelected;
    }

    private List<Integer> copySelectedList(List<Integer> selected){
        List<Integer> selectedCopy=new ArrayList<>(selected.size());
        selectedCopy.addAll(selected);
        return selectedCopy;
    }

    private List<Integer> copySelectedPosList(Instance instance) {
        int p = instance.getNumElementsSol();
        List<Integer> selectedCopy = new ArrayList<>(p);
        for (int i = 0; i < p; i++) {
            selectedCopy.add(i);
        }
        return selectedCopy;
    }

    @Override
    public String toString() {
        return " LocalSearchEfficient(TabuSearch)";
    }
}
