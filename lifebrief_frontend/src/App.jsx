import React from 'react';
import { RouteProvider } from "./components/RouteTracker";
import { Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './components/LoginPage';
import SignupPage from './components/SignupPage';
import HomePage from './components/HomePage';
import ProfilePage from './components/ProfilePage';
import TaskPlanner from './components/TaskPlanner';
import HealthChat from './components/HealthChat';
import FocusMode from './components/FocusMode';   // ✅ Import FocusMode
import ProtectedRoute from './components/ProtectedRoute';
import './App.css';

export default function App() {
  return (
    <RouteProvider>
      <Routes>
        {/* Default redirect to signup */}
        <Route path="/" element={<Navigate to="/signup" replace />} />

        {/* Public routes */}
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/login" element={<LoginPage />} />

        {/* Protected routes */}
        <Route
          path="/homepage"
          element={
            <ProtectedRoute>
              <HomePage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/profile"
          element={
            <ProtectedRoute>
              <ProfilePage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/task-planner"
          element={
            <ProtectedRoute>
              <TaskPlanner />
            </ProtectedRoute>
          }
        />
        <Route
          path="/healthchat"
          element={
            <ProtectedRoute>
              <HealthChat />
            </ProtectedRoute>
          }
        />
        <Route
          path="/focus-mode"   // ✅ New route for Focus Mode
          element={
            <ProtectedRoute>
              <FocusMode />
            </ProtectedRoute>
          }
        />
      </Routes>
    </RouteProvider>
  );
}