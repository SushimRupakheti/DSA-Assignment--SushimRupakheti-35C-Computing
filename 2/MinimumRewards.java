import java.util.*;

public class MinimumRewards {
    public static int minRewards(int[] ratings) {
        int n = ratings.length;
        int[] rewards = new int[n];
        Arrays.fill(rewards, 1); // Step 1: Every employee gets at least 1 reward

        // Step 2: Left to Right pass
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }

        // Step 3: Right to Left pass
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }

        // Step 4: Sum up rewards
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward;
        }
        
        return totalRewards;
    }

    public static void main(String[] args) {
        int[] ratings1 = {1, 0, 2};
        int[] ratings2 = {1, 2, 2};

        System.out.println(minRewards(ratings1)); // Output: 5
        System.out.println(minRewards(ratings2)); // Output: 4
    }
}
