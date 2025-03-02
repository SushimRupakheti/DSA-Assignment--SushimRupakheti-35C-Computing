public class CriticalTemperatureFinder {
    public static int minMeasurements(int k, int n) {
        // If we have only 1 sample, we must check every level one by one
        if (k == 1) {
            return n;}
        // dp[i][j] represents the minimum measurements with i samples and j temperature levels
        int[][] dp = new int[k + 1][n + 1];
        // If we have only 1 temperature level, 1 test is needed
        for (int i = 1; i <= k; i++) {
            dp[i][1] = 1;
        }
        // If we have 1 sample, we need to check all levels sequentially
        for (int j = 1; j <= n; j++) {
            dp[1][j] = j;
        }
        // Fill the dp table using optimized binary search
        for (int i = 2; i <= k; i++) {
            for (int j = 2; j <= n; j++) {
                int low = 1, high = j, result = j;
                while (low <= high) {
                    int mid = (low + high) / 2;
                    int breakCase = dp[i - 1][mid - 1];  // If it breaks at mid
                    int noBreakCase = dp[i][j - mid];    // If it does not break at mid
                    int worstCase = 1 + Math.max(breakCase, noBreakCase);

                    result = Math.min(result, worstCase);

                    // Optimize search range
                    if (breakCase > noBreakCase) {
                        high = mid - 1;
                    } else {
                        low = mid + 1;
                    }}
                dp[i][j] = result;
            }
        }
        return dp[k][n];
    }
    public static void main(String[] args) {
        System.out.println(minMeasurements(1, 2));  // Output: 2
        System.out.println(minMeasurements(2, 6));  // Output: 3
        System.out.println(minMeasurements(3, 14)); // Output: 4
    }
}
