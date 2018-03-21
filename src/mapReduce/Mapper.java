package mapReduce;

import mainRunner.Runner;
import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Mapper {
//------------------------ Mapper for Map Reduce Framework ---------------------------------
	public final ExecutorService executorService = Executors.newFixedThreadPool(Runner.MAPPER_THREADS);
	public abstract KeyValue map(String input);
	public final List<Future> futures = new ArrayList<>();
    public final List<KeyValue> output = new ArrayList<>();

  //------------------------ Execution of each Mapper and return the list of key value pair-------------------------   
    public List<KeyValue> execute(String[] input) {
    	
        for (String line : input) {
            Future future = executorService.submit(() -> map(line));
            futures.add(future);
        }

        results(output);
        return output;
    }
    
    //------------------------- Collection of Results from all the jobs--------------------
    public void results(List<KeyValue> output) {

        for (Future future : futures) {
            try {
                KeyValue outputkeyValue = (KeyValue) future.get();

                if (outputkeyValue != null) {
                    output.add(outputkeyValue);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
    }
 
//---------------------------------------------------------------------------------------------- 
}
