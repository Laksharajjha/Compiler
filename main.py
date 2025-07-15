def trap(height):
    if not height:
        return 0
        
    left, right = 0, len(height) - 1
    left_max = right_max = water = 0
    
    while left < right:
        # Update the maximum height seen from left and right
        left_max = max(left_max, height[left])
        right_max = max(right_max, height[right])
        
        # Water trapped at current position is determined by the smaller of the two maxima
        if left_max <= right_max:
            water += left_max - height[left]
            left += 1
        else:
            water += right_max - height[right]
            right -= 1
            
    return water

# Example usage
if __name__ == "__main__":
    height = [0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1]
    print(trap(height))  # Output: 6