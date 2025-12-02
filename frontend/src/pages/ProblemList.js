import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import api from '../services/api';
import ProblemCard from '../components/ProblemCard';

function ProblemList() {
  const { user, loading: authLoading } = useAuth();
  const [problems, setProblems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filters, setFilters] = useState({
    difficulty: '',
    topic: '',
    search: ''
  });

  useEffect(() => {
    // Wait for auth to finish loading before making API calls
    if (!authLoading) {
      // Only fetch if filters have actually changed (not on initial mount with empty filters)
      if (filters.difficulty || filters.topic || filters.search || 
          (!filters.difficulty && !filters.topic && !filters.search)) {
        fetchProblems();
      }
    }
  }, [filters, authLoading]);

  const fetchProblems = async () => {
    try {
      setLoading(true);
      setError('');
      const params = new URLSearchParams();
      
      if (filters.difficulty) params.append('difficulty', filters.difficulty);
      if (filters.topic) params.append('topic', filters.topic);
      if (filters.search) params.append('search', filters.search);

      // Problems endpoint is public, but we can still include token if available
      console.log('[ProblemList] Fetching problems...');
      const response = await api.get(`/problems?${params.toString()}`);
      console.log('[ProblemList] Success! Got', response.data.length, 'problems');
      setProblems(response.data);
    } catch (err) {
      console.error('[ProblemList] Error details:', {
        status: err.response?.status,
        statusText: err.response?.statusText,
        data: err.response?.data,
        message: err.message,
        url: err.config?.url
      });
      
      // Don't redirect on 401/403 for problems endpoint (it's public)
      if (err.response?.status === 401 || err.response?.status === 403) {
        console.warn('Unexpected auth error on public endpoint');
        setError('Authentication error. Please log in again.');
      } else if (err.response?.status >= 500) {
        setError('Server error. Please try again later.');
      } else if (!err.response) {
        setError('Unable to connect to server. Please check your connection.');
      } else {
        setError('Failed to load problems. Please try again.');
      }
      console.error('Problems error:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleFilterChange = (key, value) => {
    setFilters(prev => ({
      ...prev,
      [key]: value
    }));
  };

  const clearFilters = () => {
    setFilters({
      difficulty: '',
      topic: '',
      search: ''
    });
  };

  const difficulties = ['EASY', 'MEDIUM', 'HARD'];
  const topics = [
    'ARRAYS', 'STRINGS', 'TREES', 'GRAPHS', 'DYNAMIC_PROGRAMMING',
    'GREEDY', 'SORTING', 'SEARCHING', 'MATH', 'HASH_TABLE',
    'STACK', 'QUEUE', 'LINKED_LIST', 'BINARY_TREE', 'HEAP'
  ];

  // Show loading while auth is initializing or data is loading
  if (authLoading || loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-gray-900">Problems</h1>
        <div className="text-sm text-gray-600">
          {problems.length} problem{problems.length !== 1 ? 's' : ''} found
        </div>
      </div>

      {/* Filters */}
      <div className="card">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
          <div>
            <label className="label">
              Search
            </label>
            <div className="relative">
              <input
                type="text"
                placeholder="Search problems..."
                className="input pl-10"
                value={filters.search}
                onChange={(e) => handleFilterChange('search', e.target.value)}
              />
              <svg className="w-5 h-5 text-slate-400 absolute left-3 top-2.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </div>
          </div>

          <div>
            <label className="label">
              Difficulty
            </label>
            <select
              className="input"
              value={filters.difficulty}
              onChange={(e) => handleFilterChange('difficulty', e.target.value)}
            >
              <option value="">All Difficulties</option>
              {difficulties.map(diff => (
                <option key={diff} value={diff}>{diff}</option>
              ))}
            </select>
          </div>

          <div>
            <label className="label">
              Topic
            </label>
            <select
              className="input"
              value={filters.topic}
              onChange={(e) => handleFilterChange('topic', e.target.value)}
            >
              <option value="">All Topics</option>
              {topics.map(topic => (
                <option key={topic} value={topic}>
                  {topic.replace('_', ' ')}
                </option>
              ))}
            </select>
          </div>

          <div className="flex items-end">
            <button
              onClick={clearFilters}
              className="btn-secondary w-full"
            >
              Clear Filters
            </button>
          </div>
        </div>
      </div>

      {/* Error Message */}
      {error && (
        <div className="bg-danger-50 border border-danger-200 text-danger-700 px-4 py-3 rounded-lg flex items-center">
          <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          {error}
        </div>
      )}

      {/* Problems Grid */}
      {problems.length === 0 ? (
        <div className="text-center py-16 bg-white rounded-xl border border-slate-200 border-dashed">
          <div className="text-slate-300 mb-4">
            <svg className="mx-auto h-16 w-16" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <h3 className="text-lg font-medium text-slate-900 mb-2">No problems found</h3>
          <p className="text-slate-500 max-w-sm mx-auto">
            We couldn't find any problems matching your filters. Try adjusting them or check back later.
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {problems.map((problem) => (
            <ProblemCard key={problem.id} problem={problem} />
          ))}
        </div>
      )}
    </div>
  );
}

export default ProblemList;
