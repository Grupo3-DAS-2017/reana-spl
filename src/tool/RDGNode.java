package tool;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import tool.analyzers.buildingblocks.Component;
import fdtmc.FDTMC;


public class RDGNode {

	//This reference is used to store all the RDGnodes created during the evaluation
	private static Map<String, RDGNode> rdgNodes = new HashMap<String, RDGNode>();
	private static List<RDGNode> nodesInCreationOrder = new LinkedList<RDGNode>();
	private TopologicalSort topologicalSort = new TopologicalSort();

    private static int lastNodeIndex = 0;

	// Node identifier
	private String id;
	//This attribute is used to store the FDTMC for the RDG node.
	private FDTMC fdtmc;
	/**
	 * The node must have an associated presence condition, which is
	 * a boolean expression over features.
	 */
	private String presenceCondition;
	// Nodes on which this one depends
	private Collection<RDGNode> dependencies;
	/**
	 * Height of the RDGNode.
	 */
	private int height;


	/**
	 * The id, presence condition and model (FDTMC) of an RDG node must
	 * be immutable, so there must be no setters for them. Hence, they
	 * must be set at construction-time.
	 *
	 * @param id Node's identifier. It is preferably a valid Java identifier.
	 * @param presenceCondition Boolean expression over features (using Java operators).
	 * @param fdtmc Stochastic model of the piece of behavioral model represented by
	 *             this node.
	 */
	public RDGNode(String id, String presenceCondition, FDTMC fdtmc) {
	    this.id = id;
	    this.presenceCondition = presenceCondition;
	    this.fdtmc = fdtmc;
		this.dependencies = new HashSet<RDGNode>();
		this.height = 0;

		rdgNodes.put(id, this);
		nodesInCreationOrder.add(this);
	}

    public FDTMC getFDTMC() {
        return this.fdtmc;
    }

    public void addDependency(RDGNode child) {
        this.dependencies.add(child);
        height = Math.max(height, child.height + 1);
    }

    public Collection<RDGNode> getDependencies() {
        return dependencies;
    }

    public String getPresenceCondition() {
        return presenceCondition;
    }

    public String getId() {
        return id;
    }

    /**
     * Height of the RDGNode. This metric is defined in the same way as
     * the height of a tree node, i.e., the maximum number of nodes in a path
     * from this one to a leaf (node with no dependencies).
     */
    public int getHeight() {
        return height;
    }

    public static RDGNode getById(String id) {
        return rdgNodes.get(id);
    }

    public static String getNextId() {
        return "n" + lastNodeIndex++;
    }

    /**
     * We consider two RDG nodes to be equal whenever their behavior is
     * modeled by equal FDTMCs, their presence condition is the same and
     * their dependencies are also correspondingly equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (this.isRDGNodeObject(obj)) { 
            return this.isRDGNodeEqualsTo((RDGNode) obj);
        } 
        
        return false;
    }
     
    /**
     * Check if another RDGNode object is equals to the target RDGNode object
     * @param node
     * @return boolean
     */
    private boolean isRDGNodeEqualsTo(RDGNode node) {
    	return this.getPresenceCondition().equals(node.getPresenceCondition())
                && this.getFDTMC().equals(node.getFDTMC())
                && this.getDependencies().equals(node.getDependencies());
    }
    
    private boolean isRDGNodeObject(Object obj) {
    	return (obj != null && obj instanceof RDGNode);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode() + presenceCondition.hashCode() + fdtmc.hashCode() + dependencies.hashCode();
    }

    @Override
    public String toString() {
        return getId() + " (" + getPresenceCondition() + ")";
    }

    /**
     * Retrieves the transitive closure of the RDGNode dependency relation.
     * The node itself is part of the returned list.
     *
     * It implements the Cormen et al.'s topological sort algorithm.
     *
     * @return The descendant RDG nodes ordered bottom-up (depended-upon to dependent).
     * @throws CyclicRdgException if there is a path with a cycle starting from this node.
     */
    public List<RDGNode> getDependenciesTransitiveClosure() throws CyclicRdgException {
        List<RDGNode> transitiveDependencies = new LinkedList<RDGNode>();
        topologicalSort.executeTopologicalSortVisit(transitiveDependencies,this);
        return transitiveDependencies;
    }
    
    /**
     * Returns the first RDG node (in crescent order of creation time) which is similar
     * to the one provided.
     *
     * A similar RDG node is one for which equals() returns true.
     * @param rdgNode
     * @return a similar RDG node or null in case there is none.
     */
    public static RDGNode getSimilarNode(RDGNode target) {
        for (RDGNode candidate: nodesInCreationOrder) {
            if (candidate != target && candidate.equals(target)) {
                return candidate;
            }
        }
        return null;
    }

    /**
     * Converts this RDG node into a Component<FDTMC>.
     * @return
     */
    public Component<FDTMC> toComponent() {
        Collection<Component<FDTMC>> dependencies = this.getDependencies().stream()
                .map(RDGNode::toComponent)
                .collect(Collectors.toSet());
        return new Component<FDTMC>(this.getId(),
                                    this.getPresenceCondition(),
                                    this.getFDTMC(),
                                    dependencies);
    }

    public static List<Component<FDTMC>> toComponentList(List<RDGNode> nodes) {
        return nodes.stream()
                .map(RDGNode::toComponent)
                .collect(Collectors.toList());
    }

}
