package com.betaonly.transactionviewer;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Created by kelvinko on 7/11/2016.
 */

public class CurrencyGraph {

    private Map<String, Node> nodes;

    public CurrencyGraph() {
        nodes = new HashMap();
    }

    public void addEdge(String source, String target, BigDecimal rate) {
        Node sourceNode;
        Node targetNode;

        sourceNode = nodes.get(source);
        targetNode = nodes.get(target);

        if (sourceNode == null) {
            sourceNode = new Node(source);
            nodes.put(source, sourceNode);
        }
        if (targetNode == null) {
            targetNode = new Node(target);
            nodes.put(target, targetNode);
        }

        linkNode(sourceNode, targetNode, rate);
    }

    private void linkNode(Node source, Node target, BigDecimal linkValue) {
        source.edges.add(new Edge(source, target, linkValue));
    }

    /**
     * Calculate rate by finding shortest routes from Source to Target currency
     * Assumed best rate should
     * @param source
     * @param target
     * @return A list of routes from source to target
     */
    public BigDecimal calculateRate(String  source, String target) {

        List<Edge> routes = bfs( nodes.get(source), nodes.get(target) );

        if (routes == null) {
            return null;
        } else {
            BigDecimal rate = new BigDecimal("1.0");
            for (Edge edge : routes) {
                rate = rate.multiply(edge.rate);
            }
            return rate;
        }
    }

    /**
     * Perform bread first search to find the shortest route to target
     * @param source
     * @param target
     * @return all the shortest path
     */
    private List<Edge> bfs(Node source, Node target) {
        if (source == null || target == null) {
            return null;
        }

        Queue<Node> queue = new ArrayDeque<>();
        Map<Node, Edge> prev = new HashMap<>();
        Set<Node> visited = new HashSet<>();

        boolean reachTarget = false;
        Node current = null;
        queue.offer(source);

        // while not finish traverse and not reach target
        while( ! queue.isEmpty() &&
                ! reachTarget ) {

            current = queue.poll();
            if (current != null) {
                for(Edge edge : current.edges) {
                    // For each not visited node
                    if (!visited.contains(edge.target)) {
                        // mark target node's parent
                        prev.put(edge.target, edge);
                        visited.add(edge.target);

                        // Stop searching if target found
                        if (edge.target == target) {
                            reachTarget = true;
                        // Otherwise, put the edge target to searching queue
                        } else {
                            queue.offer(edge.target);
                        }
                    }
                }
            }
        }

        if (reachTarget) {
            LinkedList<Edge> route = new LinkedList<>();

            Node parent = target;
            while (parent != source) {
                Edge prevEdge = prev.get(parent);
                parent = prevEdge.source;
                route.addFirst(prevEdge);
            }
            return route;
        } else {
            return null;
        }
    }

    private class Node {
        String currency;
        List<Edge> edges;

        Node(String value) {
            edges = new ArrayList<>();
            this.currency = value;
        }
    }

    private class Edge {
        Node source;
        Node target;
        BigDecimal rate;
        Edge(Node source, Node target, BigDecimal value) {
            this.source = source;
            this.target = target;
            this.rate = value;
        }
    }
}
