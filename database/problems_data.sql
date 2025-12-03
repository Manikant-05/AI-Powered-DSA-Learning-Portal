-- Clear existing data
TRUNCATE TABLE test_cases CASCADE;
TRUNCATE TABLE submissions CASCADE;
TRUNCATE TABLE problems CASCADE;
ALTER SEQUENCE problems_id_seq RESTART WITH 1;
ALTER SEQUENCE test_cases_id_seq RESTART WITH 1;

-- Insert 100 Problems
INSERT INTO problems (title, description, difficulty, topic, input_format, output_format, constraints, hint, time_limit, memory_limit) VALUES 
-- 1. Two Sum
('Two Sum', 'Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.', 'EASY', 'ARRAYS', 'First line: n\nSecond line: n integers\nThird line: target', 'Two space-separated indices', '2 ≤ n ≤ 10^4', 'Use a hash map to store the complement of each number as you iterate.', 1000, 256),
-- 2. Valid Parentheses
('Valid Parentheses', 'Given a string s containing just the characters ''('', '')'', ''{'', ''}'', ''['' and '']'', determine if the input string is valid.', 'EASY', 'STACK', 'String s', 'true or false', '1 ≤ s.length ≤ 10^4', 'Use a stack to keep track of opening brackets. When you see a closing bracket, check if it matches the top of the stack.', 1000, 256),
-- 3. Reverse String
('Reverse String', 'Write a function that reverses a string. The input string is given as an array of characters s.', 'EASY', 'STRINGS', 'String s', 'Reversed string', '1 ≤ s.length ≤ 10^5', 'Use two pointers, one at the beginning and one at the end, and swap them until they meet.', 1000, 256),
-- 4. Maximum Subarray
('Maximum Subarray', 'Given an integer array nums, find the contiguous subarray (containing at least one number) which has the largest sum and return its sum.', 'MEDIUM', 'DYNAMIC_PROGRAMMING', 'First line: n\nSecond line: n integers', 'Maximum sum', '1 ≤ n ≤ 10^5', 'Use Kadane''s algorithm. Maintain a running sum and reset it if it becomes negative.', 1000, 256),
-- 5. Climbing Stairs
('Climbing Stairs', 'You are climbing a staircase. It takes n steps to reach the top. Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?', 'EASY', 'DYNAMIC_PROGRAMMING', 'Integer n', 'Number of ways', '1 ≤ n ≤ 45', 'This is essentially the Fibonacci sequence. dp[i] = dp[i-1] + dp[i-2].', 1000, 256),
-- 6. Best Time to Buy and Sell Stock
('Best Time to Buy and Sell Stock', 'You are given an array prices where prices[i] is the price of a given stock on the ith day. Return the maximum profit you can achieve.', 'EASY', 'ARRAYS', 'First line: n\nSecond line: n integers', 'Maximum profit', '1 ≤ n ≤ 10^5', 'Track the minimum price seen so far and calculate the potential profit at each day.', 1000, 256),
-- 7. Linked List Cycle
('Linked List Cycle', 'Given head, the head of a linked list, determine if the linked list has a cycle in it.', 'EASY', 'LINKED_LIST', 'Linked list elements', 'true or false', '0 ≤ n ≤ 10^4', 'Use Floyd''s Cycle-Finding Algorithm (Tortoise and Hare).', 1000, 256),
-- 8. Merge Two Sorted Lists
('Merge Two Sorted Lists', 'Merge two sorted linked lists and return it as a sorted list.', 'EASY', 'LINKED_LIST', 'Two sorted lists', 'Merged sorted list', '0 ≤ n, m ≤ 50', 'Use a dummy node and iterate through both lists, appending the smaller node to the result.', 1000, 256),
-- 9. Invert Binary Tree
('Invert Binary Tree', 'Given the root of a binary tree, invert the tree, and return its root.', 'EASY', 'TREES', 'Binary tree', 'Inverted binary tree', '0 ≤ n ≤ 100', 'Recursively swap the left and right children of every node.', 1000, 256),
-- 10. Valid Anagram
('Valid Anagram', 'Given two strings s and t, return true if t is an anagram of s, and false otherwise.', 'EASY', 'STRINGS', 'Two strings s and t', 'true or false', '1 ≤ s.length, t.length ≤ 5 * 10^4', 'Count the frequency of each character in both strings and compare them.', 1000, 256),
-- 11. Binary Search
('Binary Search', 'Given an array of integers nums which is sorted in ascending order, and an integer target, write a function to search target in nums.', 'EASY', 'SEARCHING', 'First line: n\nSecond line: n integers\nThird line: target', 'Index of target or -1', '1 ≤ n ≤ 10^4', 'Use the standard binary search algorithm with left, right, and mid pointers.', 1000, 256),
-- 12. Flood Fill
('Flood Fill', 'An image is represented by an m x n integer grid image where image[i][j] represents the pixel value of the image. Perform a flood fill.', 'EASY', 'GRAPHS', 'Grid dimensions and start point', 'Modified grid', '1 ≤ m, n ≤ 50', 'Use BFS or DFS to traverse connected pixels with the same color.', 1000, 256),
-- 13. Lowest Common Ancestor of a BST
('Lowest Common Ancestor of a BST', 'Given a binary search tree (BST), find the lowest common ancestor (LCA) of two given nodes in the BST.', 'EASY', 'TREES', 'BST and two nodes', 'LCA node value', '2 ≤ n ≤ 10^5', 'Utilize the BST property: if both nodes are smaller than root, go left; if larger, go right.', 1000, 256),
-- 14. Balanced Binary Tree
('Balanced Binary Tree', 'Given a binary tree, determine if it is height-balanced.', 'EASY', 'TREES', 'Binary tree', 'true or false', '0 ≤ n ≤ 5000', 'Calculate the height of left and right subtrees for every node and check the difference.', 1000, 256),
-- 15. Implement Queue using Stacks
('Implement Queue using Stacks', 'Implement a first in first out (FIFO) queue using only two stacks.', 'EASY', 'STACK', 'Commands', 'Output of commands', '1 ≤ operations ≤ 100', 'Use two stacks: one for input and one for output. Move elements only when output stack is empty.', 1000, 256),
-- 16. First Bad Version
('First Bad Version', 'You are a product manager and currently leading a team to develop a new product. Find the first bad version.', 'EASY', 'SEARCHING', 'n versions', 'First bad version', '1 ≤ n ≤ 2^31 - 1', 'This is a binary search problem. Find the first index where isBadVersion returns true.', 1000, 256),
-- 17. Ransom Note
('Ransom Note', 'Given two strings ransomNote and magazine, return true if ransomNote can be constructed from magazine.', 'EASY', 'HASH_TABLE', 'Two strings', 'true or false', '1 ≤ length ≤ 10^5', 'Count character frequencies in magazine and check if ransomNote can be formed.', 1000, 256),
-- 18. Longest Palindrome
('Longest Palindrome', 'Given a string s which consists of lowercase or uppercase letters, return the length of the longest palindrome that can be built with those letters.', 'EASY', 'STRINGS', 'String s', 'Length of longest palindrome', '1 ≤ s.length ≤ 2000', 'Count character frequencies. You can use all even counts and at most one odd count.', 1000, 256),
-- 19. Reverse Linked List
('Reverse Linked List', 'Given the head of a singly linked list, reverse the list, and return the reversed list.', 'EASY', 'LINKED_LIST', 'Linked list', 'Reversed linked list', '0 ≤ n ≤ 5000', 'Iterate through the list, changing the next pointer of each node to point to the previous node.', 1000, 256),
-- 20. Majority Element
('Majority Element', 'Given an array nums of size n, return the majority element (appears more than n / 2 times).', 'EASY', 'ARRAYS', 'Array nums', 'Majority element', '1 ≤ n ≤ 5 * 10^4', 'Use the Boyer-Moore Voting Algorithm.', 1000, 256),
-- 21. Add Binary
('Add Binary', 'Given two binary strings a and b, return their sum as a binary string.', 'EASY', 'MATH', 'Two binary strings', 'Binary sum string', '1 ≤ length ≤ 10^4', 'Simulate binary addition from right to left, keeping track of the carry.', 1000, 256),
-- 22. Diameter of Binary Tree
('Diameter of Binary Tree', 'Given the root of a binary tree, return the length of the diameter of the tree.', 'EASY', 'TREES', 'Binary tree', 'Diameter', '1 ≤ n ≤ 10^4', 'The diameter is the maximum of (left_height + right_height) for any node.', 1000, 256),
-- 23. Middle of the Linked List
('Middle of the Linked List', 'Given the head of a singly linked list, return the middle node of the linked list.', 'EASY', 'LINKED_LIST', 'Linked list', 'Middle node value', '1 ≤ n ≤ 100', 'Use two pointers: slow (1 step) and fast (2 steps). When fast reaches end, slow is at middle.', 1000, 256),
-- 24. Maximum Depth of Binary Tree
('Maximum Depth of Binary Tree', 'Given the root of a binary tree, return its maximum depth.', 'EASY', 'TREES', 'Binary tree', 'Max depth', '0 ≤ n ≤ 10^4', 'Use recursion: max(depth(left), depth(right)) + 1.', 1000, 256),
-- 25. Contains Duplicate
('Contains Duplicate', 'Given an integer array nums, return true if any value appears at least twice in the array.', 'EASY', 'ARRAYS', 'Array nums', 'true or false', '1 ≤ n ≤ 10^5', 'Use a HashSet to check for existing elements as you iterate.', 1000, 256),
-- 26. Meeting Rooms
('Meeting Rooms', 'Given an array of meeting time intervals, determine if a person could attend all meetings.', 'EASY', 'SORTING', 'Intervals', 'true or false', '0 ≤ n ≤ 10^4', 'Sort intervals by start time and check for overlaps.', 1000, 256),
-- 27. Roman to Integer
('Roman to Integer', 'Given a roman numeral, convert it to an integer.', 'EASY', 'MATH', 'Roman numeral string', 'Integer value', '1 ≤ s.length ≤ 15', 'Iterate through the string. If current value < next value, subtract it; otherwise add it.', 1000, 256),
-- 28. Backspace String Compare
('Backspace String Compare', 'Given two strings s and t, return true if they are equal when both are typed into empty text editors. # means a backspace character.', 'EASY', 'STACK', 'Two strings', 'true or false', '1 ≤ length ≤ 200', 'Process both strings using a stack or simulate the backspace logic.', 1000, 256),
-- 29. Counting Bits
('Counting Bits', 'Given an integer n, return an array ans of length n + 1 such that for each i (0 <= i <= n), ans[i] is the number of 1''s in the binary representation of i.', 'EASY', 'DYNAMIC_PROGRAMMING', 'Integer n', 'Array of counts', '0 ≤ n ≤ 10^5', 'Use DP: bits[i] = bits[i >> 1] + (i & 1).', 1000, 256),
-- 30. Same Tree
('Same Tree', 'Given the roots of two binary trees p and q, write a function to check if they are the same or not.', 'EASY', 'TREES', 'Two binary trees', 'true or false', '0 ≤ n ≤ 100', 'Recursively check if current nodes are equal and their left/right subtrees are equal.', 1000, 256),
-- 31. Number of 1 Bits
('Number of 1 Bits', 'Write a function that takes an unsigned integer and returns the number of ''1'' bits it has.', 'EASY', 'MATH', 'Integer n', 'Count of 1s', 'n is 32-bit', 'Use n & (n-1) to clear the least significant bit repeatedly.', 1000, 256),
-- 32. Longest Common Prefix
('Longest Common Prefix', 'Write a function to find the longest common prefix string amongst an array of strings.', 'EASY', 'STRINGS', 'Array of strings', 'Longest common prefix', '1 ≤ n ≤ 200', 'Sort the array and compare the first and last strings.', 1000, 256),
-- 33. Single Number
('Single Number', 'Given a non-empty array of integers nums, every element appears twice except for one. Find that single one.', 'EASY', 'MATH', 'Array nums', 'Single number', '1 ≤ n ≤ 3 * 10^4', 'XOR all numbers together. Duplicates cancel out.', 1000, 256),
-- 34. Palindrome Linked List
('Palindrome Linked List', 'Given the head of a singly linked list, return true if it is a palindrome.', 'EASY', 'LINKED_LIST', 'Linked list', 'true or false', '1 ≤ n ≤ 10^5', 'Find the middle, reverse the second half, and compare with the first half.', 1000, 256),
-- 35. Move Zeroes
('Move Zeroes', 'Given an integer array nums, move all 0''s to the end of it while maintaining the relative order of the non-zero elements.', 'EASY', 'ARRAYS', 'Array nums', 'Modified array', '1 ≤ n ≤ 10^4', 'Keep a pointer for the position of the next non-zero element.', 1000, 256),
-- 36. Symmetric Tree
('Symmetric Tree', 'Given the root of a binary tree, check whether it is a mirror of itself.', 'EASY', 'TREES', 'Binary tree', 'true or false', '1 ≤ n ≤ 1000', 'Recursively check if left.left == right.right and left.right == right.left.', 1000, 256),
-- 37. Missing Number
('Missing Number', 'Given an array nums containing n distinct numbers in the range [0, n], return the only number in the range that is missing from the array.', 'EASY', 'ARRAYS', 'Array nums', 'Missing number', 'n == nums.length', 'Calculate sum of 0..n and subtract sum of array elements.', 1000, 256),
-- 38. Palindrome Number
('Palindrome Number', 'Given an integer x, return true if x is palindrome integer.', 'EASY', 'MATH', 'Integer x', 'true or false', '-2^31 ≤ x ≤ 2^31 - 1', 'Reverse the integer (handling overflow) and compare with original.', 1000, 256),
-- 39. Convert Sorted Array to BST
('Convert Sorted Array to BST', 'Given an integer array nums where the elements are sorted in ascending order, convert it to a height-balanced binary search tree.', 'EASY', 'TREES', 'Sorted array', 'BST root', '1 ≤ n ≤ 10^4', 'Pick the middle element as root, then recursively build left and right subtrees.', 1000, 256),
-- 40. Reverse Bits
('Reverse Bits', 'Reverse bits of a given 32 bits unsigned integer.', 'EASY', 'MATH', 'Integer n', 'Reversed integer', 'n is 32-bit', 'Iterate 32 times, shifting result left and adding the last bit of n.', 1000, 256),
-- 41. Subtree of Another Tree
('Subtree of Another Tree', 'Given the roots of two binary trees root and subRoot, return true if there is a subtree of root with the same structure and node values of subRoot.', 'EASY', 'TREES', 'Two trees', 'true or false', '0 ≤ n ≤ 2000', 'Traverse root; for each node, check if the subtree rooted there matches subRoot.', 1000, 256),
-- 42. Squares of a Sorted Array
('Squares of a Sorted Array', 'Given an integer array nums sorted in non-decreasing order, return an array of the squares of each number sorted in non-decreasing order.', 'EASY', 'ARRAYS', 'Sorted array', 'Sorted squares', '1 ≤ n ≤ 10^4', 'Use two pointers from both ends, comparing absolute values.', 1000, 256),
-- 43. Maximum Product Subarray
('Maximum Product Subarray', 'Given an integer array nums, find a contiguous non-empty subarray within the array that has the largest product, and return the product.', 'MEDIUM', 'DYNAMIC_PROGRAMMING', 'Array nums', 'Max product', '1 ≤ n ≤ 2 * 10^4', 'Keep track of both max and min product at each position (due to negative numbers).', 1000, 256),
-- 44. Search in Rotated Sorted Array
('Search in Rotated Sorted Array', 'Given the array nums after the possible rotation and an integer target, return the index of target if it is in nums, or -1 if it is not in nums.', 'MEDIUM', 'SEARCHING', 'Rotated sorted array', 'Index or -1', '1 ≤ n ≤ 5000', 'Use modified binary search. Determine which half is sorted and check if target lies there.', 1000, 256),
-- 45. 3Sum
('3Sum', 'Given an integer array nums, return all the triplets [nums[i], nums[j], nums[k]] such that i != j, i != k, and j != k, and nums[i] + nums[j] + nums[k] == 0.', 'MEDIUM', 'ARRAYS', 'Array nums', 'List of triplets', '0 ≤ n ≤ 3000', 'Sort the array, then iterate and use two pointers for the remaining two numbers.', 1000, 256),
-- 46. Container With Most Water
('Container With Most Water', 'Find two lines that together with the x-axis form a container, such that the container contains the most water.', 'MEDIUM', 'ARRAYS', 'Array of heights', 'Max area', '2 ≤ n ≤ 10^5', 'Use two pointers starting from outside. Move the pointer with the smaller height inward.', 1000, 256),
-- 47. Product of Array Except Self
('Product of Array Except Self', 'Given an integer array nums, return an array answer such that answer[i] is equal to the product of all the elements of nums except nums[i].', 'MEDIUM', 'ARRAYS', 'Array nums', 'Result array', '2 ≤ n ≤ 10^5', 'Calculate prefix products and suffix products, then multiply them.', 1000, 256),
-- 48. Group Anagrams
('Group Anagrams', 'Given an array of strings strs, group the anagrams together.', 'MEDIUM', 'HASH_TABLE', 'Array of strings', 'Grouped anagrams', '1 ≤ n ≤ 10^4', 'Use a hash map where the key is the sorted string or character count.', 1000, 256),
-- 49. Longest Substring Without Repeating Characters
('Longest Substring Without Repeating Characters', 'Given a string s, find the length of the longest substring without repeating characters.', 'MEDIUM', 'STRINGS', 'String s', 'Max length', '0 ≤ s.length ≤ 5 * 10^4', 'Use a sliding window and a hash set/map to track characters in the current window.', 1000, 256),
-- 50. Longest Repeating Character Replacement
('Longest Repeating Character Replacement', 'You are given a string s and an integer k. You can choose any character of the string and change it to any other uppercase English character. Find the length of the longest substring containing the same letter you can get after performing the above operations at most k times.', 'MEDIUM', 'STRINGS', 'String s, Integer k', 'Max length', '1 ≤ s.length ≤ 10^5', 'Use sliding window. Valid window condition: (length - max_freq) <= k.', 1000, 256),
-- 51. Number of Islands
('Number of Islands', 'Given an m x n 2D binary grid grid which represents a map of ''1''s (land) and ''0''s (water), return the number of islands.', 'MEDIUM', 'GRAPHS', 'Binary grid', 'Number of islands', '1 ≤ m, n ≤ 300', 'Iterate through grid. When ''1'' is found, increment count and DFS/BFS to mark all connected ''1''s.', 1000, 256),
-- 52. Remove Nth Node From End of List
('Remove Nth Node From End of List', 'Given the head of a linked list, remove the nth node from the end of the list and return its head.', 'MEDIUM', 'LINKED_LIST', 'Linked list, n', 'Modified list', '1 ≤ n ≤ sz', 'Use two pointers with a gap of n nodes.', 1000, 256),
-- 53. Reorder List
('Reorder List', 'You are given the head of a singly linked-list. The list can be represented as: L0 → L1 → … → Ln - 1 → Ln. Reorder the list to be on the following form: L0 → Ln → L1 → Ln - 1 → L2 → Ln - 2 → …', 'MEDIUM', 'LINKED_LIST', 'Linked list', 'Reordered list', '1 ≤ n ≤ 5 * 10^4', 'Find middle, reverse second half, then merge the two halves.', 1000, 256),
-- 54. Clone Graph
('Clone Graph', 'Given a reference of a node in a connected undirected graph. Return a deep copy (clone) of the graph.', 'MEDIUM', 'GRAPHS', 'Graph node', 'Cloned graph node', '0 ≤ n ≤ 100', 'Use DFS or BFS with a hash map to map original nodes to clones.', 1000, 256),
-- 55. Pacific Atlantic Water Flow
('Pacific Atlantic Water Flow', 'There is an m x n rectangular island that borders both the Pacific Ocean and Atlantic Ocean. Return a list of grid coordinates where water can flow to both the Pacific and Atlantic oceans.', 'MEDIUM', 'GRAPHS', 'Height matrix', 'List of coordinates', '1 ≤ m, n ≤ 200', 'DFS/BFS from Pacific borders and Atlantic borders separately. Find intersection.', 1000, 256),
-- 56. Longest Palindromic Substring
('Longest Palindromic Substring', 'Given a string s, return the longest palindromic substring in s.', 'MEDIUM', 'DYNAMIC_PROGRAMMING', 'String s', 'Longest palindrome', '1 ≤ s.length ≤ 1000', 'Expand around center for each character (and between characters).', 1000, 256),
-- 57. House Robber
('House Robber', 'You are a professional robber planning to rob houses along a street. Adjacent houses have security systems connected. Return the maximum amount of money you can rob tonight without alerting the police.', 'MEDIUM', 'DYNAMIC_PROGRAMMING', 'Array of money', 'Max money', '1 ≤ n ≤ 100', 'dp[i] = max(dp[i-1], dp[i-2] + nums[i]).', 1000, 256),
-- 58. House Robber II
('House Robber II', 'You are a professional robber planning to rob houses along a street. The houses are arranged in a circle.', 'MEDIUM', 'DYNAMIC_PROGRAMMING', 'Array of money', 'Max money', '1 ≤ n ≤ 100', 'Run House Robber logic twice: once excluding first house, once excluding last.', 1000, 256),
-- 59. Palindromic Substrings
('Palindromic Substrings', 'Given a string s, return the number of palindromic substrings in it.', 'MEDIUM', 'DYNAMIC_PROGRAMMING', 'String s', 'Count', '1 ≤ s.length ≤ 1000', 'Expand around center for all possible centers.', 1000, 256),
-- 60. Decode Ways
('Decode Ways', 'A message containing letters from A-Z can be encoded into numbers using the mapping ''A'' -> "1", ''B'' -> "2", ... ''Z'' -> "26". Given a string s containing only digits, return the number of ways to decode it.', 'MEDIUM', 'DYNAMIC_PROGRAMMING', 'Digit string', 'Number of ways', '1 ≤ s.length ≤ 100', 'dp[i] depends on single digit validity (s[i]) and two-digit validity (s[i-1:i+1]).', 1000, 256),
-- 61. Coin Change
('Coin Change', 'You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money. Return the fewest number of coins that you need to make up that amount.', 'MEDIUM', 'DYNAMIC_PROGRAMMING', 'Coins array, amount', 'Min coins', '1 ≤ amount ≤ 10^4', 'dp[i] = min(dp[i], dp[i - coin] + 1) for each coin.', 1000, 256),
-- 62. Maximum Product Subarray
('Maximum Product Subarray', 'Given an integer array nums, find a contiguous non-empty subarray within the array that has the largest product, and return the product.', 'MEDIUM', 'DYNAMIC_PROGRAMMING', 'Array nums', 'Max product', '1 ≤ n ≤ 2 * 10^4', 'Track max and min product at each step.', 1000, 256),
-- 63. Word Break
('Word Break', 'Given a string s and a dictionary of strings wordDict, return true if s can be segmented into a space-separated sequence of one or more dictionary words.', 'MEDIUM', 'DYNAMIC_PROGRAMMING', 'String s, Dictionary', 'true or false', '1 ≤ s.length ≤ 300', 'dp[i] is true if dp[j] is true and s[j:i] is in dictionary.', 1000, 256),
-- 64. Longest Increasing Subsequence
('Longest Increasing Subsequence', 'Given an integer array nums, return the length of the longest strictly increasing subsequence.', 'MEDIUM', 'DYNAMIC_PROGRAMMING', 'Array nums', 'Length', '1 ≤ n ≤ 2500', 'dp[i] = max(dp[j]) + 1 for all j < i where nums[j] < nums[i].', 1000, 256),
-- 65. Unique Paths
('Unique Paths', 'There is a robot on an m x n grid. The robot is initially located at the top-left corner. The robot tries to move to the bottom-right corner. The robot can only move either down or right at any point in time. How many possible unique paths are there?', 'MEDIUM', 'DYNAMIC_PROGRAMMING', 'm, n', 'Number of paths', '1 ≤ m, n ≤ 100', 'dp[i][j] = dp[i-1][j] + dp[i][j-1].', 1000, 256),
-- 66. Jump Game
('Jump Game', 'You are given an integer array nums. You are initially positioned at the array''s first index, and each element in the array represents your maximum jump length at that position. Return true if you can reach the last index.', 'MEDIUM', 'GREEDY', 'Array nums', 'true or false', '1 ≤ n ≤ 10^4', 'Track the furthest reachable index.', 1000, 256),
-- 67. Course Schedule
('Course Schedule', 'There are a total of numCourses courses you have to take. Some courses may have prerequisites. Return true if you can finish all courses.', 'MEDIUM', 'GRAPHS', 'numCourses, prerequisites', 'true or false', '1 ≤ numCourses ≤ 2000', 'Detect cycle in a directed graph using DFS or Topological Sort (Kahn''s Algorithm).', 1000, 256),
-- 68. Number of Connected Components in an Undirected Graph
('Number of Connected Components', 'You have a graph of n nodes. You are given an integer n and an array edges. Return the number of connected components in the graph.', 'MEDIUM', 'GRAPHS', 'n, edges', 'Count', '1 ≤ n ≤ 2000', 'Use Union-Find or DFS/BFS to count components.', 1000, 256),
-- 69. Graph Valid Tree
('Graph Valid Tree', 'You have a graph of n nodes labeled from 0 to n - 1. You are given an integer n and a list of edges. Write a function to check whether these edges make up a valid tree.', 'MEDIUM', 'GRAPHS', 'n, edges', 'true or false', '1 ≤ n ≤ 2000', 'A graph is a tree if it has n-1 edges and is fully connected (no cycles).', 1000, 256),
-- 70. Alien Dictionary
('Alien Dictionary', 'There is a new alien language that uses the English alphabet. However, the order among the letters is unknown to you. You are given a list of strings words from the alien language''s dictionary. Return a string of the unique letters in the new alien language sorted in lexicographically increasing order by the new language''s rules.', 'HARD', 'GRAPHS', 'List of words', 'Order string', '1 ≤ words.length ≤ 100', 'Topological sort on the character dependency graph.', 1000, 256),
-- 71. Encode and Decode Strings
('Encode and Decode Strings', 'Design an algorithm to encode a list of strings to a string. The encoded string is then sent over the network and is decoded back to the original list of strings.', 'MEDIUM', 'STRINGS', 'List of strings', 'Encoded string', '0 ≤ n ≤ 200', 'Use length-prefixing (e.g., "4#code") to handle delimiters properly.', 1000, 256),
-- 72. Top K Frequent Elements
('Top K Frequent Elements', 'Given an integer array nums and an integer k, return the k most frequent elements.', 'MEDIUM', 'HEAP', 'Array nums, k', 'k elements', '1 ≤ n ≤ 10^5', 'Use a hash map for frequency and a min-heap (or bucket sort) to find top k.', 1000, 256),
-- 73. Find Median from Data Stream
('Find Median from Data Stream', 'The median is the middle value in an ordered integer list. Implement the MedianFinder class.', 'HARD', 'HEAP', 'Commands', 'Median', 'At most 5 * 10^4 calls', 'Use two heaps: a max-heap for the lower half and a min-heap for the upper half.', 1000, 256),
-- 74. Word Search
('Word Search', 'Given an m x n grid of characters board and a string word, return true if word exists in the grid.', 'MEDIUM', 'GRAPHS', 'Board, word', 'true or false', '1 ≤ m, n ≤ 6', 'DFS with backtracking.', 1000, 256),
-- 75. Word Search II
('Word Search II', 'Given an m x n board of characters and a list of strings words, return all words on the board.', 'HARD', 'GRAPHS', 'Board, words', 'List of found words', '1 ≤ m, n ≤ 12', 'Use a Trie to store words and DFS on the board.', 1000, 256),
-- 76. Merge Intervals
('Merge Intervals', 'Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals.', 'MEDIUM', 'SORTING', 'Intervals', 'Merged intervals', '1 ≤ n ≤ 10^4', 'Sort by start time and merge if current start <= previous end.', 1000, 256),
-- 77. Insert Interval
('Insert Interval', 'You are given an array of non-overlapping intervals intervals where intervals[i] = [starti, endi] represent the start and the end of the ith interval and intervals is sorted in ascending order by starti. You are also given an interval newInterval = [start, end] that represents the start and end of another interval. Insert newInterval into intervals such that intervals is still sorted in ascending order by starti and intervals still does not have any overlapping intervals (merge overlapping intervals if necessary).', 'MEDIUM', 'SORTING', 'Intervals, newInterval', 'Modified intervals', '0 ≤ n ≤ 10^4', 'Iterate and add non-overlapping intervals; merge overlapping ones.', 1000, 256),
-- 78. Non-overlapping Intervals
('Non-overlapping Intervals', 'Given an array of intervals intervals where intervals[i] = [starti, endi], return the minimum number of intervals you need to remove to make the rest of the intervals non-overlapping.', 'MEDIUM', 'GREEDY', 'Intervals', 'Count', '1 ≤ n ≤ 10^5', 'Sort by end time and greedily select non-overlapping intervals.', 1000, 256),
-- 79. Serialize and Deserialize Binary Tree
('Serialize and Deserialize Binary Tree', 'Design an algorithm to serialize and deserialize a binary tree.', 'HARD', 'TREES', 'Binary tree', 'Serialized string', '0 ≤ n ≤ 10^4', 'Use Preorder traversal (DFS) or Level-order traversal (BFS).', 1000, 256),
-- 80. Binary Tree Maximum Path Sum
('Binary Tree Maximum Path Sum', 'A path in a binary tree is a sequence of nodes where each pair of adjacent nodes in the sequence has an edge connecting them. A node can only appear in the sequence at most once. Note that the path does not need to pass through the root. The path sum is the sum of the node''s values in the path. Given the root of a binary tree, return the maximum path sum of any non-empty path.', 'HARD', 'TREES', 'Binary tree', 'Max path sum', '1 ≤ n ≤ 3 * 10^4', 'Recursively find max gain from left and right subtrees. Update global max with (left + right + root).', 1000, 256),
-- 81. Construct Binary Tree from Preorder and Inorder Traversal
('Construct Binary Tree', 'Given two integer arrays preorder and inorder where preorder is the preorder traversal of a binary tree and inorder is the inorder traversal of the same tree, construct and return the binary tree.', 'MEDIUM', 'TREES', 'Preorder, Inorder', 'Binary tree', '1 ≤ n ≤ 3000', 'First element of preorder is root. Find it in inorder to split left/right subtrees.', 1000, 256),
-- 82. Validate Binary Search Tree
('Validate Binary Search Tree', 'Given the root of a binary tree, determine if it is a valid binary search tree (BST).', 'MEDIUM', 'TREES', 'Binary tree', 'true or false', '1 ≤ n ≤ 10^4', 'Recursively validate with (min, max) range constraints.', 1000, 256),
-- 83. Kth Smallest Element in a BST
('Kth Smallest Element in a BST', 'Given the root of a binary search tree, and an integer k, return the kth smallest value (1-indexed) of all the values of the nodes in the tree.', 'MEDIUM', 'TREES', 'BST, k', 'kth smallest', '1 ≤ n ≤ 10^4', 'Inorder traversal gives sorted values. Return the kth element.', 1000, 256),
-- 84. Implement Trie (Prefix Tree)
('Implement Trie', 'A trie (pronounced as "try") or prefix tree is a tree data structure used to efficiently store and retrieve keys in a dataset of strings. Implement the Trie class.', 'MEDIUM', 'TREES', 'Commands', 'Output', '1 ≤ word.length ≤ 2000', 'Use a tree where each node has 26 children (for lowercase English letters).', 1000, 256),
-- 85. Design Add and Search Words Data Structure
('Design Add and Search Words', 'Design a data structure that supports adding new words and finding if a string matches any previously added string. Support . as a wildcard.', 'MEDIUM', 'TREES', 'Commands', 'Output', '1 ≤ word.length ≤ 25', 'Use a Trie. For wildcard, recursively check all children.', 1000, 256),
-- 86. Word Search II
('Word Search II', 'Given an m x n board of characters and a list of strings words, return all words on the board.', 'HARD', 'GRAPHS', 'Board, words', 'List of words', '1 ≤ m, n ≤ 12', 'Backtracking with Trie optimization.', 1000, 256),
-- 87. Merge k Sorted Lists
('Merge k Sorted Lists', 'You are given an array of k linked-lists lists, each linked-list is sorted in ascending order. Merge all the linked-lists into one sorted linked-list and return it.', 'HARD', 'HEAP', 'List of linked lists', 'Merged list', '0 ≤ k ≤ 10^4', 'Use a min-heap to keep track of the smallest head node among all lists.', 1000, 256),
-- 88. Trapping Rain Water
('Trapping Rain Water', 'Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.', 'HARD', 'ARRAYS', 'Elevation map', 'Water amount', '1 ≤ n ≤ 2 * 10^4', 'Use two pointers or precompute max-left and max-right arrays.', 1000, 256),
-- 89. Largest Rectangle in Histogram
('Largest Rectangle in Histogram', 'Given an array of integers heights representing the histogram''s bar height where the width of each bar is 1, return the area of the largest rectangle in the histogram.', 'HARD', 'STACK', 'Heights array', 'Max area', '1 ≤ n ≤ 10^5', 'Use a monotonic stack to find the nearest smaller element on left and right.', 1000, 256),
-- 90. Median of Two Sorted Arrays
('Median of Two Sorted Arrays', 'Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.', 'HARD', 'SEARCHING', 'Two sorted arrays', 'Median', '0 ≤ m, n ≤ 1000', 'Binary search on the partition of the smaller array.', 1000, 256),
-- 91. Longest Valid Parentheses
('Longest Valid Parentheses', 'Given a string containing just the characters ''('' and '')'', find the length of the longest valid (well-formed) parentheses substring.', 'HARD', 'STACK', 'String s', 'Max length', '0 ≤ s.length ≤ 3 * 10^4', 'Use a stack or DP. Stack stores indices of unmatched parentheses.', 1000, 256),
-- 92. Edit Distance
('Edit Distance', 'Given two strings word1 and word2, return the minimum number of operations required to convert word1 to word2.', 'HARD', 'DYNAMIC_PROGRAMMING', 'Two strings', 'Min operations', '0 ≤ length ≤ 500', 'dp[i][j] is min edits for word1[0..i] and word2[0..j].', 1000, 256),
-- 93. Maximal Rectangle
('Maximal Rectangle', 'Given a rows x cols binary matrix filled with 0''s and 1''s, find the largest rectangle containing only 1''s and return its area.', 'HARD', 'STACK', 'Binary matrix', 'Max area', '1 ≤ rows, cols ≤ 200', 'Treat each row as a histogram and use Largest Rectangle in Histogram logic.', 1000, 256),
-- 94. Minimum Window Substring
('Minimum Window Substring', 'Given two strings s and t of lengths m and n respectively, return the minimum window substring of s such that every character in t (including duplicates) is included in the window.', 'HARD', 'STRINGS', 'Strings s, t', 'Min window', '1 ≤ m, n ≤ 10^5', 'Sliding window with frequency map.', 1000, 256),
-- 95. Burst Balloons
('Burst Balloons', 'You are given n balloons, indexed from 0 to n - 1. Each balloon is painted with a number on it represented by an array nums. You are asked to burst all the balloons. Return the maximum coins you can collect.', 'HARD', 'DYNAMIC_PROGRAMMING', 'Array nums', 'Max coins', '1 ≤ n ≤ 300', 'DP on intervals. dp[i][j] is max coins from bursting balloons between i and j.', 1000, 256),
-- 96. Count of Smaller Numbers After Self
('Count of Smaller Numbers After Self', 'You are given an integer array nums and you have to return a new counts array. The counts array has the property where counts[i] is the number of smaller elements to the right of nums[i].', 'HARD', 'SORTING', 'Array nums', 'Counts array', '1 ≤ n ≤ 10^5', 'Use Merge Sort or Fenwick Tree/Segment Tree.', 1000, 256),
-- 97. Remove Invalid Parentheses
('Remove Invalid Parentheses', 'Given a string s that contains parentheses and letters, remove the minimum number of invalid parentheses to make the input string valid.', 'HARD', 'GRAPHS', 'String s', 'List of valid strings', '1 ≤ s.length ≤ 25', 'BFS to find valid strings with minimum removals.', 1000, 256),
-- 98. Regular Expression Matching
('Regular Expression Matching', 'Given an input string s and a pattern p, implement regular expression matching with support for ''.'' and ''*''.', 'HARD', 'DYNAMIC_PROGRAMMING', 'String s, Pattern p', 'true or false', '1 ≤ s.length ≤ 20', 'dp[i][j] matches s[0..i] and p[0..j]. Handle ''*'' carefully.', 1000, 256),
-- 99. Wildcard Matching
('Wildcard Matching', 'Given an input string (s) and a pattern (p), implement wildcard pattern matching with support for ''?'' and ''*''.', 'HARD', 'DYNAMIC_PROGRAMMING', 'String s, Pattern p', 'true or false', '0 ≤ s.length ≤ 2000', 'Similar to Regex matching but ''*'' matches any sequence.', 1000, 256),
-- 100. N-Queens
('N-Queens', 'The n-queens puzzle is the problem of placing n queens on an n x n chessboard such that no two queens attack each other. Given an integer n, return all distinct solutions to the n-queens puzzle.', 'HARD', 'GRAPHS', 'Integer n', 'List of boards', '1 ≤ n ≤ 9', 'Backtracking. Place queens row by row, checking column and diagonals.', 1000, 256);

-- Insert Test Cases (Sample for first 5 to ensure functionality)
INSERT INTO test_cases (problem_id, input_data, expected_output, is_sample) VALUES
(1, '4\n2 7 11 15\n9', '0 1', true),
(2, '()', 'true', true),
(3, 'hello', 'olleh', true),
(4, '9\n-2 1 -3 4 -1 2 1 -5 4', '6', true),
(5, '2', '2', true);
