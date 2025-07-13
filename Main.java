public class Main {
    public int trap(int[] height) {
        if (height == null || height.length == 0) {
            return 0;
        }
        
        int left = 0;
        int right = height.length - 1;
        int leftMax = 0;
        int rightMax = 0;
        int water = 0;
        
        while (left < right) {
            // Update maximum heights seen from left and right
            leftMax = Math.max(leftMax, height[left]);
            rightMax = Math.max(rightMax, height[right]);
            
            // Water trapped at current position is determined by the smaller of leftMax and rightMax
            if (leftMax <= rightMax) {
                water += leftMax - height[left];
                left++;
            } else {
                water += rightMax - height[right];
                right--;
            }
        }
        
        return water;
    }

    // Main method for testing
    public static void main(String[] args) {
        Main solution = new Main();
        int[] height = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        int result = solution.trap(height);
        System.out.println(result); // Output: Trapped water: 6
    }
}