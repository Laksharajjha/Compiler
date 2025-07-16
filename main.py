def maxSubArray(nums):
    max_sum = nums[0]  # Initialize max_sum with first element
    current_sum = nums[0]  # Initialize current_sum with first element
    
    for num in nums[1:]:
        # For each element, decide whether to start new subarray or extend existing
        current_sum = max(num, current_sum + num)
        # Update max_sum if current_sum is larger
        max_sum = max(max_sum, current_sum)
    
    return max_sum

# Test with sample input
nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
result = maxSubArray(nums)
print(result)  # Output: 6