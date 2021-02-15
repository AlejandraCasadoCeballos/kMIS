package kmis;

import kmis.constructive.IConstructive;
import kmis.constructive.RandomConstructive;
import kmis.structure.Instance;
import kmis.structure.RandomManager;
import kmis.structure.Solution;

import java.io.File;
import java.util.*;

public class Main {

    final static String pathFolder = "./test";
    static ArrayList<Instance> instances;

    final static boolean readAllFolders = false;
    final static boolean readAllInstances = false;
    final static boolean readFromInput = false;

    final static String folderIndex = "paper";
    final static String instanceIndex = "paperExample.txt";

    static List<String> foldersNames;
    static List<String> instancesNames;
    static String instanceFolderPath;

    final public static boolean DEBUG = true;
    static IConstructive constructive = new RandomConstructive();
    final static int seed=13;

    public static void main(String[] args){
        readData();
        for (Instance instance:instances) {
            RandomManager.setSeed(seed);
            Solution sol = constructive.construct(instance);
            if(Main.DEBUG)
                sol.printSol();
        }
    }

    private static void readData(){
        instances = new ArrayList<>();
        foldersNames = Arrays.asList(new File(pathFolder).list());

        if(readFromInput){
            readInstanceFromInput();
        } else {
            if(readAllFolders) readAllFolders();
            else if (foldersNames.contains(folderIndex)) readFolder(folderIndex);
            else System.out.println("Folder index exceeds the bounds of the array");
        }
    }

    private static void readInstanceFromInput(){
        Scanner sc = new Scanner(System.in);
        String line;
        String[] lineContent;
        int elementL;
        int elementR;
        int numElementsL;
        int numElementsR;
        int numEdges;
        int numElementsSol;
        BitSet []conexions;
        line = sc.nextLine();
        lineContent = line.split(" ");

        numElementsL = Integer.parseInt(lineContent[0]);
        numElementsR = Integer.parseInt(lineContent[1]);
        numEdges=Integer.parseInt(lineContent[2]);
        numElementsSol=Integer.parseInt(lineContent[3]);
        conexions = new BitSet[numEdges];

        for (int i=0; i< conexions.length;i++){
            conexions[i]=new BitSet(numElementsR);
            line = sc.nextLine();
            lineContent = line.split(" ");
            elementL = (Integer.parseInt(lineContent[0]))-1;
            elementR = (Integer.parseInt(lineContent[1]))-1;
            conexions[elementL].set(elementR);
        }
        instances.add(new Instance(conexions, numElementsSol, numEdges, numElementsR, numElementsL));
    }

    private static void readAllFolders(){
        instances = new ArrayList<>();
        String [] folders =new File(pathFolder).list();

        for(String fileName : folders){
            readFolder(fileName);
        }
    }

    private static void readFolder(String fileName){
        File file;
        file=new File(pathFolder+"/"+fileName);
        if(!fileName.startsWith(".") && !fileName.startsWith("..") && file.isDirectory()){
            instancesNames = Arrays.asList(file.list());
            instanceFolderPath = file.getPath() + "/";
            if(readAllInstances) readAllInstances();
            else if (instancesNames.contains(instanceIndex)) readInstance(instanceIndex);
            else System.out.println("Instance index exceeds the bounds of the array");
        }
    }

    private static void readAllInstances(){
        for(String instanceName : instancesNames){
            readInstance(instanceName);
        }
    }

    private static void readInstance(String instanceName){
        instances.add(new Instance(instanceFolderPath +instanceName));
    }
}
