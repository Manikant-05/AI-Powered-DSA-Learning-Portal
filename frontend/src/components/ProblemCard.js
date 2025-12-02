import React from 'react';
import { Link } from 'react-router-dom';

function ProblemCard({ problem }) {
  const getDifficultyColor = (difficulty) => {
    switch (difficulty) {
      case 'EASY':
        return 'badge-easy';
      case 'MEDIUM':
        return 'badge-medium';
      case 'HARD':
        return 'badge-hard';
      default:
        return 'badge-easy';
    }
  };

  const getTopicColor = (topic) => {
    const colors = {
      'ARRAYS': 'bg-blue-100 text-blue-800',
      'STRINGS': 'bg-green-100 text-green-800',
      'TREES': 'bg-yellow-100 text-yellow-800',
      'GRAPHS': 'bg-purple-100 text-purple-800',
      'DYNAMIC_PROGRAMMING': 'bg-red-100 text-red-800',
      'GREEDY': 'bg-indigo-100 text-indigo-800',
      'SORTING': 'bg-pink-100 text-pink-800',
      'SEARCHING': 'bg-orange-100 text-orange-800',
      'MATH': 'bg-teal-100 text-teal-800',
      'HASH_TABLE': 'bg-cyan-100 text-cyan-800',
      'STACK': 'bg-amber-100 text-amber-800',
      'QUEUE': 'bg-lime-100 text-lime-800',
      'LINKED_LIST': 'bg-emerald-100 text-emerald-800',
      'BINARY_TREE': 'bg-violet-100 text-violet-800',
      'HEAP': 'bg-rose-100 text-rose-800',
    };
    return colors[topic] || 'bg-gray-100 text-gray-800';
  };

  return (
    <div className="card card-hover group h-full flex flex-col">
      <div className="flex justify-between items-start mb-4">
        <h3 className="text-lg font-bold text-slate-900 line-clamp-2 group-hover:text-primary-600 transition-colors">
          {problem.title}
        </h3>
        <span className={`badge ${getDifficultyColor(problem.difficulty)} ml-3 flex-shrink-0`}>
          {problem.difficulty}
        </span>
      </div>
      
      <p className="text-slate-600 text-sm mb-6 line-clamp-3 flex-grow">
        {problem.description}
      </p>
      
      <div className="flex flex-wrap gap-2 mb-6">
        <span className="badge badge-topic">
          {problem.topic.replace('_', ' ')}
        </span>
      </div>
      
      <div className="flex justify-between items-center text-xs text-slate-500 mb-6 pt-4 border-t border-slate-100">
        <div className="flex items-center">
          <svg className="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          {problem.timeLimit}ms
        </div>
        <div className="flex items-center">
          <svg className="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
          </svg>
          {problem.memoryLimit}MB
        </div>
      </div>
      
      <Link 
        to={`/problems/${problem.id}`}
        className="btn-primary w-full text-center block shadow-lg shadow-primary-500/20 hover:shadow-primary-500/40"
      >
        Solve Problem
      </Link>
    </div>
  );
}

export default ProblemCard;
