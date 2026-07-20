import {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import axiosClient from '../api/axiosClient';

const LoginForm= ({title}) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading]= useState(false);
    const navigate= useNavigate();

    async function handleSubmit(e){
        e.preventDefault();

        if(!username || !password){
            setError("Username and password are required");
            return ;
        }

        setError('');
        setIsLoading(true);

        try{
            const response= await axiosClient.post('/auth/login', {username, password});
            const token= response.data.token;
            localStorage.setItem('token',token);
            navigate('/dashboard');
        }
        catch(err){
            setError('Invalid username or password');
        }
        finally{
            setIsLoading(false);
        }
    }

    return (
        <form onSubmit={handleSubmit}> 
            <p>{title}</p>
            <div>
                <label>Username </label>
                <input 
                    type='text'
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
            </div>
            <div>
                <label>Password </label>
                <input 
                    type='password'
                    value={password}
                    onChange={(e)=>setPassword(e.target.value)}
                />
            </div>
            {error && <p style={{color: 'red'}}>{error}</p>}
            <button type='submit' disabled={isLoading}>
                {isLoading ? 'Logging in...' : 'Login'}
            </button>
        </form>
    )
}

export default LoginForm;