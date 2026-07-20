import {useState,useEffect} from 'react';
import axiosClient from '../api/axiosClient';

function TasksPage() {
  const [data, setData]= useState([]);
  const [isLoading, setIsLoading]= useState(false);
  const [error, setError]= useState('');

  useEffect(()=>{
    axiosClient.get("/tasks")
      .then((res)=>setData(res.data.content))
      .catch((err)=>setError(err.message))
      .finally(()=>setIsLoading(false));
  },[])

  if(isLoading) return <p>Loading tasks...</p>;
  if(error) return <p>Failed to load tasks: {error}</p>;

  return (
    <ul>
      {data.map((task)=>(
        <li key={task.id}>{task.title} — {task.status}</li>
      ))}
    </ul>
  );
}

export default TasksPage;