package tool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopologicalSort {

	
	/**
     * Execute {@link #topoSortVisit(RDGNode, Map, List)} to get one sort graph
     *
     * @param transitiveDependencies
     */
    public void executeTopologicalSortVisit(List<RDGNode> transitiveDependencies, RDGNode node) {
    	Map<RDGNode, Boolean> marks = new HashMap<RDGNode, Boolean>();
    	topoSortVisit(node, marks, transitiveDependencies);
    }
    
    public static boolean isRunningTopologicalSort(RDGNode node, Map<RDGNode, Boolean> marks) {
    	boolean isRunning = false;

    	if (marks.containsKey(node) && marks.get(node) == false) {
	        // Visiting temporarily marked node -- this means a cyclic dependency!
	        throw new CyclicRdgException();
	    } else if (!marks.containsKey(node)) {
	    	isRunning = true;
	    }

	    return isRunning;
    }
    
    /**
     * Topological sort {@code visit} function (Cormen et al.'s algorithm).
     * @param node
     * @param marks
     * @param sorted
     * @throws CyclicRdgException
     */
    private void topoSortVisit(RDGNode node, Map<RDGNode, Boolean> marks, List<RDGNode> sorted) throws CyclicRdgException {
        if (isRunningTopologicalSort(node,marks)){
            // Mark node temporarily (cycle detection)
            marks.put(node, false);
            for (RDGNode child: node.getDependencies()) {
                topoSortVisit(child, marks, sorted);
            }
            // Mark node permanently (finished sorting branch)
            marks.put(node, true);
            sorted.add(node);
        }
    }

}
