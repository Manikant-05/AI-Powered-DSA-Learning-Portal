package com.dsaportal.config;

import com.dsaportal.entity.Problem;
import com.dsaportal.entity.TestCase;
import com.dsaportal.entity.User;
import com.dsaportal.repository.ProblemRepository;
import com.dsaportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final int TARGET_PROBLEM_COUNT = 50;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create admin user
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@dsaportal.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(User.Role.ADMIN);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setLastLogin(LocalDateTime.now());
            userRepository.save(admin);
            System.out.println("Admin user created");
        }

        // Create test user
        if (!userRepository.existsByUsername("testuser")) {
            User testUser = new User();
            testUser.setUsername("testuser");
            testUser.setEmail("test@example.com");
            testUser.setPassword(passwordEncoder.encode("password123"));
            testUser.setRole(User.Role.USER);
            testUser.setCreatedAt(LocalDateTime.now());
            testUser.setLastLogin(LocalDateTime.now());
            userRepository.save(testUser);
            System.out.println("Test user created");
        }

        // Create sample problems
        if (problemRepository.count() == 0) {
            // Problem 1: Two Sum
            Problem problem1 = new Problem();
            problem1.setTitle("Two Sum");
            problem1.setDescription("Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.\n\nYou may assume that each input would have exactly one solution, and you may not use the same element twice.\n\nYou can return the answer in any order.");
            problem1.setDifficulty(Problem.Difficulty.EASY);
            problem1.setTopic(Problem.Topic.ARRAYS);
            problem1.setInputFormat("First line contains n (2 ≤ n ≤ 10^4)\nSecond line contains n integers\nThird line contains target");
            problem1.setOutputFormat("Print two space-separated indices");
            problem1.setConstraints("2 ≤ n ≤ 10^4\n-10^9 ≤ nums[i] ≤ 10^9\n-10^9 ≤ target ≤ 10^9");
            problem1.setTimeLimit(1000);
            problem1.setMemoryLimit(256);
            problem1.setCreatedAt(LocalDateTime.now());
            problem1.setUpdatedAt(LocalDateTime.now());
            problem1 = problemRepository.save(problem1);

            // Add test cases for problem 1
            TestCase testCase1 = new TestCase();
            testCase1.setProblem(problem1);
            testCase1.setInputData("4\n2 7 11 15\n9");
            testCase1.setExpectedOutput("0 1");
            testCase1.setIsSample(true);
            problem1.setTestCases(new ArrayList<>(Arrays.asList(testCase1)));

            TestCase testCase2 = new TestCase();
            testCase2.setProblem(problem1);
            testCase2.setInputData("3\n3 2 4\n6");
            testCase2.setExpectedOutput("1 2");
            testCase2.setIsSample(false);
            problem1.getTestCases().add(testCase2);
            
            problemRepository.save(problem1);

            // Problem 2: Valid Parentheses
            Problem problem2 = new Problem();
            problem2.setTitle("Valid Parentheses");
            problem2.setDescription("Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.\n\nAn input string is valid if:\n1. Open brackets must be closed by the same type of brackets.\n2. Open brackets must be closed in the correct order.\n3. Every close bracket has a corresponding open bracket of the same type.");
            problem2.setDifficulty(Problem.Difficulty.EASY);
            problem2.setTopic(Problem.Topic.STACK);
            problem2.setInputFormat("Single line containing string s");
            problem2.setOutputFormat("Print \"true\" if valid, \"false\" otherwise");
            problem2.setConstraints("1 ≤ s.length ≤ 10^4\ns consists of parentheses only");
            problem2.setTimeLimit(1000);
            problem2.setMemoryLimit(256);
            problem2.setCreatedAt(LocalDateTime.now());
            problem2.setUpdatedAt(LocalDateTime.now());
            problem2 = problemRepository.save(problem2);

            // Add test cases for problem 2
            TestCase testCase3 = new TestCase();
            testCase3.setProblem(problem2);
            testCase3.setInputData("()");
            testCase3.setExpectedOutput("true");
            testCase3.setIsSample(true);
            problem2.setTestCases(new ArrayList<>(Arrays.asList(testCase3)));

            TestCase testCase4 = new TestCase();
            testCase4.setProblem(problem2);
            testCase4.setInputData("()[]{}");
            testCase4.setExpectedOutput("true");
            testCase4.setIsSample(true);
            problem2.getTestCases().add(testCase4);

            TestCase testCase5 = new TestCase();
            testCase5.setProblem(problem2);
            testCase5.setInputData("(]");
            testCase5.setExpectedOutput("false");
            testCase5.setIsSample(true);
            problem2.getTestCases().add(testCase5);
            
            problemRepository.save(problem2);

            // Problem 3: Best Time to Buy and Sell Stock (ARRAYS - EASY)
            Problem problem3 = createProblem(
                "Best Time to Buy and Sell Stock",
                "You are given an array prices where prices[i] is the price of a given stock on the ith day.\n\nYou want to maximize your profit by choosing a single day to buy one stock and choosing a different day in the future to sell that stock.\n\nReturn the maximum profit you can achieve from this transaction. If you cannot achieve any profit, return 0.",
                Problem.Difficulty.EASY,
                Problem.Topic.ARRAYS,
                "First line contains n (1 ≤ n ≤ 10^5)\nSecond line contains n integers representing prices",
                "Print the maximum profit",
                "1 ≤ n ≤ 10^5\n0 ≤ prices[i] ≤ 10^4"
            );
            addTestCase(problem3, "6\n7 1 5 3 6 4", "5", true);
            addTestCase(problem3, "5\n7 6 4 3 1", "0", false);
            problemRepository.save(problem3);

            // Problem 4: Contains Duplicate (ARRAYS - EASY)
            Problem problem4 = createProblem(
                "Contains Duplicate",
                "Given an integer array nums, return true if any value appears at least twice in the array, and return false if every element is distinct.",
                Problem.Difficulty.EASY,
                Problem.Topic.ARRAYS,
                "First line contains n (1 ≤ n ≤ 10^5)\nSecond line contains n integers",
                "Print \"true\" if duplicate exists, \"false\" otherwise",
                "1 ≤ n ≤ 10^5\n-10^9 ≤ nums[i] ≤ 10^9"
            );
            addTestCase(problem4, "4\n1 2 3 1", "true", true);
            addTestCase(problem4, "4\n1 2 3 4", "false", false);
            problemRepository.save(problem4);

            // Problem 5: Maximum Subarray (ARRAYS - MEDIUM)
            Problem problem5 = createProblem(
                "Maximum Subarray",
                "Given an integer array nums, find the contiguous subarray (containing at least one number) which has the largest sum and return its sum.\n\nA subarray is a contiguous part of an array.",
                Problem.Difficulty.MEDIUM,
                Problem.Topic.ARRAYS,
                "First line contains n (1 ≤ n ≤ 10^5)\nSecond line contains n integers",
                "Print the maximum sum",
                "1 ≤ n ≤ 10^5\n-10^4 ≤ nums[i] ≤ 10^4"
            );
            addTestCase(problem5, "9\n-2 1 -3 4 -1 2 1 -5 4", "6", true);
            addTestCase(problem5, "1\n1", "1", false);
            problemRepository.save(problem5);

            // Problem 6: Product of Array Except Self (ARRAYS - MEDIUM)
            Problem problem6 = createProblem(
                "Product of Array Except Self",
                "Given an integer array nums, return an array answer such that answer[i] is equal to the product of all the elements of nums except nums[i].\n\nThe product of any prefix or suffix of nums is guaranteed to fit in a 32-bit integer.\n\nYou must write an algorithm that runs in O(n) time and without using the division operator.",
                Problem.Difficulty.MEDIUM,
                Problem.Topic.ARRAYS,
                "First line contains n (2 ≤ n ≤ 10^5)\nSecond line contains n integers",
                "Print n space-separated integers",
                "2 ≤ n ≤ 10^5\n-30 ≤ nums[i] ≤ 30"
            );
            addTestCase(problem6, "4\n1 2 3 4", "24 12 8 6", true);
            addTestCase(problem6, "3\n-1 1 0 -3 3", "0 0 9 0 0", false);
            problemRepository.save(problem6);

            // Problem 7: Reverse String (STRINGS - EASY)
            Problem problem7 = createProblem(
                "Reverse String",
                "Write a function that reverses a string. The input string is given as an array of characters s.\n\nYou must do this by modifying the input array in-place with O(1) extra memory.",
                Problem.Difficulty.EASY,
                Problem.Topic.STRINGS,
                "Single line containing a string s",
                "Print the reversed string",
                "1 ≤ s.length ≤ 10^5\ns[i] is a printable ascii character"
            );
            addTestCase(problem7, "hello", "olleh", true);
            addTestCase(problem7, "Hannah", "hannaH", false);
            problemRepository.save(problem7);

            // Problem 8: Valid Anagram (STRINGS - EASY)
            Problem problem8 = createProblem(
                "Valid Anagram",
                "Given two strings s and t, return true if t is an anagram of s, and false otherwise.\n\nAn Anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.",
                Problem.Difficulty.EASY,
                Problem.Topic.STRINGS,
                "First line contains string s\nSecond line contains string t",
                "Print \"true\" if anagram, \"false\" otherwise",
                "1 ≤ s.length, t.length ≤ 5 * 10^4\ns and t consist of lowercase English letters"
            );
            addTestCase(problem8, "anagram\nnagaram", "true", true);
            addTestCase(problem8, "rat\ncar", "false", false);
            problemRepository.save(problem8);

            // Problem 9: Longest Substring Without Repeating Characters (STRINGS - MEDIUM)
            Problem problem9 = createProblem(
                "Longest Substring Without Repeating Characters",
                "Given a string s, find the length of the longest substring without repeating characters.",
                Problem.Difficulty.MEDIUM,
                Problem.Topic.STRINGS,
                "Single line containing string s",
                "Print the length of longest substring",
                "0 ≤ s.length ≤ 5 * 10^4\ns consists of English letters, digits, symbols and spaces"
            );
            addTestCase(problem9, "abcabcbb", "3", true);
            addTestCase(problem9, "bbbbb", "1", false);
            problemRepository.save(problem9);

            // Problem 10: Group Anagrams (STRINGS - MEDIUM)
            Problem problem10 = createProblem(
                "Group Anagrams",
                "Given an array of strings strs, group the anagrams together. You can return the answer in any order.\n\nAn Anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.",
                Problem.Difficulty.MEDIUM,
                Problem.Topic.STRINGS,
                "First line contains n (1 ≤ n ≤ 10^4)\nNext n lines contain strings",
                "Print grouped anagrams (each group on a new line, words separated by space)",
                "1 ≤ n ≤ 10^4\n0 ≤ strs[i].length ≤ 100"
            );
            addTestCase(problem10, "6\neat\ntea\ntan\nate\nnat\nbat", "eat tea ate\ntan nat\nbat", true);
            problemRepository.save(problem10);

            // Problem 11: Maximum Depth of Binary Tree (TREES - EASY)
            Problem problem11 = createProblem(
                "Maximum Depth of Binary Tree",
                "Given the root of a binary tree, return its maximum depth.\n\nA binary tree's maximum depth is the number of nodes along the longest path from the root node down to the farthest leaf node.",
                Problem.Difficulty.EASY,
                Problem.Topic.BINARY_TREE,
                "First line contains n (number of nodes)\nNext lines contain tree structure (level-order)",
                "Print the maximum depth",
                "0 ≤ number of nodes ≤ 10^4\n-100 ≤ Node.val ≤ 100"
            );
            addTestCase(problem11, "7\n3 9 20 null null 15 7", "3", true);
            addTestCase(problem11, "2\n1 null 2", "2", false);
            problemRepository.save(problem11);

            // Problem 12: Same Tree (TREES - EASY)
            Problem problem12 = createProblem(
                "Same Tree",
                "Given the roots of two binary trees p and q, write a function to check if they are the same or not.\n\nTwo binary trees are considered the same if they are structurally identical, and the nodes have the same value.",
                Problem.Difficulty.EASY,
                Problem.Topic.BINARY_TREE,
                "First two lines contain tree p (level-order)\nNext two lines contain tree q (level-order)",
                "Print \"true\" if same, \"false\" otherwise",
                "0 ≤ number of nodes ≤ 100\n-10^4 ≤ Node.val ≤ 10^4"
            );
            addTestCase(problem12, "3\n1 2 3\n3\n1 2 3", "true", true);
            addTestCase(problem12, "2\n1 2\n2\n1 null 2", "false", false);
            problemRepository.save(problem12);

            // Problem 13: Invert Binary Tree (TREES - EASY)
            Problem problem13 = createProblem(
                "Invert Binary Tree",
                "Given the root of a binary tree, invert the tree, and return its root.",
                Problem.Difficulty.EASY,
                Problem.Topic.BINARY_TREE,
                "First line contains n (number of nodes)\nNext line contains tree structure (level-order)",
                "Print inverted tree (level-order)",
                "0 ≤ number of nodes ≤ 100\n-100 ≤ Node.val ≤ 100"
            );
            addTestCase(problem13, "7\n4 2 7 1 3 6 9", "4 7 2 9 6 3 1", true);
            problemRepository.save(problem13);

            // Problem 14: Binary Tree Level Order Traversal (TREES - MEDIUM)
            Problem problem14 = createProblem(
                "Binary Tree Level Order Traversal",
                "Given the root of a binary tree, return the level order traversal of its nodes' values. (i.e., from left to right, level by level).",
                Problem.Difficulty.MEDIUM,
                Problem.Topic.BINARY_TREE,
                "First line contains n (number of nodes)\nNext line contains tree structure (level-order)",
                "Print level-order traversal (each level on new line)",
                "0 ≤ number of nodes ≤ 2000\n-1000 ≤ Node.val ≤ 1000"
            );
            addTestCase(problem14, "6\n3 9 20 null null 15 7", "3\n9 20\n15 7", true);
            problemRepository.save(problem14);

            // Problem 15: Validate Binary Search Tree (TREES - MEDIUM)
            Problem problem15 = createProblem(
                "Validate Binary Search Tree",
                "Given the root of a binary tree, determine if it is a valid binary search tree (BST).\n\nA valid BST is defined as follows:\n- The left subtree of a node contains only nodes with keys less than the node's key.\n- The right subtree of a node contains only nodes with keys greater than the node's key.\n- Both the left and right subtrees must also be binary search trees.",
                Problem.Difficulty.MEDIUM,
                Problem.Topic.BINARY_TREE,
                "First line contains n (number of nodes)\nNext line contains tree structure (level-order)",
                "Print \"true\" if valid BST, \"false\" otherwise",
                "1 ≤ number of nodes ≤ 10^4\n-2^31 ≤ Node.val ≤ 2^31 - 1"
            );
            addTestCase(problem15, "3\n2 1 3", "true", true);
            addTestCase(problem15, "3\n5 1 4 null null 3 6", "false", false);
            problemRepository.save(problem15);

            // Problem 16: Number of Islands (GRAPHS - MEDIUM)
            Problem problem16 = createProblem(
                "Number of Islands",
                "Given an m x n 2D binary grid grid which represents a map of '1's (land) and '0's (water), return the number of islands.\n\nAn island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically. You may assume all four edges of the grid are all surrounded by water.",
                Problem.Difficulty.MEDIUM,
                Problem.Topic.GRAPHS,
                "First line contains m and n (dimensions)\nNext m lines contain n characters each ('1' or '0')",
                "Print the number of islands",
                "1 ≤ m, n ≤ 300\ngrid[i][j] is '0' or '1'"
            );
            addTestCase(problem16, "3 4\n1100\n1100\n0011", "2", true);
            addTestCase(problem16, "3 3\n111\n010\n111", "1", false);
            problemRepository.save(problem16);

            // Problem 17: Clone Graph (GRAPHS - MEDIUM)
            Problem problem17 = createProblem(
                "Clone Graph",
                "Given a reference of a node in a connected undirected graph.\n\nReturn a deep copy (clone) of the graph.\n\nEach node in the graph contains a value (int) and a list (List[Node]) of its neighbors.",
                Problem.Difficulty.MEDIUM,
                Problem.Topic.GRAPHS,
                "First line contains n (number of nodes)\nNext lines contain adjacency list",
                "Print cloned graph structure",
                "0 ≤ number of nodes ≤ 100\n1 ≤ Node.val ≤ 100"
            );
            addTestCase(problem17, "4\n1:2,4\n2:1,3\n3:2,4\n4:1,3", "1:2,4\n2:1,3\n3:2,4\n4:1,3", true);
            problemRepository.save(problem17);

            // Problem 18: Course Schedule (GRAPHS - MEDIUM)
            Problem problem18 = createProblem(
                "Course Schedule",
                "There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1. You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that you must take course bi first if you want to take course ai.\n\nFor example, the pair [0, 1], indicates that to take course 0 you have to first take course 1.\n\nReturn true if you can finish all courses. Otherwise, return false.",
                Problem.Difficulty.MEDIUM,
                Problem.Topic.GRAPHS,
                "First line contains numCourses and m (number of prerequisites)\nNext m lines contain two integers each (course, prerequisite)",
                "Print \"true\" if possible, \"false\" otherwise",
                "1 ≤ numCourses ≤ 10^5\n0 ≤ prerequisites.length ≤ 5000"
            );
            addTestCase(problem18, "2 2\n1 0\n0 1", "false", true);
            addTestCase(problem18, "2 1\n1 0", "true", false);
            problemRepository.save(problem18);

            // Problem 19: Climbing Stairs (DYNAMIC_PROGRAMMING - EASY)
            Problem problem19 = createProblem(
                "Climbing Stairs",
                "You are climbing a staircase. It takes n steps to reach the top.\n\nEach time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?",
                Problem.Difficulty.EASY,
                Problem.Topic.DYNAMIC_PROGRAMMING,
                "Single line containing n (number of steps)",
                "Print the number of distinct ways",
                "1 ≤ n ≤ 45"
            );
            addTestCase(problem19, "2", "2", true);
            addTestCase(problem19, "3", "3", false);
            problemRepository.save(problem19);

            // Problem 20: House Robber (DYNAMIC_PROGRAMMING - MEDIUM)
            Problem problem20 = createProblem(
                "House Robber",
                "You are a professional robber planning to rob houses along a street. Each house has a certain amount of money stashed, the only constraint stopping you from robbing each of them is that adjacent houses have security systems connected and it will automatically contact the police if two adjacent houses were broken into on the same night.\n\nGiven an integer array nums representing the amount of money of each house, return the maximum amount of money you can rob tonight without alerting the police.",
                Problem.Difficulty.MEDIUM,
                Problem.Topic.DYNAMIC_PROGRAMMING,
                "First line contains n (1 ≤ n ≤ 100)\nSecond line contains n integers (money in each house)",
                "Print maximum amount that can be robbed",
                "1 ≤ n ≤ 100\n0 ≤ nums[i] ≤ 400"
            );
            addTestCase(problem20, "4\n1 2 3 1", "4", true);
            addTestCase(problem20, "5\n2 7 9 3 1", "12", false);
            problemRepository.save(problem20);

            // Problem 21: Coin Change (DYNAMIC_PROGRAMMING - MEDIUM)
            Problem problem21 = createProblem(
                "Coin Change",
                "You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money.\n\nReturn the fewest number of coins that you need to make up that amount. If that amount of money cannot be made up by any combination of the coins, return -1.\n\nYou may assume that you have an infinite number of each kind of coin.",
                Problem.Difficulty.MEDIUM,
                Problem.Topic.DYNAMIC_PROGRAMMING,
                "First line contains n (number of coin types) and amount\nSecond line contains n integers (coin denominations)",
                "Print the fewest number of coins needed, or -1 if impossible",
                "1 ≤ coins.length ≤ 12\n1 ≤ coins[i] ≤ 2^31 - 1\n0 ≤ amount ≤ 10^4"
            );
            addTestCase(problem21, "3 11\n1 2 5", "3", true);
            addTestCase(problem21, "1 3\n2", "-1", false);
            problemRepository.save(problem21);

            // Problem 22: Longest Increasing Subsequence (DYNAMIC_PROGRAMMING - MEDIUM)
            Problem problem22 = createProblem(
                "Longest Increasing Subsequence",
                "Given an integer array nums, return the length of the longest strictly increasing subsequence.",
                Problem.Difficulty.MEDIUM,
                Problem.Topic.DYNAMIC_PROGRAMMING,
                "First line contains n (1 ≤ n ≤ 2500)\nSecond line contains n integers",
                "Print the length of longest increasing subsequence",
                "1 ≤ n ≤ 2500\n-10^4 ≤ nums[i] ≤ 10^4"
            );
            addTestCase(problem22, "8\n10 9 2 5 3 7 101 18", "4", true);
            addTestCase(problem22, "6\n0 1 0 3 2 3", "4", false);
            problemRepository.save(problem22);

            long remaining = TARGET_PROBLEM_COUNT - problemRepository.count();
            if (remaining > 0) {
                createPracticeProblems(remaining);
            }

            System.out.println("Sample problems created");
        }
    }

    private Problem createProblem(String title, String description, Problem.Difficulty difficulty, 
                                   Problem.Topic topic, String inputFormat, String outputFormat, String constraints) {
        Problem problem = new Problem();
        problem.setTitle(title);
        problem.setDescription(description);
        problem.setDifficulty(difficulty);
        problem.setTopic(topic);
        problem.setInputFormat(inputFormat);
        problem.setOutputFormat(outputFormat);
        problem.setConstraints(constraints);
        problem.setTimeLimit(1000);
        problem.setMemoryLimit(256);
        problem.setCreatedAt(LocalDateTime.now());
        problem.setUpdatedAt(LocalDateTime.now());
        problem.setTestCases(new ArrayList<>());
        return problem;
    }

    private void addTestCase(Problem problem, String inputData, String expectedOutput, boolean isSample) {
        TestCase testCase = new TestCase();
        testCase.setProblem(problem);
        testCase.setInputData(inputData);
        testCase.setExpectedOutput(expectedOutput);
        testCase.setIsSample(isSample);
        problem.getTestCases().add(testCase);
    }

    private void createPracticeProblems(long remaining) {
        if (remaining <= 0) {
            return;
        }

        Problem.Difficulty[] difficultyCycle = new Problem.Difficulty[] {
            Problem.Difficulty.EASY,
            Problem.Difficulty.EASY,
            Problem.Difficulty.MEDIUM,
            Problem.Difficulty.MEDIUM,
            Problem.Difficulty.HARD
        };

        // Ensure we cover 8-10 categories: ARRAYS, STRINGS, TREES, GRAPHS, DP already covered
        // Add: HASH_TABLE, QUEUE, GREEDY, MATH, SORTING, SEARCHING, HEAP, LINKED_LIST, STACK
        List<PracticeTopic> practiceTopics = List.of(
                new PracticeTopic(
                        Problem.Topic.HASH_TABLE,
                        "Hash Table Insights",
                        "Design an efficient hashing strategy that focuses on %s scenarios.",
                        "First line: n (number of keys)\nNext line: n space-separated keys\nLast line: target key",
                        "Print diagnostic information about collisions and whether the target exists.",
                        "1 ≤ n ≤ 10^4",
                        "5\napple banana apple orange banana\nbanana",
                        "Key banana found with 2 occurrences",
                        "4\nalpha beta gamma beta\nalpha",
                        "Key alpha found with 1 occurrences"
                ),
                new PracticeTopic(
                        Problem.Topic.QUEUE,
                        "Queue Simulation",
                        "Simulate a queue-based workflow emphasizing %s techniques.",
                        "First line: m (number of operations)\nNext m lines: operation value",
                        "Print the resulting queue and any dequeued values.",
                        "1 ≤ m ≤ 10^4",
                        "6\nENQUEUE 1\nENQUEUE 2\nDEQUEUE\nENQUEUE 3\nDEQUEUE\nENQUEUE 4",
                        "Dequeued: 1, 2\nQueue: [3, 4]",
                        "4\nENQUEUE 5\nENQUEUE 6\nDEQUEUE\nDEQUEUE",
                        "Dequeued: 5, 6\nQueue: []"
                ),
                new PracticeTopic(
                        Problem.Topic.GREEDY,
                        "Greedy Strategy Builder",
                        "Construct a greedy algorithm that optimizes %s.",
                        "First line: n (items)\nSecond line: item values",
                        "Print chosen items and the final score.",
                        "1 ≤ n ≤ 10^5",
                        "5\n2 7 1 8 2",
                        "Chosen: 8 7 2 -> Score: 17",
                        "4\n5 3 9 1",
                        "Chosen: 9 5 -> Score: 14"
                ),
                new PracticeTopic(
                        Problem.Topic.MATH,
                        "Mathematical Reasoning Drill",
                        "Apply mathematical reasoning to handle %s inputs.",
                        "Input: single integer n",
                        "Print computed metrics such as factorial mod and digital root.",
                        "1 ≤ n ≤ 10^6",
                        "10",
                        "FactorialMod: 3628800, DigitalRoot: 1",
                        "27",
                        "FactorialMod: 459042011, DigitalRoot: 9"
                ),
                new PracticeTopic(
                        Problem.Topic.SORTING,
                        "Sorting Lab",
                        "Experiment with %s sorting patterns.",
                        "First line: n\nSecond line: array values",
                        "Print sorted array and swap counts.",
                        "1 ≤ n ≤ 10^5",
                        "6\n4 1 3 2 5 6",
                        "Sorted: 1 2 3 4 5 6 | Swaps: 4",
                        "5\n9 7 5 3 1",
                        "Sorted: 1 3 5 7 9 | Swaps: 4"
                ),
                new PracticeTopic(
                        Problem.Topic.SEARCHING,
                        "Search Pattern Studio",
                        "Implement search strategies tailored for %s datasets.",
                        "First line: n\nSecond line: sorted values\nThird line: target",
                        "Print index of target or -1.",
                        "1 ≤ n ≤ 10^5",
                        "7\n1 4 6 8 10 13 15\n10",
                        "Index: 4",
                        "5\n2 3 5 7 11\n6",
                        "Index: -1"
                ),
                new PracticeTopic(
                        Problem.Topic.HEAP,
                        "Heap Mechanics",
                        "Use heap-based thinking to solve %s aggregation tasks.",
                        "First line: n\nSecond line: values\nThird line: k",
                        "Print the k largest values.",
                        "1 ≤ n ≤ 10^5",
                        "8\n5 12 3 7 19 2 11 4\n3",
                        "Top: 19 12 11",
                        "6\n9 1 4 7 3 8\n2",
                        "Top: 9 8"
                ),
                new PracticeTopic(
                        Problem.Topic.LINKED_LIST,
                        "Linked List Workshop",
                        "Manipulate linked lists focusing on %s behaviors.",
                        "First line: n\nSecond line: node values\nThird line: operation details",
                        "Print resulting list after operations.",
                        "1 ≤ n ≤ 10^4",
                        "5\n1 2 3 4 5\nREVERSE",
                        "Result: 5 4 3 2 1",
                        "4\n10 20 30 40\nREMOVE 2",
                        "Result: 10 20 40"
                ),
                new PracticeTopic(
                        Problem.Topic.STACK,
                        "Stack Operations",
                        "Master stack-based algorithms with %s patterns.",
                        "First line: n (operations)\nNext n lines: operation value",
                        "Print stack state after operations.",
                        "1 ≤ n ≤ 10^4",
                        "5\nPUSH 1\nPUSH 2\nPOP\nPUSH 3\nPOP",
                        "Stack: [1]",
                        "4\nPUSH 5\nPUSH 6\nPOP\nPOP",
                        "Stack: []"
                )
        );

        int created = 0;
        int sequence = 1;
        for (PracticeTopic topic : practiceTopics) {
            for (int i = 0; i < difficultyCycle.length && created < remaining; i++) {
                Problem.Difficulty difficulty = difficultyCycle[i];
                String suffix = switch (difficulty) {
                    case EASY -> "Foundations";
                    case MEDIUM -> (i % 2 == 0 ? "Progression" : "Challenge");
                    case HARD -> "Mastery";
                };

                String title = topic.baseName + " " + suffix + " #" + sequence;
                sequence++;

                Problem problem = createProblem(
                        title,
                        String.format(topic.descriptionTemplate, suffix.toLowerCase()),
                        difficulty,
                        topic.topic,
                        topic.inputFormat,
                        topic.outputFormat,
                        topic.constraints
                );

                addTestCase(problem, topic.sampleInput, topic.sampleOutput, true);
                addTestCase(problem, topic.exampleInput, topic.exampleOutput, false);
                problemRepository.save(problem);
                created++;
            }
            if (created >= remaining) {
                break;
            }
        }
    }

    private static class PracticeTopic {
        private final Problem.Topic topic;
        private final String baseName;
        private final String descriptionTemplate;
        private final String inputFormat;
        private final String outputFormat;
        private final String constraints;
        private final String sampleInput;
        private final String sampleOutput;
        private final String exampleInput;
        private final String exampleOutput;

        private PracticeTopic(Problem.Topic topic,
                              String baseName,
                              String descriptionTemplate,
                              String inputFormat,
                              String outputFormat,
                              String constraints,
                              String sampleInput,
                              String sampleOutput,
                              String exampleInput,
                              String exampleOutput) {
            this.topic = topic;
            this.baseName = baseName;
            this.descriptionTemplate = descriptionTemplate;
            this.inputFormat = inputFormat;
            this.outputFormat = outputFormat;
            this.constraints = constraints;
            this.sampleInput = sampleInput;
            this.sampleOutput = sampleOutput;
            this.exampleInput = exampleInput;
            this.exampleOutput = exampleOutput;
        }
    }
}
