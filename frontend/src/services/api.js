import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    console.log('[API Request]', {
      method: config.method.toUpperCase(),
      url: config.url,
      hasToken: !!token
    });
    
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle auth errors
api.interceptors.response.use(
  (response) => {
    console.log('[API Response] Success:', {
      status: response.status,
      url: response.config.url
    });
    return response;
  },
  (error) => {
    const requestUrl = error.config?.url || 'unknown';
    const status = error.response?.status || 'no-response';
    
    console.error('[API Response] Error:', {
      status,
      url: requestUrl,
      path: window.location.pathname,
      hasToken: !!localStorage.getItem('token')
    });
    
    // Don't redirect on login/register endpoints - let them handle the error
    const isAuthEndpoint = error.config?.url?.includes('/auth/login') || 
                          error.config?.url?.includes('/auth/register');
    
    // Handle 401 Unauthorized (expired or invalid token)
    if (error.response?.status === 401 || error.response?.status === 403) {
      console.error('[API] 401/403 detected! Analyzing...');
      console.error('Is auth endpoint:', isAuthEndpoint);
      console.error('Request URL:', requestUrl);
      
      // Don't redirect on auth endpoints - let them handle it
      if (isAuthEndpoint) {
        console.log('[API] Auth endpoint - NOT redirecting');
        return Promise.reject(error);
      }
      
      // Check if we're on a protected page that actually needs auth
      const currentPath = window.location.pathname;
      const isOnAuthPage = currentPath === '/login' || currentPath === '/register';
      
      // For public endpoints like /problems, don't force redirect
      const isPublicEndpoint = requestUrl.includes('/problems');
      
      const token = localStorage.getItem('token');
      
      console.error('[API] Redirect decision:', {
        hasToken: !!token,
        isOnAuthPage,
        isPublicEndpoint,
        willRedirect: token && !isOnAuthPage && !isPublicEndpoint
      });
      
      // Only redirect if:
      // 1. We have a token (meaning user was authenticated but it's now invalid)
      // 2. We're not on an auth page already
      // 3. We're not on a public endpoint
      if (token && !isOnAuthPage && !isPublicEndpoint) {
        console.error('[API] REDIRECTING TO LOGIN - token invalid');
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        delete api.defaults.headers.common['Authorization'];
        window.location.href = '/login';
        return Promise.reject(error);
      } else if (!token && !isOnAuthPage && !isPublicEndpoint) {
        console.error('[API] REDIRECTING TO LOGIN - no token on protected endpoint');
        window.location.href = '/login';
        return Promise.reject(error);
      }
    }
    
    // Handle network errors
    if (!error.response && error.message === 'Network Error') {
      console.error('Network error: Unable to connect to backend API');
    }
    
    return Promise.reject(error);
  }
);

export default api;
