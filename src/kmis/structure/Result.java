package kmis.structure;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Result {

    private String instanceName;
    private Map<String,Float> map=new HashMap<>();

    public Result(String instanceName){
        this.instanceName=instanceName;
    }

    public void add(String key, float value){
        map.put(key,value);
    }

    public float get(String key){
        return map.get(key);
    }

    public Set<String> getKeys(){
        return map.keySet();
    }

    public String getInstanceName() {
        return instanceName;
    }
}
