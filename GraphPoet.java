/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import P1.graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {

    /**
     * Abstraction function:
     *  graph为由语料库中的词句生成的带权有向图，用来生成poem
     * Representation invariant:
     *   graph中权重均大于零
     * Safety from rep exposure:
     *   用private final修饰且使用防御性拷贝
     */
    private final Graph<String> graph = Graph.empty();

    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        String string;
        try (BufferedReader reader = new BufferedReader(new FileReader(corpus))){
            while((string = reader.readLine()) != null) {
                String[] sentences = string.split("[.?,;:!]");
                for(String sentence: sentences) {
                    String[] words = sentence.split(" ");
                    for(int i = 0; i < words.length - 1; i++) {
                        int count = getCountBetween(words[i], words[i + 1]);
                        graph.set(words[i].toLowerCase(), words[i + 1].toLowerCase(), count + 1);
                    }
                }
            }
        }catch (IOException e) {
            throw new IOException(e);
        }
    }

    private void checkRep() {
        for(String source : graph.vertices()){
            for(int value : graph.targets(source).values()){
                assert value > 0;
            }
        }
    }

    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        StringBuilder poem = new StringBuilder();
        List<Character> marks = new ArrayList<>();
        for(int i = 0; i < input.length(); i++) {
            if((input.charAt(i) == '.') ||(input.charAt(i) == ',') ||(input.charAt(i) == '?') ||(input.charAt(i) == '!') ||(input.charAt(i) == ':') ||(input.charAt(i) == ';'))
                marks.add(input.charAt(i));
        }
        String[] sentences = input.split("[.?,;:!]");
        for(int i = 0; i < marks.size(); i++){
            String[] words = sentences[i].split(" ");
            for(int j = 0; j < words.length - 1; j++) {
                String bridge = getBridgeWord(words[j].toLowerCase(), words[j + 1].toLowerCase());
                if(bridge != null) {
                    poem.append(words[j]).append(" ").append(bridge).append(" ");
                }
                else poem.append(words[j]).append(" ");
                if(j == words.length -2){
                    poem.append(words[j+1]);
                }
            }
            poem.append(marks.get(i));
        }
        checkRep();
        return poem.toString();
    }

    private int getCountBetween(String source, String target) {
        Map<String, Integer> map = graph.targets(source);
        if(!map.containsKey(target)) return 0;
        checkRep();
        return map.get(target);
    }

    private String getBridgeWord(String source, String target){
        int maxWeight = 0;
        String maximumBridge = null;
        Map<String, Integer> bridges = graph.targets(source);
        for(String bridge: bridges.keySet()) {
            int weight = bridges.get(bridge);
            Map<String, Integer> targets = graph.targets(bridge);
            if(targets.containsKey(target)) {
                if(weight + targets.get(target) > maxWeight) {
                    maxWeight = weight + targets.get(target);
                    maximumBridge = bridge;
                }
            }
        }
        checkRep();
        return maximumBridge;
    }
}
