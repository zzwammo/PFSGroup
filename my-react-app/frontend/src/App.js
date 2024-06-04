import React from 'react';

import Login from './Componenets/Login';
import logo from './Componenets/Assets/Our_Meat_Logo.png'; // Adjust the path based on your actual file location

function App() {
    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} alt="Company Logo" className="company-logo" />
                <h1></h1>
                <Login />
            </header>
        </div>
    );
}

export default App;
