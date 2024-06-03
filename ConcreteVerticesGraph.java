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
public class ConcreteVerticesGraph<L> implements Graph<L> {
    
    private final List<Vertex<L>> vertices = new ArrayList<>();

    /**
     * Abstraction function:
     *   vertices映射到有向图的顶点及其含有的边
     * Representation invariant:
     *   图中所有边的边权均大于0
     * Safety from rep exposure:
     *   使用private final修饰并在方法中使用防御性拷贝
     */
    public ConcreteVerticesGraph(){}
    private void checkRep() {
        for(Vertex<L> vertex : vertices){
            for(int value : vertex.targets().values()){
                assert value > 0;
            }
        }
    }
    @Override public boolean add(L vertex) {
        for(Vertex<L> v : vertices) {
            if(v.getName().equals(vertex)){
                checkRep();
                return false;
            }
        }
        checkRep();
        return vertices.add(new Vertex<>(vertex));
    }
    
    @Override public int set(L source, L target, int weight) {
        if(weight > 0) {
            this.add(source);
            this.add(target);
        }
        for (Vertex<L> vertex : vertices) {
            if (vertex.getName().equals(source)) {
                if (vertex.targets().containsKey(target)) {
                    int previousValue = vertex.targets().get(target);
                    vertex.set(target, weight);
                    checkRep();
                    return previousValue;
                } else {
                    if (weight > 0) {
                        vertex.set(target, weight);
                        checkRep();
                        return 0;
                    }
                }
            }
        }
        checkRep();
        return 0;
    }
    
    @Override public boolean remove(L vertex) {
        boolean flag = false;
        for(Vertex<L> v : vertices){
            v.set(vertex, 0);
        }
        Iterator<Vertex<L>> vertexIterator = vertices.iterator();
        while(vertexIterator.hasNext()) {
            Vertex<L> v = vertexIterator.next();
            if(v.getName().equals(vertex)) {
                vertexIterator.remove();
                flag = true;
            }
        }
        checkRep();
        return flag;
    }
    
    @Override public Set<L> vertices() {
        Set<L> copySet = new HashSet<>();
        for(Vertex<L> vertex : vertices)
            copySet.add(vertex.getName());
        checkRep();
        return copySet;
    }
    
    @Override public Map<L, Integer> sources(L target) {
        Map<L, Integer> map = new HashMap<>();
        for(Vertex<L> vertex : vertices){
            if(vertex.targets().containsKey(target)){
                map.put(vertex.getName(), vertex.targets().get(target));
            }
        }
        checkRep();
        return map;
    }
    
    @Override public Map<L, Integer> targets(L source) {
        Map<L, Integer> map = new HashMap<>();
        for(Vertex<L> vertex : vertices){
            if(vertex.getName().equals(source)){
                map = vertex.targets();
                break;
            }
        }
        checkRep();
        return map;
    }
    
    @Override
    public String toString() {
        Set<String> V = new TreeSet<>();
        Set<String> E = new TreeSet<>();
        for(Vertex<L> vertex : vertices){
            V.add(vertex.getName().toString());
        }
        for(Vertex<L> vertex : vertices) {
            if(!vertex.targets().isEmpty()){
                E.add(vertex.toString());
            }
        }
        checkRep();
        return V + "," + E;
    }
}

/**
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex<L> {
    
    private final L name;
    final private Map<L, Integer> targetsMap = new HashMap<>();



    /**
     *Abstraction function:
     *   name映射到有向图中的点，targetsMap映射到以该点为源点的所有有向边
     *Representation invariant:
     *   name和targetsMap中的键值不为空，对应的权重为正整数
     *Safety from rep exposure:
     *   使用final private修饰且使用防御性拷贝
     */

    public Vertex(L vertex) {
        this.name = vertex;
        checkRep();
    }
    private void checkRep() {
        assert !name.toString().isEmpty();
        for(int value : targetsMap.values()){
            assert value > 0;
        }
    }
    public void set(L target, int weight){
        if(weight == 0) {
            targetsMap.remove(target);
            checkRep();
        } else if(weight > 0) {
            targetsMap.put(target, weight);
            checkRep();
        }
    }

    public Map<L, Integer> targets(){
        Map<L, Integer> copyMap;
        copyMap = this.targetsMap;
        return copyMap;
    }

    public L getName() {
        return this.name;
    }
    @Override
    public String toString(){
        Set<String> set = new TreeSet<>();
        if(targetsMap.isEmpty()){
            return "";
        }
        if (targetsMap.size() == 1) {
            for(L target : targetsMap.keySet()){
                return "[" + name + "," + target + "," + targetsMap.get(target) + "]";
            }
        }
        int flag = 0;
        for(L target : targetsMap.keySet()) {
            if(flag == 0){
                set.add(name.toString() + ',' + target + ',' + targetsMap.get(target) + ']');
                flag++;
            } else if(flag == targetsMap.size()-1){
                set.add('[' + name.toString() + ',' + target + ',' + targetsMap.get(target));
            } else{
                set.add('[' + name.toString() + ',' + target + ',' + targetsMap.get(target) + ']');
            }
        }
        return set.toString();
    }
}
