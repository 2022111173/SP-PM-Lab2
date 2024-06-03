/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import java.util.*;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {
    
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();

    /**
     * Abstraction function:
     *   vertices中点组成的集合V和edges中边组成的集合E映射到构成的图(V,E)
     * Representation invariant:
     *   图中所有边的边权均大于0
     * Safety from rep exposure:
     *   使用private final修饰并在方法中使用防御性拷贝
     */
    public ConcreteEdgesGraph(){}
    private void checkRep() {
        for(Edge<L> edge : edges){
            assert edge.getWeight() > 0;
        }
    }
    @Override public boolean add(L vertex) {
        if(vertices.contains(vertex)) {
            checkRep();
            return false;
        }
        checkRep();
        return vertices.add(vertex);
    }
    
    @Override public int set(L source, L target, int weight) {
        Iterator<Edge<L>> iterator = edges.iterator();
        while(iterator.hasNext()) {
            Edge<L> edge = iterator.next();
            if(edge.getSource().equals(source) && edge.getTarget().equals(target)) {
                int previousValue = edge.getWeight();
                iterator.remove();
                if(weight > 0) {
                    edges.add(new Edge<>(source, target, weight));
                }
                checkRep();
                return previousValue;
            }
        }
        if(weight > 0) {
            vertices.add(source);
            vertices.add(target);
            edges.add(new Edge<>(source, target, weight));
            checkRep();
            return 0;
        }
        checkRep();
        return 0;
    }
    
    @Override public boolean remove(L vertex) {
        Iterator<L> LIterator = vertices.iterator();
        boolean flag = false;
        while(LIterator.hasNext()) {
            L name = LIterator.next();
            if(name.equals(vertex)) {
                for(int i = 0; i < edges.size(); i++) {
                    if (edges.get(i).getSource().equals(vertex) || edges.get(i).getTarget().equals(vertex)) {
                        edges.remove(edges.get(i));
                    }
                }
                LIterator.remove();
                flag = true;
                break;
            }
        }
        checkRep();
        return flag;
    }

    @Override public Set<L> vertices() {
        checkRep();
        return new HashSet<>(vertices);
    }

    @Override public Map<L, Integer> sources(L target) {
        Map<L, Integer> sourcesMap = new HashMap<>();
        for(Edge<L> edge: edges) {
            if(edge.getTarget().equals(target)) {
                sourcesMap.put(edge.getSource(), edge.getWeight());
            }
        }
        checkRep();
        return sourcesMap;
    }
    
    @Override public Map<L, Integer> targets(L source) {
        Map<L, Integer> targetsMap = new HashMap<>();
        for(Edge<L> edge: edges) {
            if(edge.getSource().equals(source)) {
                targetsMap.put(edge.getTarget(), edge.getWeight());
            }
        }
        checkRep();
        return targetsMap;
    }
    
    @Override
    public String toString() {
        Set<String> E = new TreeSet<>();
        Set<L> V = new TreeSet<>(vertices);
        for(Edge<L> e : edges) E.add(e.toString());
        checkRep();
        return V + "," + E;
    }
}

/**
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 *
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge<L> {

    private final int weight;
    private final L source, target;
    /**
     * Abstraction function:
     *   由source和target及weight映射到一条有向边,source是起始点，target是终点，weight是权值
     * Representation invariant:
     *   source和target不能为空，weight为int型正整数
     * Safety from rep exposure:
     *   使用private final修饰
     */
    public Edge(L source, L target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        checkRep();
    }
    private void checkRep() {
        assert this.weight > 0;
        assert !this.source.toString().isEmpty();
        assert !this.target.toString().isEmpty();
    }

    /**
     * 得到所选边的源点
     * @return 边的源点
     */
    public L getSource() {
        return this.source;
    }

    /**
     * 得到所选边的权值
     * @return 边的权值
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * 得到所选边的终点
     * @return 边的终点
     */
    public L getTarget() {
        return this.target;
    }
    @Override
    public String toString() {
        return "[" + source + ',' + target + ',' + weight + "]";
    }

}
