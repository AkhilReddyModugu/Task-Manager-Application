import {useState, useEffect} from 'react';

const LoginForm= () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading]= useState(false);

    useEffect(() => {
        document.title = username ? `Login - ${username}` : 'Login - Task Manager';
    });

    function handleSubmit(e){
        e.preventDefault();

        if(!username || !password){
            setError("Username and password are required");
            return ;
        }

        setError('');
        setIsLoading(true);
        console.log('Submitted:', username, password);
        setIsLoading(false);
    }

    return (
        <form onSubmit={handleSubmit}> 
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