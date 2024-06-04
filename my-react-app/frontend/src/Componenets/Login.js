import React, { useState } from 'react';
import './Login.css';
import logo from './Assets/Our_Meat_Logo.png'; // Adjust the path based on your actual file location

function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = (e) => {
        e.preventDefault();
        // Add your login logic here
        console.log('Username:', username);
        console.log('Password:', password);
    };

    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} alt="Company Logo" className="company-logo" />
                <h1>Welcome to Our Meat Page</h1>
                <div className="login-container">
                    <h2>Login</h2>
                    <form onSubmit={handleLogin}>
                        <div className="input-group">
                            <label htmlFor="username">Username</label>
                            <input 
                                type="text" 
                                id="username" 
                                value={username} 
                                onChange={(e) => setUsername(e.target.value)}
                                placeholder="Username"
                            />
                        </div>
                        <div className="input-group">
                            <label htmlFor="password">Password</label>
                            <input 
                                type="password" 
                                id="password" 
                                value={password} 
                                onChange={(e) => setPassword(e.target.value)}
                                placeholder="Password"
                            />
                        </div>
                        <button type="submit">Login</button>
                    </form>
                </div>
            </header>
        </div>
    );
}

export default Login;
