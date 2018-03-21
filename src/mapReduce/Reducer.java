package mapReduce;

import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;
import mainRunner.Runner;

public abstract class Reducer {
//------------------------ Reducer for Map Reduce Framework ---------------------------------

	 private final ExecutorService executorService = Executors.newFixedThreadPool(Runner.REDUCER_THREADS);
     public abstract KeyValue reduce(KeyValue<String, List> input, List<KeyValue<String, List>> shuffled);
	 private final List<Future> futures = new ArrayList<>();
	 private final List<KeyValue> output = new ArrayList<>();
 //------------------------- Collection of Results from all the jobs--------------------

	 private void results(List<KeyValue> output) {

	        for (Future future : futures) {
	            try {
	                KeyValue outputPair = (KeyValue) future.get();

	                if (outputPair != null) {
	                    output.add(outputPair);
	                }
	            } catch (InterruptedException | ExecutionException e) {
	                e.printStackTrace();
	            }
	        }

	        executorService.shutdown();
	    }
//------------------------ Execution of each Reducer and return the list of key value pair----   
	 public List<KeyValue> execute(List<KeyValue<String, List>> input) {

	        for (KeyValue<String, List> pair : input) {
	            Future future = executorService.submit(() -> reduce(pair, input));
	            futures.add(future);
	        }

	        results(output);
	        return output;
	    }
	 
//-----------------------------------------------------------------------------------------------	 
}
