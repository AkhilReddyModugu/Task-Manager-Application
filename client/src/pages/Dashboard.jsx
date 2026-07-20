import { Link, Outlet, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axiosClient from "../api/axiosClient";

const Dashboard = () => {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    axiosClient.get('/auth/me').then((res) => setUser(res.data));
  }, []);

  function handleLogout() {
    localStorage.removeItem('token');
    navigate('/login');
  }

  return (
    <div>
      <h1>Dashboard</h1>
      {user && <p>Welcome, {user.username} ({user.role})</p>}
      <nav>
        <Link to="/dashboard/projects">Projects</Link> |{' '}
        <Link to="/dashboard/tasks">Tasks</Link> |{' '}
        <button onClick={handleLogout}>Logout</button>
      </nav>
      <hr />
      <Outlet />
    </div>
  );
};

export default Dashboard;