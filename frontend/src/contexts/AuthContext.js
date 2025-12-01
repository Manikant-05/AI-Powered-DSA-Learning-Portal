import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../services/api';

const AuthContext = createContext();

export function useAuth() {
  return useContext(AuthContext);
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    console.log('[AuthContext] Starting initialization...');
    const initializeAuth = () => {
      try {
        const token = localStorage.getItem('token');
        const userStr = localStorage.getItem('user');
        
        console.log('[AuthContext] Found in localStorage:', {
          token: !!token,
          user: !!userStr
        });
        
        if (token) {
          api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
          
          if (userStr) {
            try {
              const userInfo = JSON.parse(userStr);
              if (userInfo && userInfo.id) {
                console.log('[AuthContext] Successfully loaded user:', userInfo.username);
                setUser(userInfo);
                setLoading(false);
                return;
              }
            } catch (parseError) {
              console.error('[AuthContext] Error parsing user data:', parseError);
              localStorage.removeItem('token');
              localStorage.removeItem('user');
              delete api.defaults.headers.common['Authorization'];
            }
          } else {
            console.warn('[AuthContext] Token exists but no user data');
            localStorage.removeItem('token');
            delete api.defaults.headers.common['Authorization'];
          }
        }
        
        console.log('[AuthContext] No valid auth data found');
        setUser(null);
      } catch (error) {
        console.error('[AuthContext] Error initializing auth:', error);
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        delete api.defaults.headers.common['Authorization'];
        setUser(null);
      } finally {
        console.log('[AuthContext] Initialization complete - setting loading to false');
        setLoading(false);
      }
    };
    
    initializeAuth();
  }, []);

  const login = async (username, password) => {
    try {
      // Clear any existing auth data first
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      delete api.defaults.headers.common['Authorization'];
      
      const response = await api.post('/auth/login', { username, password });
      const { token, type, ...userData } = response.data;
      
      // Validate response
      if (!token || !userData.id) {
        throw new Error('Invalid response from server');
      }
      
      // Store auth data
      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(userData));
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      
      // Update state
      setUser(userData);
      
      return { success: true };
    } catch (error) {
      console.error('Login error:', error.response?.data || error.message);
      let errorMessage = 'Login failed';
      
      if (error.response?.data) {
        // Handle string error messages
        if (typeof error.response.data === 'string') {
          errorMessage = error.response.data.replace('Error: ', '');
        } 
        // Handle object error messages
        else if (error.response.data.message) {
          errorMessage = error.response.data.message;
        } 
        // Handle error object
        else if (error.response.data.error) {
          errorMessage = error.response.data.error;
        }
      } else if (error.message) {
        errorMessage = error.message;
      }
      
      // Ensure auth data is cleared on error
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      delete api.defaults.headers.common['Authorization'];
      setUser(null);
      
      return { 
        success: false, 
        error: errorMessage
      };
    }
  };

  const register = async (username, email, password) => {
    try {
      const response = await api.post('/auth/register', { username, email, password });
      const { token, ...userData } = response.data;
      
      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(userData));
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      
      setUser(userData);
      return { success: true };
    } catch (error) {
      console.error('Registration error:', error.response?.data);
      return { 
        success: false, 
        error: error.response?.data || 'Registration failed' 
      };
    }
  };

  const logout = () => {
    // Clear all auth data
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    delete api.defaults.headers.common['Authorization'];
    setUser(null);
    // Use window.location for a clean state reset
    setTimeout(() => {
      window.location.href = '/login';
    }, 100);
  };

  const value = {
    user,
    login,
    register,
    logout,
    loading
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}
