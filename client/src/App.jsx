import './App.css'
import LoginPage from './pages/LoginPage';
import Dashboard from './pages/Dashboard';
import ProtectedRoute from './components/ProtectedRoute';
import {BrowserRouter, Routes, Route, Navigate} from 'react-router-dom'
import ProjectsPage from './pages/ProjectsPage';
import TasksPage from './pages/TasksPage';

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path='/login'  element={<LoginPage />} />
        <Route 
          path='/dashboard' 
          element={
            <ProtectedRoute>
              <Dashboard/>
            </ProtectedRoute>
          } 
        >
          <Route path='projects' element={<ProjectsPage />}/>
          <Route path='tasks' element={<TasksPage />}/>
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
