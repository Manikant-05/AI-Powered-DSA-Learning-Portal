import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import ProblemList from './pages/ProblemList';
import ProblemDetail from './pages/ProblemDetail';
import Profile from './pages/Profile';
import './App.css';

function ProtectedRoute({ children }) {
  const { user, loading } = useAuth();
  
  React.useEffect(() => {
    console.log('=== ProtectedRoute Status ===');
    console.log('Loading:', loading);
    console.log('User:', user);
    console.log('Current path:', window.location.pathname);
    console.log('Token in localStorage:', !!localStorage.getItem('token'));
    console.log('=============================');
  }, [user, loading]);
  
  // Show loading while auth is being checked
  if (loading) {
    console.log('[ProtectedRoute] Showing loading spinner - auth still loading');
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-primary-600"></div>
      </div>
    );
  }
  
  // Check if user is authenticated
  if (!user) {
    const token = localStorage.getItem('token');
    const userStr = localStorage.getItem('user');
    
    console.warn('[ProtectedRoute] User not authenticated!');
    console.warn('Token exists:', !!token);
    console.warn('User data exists:', !!userStr);
    
    if (token && userStr) {
      try {
        const userInfo = JSON.parse(userStr);
        if (userInfo && userInfo.id) {
          console.warn('[ProtectedRoute] User data in storage but not in state - showing loading');
          return (
            <div className="min-h-screen flex items-center justify-center">
              <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-primary-600"></div>
            </div>
          );
        }
      } catch (e) {
        console.error('[ProtectedRoute] Failed to parse user data:', e);
      }
    }
    
    console.log('[ProtectedRoute] Redirecting to /login');
    return <Navigate to="/login" replace />;
  }
  
  console.log('[ProtectedRoute] User authenticated, rendering children');
  return children;
}

function AppContent() {
  const { user, loading } = useAuth();
  
  // Show loading screen while checking auth
  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-primary-600"></div>
      </div>
    );
  }
  
  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      <main className="container mx-auto px-4 py-8">
        <Routes>
          <Route 
            path="/login" 
            element={
              user ? <Navigate to="/dashboard" replace /> : <Login />
            } 
          />
          <Route 
            path="/register" 
            element={
              user ? <Navigate to="/dashboard" replace /> : <Register />
            } 
          />
          <Route 
            path="/dashboard" 
            element={
              <ProtectedRoute>
                <Dashboard />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/problems" 
            element={<ProblemList />}
          />
          <Route 
            path="/problems/:id" 
            element={<ProblemDetail />}
          />
          <Route 
            path="/profile" 
            element={
              <ProtectedRoute>
                <Profile />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/" 
            element={
              <Navigate to={user ? "/dashboard" : "/login"} replace />
            } 
          />
        </Routes>
      </main>
    </div>
  );
}

function App() {
  return (
    <AuthProvider>
      <Router>
        <AppContent />
      </Router>
    </AuthProvider>
  );
}

export default App;
