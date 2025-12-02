-- Create database
CREATE DATABASE dsa_portal;

-- Use the database
\c dsa_portal;

-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- Create problems table
CREATE TABLE problems (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    difficulty VARCHAR(20) NOT NULL CHECK (difficulty IN ('EASY', 'MEDIUM', 'HARD')),
    topic VARCHAR(50) NOT NULL CHECK (topic IN (
        'ARRAYS', 'STRINGS', 'TREES', 'GRAPHS', 'DYNAMIC_PROGRAMMING',
        'GREEDY', 'SORTING', 'SEARCHING', 'MATH', 'HASH_TABLE',
        'STACK', 'QUEUE', 'LINKED_LIST', 'BINARY_TREE', 'HEAP'
    )),
    input_format TEXT,
    output_format TEXT,
    constraints TEXT,
    time_limit INTEGER DEFAULT 1000,
    memory_limit INTEGER DEFAULT 256,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create test_cases table
CREATE TABLE test_cases (
    id BIGSERIAL PRIMARY KEY,
    problem_id BIGINT NOT NULL REFERENCES problems(id) ON DELETE CASCADE,
    input_data TEXT,
    expected_output TEXT,
    is_sample BOOLEAN DEFAULT FALSE
);

-- Create submissions table
CREATE TABLE submissions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    problem_id BIGINT NOT NULL REFERENCES problems(id) ON DELETE CASCADE,
    code TEXT,
    language VARCHAR(20) NOT NULL CHECK (language IN ('PYTHON', 'JAVA', 'CPP', 'JAVASCRIPT', 'C')),
    status VARCHAR(30) CHECK (status IN (
        'PENDING', 'ACCEPTED', 'WRONG_ANSWER', 'TIME_LIMIT_EXCEEDED',
        'MEMORY_LIMIT_EXCEEDED', 'RUNTIME_ERROR', 'COMPILATION_ERROR'
    )),
    time_taken INTEGER,
    memory_used INTEGER,
    test_cases_passed INTEGER DEFAULT 0,
    total_test_cases INTEGER DEFAULT 0,
    accuracy DECIMAL(5,2) DEFAULT 0.0,
    error_message TEXT,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    judge0_token VARCHAR(255)
);

-- Create indexes for better performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_problems_difficulty ON problems(difficulty);
CREATE INDEX idx_problems_topic ON problems(topic);
CREATE INDEX idx_submissions_user_id ON submissions(user_id);
CREATE INDEX idx_submissions_problem_id ON submissions(problem_id);
CREATE INDEX idx_submissions_status ON submissions(status);
CREATE INDEX idx_submissions_submitted_at ON submissions(submitted_at);

-- Insert sample data
-- Password for 'admin' is 'admin123'
-- Password for 'testuser' is 'password123'
INSERT INTO users (username, email, password, role) VALUES 
('admin', 'admin@dsaportal.com', '$2a$10$B3f2mfGBIHYWlnhyD08UNeeFP9MFLphAsLrM5IGybR1Ngod.88rNW', 'ADMIN'),
('testuser', 'test@example.com', '$2a$10$cFNGWAd9bdC92rRzcDU2POMijvWecWpPXW1HY/vHEAlkLF/gx4bgG', 'USER');

-- Insert sample problems
INSERT INTO problems (title, description, difficulty, topic, input_format, output_format, constraints, time_limit, memory_limit) VALUES 
('Two Sum', 'Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.', 'EASY', 'ARRAYS', 'First line contains n (2 ≤ n ≤ 10^4)\nSecond line contains n integers\nThird line contains target', 'Print two space-separated indices', '2 ≤ n ≤ 10^4\n-10^9 ≤ nums[i] ≤ 10^9\n-10^9 ≤ target ≤ 10^9', 1000, 256),
('Valid Parentheses', 'Given a string s containing just the characters ''('', '')'', ''{'', ''}'', ''['' and '']'', determine if the input string is valid.', 'EASY', 'STACK', 'Single line containing string s', 'Print "true" if valid, "false" otherwise', '1 ≤ s.length ≤ 10^4\ns consists of parentheses only', 1000, 256),
('Binary Tree Inorder Traversal', 'Given the root of a binary tree, return the inorder traversal of its nodes'' values.', 'EASY', 'TREES', 'First line contains n (number of nodes)\nNext n lines contain node values', 'Print space-separated values in inorder', '0 ≤ n ≤ 100\n-100 ≤ node values ≤ 100', 1000, 256),
('Maximum Subarray', 'Given an integer array nums, find the contiguous subarray (containing at least one number) which has the largest sum and return its sum.', 'MEDIUM', 'DYNAMIC_PROGRAMMING', 'First line contains n (1 ≤ n ≤ 10^5)\nSecond line contains n integers', 'Print the maximum sum', '1 ≤ n ≤ 10^5\n-10^4 ≤ nums[i] ≤ 10^4', 1000, 256),
('Word Ladder', 'Given two words (beginWord and endWord), and a dictionary''s word list, find the length of shortest transformation sequence from beginWord to endWord.', 'HARD', 'GRAPHS', 'First line contains beginWord\nSecond line contains endWord\nThird line contains n (number of words in dictionary)\nNext n lines contain dictionary words', 'Print the length of shortest transformation sequence, or 0 if not possible', '1 ≤ beginWord.length ≤ 10\n1 ≤ endWord.length ≤ 10\n1 ≤ n ≤ 5000', 2000, 512);

-- Insert sample test cases
INSERT INTO test_cases (problem_id, input_data, expected_output, is_sample) VALUES 
(1, '4\n2 7 11 15\n9', '0 1', true),
(1, '3\n3 2 4\n6', '1 2', false),
(2, '()', 'true', true),
(2, '()[]{}', 'true', true),
(2, '(]', 'false', true),
(3, '3\n1\n2\n3', '1 2 3', true),
(4, '5\n-2 1 -3 4 -1', '4', true),
(4, '1\n1', '1', false),
(5, 'hit\ncog\n6\nhot dot dog lot log cog', '5', true);

-- Create a function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create trigger for problems table
CREATE TRIGGER update_problems_updated_at BEFORE UPDATE ON problems
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
