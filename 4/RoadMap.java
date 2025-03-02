import java.util.*;

public class RoadMap {

    // Main method to test the solution with sample inputs.
    public static void main(String[] args) {
        // Example 1
        int[] packages1 = {0,0,0,1,1,0,0,1};
        int[][] roads1 = {
            {0, 1},
            {0, 2},
            {1, 3},
            {1, 4},
            {2, 5},
            {5, 6},
            {5, 7}
        };
        System.out.println("Output (Example 1): " + minRoads(packages1, roads1)); // Expected 2
        
    }

    public static int minRoads(int[] packages, int[][] roads) {
        int n = packages.length;
        // If there are no packages, no roads need to be traversed.
        boolean anyPackage = false;
        for (int p : packages) {
            if (p == 1) {
                anyPackage = true;
                break;
            }}
        if (!anyPackage) return 0;
        // Build the undirected graph.
        List<Integer>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] edge : roads) {
            int u = edge[0], v = edge[1];
            graph[u].add(v);
            graph[v].add(u);
        }
        // Compute all-pairs shortest distances.
        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            bfs(i, graph, dist[i]);
        }
        int minAns = Integer.MAX_VALUE;

        int totalSubsets = 1 << n;
        for (int mask = 1; mask < totalSubsets; mask++) {
            // Check if S (the set of stops given by 'mask') covers every package.
            if (!coversPackages(mask, packages, dist)) {
                continue;
            }
            // Build a list of the nodes (stops) in this subset.
            List<Integer> stops = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (((mask >> j) & 1) == 1) {
                    stops.add(j);
                }}
            int tourCost = tspTour(stops, dist);
            minAns = Math.min(minAns, tourCost);
        }
        return minAns == Integer.MAX_VALUE ? -1 : minAns;
    }
    
    /**
     * Standard BFS from source 's' to compute shortest distances in an unweighted graph.
     * Fills the distances array 'd' (d[i] = distance from s to i).
     */
    private static void bfs(int s, List<Integer>[] graph, int[] d) {
        Queue<Integer> q = new LinkedList<>();
        d[s] = 0;
        q.offer(s);
        while (!q.isEmpty()) {
            int cur = q.poll();
            for (int nei : graph[cur]) {
                if (d[nei] == Integer.MAX_VALUE) {
                    d[nei] = d[cur] + 1;
                    q.offer(nei);
                }}}}
    private static boolean coversPackages(int mask, int[] packages, int[][] dist) {
        int n = packages.length;
        for (int i = 0; i < n; i++) {
            if (packages[i] == 1) {
                boolean covered = false;
                for (int j = 0; j < n; j++) {
                    if (((mask >> j) & 1) == 1) {
                        if (dist[i][j] <= 2) {
                            covered = true;
                            break;
                        }}}
                if (!covered) return false;
            }}
        return true;
    }
    private static int tspTour(List<Integer> stops, int[][] dist) {
        int k = stops.size();
        int fullMask = (1 << k) - 1;
        int[][] dp = new int[1 << k][k];
        for (int i = 0; i < (1 << k); i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        dp[1 << 0][0] = 0;
        for (int mask = 0; mask < (1 << k); mask++) {
            for (int i = 0; i < k; i++) {
                if (((mask >> i) & 1) == 1 && dp[mask][i] != Integer.MAX_VALUE) {
                    for (int j = 0; j < k; j++) {
                        if (((mask >> j) & 1) == 0) { // j not yet visited
                            int nextMask = mask | (1 << j);
                            int cost = dp[mask][i] + dist[stops.get(i)][stops.get(j)];
                            dp[nextMask][j] = Math.min(dp[nextMask][j], cost);
                        }}}}}
        // Complete the cycle by returning to the starting stop.
        int best = Integer.MAX_VALUE;
        for (int i = 0; i < k; i++) {
            if (dp[fullMask][i] != Integer.MAX_VALUE) {
                best = Math.min(best, dp[fullMask][i] + dist[stops.get(i)][stops.get(0)]);
            }}
        return best;
        }}